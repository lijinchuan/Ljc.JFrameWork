package Ljc.JFramework.TypeUtil;

public class UShort {
	public static int MAX_VAL = 65535;
	private short _val = 0;

	public UShort(int val) {
		_val = (short) val;
	}

	public static UShort valueOf(int val) {

		return new UShort(val);
	}

	public int getVal() {
		if (_val > 0) {
			return _val;
		}

		return _val & MAX_VAL;
	}

}
