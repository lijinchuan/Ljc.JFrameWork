package Ljc.JFramework.SOA;

public class ESBServiceInfo {
	private int _clientID;
	private int _serviceNo;
	private Ljc.JFramework.SocketApplication.Session _session;
	private String[] _redirectTcpIps;
	private int _redirectTcpPort;
	private String[] _redirectUdpIps;
	private int _redirectUdpPort;

	public int getClientID() {
		return this._clientID;
	}

	public void setClientID(int value) {
		this._clientID = value;
	}

	public int getServiceNo() {
		return this._serviceNo;
	}

	public void setServiceNo(int value) {
		this._serviceNo = value;
	}

	/// <summary>
	/// 会话信息
	/// </summary>
	public Ljc.JFramework.SocketApplication.Session getSession() {
		return this._session;
	}

	public void setSession(Ljc.JFramework.SocketApplication.Session value) {
		this._session = value;
	}

	public String[] getRedirectTcpIps() {
		return this._redirectTcpIps;
	}

	public void setRedirectTcpIps(String[] value) {
		this._redirectTcpIps = value;
	}

	public int getRedirectTcpPort() {
		return this._redirectTcpPort;
	}

	public void setRedirectTcpPort(int value) {
		this._redirectTcpPort = value;
	}

	public String[] getRedirectUdpIps() {
		return this._redirectUdpIps;
	}

	public void setRedirectUdpIps(String[] value) {
		this._redirectUdpIps = value;
	}

	public int getRedirectUdpPort() {
		return this._redirectUdpPort;
	}

	public void setRedirectUdpPort(int value) {
		this._redirectUdpPort = value;
	}
}
