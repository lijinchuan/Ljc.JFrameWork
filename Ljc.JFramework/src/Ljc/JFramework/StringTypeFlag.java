package Ljc.JFramework;

enum StringTypeFlag {
	DEFAULT(0), NULL(1), Empty(2), ByteLen(4), ShortLen(8), IntLen(16), LongLen(32), AssciiEncoding(64), UTF8Encoding(
			128);

	private StringTypeFlag(int val) {
		this.Val = val;
	}

	private int Val = 0;

	public int getVal() {
		return this.Val;
	}
}
