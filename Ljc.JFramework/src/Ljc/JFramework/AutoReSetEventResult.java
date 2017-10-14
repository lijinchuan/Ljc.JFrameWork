package Ljc.JFramework;

public class AutoReSetEventResult extends AutoResetEvent {
	private String _reqID;

	public String getReqID() {
		return this._reqID;
	}

	public void setReqID(String reqid) {
		this._reqID = reqid;
	}

	public AutoReSetEventResult(String reqID) {
		super(false);
		this._reqID = reqID;
	}

	private Object _waitResult;

	/// <summary>
	/// 等待返回的结果
	/// </summary>
	public Object getWaitResult() {
		return this._waitResult;
	}

	public void setWaitResult(Object value) {
		this._waitResult = value;
	}

	private boolean _isTimeOut = true;

	public boolean getIsTimeOut() {
		return this._isTimeOut;
	}

	public void setIsTimeOut(boolean value) {
		this._isTimeOut = value;
	}

	private CoreException _dataException;

	public CoreException getDataException() {
		return this._dataException;
	}

	public void setDataException(CoreException value) {
		this._dataException = value;
	}
}
