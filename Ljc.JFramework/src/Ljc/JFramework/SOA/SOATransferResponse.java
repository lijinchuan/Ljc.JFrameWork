package Ljc.JFramework.SOA;

import java.util.Date;

import Ljc.JFramework.BeanFieldAnnotation;

public class SOATransferResponse {
	@BeanFieldAnnotation(order = 1)
	private String _clientId;
	@BeanFieldAnnotation(order = 2)
	private String _clientTransactionID;
	@BeanFieldAnnotation(order = 3)
	private boolean _isSuccess = false;
	@BeanFieldAnnotation(order = 4)
	private Date _responseTime;
	@BeanFieldAnnotation(order = 5)
	private String _errMsg;
	@BeanFieldAnnotation(order = 6)
	private byte[] _result;

	public String getClientId() {
		return this._clientId;
	}

	public void setClientId(String value) {
		this._clientId = value;
	}

	public String getClientTransactionID() {
		return this._clientTransactionID;
	}

	public void setClientTransactionID(String value) {
		this._clientTransactionID = value;
	}

	public boolean getIsSuccess() {
		return this._isSuccess;
	}

	public void setIsSuccess(boolean value) {
		this._isSuccess = value;
	}

	public Date getResponseTime() {
		return this._responseTime;
	}

	public void setResponseTime(Date value) {
		this._responseTime = value;
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
