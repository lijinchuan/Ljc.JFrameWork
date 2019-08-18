package Ljc.JFramework.SocketApplication;

import Ljc.JFramework.BeanFieldAnnotation;

public class LoginResponseMessage {
	@BeanFieldAnnotation(order = 1)
	private String _loginID;

	public String getLoginID() {
		return this._loginID;
	}

	public void setLoginID(String value) {
		this._loginID = value;
	}

	@BeanFieldAnnotation(order = 2)
	private boolean _loginResult;

	public boolean getLoginResult() {
		return this._loginResult;
	}

	public void setLoginResult(boolean value) {
		this._loginResult = value;
	}

	@BeanFieldAnnotation(order = 3)
	private String _sessionID;

	public String getSessionID() {
		return this._sessionID;
	}

	public void setSessionID(String value) {
		this._sessionID = value;
	}

	@BeanFieldAnnotation(order = 4)
	private int _sessionTimeOut;

	public int getSessionTimeOut() {
		return this._sessionTimeOut;
	}

	public void setSessionTimeOut(int value) {
		this._sessionTimeOut = value;
	}

	@BeanFieldAnnotation(order = 5)
	private int _headBeatInterVal;

	public int getHeadBeatInterVal() {
		return this._headBeatInterVal;
	}

	public void setHeadBeatInterVal(int value) {
		this._headBeatInterVal = value;
	}

	@BeanFieldAnnotation(order = 6)
	private String _loginFailReson;

	public String getLoginFailReson() {
		return this._loginFailReson;
	}

	public void setLoginFailReson(String value) {
		this._loginFailReson = value;
	}
}
