package Ljc.JFramework.SOA;

public class GetRegisterServiceInfoResponse {
	private int _serviceNo;
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
