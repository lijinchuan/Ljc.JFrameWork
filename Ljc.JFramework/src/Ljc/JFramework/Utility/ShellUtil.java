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
			throw new CoreException("命令不能为空");
		}

		Process process = Runtime.getRuntime().exec(cmd);
		InputStream stdInput = process.getInputStream();
		InputStream errInput = process.getErrorStream();

		process.waitFor();

		String stdinfo = ReadInput(stdInput);
		String errinfo = ReadInput(errInput);

		return new Tuple<String, String>(stdinfo, errinfo);

	}

	private static final String DEFAULT_ENCODING = "utf-8";// 编码

	static String ReadInput(InputStream input) {
		// 字节数组
		byte[] bcache = new byte[2048];
		int readSize = 0;// 每次读取的字节长度
		ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
		try {
			// 一次性读取2048字节
			while ((readSize = input.read(bcache)) > 0) {
				// 将bcache中读取的input数据写入infoStream
				infoStream.write(bcache, 0, readSize);
			}
		} catch (IOException e1) {
			return "输入流读取异常";
		}

		try {
			return infoStream.toString(DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}

	}
}
