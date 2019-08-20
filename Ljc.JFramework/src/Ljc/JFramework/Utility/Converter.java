package Ljc.JFramework.Utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Converter {
	public static String GetBase64(byte[] bytes) throws UnsupportedEncodingException {
		return Base64Util.encode(bytes);
		// return Base64.encode(bytes);
	}

	public static byte[] FromBase64String(String src) throws IOException {
		return Base64Util.decode(src);
		// return Base64.decode(src);
	}
}
