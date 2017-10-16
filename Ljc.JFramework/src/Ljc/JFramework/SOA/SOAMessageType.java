package Ljc.JFramework.SOA;

public enum SOAMessageType {
	DoSOARequest(100), DoSOAResponse(101), DoSOATransferRequest(102), DoSOATransferResponse(103), RegisterService(
			104), UnRegisterService(105), DoSOARedirectRequest(106), DoSOARedirectResponse(107);

	private int _val;

	private SOAMessageType(int value) {
		this._val = value;
	}

	public int getVal() {
		return this._val;
	}
}
