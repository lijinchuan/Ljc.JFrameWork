package Ljc.JFramework.TypeUtil;

public class DateTime {
	private double _oadate = 0;
	private int _year = 1899;
	private int _mon = 12;
	private int _day = 31;
	private int _hour = 0;
	private int _min = 0;
	private int _sec = 0;
	private int _mills = 0;

	public DateTime(double oadate) {
		this._oadate = oadate;
	}

	public static DateTime valueOf(double oadate) {
		return new DateTime(oadate);
	}

	public double toOadate() {
		return this._oadate;
	}
}
