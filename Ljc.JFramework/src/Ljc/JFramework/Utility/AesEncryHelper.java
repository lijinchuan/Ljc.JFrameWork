package Ljc.JFramework.Utility;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryHelper {
	public static byte[] AesEncrypt(byte[] source, String key) throws Exception {
		if (source == null || key == null)
			return null;
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
		byte[] bytes = cipher.doFinal(source);
		return bytes;
	}

	public static byte[] AesDecrypt(byte[] source, String key) throws Exception {
		if (source == null || key == null)
			return null;
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
		byte[] bytes = cipher.doFinal(source);
		return bytes;
	}
}
