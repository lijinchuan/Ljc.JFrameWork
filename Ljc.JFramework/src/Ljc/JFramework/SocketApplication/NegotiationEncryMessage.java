package Ljc.JFramework.SocketApplication;

public class NegotiationEncryMessage {
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

	private String _encryKey;

	public String getEncryKey() {
		return this._encryKey;
	}

	public void setEncryKey(String val) {
		this._encryKey = val;
	}
}
