package Ljc.JFramework;

public enum EntityType {
	ENUM(1), BYTE(2), BOOL(3), CHAR(4), USHORT(5), SBYTE(6), SHORT(7),
	/// <summary>
	/// ������
	/// </summary>
	INT16(8),
	/// <summary>
	/// 32λ����
	/// </summary>
	INT32(9),
	/// <summary>
	/// 64λ����
	/// </summary>
	INT64(10),
	/// ������
	/// </summary>
	FLOAT(11),
	/// <summary>
	/// decimal
	/// </summary>
	DECIMAL(12),
	/// <summary>
	/// ʱ������
	/// </summary>
	DATETIME(13),
	/// <summary>
	/// ˫����
	/// </summary>
	DOUBLE(14),
	/// <summary>
	/// �ַ���
	/// </summary>
	STRING(15),
	/// <summary>
	/// �ֵ�����
	/// </summary>
	DICTIONARY(16),
	/// <summary>
	/// �б�����
	/// </summary>
	LIST(17), ARRAY(18),
	/// <summary>
	/// ��������
	/// </summary>
	COMPLEX(19),
	/// <summary>
	/// δ֪
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
