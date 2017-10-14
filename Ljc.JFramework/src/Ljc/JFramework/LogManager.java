package Ljc.JFramework;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogManager {
	private static Logger logger;

	static {
		logger = Logger.getLogger("Logger");
	}

	public void Debug(Object msg) {
		if (msg != null) {
			return;
		}
		logger.log(Level.FINEST, msg.toString());
	}

	public void Info(Object message) {
		if (message == null) {
			return;
		}
		logger.info(message.toString());
	}

	public void Warn(Object message) {
		if (message == null) {
			return;
		}
		logger.warning(message.toString());
	}

	public void Error(Object message, Exception exception) {

		logger.log(Level.SEVERE, message.toString(), LoggerException.GetException(exception));
	}
}
