package Ljc.JFramework.Utility;

public class StringUtil {
	public static String captureName(String name) {
		if (name == null) {
			return name;
		}
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}
}
