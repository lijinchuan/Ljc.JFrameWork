package Ljc.JFramework.SOA;

import Ljc.JFramework.BeanFieldAnnotation;

public class RegisterServiceResponse {
	@BeanFieldAnnotation(order = 1)
	private boolean _isSuccess;
	@BeanFieldAnnotation(order = 2)
	private String _errMsg;

	public boolean getIsSuccess() {
		return this._isSuccess;
	}

	public void setIsSuccess(boolean value) {
		this._isSuccess = value;
	}

	public String getErrMsg() {
		return this._errMsg;
	}

	public void setErrMsg(String value) {
		this._errMsg = value;
	}
}
