package Ljc.JFramework.TypeUtil;

public class UInt32 {
	public static long MAX_VAL = 4294967295L;
	private int _val = 0;

	public UInt32(long val) {
		_val = (int) val;
	}

	public static UInt32 valueOf(long val) {

		return new UInt32(val);
	}

	public long getVal() {
		if (_val > 0) {
			return _val;
		}

		return _val & MAX_VAL;
	}
}
