package Ljc.JFramework.SOA;

import java.util.Date;

import Ljc.JFramework.TypeUtil.DateTime;

public class SOARequest {
	private int _serviceNo;
	private int _funcId;
	private Date _reqestTime;
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

	public Date getReqestTime() {
		return _reqestTime;
	}

	public void setReqestTime(Date val) {
		_reqestTime = val;
	}

	public byte[] getParam() {
		return this._param;
	}

	public void setParam(byte[] value) {
		this._param = value;
	}

	public SOARequest() {
		this._reqestTime = DateTime.Now();
	}
}
