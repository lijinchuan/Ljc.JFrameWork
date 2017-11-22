package Ljc.JFramework.SocketApplication;

import java.util.HashMap;

public class MessageHeader {
	private int _messageType;

	public int getMessageType() {
		return this._messageType;
	}

	public void setMessageType(int value) {
		this._messageType = value;
	}

	private String _transactionID;

	/// <summary>
	/// Á÷Ë®ºÅ
	/// </summary>
	public String getTransactionID() {
		return this._transactionID;
	}

	public void setTransactionID(String value) {
		this._transactionID = value;
	}

	private int _compressType;

	public int getCompressType() {
		return this._compressType;
	}

	public void setCompressType(int value) {
		this._compressType = value;
	}

	private HashMap<String, String> _customData;

	public HashMap<String, String> getCustomData() {
		return this._customData;
	}

	public void setCustomData(HashMap<String, String> value) {
		this._customData = value;
	}
}
