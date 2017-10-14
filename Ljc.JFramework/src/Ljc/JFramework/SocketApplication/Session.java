package Ljc.JFramework.SocketApplication;

import java.io.IOException;
import java.util.Date;

public class Session {
	private String _sessionID;

	public String getSessionID() {
		return this._sessionID;
	}

	public void setSessionID(String value) {
		this._sessionID = value;
	}

	private String _userName;

	public String getUserName() {
		return this._userName;
	}

	public void setUserName(String value) {
		this._userName = value;
	}

	private boolean _isValid;

	public boolean getIsValid() {
		return this._isValid;
	}

	public void setIsValid(boolean value) {
		this._isValid = value;
	}

	private boolean _isLogin;

	public boolean getIsLogin() {
		return this._isLogin;
	}

	public void setIsLogin(boolean value) {
		this._isLogin = value;
	}

	private java.net.Socket _socket;

	public java.net.Socket getSocket() {
		return this._socket;
	}

	void setSocket(java.net.Socket value) {
		this._socket = value;
	}

	private Date _connectTime;

	/// <summary>
	/// ����ʱ��
	/// </summary>
	public Date getConnectTime() {
		return this._connectTime;
	}

	public void setConnectTime(Date value) {
		this._connectTime = value;
	}

	private long _lastSessionTime;

	/// <summary>
	/// �ϴ�����ʱ��
	/// </summary>
	public long getLastSessionTime() {

		return _lastSessionTime;
	}

	public void setLastSessionTime(long value) {
		this._lastSessionTime = value;
	}

	private Date _businessTimeStamp;

	/// <summary>
	/// ҵ��ʱ���
	/// </summary>
	public Date getBusinessTimeStamp() {
		return this._businessTimeStamp;
	}

	void setBusinessTimeStamp(Date value) {
		this._businessTimeStamp = value;
	}

	private Object _tag;

	public Object getTag() {
		return this._tag;
	}

	public void setTag(Object value) {
		this._tag = value;
	}

	private int _headBeatInterVal;

	/// <summary>
	/// �������
	/// </summary>
	public int getHeadBeatInterVal() {
		return this._headBeatInterVal;
	}

	public void setHeadBeatInterVal(int value) {
		this._headBeatInterVal = value;
	}

	private String _iPAddress;

	public String getIPAddress() {
		return this._iPAddress;
	}

	void getIPAddress(String value) {
		this._iPAddress = value;
	}

	private int _port;

	public int getPort() {
		return this._port;
	}

	void setPort(int value) {
		this._port = value;
	}

	private int _sessionTimeOut;

	/// <summary>
	/// �Ự��ʱ
	/// </summary>
	public int getSessionTimeOut() {
		return this._sessionTimeOut;
	}

	public void setSessionTimeOut(int value) {
		this._sessionTimeOut = value;
	}

	private long _bytesRev;

	public long getBytesRev() {
		return this._bytesRev;
	}

	public void setBytesRev(long value) {
		this._bytesRev = value;
	}

	private long _bytesSend;

	public long getBytesSend() {
		return this._bytesSend;
	}

	public void setBytesSend(long value) {
		this._bytesSend = value;
	}

	public Session() {
		_headBeatInterVal = 10000;
		_sessionTimeOut = 30000;
	}

	public boolean IsTimeOut() {
		return System.currentTimeMillis() - _lastSessionTime > _sessionTimeOut * 2;
	}

	public void Close() {
		if (this._socket != null && this._socket.isConnected()) {
			try {
				this._socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this._isValid = false;
	}

	public boolean SendMessage(Message msg) throws Exception {
		if (this._socket == null)
			throw new Exception("���׽���");

		int sendcount = SocketApplicationComm.SendMessage(this._socket, msg);

		if (sendcount > 0) {
			this._lastSessionTime = System.currentTimeMillis();
			this._bytesSend += sendcount;
		}

		return sendcount > 0;
	}
}
