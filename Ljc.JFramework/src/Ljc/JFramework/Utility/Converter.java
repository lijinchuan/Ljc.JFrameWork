package Ljc.JFramework.Utility;

import java.io.IOException;

import net.iharder.Base64;

public class Converter {
	public static String GetBase64(byte[] bytes) {
		return Base64.encodeBytes(bytes);
		// return Base64.encode(bytes);
	}

	public static byte[] FromBase64String(String src) throws IOException {
		return Base64.decode(src);
		// return Base64.decode(src);
	}
}
