package Ljc.JFramework.TypeUtil;

public class UByte {
	public static short MAX_VAL = 255;
	private byte _val = 0;

	public UByte(short val) {
		_val = (byte) val;
	}

	public static UByte valueOf(int val) {

		return new UByte((short) val);
	}

	public short getVal() {
		if (_val > 0) {
			return _val;
		}

		return (short) (_val & MAX_VAL);
	}
}
