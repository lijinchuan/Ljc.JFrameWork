package Ljc.JFramework.SocketApplication;

import java.util.HashMap;

import Ljc.JFramework.BeanFieldAnnotation;
import Ljc.JFramework.EntityBufCore;

public class Message {
	Message(MessageType msgType) {
		_messageHeader.setMessageType(msgType.getVal());
	}

	public Message(int msgType) {
		_messageHeader.setMessageType(msgType);
	}

	public Message() {

	}

	@BeanFieldAnnotation(order = 1)
	private MessageHeader _messageHeader = new MessageHeader();

	/// <summary>
	/// ��Ϣͷ
	/// </summary>
	public MessageHeader getMessageHeader() {
		return this._messageHeader;
	}

	public void setMessageHeader(MessageHeader value) {
		this._messageHeader = value;
	}

	/// <summary>
	/// ����
	/// </summary>
	private Object _msgBody;

	public void SetMessageBody(Object body) {
		_msgBody = body;
	}

	@BeanFieldAnnotation(order = 2)
	private byte[] _messageBuffer;

	public byte[] getMessageBuffer() throws Exception {

		if (_messageBuffer == null) {
			if (_msgBody == null) {
				_messageBuffer = new byte[0];
			} else {
				_messageBuffer = EntityBufCore.Serialize(this._msgBody, true);
			}
		}
		return _messageBuffer;
	}

	public void setMessageBuffer(byte[] value) {
		this._messageBuffer = value;
	}

	public <T> T GetMessageBody(Class<T> classt) throws SocketApplicationException {
		try {
			return EntityBufCore.DeSerialize(classt, _messageBuffer, true);
		} catch (Exception ex) {
			SocketApplicationException e = new SocketApplicationException("��Ϣ����ʧ��", ex);
			e.Data.put("this.MessageHeader.TransactionID", this.getMessageHeader().getTransactionID());
			e.Data.put("this.MessageHeader.MessageType", this.getMessageHeader().getMessageType());
			throw e;
		}
	}

	Boolean IsMessage(MessageType msgType) {
		return this._messageHeader.getMessageType() == msgType.getVal();
	}

	public Boolean IsMessage(int msgType) {
		return this._messageHeader.getMessageType() == msgType;
	}

	public void AddCustomData(String key, String val) {
		if (key == null || val == null) {
			return;
		}
		HashMap<String, String> dic = this._messageHeader.getCustomData();
		if (dic == null) {
			dic = new HashMap<String, String>();
			this._messageHeader.setCustomData(dic);
		}
		dic.remove(key);
		dic.put(key, val);
	}
}
