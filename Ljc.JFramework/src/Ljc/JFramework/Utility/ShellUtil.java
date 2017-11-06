package Ljc.JFramework.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import Ljc.JFramework.CoreException;

public class ShellUtil {
	public static Tuple<String, String> runShellCmd(String cmd)
			throws CoreException, IOException, InterruptedException {
		if (StringUtil.isNullOrEmpty(cmd)) {
			throw new CoreException("�����Ϊ��");
		}

		Process process = Runtime.getRuntime().exec(cmd);
		InputStream stdInput = process.getInputStream();
		InputStream errInput = process.getErrorStream();

		process.waitFor();

		String stdinfo = ReadInput(stdInput);
		String errinfo = ReadInput(errInput);

		return new Tuple<String, String>(stdinfo, errinfo);

	}

	private static final String DEFAULT_ENCODING = "utf-8";// ����

	static String ReadInput(InputStream input) {
		// �ֽ�����
		byte[] bcache = new byte[2048];
		int readSize = 0;// ÿ�ζ�ȡ���ֽڳ���
		ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
		try {
			// һ���Զ�ȡ2048�ֽ�
			while ((readSize = input.read(bcache)) > 0) {
				// ��bcache�ж�ȡ��input����д��infoStream
				infoStream.write(bcache, 0, readSize);
			}
		} catch (IOException e1) {
			return "��������ȡ�쳣";
		}

		try {
			return infoStream.toString(DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}

	}
}
