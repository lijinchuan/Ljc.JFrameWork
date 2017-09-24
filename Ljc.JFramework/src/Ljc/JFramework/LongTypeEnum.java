package Ljc.JFramework;

enum LongTypeEnum {
	DEFAULT(0), Zero(1), ByteVal(2), ShortVal(4), IntVal(8), Minus(128);

	private LongTypeEnum(int val) {

		this._val = val;
	}

	private int _val = 0;

	public int getVal() {
		return this._val;
	}
}
