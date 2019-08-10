package Ljc.JFramework.SOA;

import java.util.Date;

public class SOAResponse {
	private boolean _isSuccess = false;
	private Date _responseTime;
	private String _errMsg;
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
