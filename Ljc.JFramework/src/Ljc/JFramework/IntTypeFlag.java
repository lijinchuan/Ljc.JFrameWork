package Ljc.JFramework;

enum IntTypeFlag {
	DEFAULT(0), NULL(1), BYTE(2), SHORT(4), INT(8), Zero(64), Minus(128);

	private IntTypeFlag(int val) {
		this.Val = val;
	}

	private int Val = 0;

	public int getVal() {
		return this.Val;
	}
}
