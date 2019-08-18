package Ljc.JFramework.SOA;

import java.util.Date;

import Ljc.JFramework.BeanFieldAnnotation;
import Ljc.JFramework.TypeUtil.DateTime;

public class SOARequest {
	@BeanFieldAnnotation(order = 1)
	private int _serviceNo;
	@BeanFieldAnnotation(order = 2)
	private int _funcId;
	@BeanFieldAnnotation(order = 3)
	private Date _reqestTime;
	@BeanFieldAnnotation(order = 4)
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
