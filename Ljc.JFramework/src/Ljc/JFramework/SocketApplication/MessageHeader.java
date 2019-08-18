package Ljc.JFramework.SocketApplication;

import java.util.Date;
import java.util.HashMap;

import Ljc.JFramework.BeanFieldAnnotation;
import Ljc.JFramework.TypeUtil.DateTime;

public class MessageHeader {
	@BeanFieldAnnotation(order = 1)
	private int _messageType;

	public int getMessageType() {
		return this._messageType;
	}

	public void setMessageType(int value) {
		this._messageType = value;
	}

	@BeanFieldAnnotation(order = 2)
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

	@BeanFieldAnnotation(order = 3)
	private Date _messageTime;

	public Date getMessageTime() {
		return _messageTime;
	}

	public void setMessageTime(Date val) {
		_messageTime = val;
	}

	@BeanFieldAnnotation(order = 4)
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
