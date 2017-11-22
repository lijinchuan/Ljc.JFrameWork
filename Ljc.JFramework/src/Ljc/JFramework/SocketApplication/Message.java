package Ljc.JFramework.SocketApplication;

import Ljc.JFramework.CoreException;
import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.Utility.GzipUtil;

public class Message {
	Message(MessageType msgType) {
		_messageHeader.setMessageType(msgType.getVal());
	}

	public Message(int msgType) {
		_messageHeader.setMessageType(msgType);
	}

	public Message() {

	}

	private MessageHeader _messageHeader = new MessageHeader();

	/// <summary>
	/// 消息头
	/// </summary>
	public MessageHeader getMessageHeader() {
		return this._messageHeader;
	}

	public void setMessageHeader(MessageHeader value) {
		this._messageHeader = value;
	}

	/// <summary>
	/// 内容
	/// </summary>
	private Object _msgBody;

	public void SetMessageBody(Object body) {
		_msgBody = body;
	}

	private byte[] _messageBuffer;

	public byte[] getMessageBuffer() throws Exception {

		if (_messageBuffer == null) {
			if (_msgBody == null) {
				_messageBuffer = new byte[0];
			} else {
				if (this._messageHeader.getCompressType() == MessageCompressType.none.getVal()) {
					_messageBuffer = EntityBufCore.Serialize(this._msgBody);
				} else if (this._messageHeader.getCompressType() == MessageCompressType.gzip.getVal()) {
					_messageBuffer = GzipUtil.compress(EntityBufCore.Serialize(this._msgBody));
				} else {
					throw new CoreException(String.format("未知的压缩方式:%d", this._messageHeader.getCompressType()));
				}
			}
		}
		return _messageBuffer;
	}

	public void setMessageBuffer(byte[] value) {
		this._messageBuffer = value;
	}

	public <T> T GetMessageBody(Class<T> classt) throws SocketApplicationException {
		try {
			if (this._messageHeader.getCompressType() == MessageCompressType.none.getVal()) {
				return EntityBufCore.DeSerialize(classt, _messageBuffer);
			} else if (this._messageHeader.getCompressType() == MessageCompressType.gzip.getVal()) {
				return EntityBufCore.DeSerialize(classt, GzipUtil.uncompress(_messageBuffer));
			} else {
				throw new CoreException(String.format("未知的压缩方式:%d", this._messageHeader.getCompressType()));
			}
		} catch (Exception ex) {
			SocketApplicationException e = new SocketApplicationException("消息解析失败", ex);
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
}
