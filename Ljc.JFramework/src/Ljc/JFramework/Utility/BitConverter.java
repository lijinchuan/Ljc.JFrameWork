package Ljc.JFramework.Utility;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

public class BitConverter {
	private static final byte[] bytesTrue = new byte[] { (byte) 1 };
	private static final byte[] bytesFalse = new byte[] { (byte) 0 };

	public static boolean IsLITTLE_ENDIAN() throws IOException {
		int a = 0x12345678;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeInt(a);
		byte[] b = baos.toByteArray();
		for (int i = 0; i < 4; i++) {
			System.out.println(Integer.toHexString(b[i]));
		}

		return Integer.toHexString(b[0]).equals("12");
	}

	public static byte[] GetBytes(long value) {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.putLong(value);
		buffer.position(0);
		return buffer.array();
	}

	public static long GetLong(byte[] value) {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.put(value);
		buffer.position(0);
		return buffer.getLong();
	}

	public static byte[] GetBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
		buffer.putInt(value);
		buffer.position(0);
		return buffer.array();
	}

	public static int GetInt(byte[] value) {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
		buffer.put(value);
		buffer.position(0);
		return buffer.getInt();
	}

	public static int GetInt(ByteBuffer buffer) {
		buffer.position(0);
		return buffer.getInt();
	}

	public static byte[] GetBytes(String value) throws UnsupportedEncodingException {
		return value.getBytes("utf-8");
	}

	public static String GetString(byte[] value) throws UnsupportedEncodingException {
		return new String(value, "utf-8");
	}

	public static byte[] GetBytes(double value) {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.putDouble(value);
		buffer.position(0);
		return buffer.array();
	}

	public static double GetDouble(byte[] value) {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.put(value);
		buffer.position(0);
		return buffer.getDouble();
	}

	public static byte[] GetBytes(float value) {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
		buffer.putFloat(value);
		buffer.position(0);
		return buffer.array();
	}

	public static float GetFloat(byte[] val) {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
		buffer.put(val);
		buffer.position(0);
		return buffer.getFloat();
	}

	public static byte[] GetBytes(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());
		buffer.putShort(value);
		buffer.position(0);
		return buffer.array();
	}

	public static short GetShort(byte[] value) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());
		buffer.put(value);
		buffer.position(0);
		return buffer.getShort();
	}

	public static byte[] GetBytes(char value) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());
		buffer.putChar(value);
		buffer.position(0);
		return buffer.array();
	}

	public static char GetChar(byte[] value) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());
		buffer.put(value);
		buffer.position(0);
		return buffer.getChar();
	}

	public static byte[] GetBytes(Date value) throws Exception {
		long timelong = value.getTime();
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.putLong(timelong);
		buffer.position(0);
		return buffer.array();
	}

	public static Date GetDate(byte[] value) {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
		buffer.put(value);
		buffer.position(0);
		long timestamp = buffer.getLong();

		return new Date(timestamp);
	}

	public static byte[] GetBytes(boolean value) {
		return value ? bytesTrue : bytesFalse;
	}
}
