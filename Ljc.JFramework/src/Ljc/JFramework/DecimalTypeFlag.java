package Ljc.JFramework;

enum DecimalTypeFlag {
	DEFAULT(0), ByteVal(1), ShortVal(2), IntVal(4), FloatVal(8), DoubleVal(16), Int64Val(32), Zero(64), Minus(128);

	private DecimalTypeFlag(int val) {
		this._val = val;
	}

	private int _val = 0;

	public int getVal() {
		return this._val;
	}
}
