package Ljc.JFramework.SOA;

import java.util.Date;

import Ljc.JFramework.BeanFieldAnnotation;

public class SOAResponse {
	@BeanFieldAnnotation(order = 1)
	private boolean _isSuccess = false;
	@BeanFieldAnnotation(order = 2)
	private Date _responseTime;
	@BeanFieldAnnotation(order = 3)
	private String _errMsg;
	@BeanFieldAnnotation(order = 4)
	private byte[] _result;

	public Boolean getIsSuccess() {
		return this._isSuccess;
	}

	public void setIsSuccess(boolean value) {
		this._isSuccess = value;
	}

	public Date getResponseTime() {
		return _responseTime;
	}

	public void setResponseTime(Date val) {
		_responseTime = val;
	}

	public String getErrMsg() {
		return this._errMsg;
	}

	public void setErrMsg(String value) {
		this._errMsg = value;
	}

	public byte[] getResult() {
		return this._result;
	}

	public void setResult(byte[] value) {
		this._result = value;
	}
}
