package Ljc.JFramework.SocketApplication;

import Ljc.JFramework.BeanFieldAnnotation;

public class LoginRequestMessage {
	@BeanFieldAnnotation(order = 1)
	private String _loginID;

	public String getLoginID() {
		return this._loginID;
	}

	public void setLoginID(String value) {
		this._loginID = value;
	}

	@BeanFieldAnnotation(order = 2)
	private String _loginPwd;

	public String getLoginPwd() {
		return this._loginPwd;
	}

	public void setLoginPwd(String value) {
		this._loginPwd = value;
	}
}
