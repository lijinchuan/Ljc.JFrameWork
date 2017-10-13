package Ljc.JFramework.Utility;

public class HashEncryptUtil {
	public static long GetCRC32(byte[] bytes, int off, int len) {

		if (bytes == null) {
			return 0L;
		}

		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		crc32.update(bytes, off, len);

		return crc32.getValue();
	}

	public static long GetCRC32(byte[] bytes, int off) {
		if (bytes == null) {
			return 0L;
		}
		return GetCRC32(bytes, off, bytes.length - off);
	}
}
