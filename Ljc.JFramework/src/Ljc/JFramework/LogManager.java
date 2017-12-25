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
	private static Logger debuglogger;
	private static Logger infologger;
	private static Logger warnlogger;
	private static Logger errorlogger;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static Date LogNameDate;
	private static final String LOG_FOLDER_NAME = "logs";

	private static final String LOG_FILE_SUFFIX = ".log";

	private static FileHandler DebugLogHandler = null;
	private static FileHandler InfoLogHandler = null;
	private static FileHandler WarnLogHandler = null;
	private static FileHandler ErrorLogHandler = null;

	static {
		debuglogger = Logger.getLogger("DebugLogger");
		debuglogger.setLevel(Level.ALL);

		infologger = Logger.getLogger("InfoLogger");
		infologger.setLevel(Level.ALL);

		warnlogger = Logger.getLogger("WarnLogger");
		warnlogger.setLevel(Level.ALL);

		errorlogger = Logger.getLogger("ErrorLogger");
		errorlogger.setLevel(Level.ALL);

		try {
			DebugLogHandler = GetLogHandler(Level.FINEST);
			InfoLogHandler = GetLogHandler(Level.INFO);
			WarnLogHandler = GetLogHandler(Level.WARNING);
			ErrorLogHandler = GetLogHandler(Level.SEVERE);

			debuglogger.addHandler(DebugLogHandler);
			infologger.addHandler(InfoLogHandler);
			warnlogger.addHandler(WarnLogHandler);
			errorlogger.addHandler(ErrorLogHandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static FileHandler GetLogHandler(Level level) throws SecurityException, IOException {
		FileHandler handler = new FileHandler(getLogFilePath(level), true);
		handler.setEncoding("utf-8");
		handler.setLevel(level);
		handler.setFormatter(new SimpleFormatter());
		return handler;
	}

	private synchronized static String getLogFilePath(Level loglevel) throws IOException {
		StringBuffer logFilePath = new StringBuffer("..");

		// logFilePath.append(System.getProperty("user.dir"));

		logFilePath.append(File.separatorChar);
		logFilePath.append(LOG_FOLDER_NAME);

		System.out.println(logFilePath.toString());

		logFilePath.append(File.separatorChar);
		logFilePath.append(loglevel.toString());
		File file = new File(logFilePath.toString());
		if (!file.exists()) {
			file.mkdirs();
		}

		logFilePath.append(File.separatorChar);
		LogNameDate = new Date();
		logFilePath.append(sdf.format(LogNameDate));
		logFilePath.append(LOG_FILE_SUFFIX);

		return logFilePath.toString();
	}

	private static void changeHandler(Level level) {
		try {
			FileHandler oldhandler = null;
			if (level == Level.FINEST) {
				oldhandler = DebugLogHandler;
				DebugLogHandler = GetLogHandler(level);

			} else if (level == Level.INFO) {
				oldhandler = InfoLogHandler;
				InfoLogHandler = GetLogHandler(level);
			} else if (level == Level.WARNING) {
				oldhandler = WarnLogHandler;
				WarnLogHandler = GetLogHandler(level);
			} else if (level == Level.SEVERE) {
				oldhandler = ErrorLogHandler;
				ErrorLogHandler = GetLogHandler(level);
			}

			oldhandler.flush();
			oldhandler.close();
		} catch (Exception ex) {

		}
	}

	@SuppressWarnings("deprecation")
	private static void checkHandler(Level level) {
		Date now = new Date();

		if (now.getYear() == LogNameDate.getYear() && now.getMonth() == LogNameDate.getMonth()
				&& now.getDate() == LogNameDate.getDate()) {
			return;
		}

		synchronized (LogManager.class) {
			if (now.getYear() == LogNameDate.getYear() && now.getMonth() == LogNameDate.getMonth()
					&& now.getDate() == LogNameDate.getDate()) {
				return;
			}

			changeHandler(Level.FINEST);
			changeHandler(Level.INFO);
			changeHandler(Level.WARNING);
			changeHandler(Level.SEVERE);
		}

	}

	public static void Debug(Object msg) {
		if (msg == null) {
			return;
		}
		checkHandler(Level.FINEST);
		debuglogger.fine(msg.toString());
	}

	public static void Info(Object message) {
		if (message == null) {
			return;
		}
		checkHandler(Level.INFO);
		infologger.info(message.toString());
	}

	public static void Warn(Object message) {
		if (message == null) {
			return;
		}
		checkHandler(Level.WARNING);
		warnlogger.warning(message.toString());
	}

	public static void Error(Object message, Throwable exception) {
		checkHandler(Level.SEVERE);

		errorlogger.severe(
				(message == null ? "" : message.toString()) + LoggerException.GetException(exception).toString());
	}

	public static void Error(Throwable exception) {
		checkHandler(Level.SEVERE);
		errorlogger.severe(LoggerException.GetException(exception).toString());
	}
}
