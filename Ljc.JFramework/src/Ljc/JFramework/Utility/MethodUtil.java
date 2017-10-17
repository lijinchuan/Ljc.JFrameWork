package Ljc.JFramework.Utility;

public class MethodUtil {
	public static boolean ParamsIsNullEmpty(Object... objects) {
		if (objects == null) {
			return true;
		}

		if (objects.length == 0) {
			return true;
		}

		if (objects.length == 1 && objects[0] == null) {
			return true;
		}

		return false;
	}
}
