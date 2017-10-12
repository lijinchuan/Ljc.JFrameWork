package Ljc.JFramework.SocketApplication;

import java.util.Date;

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

	private Date _messageTime;

	public Date getMessageTime() {
		return this._messageTime;
	}

	public void setMessageTime(Date value) {
		this._messageTime = value;
	}
}
