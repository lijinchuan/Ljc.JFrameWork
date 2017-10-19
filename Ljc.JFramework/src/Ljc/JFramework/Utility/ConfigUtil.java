package Ljc.JFramework.Utility;

import java.io.IOException;

public class ConfigUtil {
	public static String GetConfigPropertiy(String property) throws IOException {
		java.io.InputStream inputstream = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
		java.util.Properties properties = new java.util.Properties();
		try {
			properties.load(inputstream);
		} finally {
			inputstream.close();
		}
		return properties.getProperty(property);
	}
}
