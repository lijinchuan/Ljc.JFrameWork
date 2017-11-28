package Ljc.JFramework.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static void zip(String zipFileName, File inputFile) throws Exception {
		System.out.println("ѹ����...");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // ������ر�
		System.out.println("ѹ�����");
	}

	private static void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // ��������
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/")); // ����zipѹ�������base
				System.out.println(base + "/");
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // �ݹ�������ļ���
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // ����zipѹ�������base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // ���ֽ���д�뵱ǰzipĿ¼
			}
			bi.close();
			in.close(); // �������ر�
		}
	}

	public static void unZip(String srcPath, String dest) throws IOException {
		File file = new File(srcPath);

		if (!file.exists()) {

			throw new RuntimeException(srcPath + "��ָ�ļ�������");

		}

		ZipFile zf = new ZipFile(file);
		Enumeration entries = zf.entries();

		ZipEntry entry = null;

		while (entries.hasMoreElements()) {

			entry = (ZipEntry) entries.nextElement();

			System.out.println("��ѹ" + entry.getName());

			if (entry.isDirectory()) {

				String dirPath = dest + File.separator + entry.getName();

				File dir = new File(dirPath);

				dir.mkdirs();

			} else {

				// ��ʾ�ļ�

				File f = new File(dest + File.separator + entry.getName());

				if (!f.exists()) {

					String dirs = f.getParentFile().getAbsolutePath();

					File parentDir = new File(dirs);

					parentDir.mkdirs();

				}

				f.createNewFile();

				// ��ѹ���ļ�����д�뵽����ļ���

				InputStream is = zf.getInputStream(entry);

				FileOutputStream fos = new FileOutputStream(f);

				int count;

				byte[] buf = new byte[8192];

				while ((count = is.read(buf)) != -1) {

					fos.write(buf, 0, count);

				}

				is.close();

				fos.close();

			}

		}
	}
}
