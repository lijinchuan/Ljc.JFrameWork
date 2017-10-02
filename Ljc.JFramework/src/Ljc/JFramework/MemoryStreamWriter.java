package Ljc.JFramework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import Ljc.JFramework.TypeUtil.DateTime;
import Ljc.JFramework.TypeUtil.UInt16;
import Ljc.JFramework.Utility.BitArray;
import Ljc.JFramework.Utility.BitConverter;

public class MemoryStreamWriter {
	private ByteArrayOutputStream _ms = null;
	private long position = 0;
	private static final byte[] bytesNull = BitConverter.GetBytes(-1);
	private static final byte[] bytesTrue = BitConverter.GetBytes(true);
	private static final byte[] bytesFalse = BitConverter.GetBytes(false);

	public MemoryStreamWriter(ByteArrayOutputStream ms) {
		this._ms = ms;
	}

	private byte[] CompressInt32(int num) {
		byte[] byts = null;

		if (num >= 0) {
			if (num <= Byte.MAX_VALUE) {
				byts = new byte[] { (byte) num };
			} else if (num <= Short.MAX_VALUE) {
				byts = BitConverter.GetBytes((short) num);
			}
		}
		if (byts == null)
			byts = BitConverter.GetBytes(num);
		return byts;
	}

	public void write(byte[] b) throws IOException {
		this._ms.write(b);
	}

	@Deprecated
	public void write(int b) {
		this._ms.write(b);
	}

	public void WriteString(String str) throws Exception {
		if (str == null) {
			_ms.write((byte) StringTypeFlag.NULL.getVal());
			return;
		} else if (str.isEmpty()) {
			_ms.write((byte) StringTypeFlag.Empty.getVal());
			return;
		}

		byte[] byts;
		int flag = StringTypeFlag.DEFAULT.getVal();

		// bool isAsscii = StringHelper.IsAscii(str);
		boolean isAsscii = false;
		if (isAsscii) {
			flag = StringTypeFlag.AssciiEncoding.getVal();
			byts = BitConverter.GetBytes(str);
		} else {
			flag = StringTypeFlag.UTF8Encoding.getVal();
			byts = BitConverter.GetBytes(str);
		}

		byte[] lenbytes = null;
		if (byts.length <= Byte.MAX_VALUE) {
			flag |= StringTypeFlag.ByteLen.getVal();
			lenbytes = new byte[] { (byte) byts.length };
		} else if (byts.length <= Short.MAX_VALUE) {
			flag |= StringTypeFlag.ShortLen.getVal();
			lenbytes = BitConverter.GetBytes((short) byts.length);
		} else if (byts.length <= Integer.MAX_VALUE) {
			flag |= StringTypeFlag.IntLen.getVal();
			lenbytes = BitConverter.GetBytes(byts.length);
		} else {
			throw new Exception("×Ö·û´®Ì«³¤¡£");
		}

		_ms.write((byte) flag);
		_ms.write(lenbytes, 0, lenbytes.length);

		_ms.write(byts, 0, byts.length);

	}

	public void WriteStringArray(String[] strArray) throws Exception {
		if (strArray == null) {
			_ms.write((byte) StringTypeFlag.NULL.getVal());
			return;
		}

		byte[] lenbytes = CompressInt32(strArray.length);
		if (lenbytes.length == 1) {
			_ms.write((byte) StringTypeFlag.ByteLen.getVal());
		} else if (lenbytes.length == 2) {
			_ms.write((byte) StringTypeFlag.ShortLen.getVal());
		} else {
			_ms.write((byte) StringTypeFlag.IntLen.getVal());
		}

		_ms.write(lenbytes, 0, lenbytes.length);

		for (String s : strArray) {
			WriteString(s);
		}
	}

	public void WriteInt32(int num) {
		if (num == 0) {
			_ms.write((byte) IntTypeFlag.Zero.getVal());
			return;
		}
		int flag = IntTypeFlag.DEFAULT.getVal();
		if (num < 0) {
			flag = IntTypeFlag.Minus.getVal();
			num = Math.abs(num);
		}
		byte[] byts = null;

		if (num <= Byte.MAX_VALUE) {
			byts = new byte[] { (byte) num };
			flag |= IntTypeFlag.BYTE.getVal();
		} else if (num <= 65535) {
			byts = BitConverter.GetBytes((short) num);
			flag |= IntTypeFlag.SHORT.getVal();
		}

		if (byts == null) {
			byts = BitConverter.GetBytes(num);
			flag |= IntTypeFlag.INT.getVal();
		}

		_ms.write((byte) flag);
		_ms.write(byts, 0, byts.length);
	}

	public void WriteBytes(byte[] data) {
		_ms.write(data, 0, data.length);
	}

	public void WriteByte(byte data) {
		_ms.write(data);
	}

	public void WriteByteArray(byte[] data) {
		if (data == null) {
			this.WriteInt32(-1);
		} else {
			this.WriteInt32(data.length);
			this.WriteBytes(data);
		}
	}

	public void WriteInt16(short num) {
		if (num == 0) {
			_ms.write((byte) ShortTypeEnum.Zero.getVal());
			return;
		}

		int flag = ShortTypeEnum.DEFAULT.getVal();

		if (num < 0) {
			flag = flag | ShortTypeEnum.Minus.getVal();
			num = (short) Math.abs(num);
		}

		byte[] bytes;
		if (num <= Byte.MAX_VALUE) {
			flag = flag | ShortTypeEnum.ByteVal.getVal();
			bytes = new byte[] { (byte) num };
		} else {
			bytes = BitConverter.GetBytes(num);
		}
		_ms.write((byte) flag);
		_ms.write(bytes, 0, bytes.length);
	}

	public void WriteInt16Array(short[] numArray) {
		if (numArray == null) {
			_ms.write(bytesNull, 0, 4);
			return;
		}

		int len = numArray.length;
		WriteInt32(len);
		for (short i : numArray) {
			WriteInt16(i);
		}
	}

	public void WriteUInt16(UInt16 num) {
		if (num == null || num.getVal() == 0) {
			_ms.write((byte) UShrotTypeEnum.Zero.getVal());
			return;
		}

		int flag = UShrotTypeEnum.DEFAULT.getVal();

		byte[] bytes;
		if (num.getVal() <= Byte.MAX_VALUE) {
			flag = flag | UShrotTypeEnum.ByteVal.getVal();
			bytes = new byte[] { (byte) num.getVal() };
		} else {
			bytes = BitConverter.GetBytes((short) num.getVal());
		}
		_ms.write((byte) flag);
		_ms.write(bytes, 0, bytes.length);
	}

	public void WriteUInt16Array(UInt16[] numArray) {
		if (numArray == null) {
			_ms.write(bytesNull, 0, 4);
			return;
		}

		int len = numArray.length;
		WriteInt32(len);
		for (UInt16 i : numArray) {
			WriteUInt16(i);
		}
	}

	public void WriteChar(char num) throws IOException {
		_ms.write(Ljc.JFramework.Utility.BitConverter.GetBytes(num));
	}

	public void WriteCharArray(char[] numArray) throws IOException {
		if (numArray == null) {
			_ms.write(bytesNull, 0, 4);
			return;
		}

		int len = numArray.length;
		WriteInt32(len);
		for (char i : numArray) {
			WriteChar(i);
		}
	}

	public void WriteInt32Array(int[] numArray) {
		if (numArray == null) {
			_ms.write((byte) ArrayTypeFlag.NULL.getVal());
			return;
		}

		boolean isCompress = false;
		int flag = (isCompress ? ArrayTypeFlag.Compress : ArrayTypeFlag.DEFAULT).getVal();
		byte[] bytelen = CompressInt32(numArray.length);
		if (bytelen.length == 1) {
			_ms.write((byte) (ArrayTypeFlag.ByteLen.getVal() | flag));
		} else if (bytelen.length == 2) {
			_ms.write((byte) (ArrayTypeFlag.ShortLen.getVal() | flag));
		} else {
			_ms.write((byte) (ArrayTypeFlag.IntLen.getVal() | flag));
		}
		_ms.write(bytelen, 0, bytelen.length);
		for (int num : numArray) {
			if (!isCompress) {
				_ms.write(BitConverter.GetBytes(num), 0, 4);
			} else {
				WriteInt32(num);
			}
		}
	}

	public void WriteDecimal(BigDecimal data) {
		if (data == null || data == BigDecimal.ZERO) {
			_ms.write((byte) DecimalTypeFlag.Zero.getVal());
			return;
		}
		int flag = DecimalTypeFlag.DEFAULT.getVal();
		if (data.compareTo(BigDecimal.ZERO) < 0) {
			flag = DecimalTypeFlag.Minus.getVal();
			data = data.abs();
		}
		byte[] byts = null;

		BigDecimal mod = data.remainder(BigDecimal.ONE);
		if (BigDecimal.ZERO == mod) {
			if (data.compareTo(BigDecimal.valueOf(Byte.MAX_VALUE)) == -1) {
				flag |= DecimalTypeFlag.ByteVal.getVal();
				byts = new byte[] { data.byteValue() };
			} else if (data.compareTo(BigDecimal.valueOf(Short.MAX_VALUE)) == -1) {
				flag |= DecimalTypeFlag.ShortVal.getVal();
				byts = BitConverter.GetBytes(data.shortValue());
			} else if (data.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) == -1) {
				flag |= DecimalTypeFlag.IntVal.getVal();
				byts = BitConverter.GetBytes(data.intValue());
			} else if (data.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) == -1) {
				flag |= DecimalTypeFlag.Int64Val.getVal();
				byts = BitConverter.GetBytes(data.longValue());
			}
		} else if ((mod.multiply(BigDecimal.valueOf(10)).remainder(BigDecimal.ONE) == BigDecimal.ZERO
				&& data.compareTo(BigDecimal.valueOf(9999999)) == -1)
				|| (mod.multiply(BigDecimal.valueOf(100)).remainder(BigDecimal.ONE) == BigDecimal.ZERO
						&& data.compareTo(BigDecimal.valueOf(999999)) == -1)
				|| (mod.multiply(BigDecimal.valueOf(1000)).remainder(BigDecimal.ONE) == BigDecimal.ZERO
						&& data.compareTo(BigDecimal.valueOf(99999)) == -1)
				|| (mod.multiply(BigDecimal.valueOf(10000)).remainder(BigDecimal.ONE) == BigDecimal.ZERO
						&& data.compareTo(BigDecimal.valueOf(9999)) == -1)
				|| (mod.multiply(BigDecimal.valueOf(100000)).remainder(BigDecimal.ONE) == BigDecimal.ZERO
						&& data.compareTo(BigDecimal.valueOf(999)) == -1)
				|| (mod.multiply(BigDecimal.valueOf(1000000)).remainder(BigDecimal.ONE) == BigDecimal.ZERO
						&& data.compareTo(BigDecimal.valueOf(99)) == -1)
				|| (mod.multiply(BigDecimal.valueOf(10000000)).remainder(BigDecimal.ONE) == BigDecimal.ZERO
						&& data.compareTo(BigDecimal.valueOf(9)) == -1)) {
			flag |= DecimalTypeFlag.FloatVal.getVal();
			byts = BitConverter.GetBytes(data.floatValue());
		} else {
			flag |= DecimalTypeFlag.DoubleVal.getVal();
			byts = BitConverter.GetBytes(data.doubleValue());
		}

		_ms.write((byte) flag);
		_ms.write(byts, 0, byts.length);

	}

	public void WriteDeciamlArray(BigDecimal[] data) {
		if (data == null) {
			_ms.write(bytesNull, 0, 4);
			return;
		}

		_ms.write(BitConverter.GetBytes(data.length), 0, 4);
		for (BigDecimal d : data) {
			WriteDecimal(d);
		}
	}

	public void WriteFloat(float data) {
		byte[] bytes = BitConverter.GetBytes(data);

		_ms.write(bytes, 0, bytes.length);
	}

	public void WriteFloatArray(float[] data) {
		if (data == null || data.length == 0) {
			WriteInt32(-1);
			// WriteInt32(bytesNull, 0, 4);
			return;
		}
		WriteInt32(data.length);
		for (float d : data) {
			WriteFloat(d);
		}
	}

	public void WriteDoubleArray(double[] data) {
		if (data == null || data.length == 0) {
			WriteInt32(-1);
			// WriteInt32(bytesNull, 0, 4);
			return;
		}
		WriteInt32(data.length);
		for (double d : data) {
			WriteDouble(d);
		}
	}

	public void WriteDouble(double data) {
		byte[] bytes = BitConverter.GetBytes(data);

		_ms.write(bytes, 0, bytes.length);

		// DoubleTypeFlag flag = DoubleTypeFlag.DEFAULT;
		// if (data < 0.0)
		// {
		// flag = DoubleTypeFlag.Minus;
		// data = Math.Abs(data);
		// }
		// byte[] buffer = null;
		// int mod = (int)(data % 1);
		// if (mod == 0)
		// {
		// if (data <= byte.MaxValue)
		// {
		// flag |= DoubleTypeFlag.ByteVal;
		// buffer = new byte[] { (byte)flag };
		// }
		// else if (data <= ushort.MaxValue)
		// {
		// flag |= DoubleTypeFlag.ShortVal;
		// buffer = BitConverter.GetBytes((ushort)data);
		// }
		// else if (data <= UInt32.MaxValue)
		// {
		// flag |= DoubleTypeFlag.IntVal;
		// buffer = BitConverter.GetBytes((UInt32)data);
		// }
		// }
		// else if (((data < 10000000) && ((mod * 10) % 1 == 0))
		// ||((data < 1000000) && ((mod * 100) % 1 == 0))
		// ||((data < 100000) && ((mod * 1000) % 1 == 0))
		// ||((data < 10000) && ((mod * 10000) % 1 == 0))
		// ||((data < 1000) && ((mod * 100000) % 1 == 0))
		// ||((data < 100) && ((mod * 1000000) % 1 == 0))
		// ||((data < 10) && ((mod * 10000000) % 1 == 0)))
		// {
		// flag |= DoubleTypeFlag.FloatVal;
		// buffer = BitConverter.GetBytes((float)data);
		// }

		// if (buffer == null)
		// {
		// buffer = BitConverter.GetBytes(data);
		// flag = DoubleTypeFlag.DEFAULT;
		// }
		// _ms.WriteByte((byte)flag);
		// _ms.Write(buffer, 0, buffer.Length);
	}

	public void WriteInt64(long num) {

		if (num == 0L) {
			_ms.write((byte) LongTypeEnum.Zero.getVal());
			return;
		}
		int flag = LongTypeEnum.DEFAULT.getVal();
		if (num < 0) {
			flag = LongTypeEnum.Minus.getVal();
			num = Math.abs(num);
		}

		byte[] byts = null;
		if (num <= Byte.MAX_VALUE) {
			flag |= LongTypeEnum.ByteVal.getVal();
			byts = new byte[] { (byte) num };
		} else if (num < Short.MAX_VALUE) {
			flag |= LongTypeEnum.ShortVal.getVal();
			byts = BitConverter.GetBytes((short) num);
		} else if (num < Integer.MAX_VALUE) {
			flag |= LongTypeEnum.IntVal.getVal();
			byts = BitConverter.GetBytes((int) num);
		} else {
			byts = BitConverter.GetBytes(num);
		}
		_ms.write((byte) flag);
		_ms.write(byts, 0, byts.length);
	}

	public void WriteInt64Array(long[] intArray) {
		if (intArray == null) {
			_ms.write((byte) ArrayTypeFlag.NULL.getVal());
			return;
		} else if (intArray.length == 0) {
			_ms.write((byte) ArrayTypeFlag.Empty.getVal());
			return;
		}
		int flag = ArrayTypeFlag.DEFAULT.getVal();
		int len = intArray.length;
		byte[] byts = CompressInt32(len);
		if (byts.length == 1) {
			flag |= ArrayTypeFlag.ByteLen.getVal();
		} else if (byts.length == 2) {
			flag |= ArrayTypeFlag.ShortLen.getVal();
		}

		_ms.write((byte) flag);
		_ms.write(byts, 0, byts.length);
		for (long num : intArray) {
			WriteInt64(num);
		}
	}

	public void WriteDateTimeArray(Date[] dateTimes) {
		if (dateTimes == null) {
			_ms.write((byte) ArrayTypeFlag.NULL.getVal());
			return;
		} else if (dateTimes.length == 0) {
			_ms.write((byte) ArrayTypeFlag.Empty.getVal());
			return;
		}

		int flag = ArrayTypeFlag.DEFAULT.getVal();
		int len = dateTimes.length;
		byte[] bytslen = CompressInt32(len);
		if (bytslen.length == 1) {
			flag = ArrayTypeFlag.ByteLen.getVal();
		} else if (bytslen.length == 2) {
			flag = ArrayTypeFlag.ShortLen.getVal();
		}

		_ms.write((byte) flag);
		_ms.write(bytslen, 0, bytslen.length);
		for (Date dt : dateTimes) {
			byte[] byts = BitConverter.GetBytes(DateTime.ToOADate(dt));
			_ms.write(byts, 0, byts.length);
		}
	}

	public void WriteDateTime(Date dateTime) {
		byte[] byts = BitConverter.GetBytes(DateTime.ToOADate(dateTime));

		_ms.write(byts, 0, byts.length);
	}

	public void WriteBoolArray(boolean[] booArray) {
		if (booArray == null) {
			_ms.write((byte) ArrayTypeFlag.NULL.getVal());
			return;
		} else if (booArray.length == 0) {
			_ms.write((byte) ArrayTypeFlag.Empty.getVal());
			return;
		}

		int flag = ArrayTypeFlag.DEFAULT.getVal();
		int len = booArray.length;
		byte[] bytelen = CompressInt32(len);
		if (bytelen.length == 1) {
			flag = ArrayTypeFlag.ByteLen.getVal();
		} else if (bytelen.length == 2) {
			flag = ArrayTypeFlag.ShortLen.getVal();
		}

		_ms.write((byte) flag);
		_ms.write(bytelen, 0, bytelen.length);
		BitArray ba = new BitArray(booArray);

		for (byte b : ba.toByteArray()) {
			_ms.write(b);
		}
	}

	public void WriteBool(boolean boo) {
		if (boo) {
			_ms.write(bytesTrue, 0, bytesTrue.length);
		} else {
			_ms.write(bytesFalse, 0, bytesFalse.length);
		}

		// byte[] bts = BitConverter.GetBytes(boo);
		// _ms.Write(bts, 0, bts.Length);
	}

	public byte[] GetBytes() {
		return this._ms.toByteArray();
	}
}
