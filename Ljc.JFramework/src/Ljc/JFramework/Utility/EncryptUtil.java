package Ljc.JFramework.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {
	public static String md5Encrypt(String s, boolean fix) throws NoSuchAlgorithmException {
		// �������ܶ���
		MessageDigest digest = MessageDigest.getInstance("md5");

		// ���ü��ܶ���ķ��������ܵĶ����Ѿ����
		byte[] bs = digest.digest(s.getBytes());
		// ������������Ҫ�Լ��ܺ�Ľ���������Ż�������mysql���Ż�˼·��
		// mysql���Ż�˼·��
		// ��һ����������ȫ��ת����������
		String hexString = "";
		for (byte b : bs) {
			// ��һ����������ȫ��ת����������
			// ���ͣ�Ϊʲô����b&255
			/*
			 * b:��������һ��byte���͵�����(1���ֽ�) 255����һ��int���͵�����(4���ֽ�)
			 * byte���͵�������int���͵����ݽ������㣬���Զ���������Ϊint���� eg: b: 1001 1100(ԭʼ����) ����ʱ�� b: 0000 0000
			 * 0000 0000 0000 0000 1001 1100 255: 0000 0000 0000 0000 0000 0000 1111 1111
			 * �����0000 0000 0000 0000 0000 0000 1001 1100 ��ʱ��temp��һ��int���͵�����
			 */
			int temp = b & 255;
			// �ڶ����������е�����ת����16���Ƶ���ʽ
			// ע�⣺ת����ʱ��ע��if����>=0&&<16����ô���ʹ��Integer.toHexString()�����ܻ����ȱ��λ��
			// ��ˣ���Ҫ��temp�����ж�
			if (temp < 16 && temp >= 0 && fix) {
				// �ֶ�����һ����0��
				hexString = hexString + "0" + Integer.toHexString(temp);
			} else {
				hexString = hexString + Integer.toHexString(temp);
			}
		}
		return hexString;
	}
}
