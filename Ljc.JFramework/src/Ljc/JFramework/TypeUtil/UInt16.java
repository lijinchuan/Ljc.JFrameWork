package Ljc.JFramework.TypeUtil;

public class UInt16 {
	public static int MAX_VAL = 65535;
	private short _val = 0;

	public UInt16(int val) {
		_val = (short) val;
	}

	public static UInt16 valueOf(int val) {

		return new UInt16(val);
	}

	public int getVal() {
		if (_val > 0) {
			return _val;
		}

		return _val & MAX_VAL;
	}
}
