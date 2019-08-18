package Ljc.JFramework.SocketApplication;

import Ljc.JFramework.BeanFieldAnnotation;

public class NegotiationEncryMessage {
	@BeanFieldAnnotation(order = 1)
	private String _publicKey;

	/// <summary>
	/// ¹«Ô¿
	/// </summary>
	public String getPublicKey() {
		return this._publicKey;
	}

	public void setPublicKey(String val) {
		this._publicKey = val;
	}

	@BeanFieldAnnotation(order = 2)
	private String _encryKey;

	public String getEncryKey() {
		return this._encryKey;
	}

	public void setEncryKey(String val) {
		this._encryKey = val;
	}
}
