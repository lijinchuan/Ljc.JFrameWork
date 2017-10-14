package Ljc.JFramework.Utility;

import java.util.Base64;

public class Converter {
	public static String GetBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	public static byte[] FromBase64String(String src) {
		return Base64.getDecoder().decode(src);
	}
}
