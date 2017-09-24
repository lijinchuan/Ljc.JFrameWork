package Ljc.JFramework;

enum UShrotTypeEnum {
	DEFAULT(0), Zero(1), ByteVal(2);

	private UShrotTypeEnum(int val) {
		this._val = val;
	}

	private int _val = 0;

	public int getVal() {
		return this._val;
	}
}
