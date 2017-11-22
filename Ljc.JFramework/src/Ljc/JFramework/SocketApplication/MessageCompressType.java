package Ljc.JFramework.SocketApplication;

public enum MessageCompressType {
	none(0), gzip(1);
	private Integer _val;

	MessageCompressType(int value) {
		this._val = value;
	}

	public int getVal() {
		return this._val;
	}
}
