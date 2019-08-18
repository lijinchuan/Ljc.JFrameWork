package Ljc.JFramework.SOA;

import Ljc.JFramework.BeanFieldAnnotation;

public class GetRegisterServiceInfoResponse {
	@BeanFieldAnnotation(order = 1)
	private int _serviceNo;
	@BeanFieldAnnotation(order = 2)
	private RegisterServiceInfo[] _infos;

	public int getServiceNo() {
		return this._serviceNo;
	}

	public void setServiceNo(int value) {
		this._serviceNo = value;
	}

	public RegisterServiceInfo[] getInfos() {
		return this._infos;
	}

	public void setInfos(RegisterServiceInfo[] value) {
		this._infos = value;
	}
}
