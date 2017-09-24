package Ljc.JFramework;

public enum EntityType {
	ENUM(1), BYTE(2), BOOL(3), CHAR(4), USHORT(5), SBYTE(6), SHORT(7),
	/// <summary>
	/// 短整形
	/// </summary>
	INT16(8),
	/// <summary>
	/// 32位整形
	/// </summary>
	INT32(9),
	/// <summary>
	/// 64位整形
	/// </summary>
	INT64(10),
	/// 浮点数
	/// </summary>
	FLOAT(11),
	/// <summary>
	/// decimal
	/// </summary>
	DECIMAL(12),
	/// <summary>
	/// 时间类型
	/// </summary>
	DATETIME(13),
	/// <summary>
	/// 双精度
	/// </summary>
	DOUBLE(14),
	/// <summary>
	/// 字符串
	/// </summary>
	STRING(15),
	/// <summary>
	/// 字典类型
	/// </summary>
	DICTIONARY(16),
	/// <summary>
	/// 列表类型
	/// </summary>
	LIST(17), ARRAY(18),
	/// <summary>
	/// 复杂类型
	/// </summary>
	COMPLEX(19),
	/// <summary>
	/// 未知
	/// </summary>
	UNKNOWN(20);

	private EntityType(int val) {
		this.Val = val;
	}

	private int Val = 0;

	public int getVal() {
		return this.Val;
	}
}
