package Ljc.JFramework.Utility;

public class Tuple<T1, T2> {
	private T1 Item1;
	private T2 Item2;

	public Tuple(T1 item1, T2 item2) {
		this.Item1 = item1;
		this.Item2 = item2;
	}

	public T1 GetItem1() {
		return this.Item1;
	}

	public T2 GetItem2() {
		return this.Item2;
	}

}
