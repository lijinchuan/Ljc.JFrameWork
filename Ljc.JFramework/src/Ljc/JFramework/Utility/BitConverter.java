package Ljc.JFramework.Utility;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

public class BitConverter {
	public static byte[] GetBytes(long value) {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.putLong(value);
		return buffer.array();
	}

	public static byte[] GetBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
		buffer.putInt(value);
		return buffer.array();
	}

	public static byte[] GetBytes(String value) throws UnsupportedEncodingException {
		// byte[] bs = value.getBytes("UTF-8");
		// ByteBuffer buffer =
		// ByteBuffer.allocate(bs.length).order(ByteOrder.LITTLE_ENDIAN);
		// buffer.put(bs);
		// return buffer.array();

		ByteBuffer buffer = ByteBuffer.allocate(value.toCharArray().length).order(ByteOrder.BIG_ENDIAN);

		for (char ch : value.toCharArray()) {
			// buffer.put((byte) ((ch >> 8) & 255));
			buffer.put((byte) (ch & 255));
		}

		return buffer.array();
	}

	public static byte[] GetBytes(double value) {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.putDouble(value);
		return buffer.array();
	}

	public static byte[] GetBytes(float value) {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
		buffer.putFloat(value);
		return buffer.array();
	}

	public static byte[] GetBytes(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());
		buffer.putShort(value);
		return buffer.array();
	}

	public static byte[] GetBytes(char value) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());
		buffer.putChar(value);
		return buffer.array();
	}

	public static byte[] GetBytes(Date value) throws Exception {
		long timelong = value.getTime();
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.putLong(timelong);
		return buffer.array();
	}
}
