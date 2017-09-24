package Ljc.JFramework;

enum DoubleTypeFlag {
	DEFAULT(0), ByteVal(1), ShortVal(2), IntVal(4), FloatVal(8), Minus(128);

	private DoubleTypeFlag(int val) {
		this._val = val;
	}

	private int _val = 0;

	public int getVal() {
		return this._val;
	}
}
