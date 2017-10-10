package Ljc.JFramework.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class SerializerUtil {
	public static <T> String SerializerToXML(T object, String savePath) throws JAXBException, IOException {
		if (object == null) {
			return "";
		}

		JAXBContext context = JAXBContext.newInstance(object.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			marshaller.marshal(object, baos);
			byte[] bytes = baos.toByteArray();
			String s = new String(bytes, "utf-8");

			if (!Ljc.JFramework.Utility.StringUtil.isNullOrEmpty(savePath)) {
				File savefile = new File(savePath);
				if (savefile.exists()) {
					savefile.delete();
				}
				savefile.createNewFile();

				FileOutputStream fs = new FileOutputStream(savePath);
				try {
					fs.write(bytes);
				} finally {
					fs.close();
				}
			}

			return s;
		} finally {
			if (baos != null) {
				baos.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T DeSerializerFile(Class<T> t, String savePath) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(t);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new File(savePath));
	}

	@SuppressWarnings("unchecked")
	public static <T> T DeserializerXML(Class<T> t, String xml) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(t);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		byte[] xmlbytes = xml.getBytes("utf-8");
		ByteArrayInputStream bis = null;
		try {
			bis = new ByteArrayInputStream(xmlbytes);

			return (T) unmarshaller.unmarshal(bis);
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}
}
