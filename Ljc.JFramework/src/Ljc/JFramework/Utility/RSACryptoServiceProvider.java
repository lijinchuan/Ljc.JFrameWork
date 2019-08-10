package Ljc.JFramework.Utility;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSACryptoServiceProvider {
	private static final String KEY_ALGORITHM = "RSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	private Map<String, Object> keyMap = null;

	private Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	public RSACryptoServiceProvider() {
		try {
			this.keyMap = this.initKey();
		} catch (Exception e) {
		}
	}

	// 获得公钥字符串
	public String getPublicKeyStr() throws Exception {
		// 获得map中的公钥对象 转为key对象
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		// 编码返回字符串
		return encryptBASE64(key.getEncoded());
	}

	// 获得私钥字符串
	public String getPrivateKeyStr() throws Exception {
		// 获得map中的私钥对象 转为key对象
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		// 编码返回字符串
		return encryptBASE64(key.getEncoded());
	}

	public String ToXmlString(boolean includePrivateParameters) {
		if (includePrivateParameters) {
			Key key = (Key) keyMap.get(PRIVATE_KEY);
			return getRSAPrivateKeyAsNetFormat(key.getEncoded());
		} else {
			Key key = (Key) keyMap.get(PUBLIC_KEY);
			return getRSAPublicKeyAsNetFormat(key.getEncoded());
		}
	}

	private static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivkey) {
		try {
			StringBuffer buff = new StringBuffer(1024);

			PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivkey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pvkKeySpec);

			buff.append("<RSAKeyValue>");
			buff.append("<Modulus>" + encryptBASE64(removeMSZero(pvkKey.getModulus().toByteArray())) + "</Modulus>");

			buff.append("<Exponent>" + encryptBASE64(removeMSZero(pvkKey.getPublicExponent().toByteArray()))
					+ "</Exponent>");

			buff.append("<P>" + encryptBASE64(removeMSZero(pvkKey.getPrimeP().toByteArray())) + "</P>");

			buff.append("<Q>" + encryptBASE64(removeMSZero(pvkKey.getPrimeQ().toByteArray())) + "</Q>");

			buff.append("<DP>" + encryptBASE64(removeMSZero(pvkKey.getPrimeExponentP().toByteArray())) + "</DP>");

			buff.append("<DQ>" + encryptBASE64(removeMSZero(pvkKey.getPrimeExponentQ().toByteArray())) + "</DQ>");

			buff.append("<InverseQ>" + encryptBASE64(removeMSZero(pvkKey.getCrtCoefficient().toByteArray()))
					+ "</InverseQ>");

			buff.append("<D>" + encryptBASE64(removeMSZero(pvkKey.getPrivateExponent().toByteArray())) + "</D>");
			buff.append("</RSAKeyValue>");

			return buff.toString().replaceAll("[ \t\n\r]", "");
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	private static String getRSAPublicKeyAsNetFormat(byte[] encodedPrivkey) {
		try {
			StringBuffer buff = new StringBuffer(1024);

			PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivkey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKey pukKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(encodedPrivkey));

			buff.append("<RSAKeyValue>");
			buff.append("<Modulus>" + encryptBASE64(removeMSZero(pukKey.getModulus().toByteArray())) + "</Modulus>");
			buff.append("<Exponent>" + encryptBASE64(removeMSZero(pukKey.getPublicExponent().toByteArray()))
					+ "</Exponent>");
			buff.append("</RSAKeyValue>");
			return buff.toString().replaceAll("[ \t\n\r]", "");
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	/***
	 * C#端公钥转换成java公钥
	 * 
	 * @param xml c# 端公钥字符串
	 * @return
	 * @throws Exception
	 */
	public static PublicKey decodePublicKeyFromXml(String xml) throws Exception {
		xml = xml.replaceAll("\r", "").replaceAll("\n", "");
		BigInteger modulus = new BigInteger(1,
				decryptBASE64(StringUtil.getMiddleString(xml, "<Modulus>", "</Modulus>")));
		BigInteger publicExponent = new BigInteger(1,
				decryptBASE64(StringUtil.getMiddleString(xml, "<Exponent>", "</Exponent>")));

		RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);

		KeyFactory keyf;
		try {
			keyf = KeyFactory.getInstance("RSA");
			return keyf.generatePublic(rsaPubKey);
		} catch (Exception e) {
			return null;
		}
	}

	/***
	 * C#端私钥转换成java私钥
	 * 
	 * @param xml c#端私钥字符串
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey decodePrivateKeyFromXml(String xml) throws Exception {
		xml = xml.replaceAll("\r", "").replaceAll("\n", "");
		BigInteger modulus = new BigInteger(1,
				decryptBASE64(StringUtil.getMiddleString(xml, "<Modulus>", "</Modulus>")));
		BigInteger publicExponent = new BigInteger(1,
				decryptBASE64(StringUtil.getMiddleString(xml, "<Exponent>", "</Exponent>")));
		BigInteger privateExponent = new BigInteger(1, decryptBASE64(StringUtil.getMiddleString(xml, "<D>", "</D>")));
		BigInteger primeP = new BigInteger(1, decryptBASE64(StringUtil.getMiddleString(xml, "<P>", "</P>")));
		BigInteger primeQ = new BigInteger(1, decryptBASE64(StringUtil.getMiddleString(xml, "<Q>", "</Q>")));
		BigInteger primeExponentP = new BigInteger(1, decryptBASE64(StringUtil.getMiddleString(xml, "<DP>", "</DP>")));
		BigInteger primeExponentQ = new BigInteger(1, decryptBASE64(StringUtil.getMiddleString(xml, "<DQ>", "</DQ>")));
		BigInteger crtCoefficient = new BigInteger(1,
				decryptBASE64(StringUtil.getMiddleString(xml, "<InverseQ>", "</InverseQ>")));

		RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP,
				primeQ, primeExponentP, primeExponentQ, crtCoefficient);

		KeyFactory keyf;
		try {
			keyf = KeyFactory.getInstance("RSA");
			return keyf.generatePrivate(rsaPriKey);
		} catch (Exception e) {
			return null;
		}
	}

	// 解码返回byte
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	// 编码返回字符串
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	private static byte[] removeMSZero(byte[] data) {
		byte[] data1;
		int len = data.length;
		if (data[0] == 0) {
			data1 = new byte[data.length - 1];
			System.arraycopy(data, 1, data1, 0, len - 1);
		} else
			data1 = data;

		return data1;
	}
}
