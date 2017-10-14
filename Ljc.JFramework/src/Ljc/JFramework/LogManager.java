package Ljc.JFramework;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogManager {
	private static Logger logger;

	static {
		logger = Logger.getLogger("Logger");
	}

	public void Debug(String msg, Exception exception) {
		logger.log(Level.INFO, msg, exception);
	}
}
