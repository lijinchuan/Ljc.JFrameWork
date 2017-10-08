package Ljc.JFramework;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import Ljc.JFramework.TypeUtil.DateTime;
import Ljc.JFramework.TypeUtil.UInt16;
import Ljc.JFramework.TypeUtil.UInt32;
import Ljc.JFramework.TypeUtil.UShort;
import Ljc.JFramework.Utility.BitArray;
import Ljc.JFramework.Utility.BitConverter;

public class MemoryStreamReader {
	private ByteArrayInputStream _reader = null;
	private final static short _defaultShort = 0;
	private final static short _defaultUShort = 0;
	private final static int _defaultInt = 0;
	private final static long _defaultLong = 0;
	private final static byte _defaultByte = 0;
	private final static char _defaultChar = '\0';
	private final static double _defaultDouble = 0;
	private final static float _defaultFloat = 0;
	private final static String _defaultString = "";
	private final static BigDecimal _defaultDecimal = BigDecimal.ZERO;
	private final static Boolean _defaultBool = Boolean.FALSE;

	public MemoryStreamReader(ByteArrayInputStream reader) {
		_reader = reader;
	}

	public byte ReadByte() {
		// TODO Auto-generated method stub
		return (byte) _reader.read();
	}

	private UShort ReadRedirectUInt16() {
		return new UShort(BitConverter.GetShort(this.ReadBytes(2)));
	}

	public short ReadRedirectInt16() {
		byte[] bytes = this.ReadBytes(2);

		return BitConverter.GetShort(bytes);
	}

	private int ReadRedirectInt32() {
		// return _reader.read() + _reader.read() << 8 + _reader.read() << 16 +
		// _reader.read() << 24;
		byte[] bytes = this.ReadBytes(4);

		return BitConverter.GetInt(bytes);
	}

	public long ReadRedirectInt64() {
		byte[] bytes = this.ReadBytes(8);

		return BitConverter.GetLong(bytes);

	}

	public double ReadRedirectDouble() {
		byte[] bytes = this.ReadBytes(8);

		return BitConverter.GetDouble(bytes);

	}

	private long ReadRedirectUInt32() {
		int val = ReadRedirectInt32();
		if (val > 0) {
			return val;
		}

		return (long) val & UInt32.MAX_VAL;
	}

	public int ReadInt32() {
		IntTypeFlag flag = IntTypeFlag.values()[_reader.read()];
		if (flag == IntTypeFlag.Zero) {
			return _defaultInt;
		}
		int ret;
		if ((flag.getVal() & IntTypeFlag.BYTE.getVal()) == IntTypeFlag.BYTE.getVal()) {
			ret = _reader.read();
		} else if ((flag.getVal() & IntTypeFlag.SHORT.getVal()) == IntTypeFlag.SHORT.getVal()) {
			ret = this.ReadRedirectUInt16().getVal();
		} else {
			ret = ReadRedirectInt32();
		}

		boolean isMinus = (flag.getVal() & IntTypeFlag.Minus.getVal()) == IntTypeFlag.Minus.getVal();
		if (isMinus) {
			return -ret;
		}
		return ret;
	}

	public byte[] ReadBytes(int len) {
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) {
			bytes[i] = (byte) _reader.read();
		}
		return bytes;
	}

	public byte[] ReadByteArray() {
		int len = ReadInt32();
		if (len == -1)
			return null;
		return this.ReadBytes(len);
	}

	public String ReadString() throws Exception {
		StringTypeFlag flag = StringTypeFlag.values()[_reader.read()];

		if (flag == StringTypeFlag.NULL)
			return null;
		if (flag == StringTypeFlag.Empty)
			return "";

		int readlen;
		if ((flag.getVal() & StringTypeFlag.ByteLen.getVal()) == StringTypeFlag.ByteLen.getVal()) {
			readlen = _reader.read();
		} else if ((flag.getVal() & StringTypeFlag.ShortLen.getVal()) == StringTypeFlag.ShortLen.getVal()) {
			readlen = this.ReadRedirectInt16();
		} else if ((flag.getVal() & StringTypeFlag.IntLen.getVal()) == StringTypeFlag.IntLen.getVal()) {
			readlen = this.ReadRedirectInt32();
		} else {
			throw new Exception("�ַ�̫��");
		}

		// Encoding encode = Encoding.UTF8;
		// if ((flag.getVal() & StringTypeFlag.AssciiEncoding.getVal()) ==
		// StringTypeFlag.AssciiEncoding.getVal())
		// {
		// encode = Encoding.ASCII;
		// }

		byte[] bytes = ReadBytes(readlen);
		return new String(bytes);
	}

	public String[] ReadStringArray() throws Exception {
		StringTypeFlag flag = StringTypeFlag.values()[_reader.read()];
		if (flag == StringTypeFlag.NULL) {
			return null;
		}
		int len;
		if (flag == StringTypeFlag.ByteLen) {
			len = _reader.read();
		} else if (flag == StringTypeFlag.ShortLen) {
			len = this.ReadRedirectInt16();
		} else {
			len = this.ReadRedirectInt32();
		}

		String[] ret = new String[len];

		for (int i = 0; i < len; i++) {
			ret[i] = ReadString();
		}
		return ret;
	}

	public short[] ReadInt16Array() {
		int len = ReadInt32();
		if (len == -1)
			return null;
		short[] ret = new short[len];
		for (int i = 0; i < len; i++) {
			ret[i] = ReadRedirectInt16();
		}
		return ret;
	}

	public UInt16[] ReadUInt16Array() {
		int len = ReadInt32();
		if (len == -1)
			return null;
		UInt16[] ret = new UInt16[len];
		for (int i = 0; i < len; i++) {
			ret[i] = ReadUInt16();
		}
		return ret;
	}

	public UInt16 ReadUInt16() {
		UShrotTypeEnum flag = UShrotTypeEnum.values()[_reader.read()];
		if (flag == UShrotTypeEnum.Zero) {
			return new UInt16(0);
		}
		UInt16 ret;
		if ((flag.getVal() & UShrotTypeEnum.ByteVal.getVal()) == UShrotTypeEnum.ByteVal.getVal()) {
			ret = new UInt16(_reader.read());
		} else {
			ret = new UInt16(this.ReadRedirectUInt16().getVal());
		}

		return ret;
	}

	public int[] ReadInt32Array() {
		ArrayTypeFlag flag = ArrayTypeFlag.values()[_reader.read()];
		if (flag == ArrayTypeFlag.NULL) {
			return null;
		}
		// ȡ����
		int len;
		if ((flag.getVal() & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag.getVal() & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
			len = this.ReadRedirectInt16();
		} else {
			len = this.ReadRedirectInt32();
		}
		int[] ret = new int[len];
		boolean isCompress = (flag.getVal() & ArrayTypeFlag.Compress.getVal()) == ArrayTypeFlag.Compress.getVal();
		for (int i = 0; i < len; i++) {
			if (isCompress)
				ret[i] = ReadInt32();
			else
				ret[i] = this.ReadRedirectInt32();
		}
		return ret;
	}

	public long ReadInt64() {
		LongTypeEnum flag = LongTypeEnum.values()[_reader.read()];
		if (flag == LongTypeEnum.Zero) {
			return 0L;
		}

		long ret;
		if ((flag.getVal() & LongTypeEnum.ByteVal.getVal()) == LongTypeEnum.ByteVal.getVal()) {
			ret = _reader.read();
		} else if ((flag.getVal() & LongTypeEnum.ShortVal.getVal()) == LongTypeEnum.ShortVal.getVal()) {
			ret = this.ReadRedirectUInt16().getVal();
		} else if ((flag.getVal() & LongTypeEnum.IntVal.getVal()) == LongTypeEnum.IntVal.getVal()) {
			ret = this.ReadRedirectUInt32();
		} else {
			ret = this.ReadRedirectInt64();
		}
		if ((flag.getVal() & LongTypeEnum.Minus.getVal()) == LongTypeEnum.Minus.getVal()) {
			return -ret;
		}
		return ret;
	}

	public long[] ReadInt64Array() {
		ArrayTypeFlag flag = ArrayTypeFlag.values()[_reader.read()];
		if (flag == ArrayTypeFlag.NULL) {
			return null;
		} else if (flag == ArrayTypeFlag.Empty) {
			return new long[0];
		}
		// ȡ����
		int len;
		if ((flag.getVal() & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag.getVal() & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
			len = this.ReadRedirectUInt16().getVal();
		} else {

			len = (int) this.ReadRedirectUInt32();
		}

		long[] ret = new long[len];
		for (int i = 0; i < len; i++) {
			ret[i] = ReadInt64();
		}
		return ret;
	}

	public double[] ReadDoubleArray() {
		int len = this.ReadInt32();
		if (len == -1)
			return null;
		double[] arr = new double[len];
		for (int i = 0; i < len; i++) {
			arr[i] = this.ReadRedirectDouble();
		}
		return arr;
	}

	public DateTime ReadDateTime() {
		double db = this.ReadRedirectDouble();
		return DateTime.FromOaDate(db);
	}

	public DateTime[] ReadDateTimeArray() {
		ArrayTypeFlag flag = ArrayTypeFlag.values()[_reader.read()];
		if (flag == ArrayTypeFlag.NULL) {
			return null;
		} else if (flag == ArrayTypeFlag.Empty) {
			return new DateTime[0];
		}

		int len;
		if ((flag.getVal() & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag.getVal() & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
			len = this.ReadRedirectUInt16().getVal();
		} else {
			len = (int) this.ReadRedirectInt64();
		}

		DateTime[] ret = new DateTime[len];
		for (int i = 0; i < len; i++) {
			double db = this.ReadRedirectDouble();
			ret[i] = DateTime.FromOaDate(db);
		}
		return ret;
	}

	public boolean[] ReadBoolArray() {
		ArrayTypeFlag flag = ArrayTypeFlag.values()[_reader.read()];
		if (flag == ArrayTypeFlag.NULL) {
			return null;
		} else if (flag == ArrayTypeFlag.Empty) {
			return new boolean[0];
		}
		// ȡ����
		int len;
		if ((flag.getVal() & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag.getVal() & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
			len = this.ReadRedirectUInt16().getVal();
		} else {
			len = (int) this.ReadRedirectUInt32();
		}
		boolean[] ret = new boolean[len];
		byte[] byts = this.ReadBytes((int) Math.ceil(len / 8.0));
		BitArray ba = new BitArray(len, byts);

		return ba.toBooleanArray();
	}

	public boolean ReadBool() {
		return _reader.read() != 0;
	}

}