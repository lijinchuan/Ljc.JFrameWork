package Ljc.JFramework.SOA;

public class RegisterServiceInfo {
	private int _serviceNo;
	private String[] _redirectTcpIps;
	private int _redirectTcpPort;
	private String[] _redirectUdpIps;
	private int _redirectUdpPort;

	public int getServiceNo() {
		return this._serviceNo;
	}

	public void setServiceNo(int value) {
		this._serviceNo = value;
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
