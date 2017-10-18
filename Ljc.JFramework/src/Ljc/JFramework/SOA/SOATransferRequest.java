package Ljc.JFramework.SOA;

import java.util.Date;

public class SOATransferRequest {
	private String _clientId;
	private String _clientTransactionID;
	private int _fundId;
	private Date _requestTime;
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
