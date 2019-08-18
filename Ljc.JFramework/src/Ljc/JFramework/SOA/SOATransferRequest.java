package Ljc.JFramework.SOA;

import java.util.Date;

import Ljc.JFramework.BeanFieldAnnotation;

public class SOATransferRequest {
	@BeanFieldAnnotation(order = 1)
	private String _clientId;
	@BeanFieldAnnotation(order = 2)
	private String _clientTransactionID;
	@BeanFieldAnnotation(order = 3)
	private int _fundId;
	@BeanFieldAnnotation(order = 4)
	private Date _requestTime;
	@BeanFieldAnnotation(order = 5)
	private byte[] _param;

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

	public int getFundId() {
		return this._fundId;
	}

	public void setFundId(int value) {
		this._fundId = value;
	}

	public Date getRequestTime() {
		return this._requestTime;
	}

	public void setRequestTime(Date value) {
		this._requestTime = value;
	}

	public byte[] getParam() {
		return this._param;
	}

	public void setParam(byte[] value) {
		this._param = value;
	}
}
