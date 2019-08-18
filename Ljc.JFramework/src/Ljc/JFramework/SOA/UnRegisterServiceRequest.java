package Ljc.JFramework.SOA;

import Ljc.JFramework.BeanFieldAnnotation;

public class UnRegisterServiceRequest {
	@BeanFieldAnnotation(order = 1)

	private int _serviceNo;

	public int getServiceNo() {
		return this._serviceNo;
	}

	public void setServiceNo(int value) {
		this._serviceNo = value;
	}
}
