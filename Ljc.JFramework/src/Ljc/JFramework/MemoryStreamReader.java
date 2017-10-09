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
		int flag = _reader.read();
		if (flag == IntTypeFlag.Zero.getVal()) {
			return _defaultInt;
		}
		int ret;
		if ((flag & IntTypeFlag.BYTE.getVal()) == IntTypeFlag.BYTE.getVal()) {
			ret = _reader.read();
		} else if ((flag & IntTypeFlag.SHORT.getVal()) == IntTypeFlag.SHORT.getVal()) {
			ret = this.ReadRedirectUInt16().getVal();
		} else {
			ret = ReadRedirectInt32();
		}

		boolean isMinus = (flag & IntTypeFlag.Minus.getVal()) == IntTypeFlag.Minus.getVal();
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
		int flag = _reader.read();

		if (flag == StringTypeFlag.NULL.getVal())
			return null;
		if (flag == StringTypeFlag.Empty.getVal())
			return "";

		int readlen;
		if ((flag & StringTypeFlag.ByteLen.getVal()) == StringTypeFlag.ByteLen.getVal()) {
			readlen = _reader.read();
		} else if ((flag & StringTypeFlag.ShortLen.getVal()) == StringTypeFlag.ShortLen.getVal()) {
			readlen = this.ReadRedirectInt16();
		} else if ((flag & StringTypeFlag.IntLen.getVal()) == StringTypeFlag.IntLen.getVal()) {
			readlen = this.ReadRedirectInt32();
		} else {
			throw new Exception("字符太长");
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
		int flag = _reader.read();
		if (flag == StringTypeFlag.NULL.getVal()) {
			return null;
		}
		int len;
		if (flag == StringTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if (flag == StringTypeFlag.ShortLen.getVal()) {
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

	public short ReadInt16() {
		int flag = _reader.read();
		if (flag == ShortTypeEnum.Zero.getVal()) {
			return _defaultShort;
		}
		short ret;
		if ((flag & ShortTypeEnum.ByteVal.getVal()) == ShortTypeEnum.ByteVal.getVal()) {
			ret = (short) _reader.read();
		} else {
			ret = this.ReadRedirectInt16();
		}

		if ((flag & ShortTypeEnum.Minus.getVal()) == ShortTypeEnum.Minus.getVal()) {
			return (short) (-ret);
		}

		return ret;
	}

	public short[] ReadInt16Array() {
		int len = ReadInt32();
		if (len == -1)
			return null;
		short[] ret = new short[len];
		for (int i = 0; i < len; i++) {
			ret[i] = ReadInt16();
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
		int flag = _reader.read();
		if (flag == UShrotTypeEnum.Zero.getVal()) {
			return new UInt16(0);
		}
		UInt16 ret;
		if ((flag & UShrotTypeEnum.ByteVal.getVal()) == UShrotTypeEnum.ByteVal.getVal()) {
			ret = new UInt16(_reader.read());
		} else {
			ret = new UInt16(this.ReadRedirectUInt16().getVal());
		}

		return ret;
	}

	public int[] ReadInt32Array() {
		int flag = _reader.read();
		if (flag == ArrayTypeFlag.NULL.getVal()) {
			return null;
		}
		// 取长度
		int len;
		if ((flag & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
			len = this.ReadRedirectInt16();
		} else {
			len = this.ReadRedirectInt32();
		}
		int[] ret = new int[len];
		boolean isCompress = (flag & ArrayTypeFlag.Compress.getVal()) == ArrayTypeFlag.Compress.getVal();
		for (int i = 0; i < len; i++) {
			if (isCompress)
				ret[i] = ReadInt32();
			else
				ret[i] = this.ReadRedirectInt32();
		}
		return ret;
	}

	public long ReadInt64() {
		int flag = _reader.read();
		if (flag == LongTypeEnum.Zero.getVal()) {
			return 0L;
		}

		long ret;
		if ((flag & LongTypeEnum.ByteVal.getVal()) == LongTypeEnum.ByteVal.getVal()) {
			ret = _reader.read();
		} else if ((flag & LongTypeEnum.ShortVal.getVal()) == LongTypeEnum.ShortVal.getVal()) {
			ret = this.ReadRedirectUInt16().getVal();
		} else if ((flag & LongTypeEnum.IntVal.getVal()) == LongTypeEnum.IntVal.getVal()) {
			ret = this.ReadRedirectUInt32();
		} else {
			ret = this.ReadRedirectInt64();
		}
		if ((flag & LongTypeEnum.Minus.getVal()) == LongTypeEnum.Minus.getVal()) {
			return -ret;
		}
		return ret;
	}

	public long[] ReadInt64Array() {
		int flag = _reader.read();
		if (flag == ArrayTypeFlag.NULL.getVal()) {
			return null;
		} else if (flag == ArrayTypeFlag.Empty.getVal()) {
			return new long[0];
		}
		// 取长度
		int len;
		if ((flag & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
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
		int flag = _reader.read();
		if (flag == ArrayTypeFlag.NULL.getVal()) {
			return null;
		} else if (flag == ArrayTypeFlag.Empty.getVal()) {
			return new DateTime[0];
		}

		int len;
		if ((flag & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
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
		int flag = _reader.read();
		if (flag == ArrayTypeFlag.NULL.getVal()) {
			return null;
		} else if (flag == ArrayTypeFlag.Empty.getVal()) {
			return new boolean[0];
		}
		// 取长度
		int len;
		if ((flag & ArrayTypeFlag.ByteLen.getVal()) == ArrayTypeFlag.ByteLen.getVal()) {
			len = _reader.read();
		} else if ((flag & ArrayTypeFlag.ShortLen.getVal()) == ArrayTypeFlag.ShortLen.getVal()) {
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

	public float ReadRedirectFloat() {
		return BitConverter.GetFloat(this.ReadBytes(4));
	}

	public float[] ReadFloatArray() {
		int len = this.ReadInt32();
		if (len == -1)
			return null;
		float[] arr = new float[len];
		for (int i = 0; i < len; i++) {
			arr[i] = ReadRedirectFloat();
		}
		return arr;
	}

	public BigDecimal ReadDecimal() {
		int flag = _reader.read();
		if (flag == DecimalTypeFlag.Zero.getVal()) {
			return _defaultDecimal;
		}
		boolean isMinus = (flag & DecimalTypeFlag.Minus.getVal()) == DecimalTypeFlag.Minus.getVal();

		BigDecimal ret;
		if ((flag & DecimalTypeFlag.ByteVal.getVal()) == DecimalTypeFlag.ByteVal.getVal()) {
			ret = BigDecimal.valueOf(_reader.read());
		} else if ((flag & DecimalTypeFlag.ShortVal.getVal()) == DecimalTypeFlag.ShortVal.getVal()) {
			ret = BigDecimal.valueOf(this.ReadUInt16().getVal());
		} else if ((flag & DecimalTypeFlag.IntVal.getVal()) == DecimalTypeFlag.IntVal.getVal()) {
			ret = BigDecimal.valueOf(this.ReadRedirectUInt32());
		} else if ((flag & DecimalTypeFlag.Int64Val.getVal()) == DecimalTypeFlag.Int64Val.getVal()) {
			ret = BigDecimal.valueOf(this.ReadRedirectInt64());
		} else if ((flag & DecimalTypeFlag.FloatVal.getVal()) == DecimalTypeFlag.FloatVal.getVal()) {
			ret = BigDecimal.valueOf(ReadRedirectFloat());
		} else {
			ret = BigDecimal.valueOf(this.ReadRedirectDouble());
		}
		if (isMinus) {
			ret = ret.multiply(BigDecimal.valueOf(-1));
		}
		return ret;
	}

	public BigDecimal[] ReadDeciamlArray() {
		int len = this.ReadInt32();
		if (len == -1)
			return null;
		BigDecimal[] arr = new BigDecimal[len];
		for (int i = 0; i < len; i++) {
			arr[i] = this.ReadDecimal();
		}
		return arr;
	}

	public char ReadRedirectChar() {
		return BitConverter.GetChar(this.ReadBytes(2));
	}

	public char[] ReadCharArray() {
		int len = this.ReadInt32();
		if (len == -1)
			return null;
		char[] arr = new char[len];
		for (int i = 0; i < len; i++) {
			arr[i] = ReadRedirectChar();
		}
		return arr;
	}

}
