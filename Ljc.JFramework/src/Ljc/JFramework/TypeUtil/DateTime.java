package Ljc.JFramework.TypeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTime extends Date {
	private static Date OADateBegin = new Date(-2209190400000L);
	private static long DayMills = 24 * 60 * 60 * 1000;

	private static SimpleDateFormat sfdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sfdate = new SimpleDateFormat("yyyy-MM-dd");

	public static Date ParseDateTime(String str) throws ParseException {
		return sfdatetime.parse(str);
	}

	public static Date ParseDate(String str) throws ParseException {
		return sfdate.parse(str);
	}

	public DateTime(long time) {
		super(time);
	}

	public double ToOadate() {
		return ToOADate((Date) this);
	}

	public static double ToOADate(Date d) {
		if (d == null) {
			return 0;
		}

		long days = (d.getTime() - OADateBegin.getTime()) / DayMills;
		TimeZone tz = TimeZone.getDefault();

		double daypart = (d.getTime() + tz.getRawOffset()) % DayMills * 1.0 / DayMills;
		return days + daypart;
	}

	public static DateTime FromOaDate(double oadate) {

		TimeZone tz = TimeZone.getDefault();
		return new DateTime(OADateBegin.getTime() + tz.getRawOffset() + (long) (oadate * DayMills));
	}

	public static Date Now() {
		return new Date();
	}
}
