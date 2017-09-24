package Ljc.JFramework;

enum ArrayTypeFlag {
	DEFAULT(0), NULL(1), Empty(2), ByteLen(4), ShortLen(8), IntLen(16), Zero(64), Compress(128);

	private ArrayTypeFlag(int val) {
		this.Val = val;
	}

	private int Val = 0;

	public int getVal() {
		return this.Val;
	}
}
