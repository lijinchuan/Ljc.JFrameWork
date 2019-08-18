package Ljc.JFramework.SOA;

import Ljc.JFramework.BeanFieldAnnotation;

public class GetRegisterServiceInfoRequest {
	@BeanFieldAnnotation(order = 1)
	private int _serviceNo;

	public int getServiceNo() {
		return this._serviceNo;
	}

	public void setServiceNo(int value) {
		this._serviceNo = value;
	}

	public GetRegisterServiceInfoRequest() {

	}

	public GetRegisterServiceInfoRequest(int sno) {
		this.setServiceNo(sno);
	}
}
