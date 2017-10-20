package Ljc.JFramework.SOA;

import java.util.Date;

public class SOARequest {
	private int _serviceNo;
	private int _funcId;
	private Date _requestTime;
	private byte[] _param;

	public int getServiceNo() {
		return this._serviceNo;
	}

	public void setServiceNo(int value) {
		this._serviceNo = value;
	}

	public int getFuncId() {
		return this._funcId;
	}

	public void setFuncId(int value) {
		this._funcId = value;
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
