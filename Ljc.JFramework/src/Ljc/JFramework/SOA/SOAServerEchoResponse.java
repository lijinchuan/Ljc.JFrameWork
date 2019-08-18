package Ljc.JFramework.SOA;

import Ljc.JFramework.BeanFieldAnnotation;

public class SOAServerEchoResponse {
	@BeanFieldAnnotation(order = 1)
	private boolean _ok;

	public boolean getOk() {
		return this._ok;
	}

	public void setOk(boolean value) {
		this._ok = value;
	}
}
