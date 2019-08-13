package Ljc.JFramework.Utility;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Converter {
	public static String GetBase64(byte[] bytes) {
		return Base64.encode(bytes);
	}

	public static byte[] FromBase64String(String src) {
		return Base64.decode(src);
	}
}
