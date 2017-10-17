package Ljc.JFramework.SOA;

public class GetRegisterServiceInfoRequest {
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
