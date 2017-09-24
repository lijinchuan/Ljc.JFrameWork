package Ljc.JFramework;

enum EntityBufTypeFlag {
	Empty(0),
	/// <summary>
	/// 是否是数组
	/// </summary>
	ArrayFlag(1), ListFlag(2),
	/// <summary>
	/// 值为null
	/// </summary>
	VlaueNull(4);

	private EntityBufTypeFlag(int val) {
		this.Val = val;
	}

	private int Val = 0;

	public int getVal() {
		return this.Val;
	}
}
