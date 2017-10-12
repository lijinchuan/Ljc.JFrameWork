package Ljc.JFramework.SocketApplication;

public enum MessageType {
	HEARTBEAT(0), TEST_REQUEST(1), RESEND_REQUEST(2), REJECT(3), SEQUENCE_RESET(4), LOGIN(5), LOGOUT(
			6), INDICATION_OF_INTEREST(
					7), ADVERTISEMENT(8), EXECUTION_REPORT(9), ORDER_CANCEL_REJECT(10), UAN(11), UAP(12),
	/// <summary>
	/// 重登陆
	/// </summary>
	RELOGIN(13),
	/// <summary>
	/// 致命错误
	/// </summary>
	FATAL(14), UDPECHO(15),
	/// <summary>
	/// UPD询问包数据
	/// </summary>
	UDPQUERYBAG(16),
	/// <summary>
	/// UPD回应查询包数据
	/// </summary>
	UDPANSWERBAG(17),
	/// <summary>
	/// 设置MTU参数
	/// </summary>
	UPDSETMTU(18),
	/// <summary>
	/// 发送文件
	/// </summary>
	SENDFILE(19), SENDFILEECHO(20),
	/// <summary>
	/// 清理bagid
	/// </summary>
	UDPCLEARBAGID(21),
	/// <summary>
	/// 断点续传检查
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
