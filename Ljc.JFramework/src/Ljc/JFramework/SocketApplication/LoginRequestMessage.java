package Ljc.JFramework.SocketApplication;

public class LoginRequestMessage {
	private String _loginID;

	public String getLoginID() {
		return this._loginID;
	}

	public void setLoginID(String value) {
		this._loginID = value;
	}

	private String _loginPwd;

	public String getLoginPwd() {
		return this._loginPwd;
	}

	public void setLoginPwd(String value) {
		this._loginPwd = value;
	}
}
