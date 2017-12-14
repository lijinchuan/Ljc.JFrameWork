package Ljc.JFramework;

import java.util.Map.Entry;

import Ljc.JFramework.Utility.EnvironmentUtil;

public class LoggerException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Throwable _fromException = null;

	private LoggerException(Throwable ex) {
		_fromException = ex;
	}

	public static Throwable GetException(Throwable ex) {
		if (ex == null)
			return null;

		if (ex instanceof LoggerException)
			return ex;

		return new LoggerException(ex);
	}

	public static Exception GetException(String message, Exception ex) {
		if (ex == null)
			return null;

		if (ex instanceof LoggerException) {
			return ex;
		}

		return new LoggerException(ex);
	}

	@Override
	public String getMessage() {
		return this._fromException.getMessage();
	}

	@Override
	public String toString() {

		Throwable inexp = _fromException;
		StringBuilder sb = new StringBuilder();

		String level = "";
		while (inexp != null) {

			sb.append(String.format("%s错误信息:%s%s", level, inexp.getMessage(), EnvironmentUtil.NEW_LINE));
			sb.append(String.format("%s堆栈信息:%s", level, EnvironmentUtil.NEW_LINE));
			for (StackTraceElement ste : inexp.getStackTrace()) {
				sb.append(String.format("%s堆栈信息:%s%s", level, ste.toString(), EnvironmentUtil.NEW_LINE));
			}
			if (inexp instanceof CoreException) {
				CoreException cexp = (CoreException) inexp;
				sb.append(String.format("%s---------数据信息---------%s", level, EnvironmentUtil.NEW_LINE));
				for (Entry<Object, Object> kv : cexp.Data.entrySet()) {
					sb.append(String.format("%s %s: %s%s", level, kv.getKey(), kv.getValue().toString(),
							EnvironmentUtil.NEW_LINE));
				}
				sb.append(String.format("%s------------数据信息END---------------%s", level, EnvironmentUtil.NEW_LINE));
			}

			if (inexp.getCause() instanceof Exception) {
				inexp = (Exception) inexp.getCause();
				level += "+";
			} else {
				inexp = null;
			}
		}
		return sb.toString();
	}
}
