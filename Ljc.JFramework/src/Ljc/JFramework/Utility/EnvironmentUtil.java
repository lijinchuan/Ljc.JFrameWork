package Ljc.JFramework.Utility;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class EnvironmentUtil {
	public static final String NEW_LINE = System.getProperty("line.separator");
	static String HOSTNAME;
	static String OSNAME;
	static String OSVERSION;

	public static String GetHostName() {
		if (HOSTNAME == null) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				HOSTNAME = addr.getHostName();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				HOSTNAME = "δ֪";
			}

		}
		return HOSTNAME;
	}

	public static String GetOSVersion() {
		if (OSNAME == null) {
			Properties props = System.getProperties();
			OSNAME = props.getProperty("os.name");
			OSVERSION = props.getProperty("os.version");
		}
		return OSNAME + "_" + OSVERSION;
	}

}
