package Ljc.JFramework.Utility;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

public class RsaEncryHelper {
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	private static final String KEY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	public static Tuple<String, String> GenPair() {
		RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();

		String publicKey = rsa.ToXmlString(false); // 公钥
		String privateKey = rsa.ToXmlString(true); // 私钥

		return new Tuple<String, String>(publicKey, privateKey);
	}

	// ************************加密解密**************************
	public static byte[] encrypt(byte[] plainText, String publicKeyStr) throws Exception {
		PublicKey publicKey = RSACryptoServiceProvider.decodePublicKeyFromXml(publicKeyStr);
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLen = plainText.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		int i = 0;
		byte[] cache;
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(plainText, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(plainText, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptText = out.toByteArray();
		out.close();
		return encryptText;
	}

	public static byte[] decrypt(byte[] encryptText, String privateKeyStr) throws Exception {
		PrivateKey privateKey = RSACryptoServiceProvider.decodePrivateKeyFromXml(privateKeyStr);
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		int inputLen = encryptText.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptText, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptText, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] plainText = out.toByteArray();
		out.close();
		return plainText;
	}

	// ***************************签名和验证*******************************
	public static byte[] sign(byte[] data, String privateKeyStr) throws Exception {
		PrivateKey priK = RSACryptoServiceProvider.decodePrivateKeyFromXml(privateKeyStr);
		Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
		sig.initSign(priK);
		sig.update(data);
		return sig.sign();
	}

	public static boolean verify(byte[] data, byte[] sign, String publicKeyStr) throws Exception {
		PublicKey pubK = RSACryptoServiceProvider.decodePublicKeyFromXml(publicKeyStr);
		Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
		sig.initVerify(pubK);
		sig.update(data);
		return sig.verify(sign);
	}

}
