package Ljc.JFramework;

public class Box<T> {
	private T _data;

	public T getData() {
		return _data;
	}

	public void setData(T val) {
		_data = val;
	}
}