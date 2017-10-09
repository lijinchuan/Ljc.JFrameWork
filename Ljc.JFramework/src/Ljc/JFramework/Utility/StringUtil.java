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

	public static boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		}

		for (char ch : str.toCharArray()) {
			if (ch != ' ') {
				return false;
			}
		}

		return true;
	}
}
