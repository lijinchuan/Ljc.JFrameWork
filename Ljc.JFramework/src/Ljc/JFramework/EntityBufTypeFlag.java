package Ljc.JFramework;

enum EntityBufTypeFlag {
	Empty(0),
	/// <summary>
	/// �Ƿ�������
	/// </summary>
	ArrayFlag(1), ListFlag(2),
	/// <summary>
	/// ֵΪnull
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
