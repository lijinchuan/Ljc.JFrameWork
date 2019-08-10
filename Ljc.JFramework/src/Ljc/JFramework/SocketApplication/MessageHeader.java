package Ljc.JFramework.SocketApplication;

import java.util.Date;
import java.util.HashMap;

import Ljc.JFramework.TypeUtil.DateTime;

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
		return _messageTime;
	}

	public void setMessageTime(Date val) {
		_messageTime = val;
	}

	private HashMap<String, String> _customData;

	public HashMap<String, String> getCustomData() {
		return this._customData;
	}

	public void setCustomData(HashMap<String, String> value) {
		this._customData = value;
	}

	public MessageHeader() {
		this._messageTime = DateTime.Now();
	}
}
