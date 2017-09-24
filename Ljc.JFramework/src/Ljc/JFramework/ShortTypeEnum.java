package Ljc.JFramework;

enum ShortTypeEnum {
	DEFAULT(0), Zero(1), ByteVal(2), Minus(128);

	private ShortTypeEnum(int val) {
		this._val = val;
	}

	private int _val = 0;

	public int getVal() {
		return this._val;
	}
}
