package Ljc.JFramework;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
	private static Logger logger;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final String LOG_FOLDER_NAME = "log";

	private static final String LOG_FILE_SUFFIX = ".log";

	static {
		logger = Logger.getLogger("Logger");
		logger.setLevel(Level.ALL);
		// 文件日志内容标记为可追加
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler(getLogFilePath(), true);
			// 以文本的形式输出
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized static String getLogFilePath() {
		StringBuffer logFilePath = new StringBuffer();
		logFilePath.append("D:\\GitHub\\Ljc.JFrameWork\\Ljc.JFramework\\");
		// logFilePath.append(System.getProperty("user.home"));
		// logFilePath.append(File.separatorChar);
		logFilePath.append(LOG_FOLDER_NAME);

		File file = new File(logFilePath.toString());
		if (!file.exists())
			file.mkdir();

		logFilePath.append(File.separatorChar);
		logFilePath.append(sdf.format(new Date()));
		logFilePath.append(LOG_FILE_SUFFIX);

		return logFilePath.toString();
	}

	public static void Debug(Object msg) {
		if (msg != null) {
			return;
		}
		logger.log(Level.FINEST, msg.toString());
	}

	public static void Info(Object message) {
		if (message == null) {
			return;
		}
		logger.info(message.toString());
	}

	public static void Warn(Object message) {
		if (message == null) {
			return;
		}
		logger.warning(message.toString());
	}

	public static void Error(Object message, Exception exception) {

		logger.log(Level.SEVERE, message.toString(), LoggerException.GetException(exception));
	}
}
