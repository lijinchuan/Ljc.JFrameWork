package Ljc.JFramework.Utility;

import java.io.File;

public class FileUtil {
	public static boolean deleteAll(File f) {
		if (!f.exists()) {
			return false;
		}

		for (File sf : f.listFiles()) {
			if (sf.isFile()) {
				sf.delete();
			}

			deleteAll(sf);
		}

		return f.delete();
	}
}
