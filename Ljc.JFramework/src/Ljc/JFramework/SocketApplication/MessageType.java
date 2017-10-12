package Ljc.JFramework.SocketApplication;

public enum MessageType {
	HEARTBEAT(0), TEST_REQUEST(1), RESEND_REQUEST(2), REJECT(3), SEQUENCE_RESET(4), LOGIN(5), LOGOUT(
			6), INDICATION_OF_INTEREST(
					7), ADVERTISEMENT(8), EXECUTION_REPORT(9), ORDER_CANCEL_REJECT(10), UAN(11), UAP(12),
	/// <summary>
	/// �ص�½
	/// </summary>
	RELOGIN(13),
	/// <summary>
	/// ��������
	/// </summary>
	FATAL(14), UDPECHO(15),
	/// <summary>
	/// UPDѯ�ʰ�����
	/// </summary>
	UDPQUERYBAG(16),
	/// <summary>
	/// UPD��Ӧ��ѯ������
	/// </summary>
	UDPANSWERBAG(17),
	/// <summary>
	/// ����MTU����
	/// </summary>
	UPDSETMTU(18),
	/// <summary>
	/// �����ļ�
	/// </summary>
	SENDFILE(19), SENDFILEECHO(20),
	/// <summary>
	/// ����bagid
	/// </summary>
	UDPCLEARBAGID(21),
	/// <summary>
	/// �ϵ��������
	/// </summary>
	SENDFILRESENDCHECK(22);

	private int _val;

	private MessageType(int value) {
		this._val = value;
	}

	public int getVal() {
		return this._val;
	}
}
