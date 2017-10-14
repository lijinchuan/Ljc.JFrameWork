package Ljc.JFramework;

import java.util.Map.Entry;

public class LoggerException extends Exception {
	private Exception _fromException = null;

	private LoggerException(Exception ex) {
		_fromException = ex;
	}

	public static Exception GetException(Exception ex) {
		if (ex == null)
			return null;

		if (ex instanceof LoggerException)
			return ex;

		return new LoggerException(ex);
	}

	@Override
	public String getMessage() {
		return this._fromException.getMessage();
	}

	@Override
	public String toString() {

		Exception inexp = _fromException;
		StringBuilder sb = new StringBuilder();

		String level = "";

		while (inexp != null) {
			sb.append(String.format("%s������Ϣ:%s", level, inexp.getMessage()));
			sb.append(String.format("%s��ջ��Ϣ:", level));
			for (StackTraceElement ste : inexp.getStackTrace()) {
				sb.append(String.format("%s��ջ��Ϣ:%s", level, ste.toString()));
			}
			if (inexp instanceof CoreException) {
				CoreException cexp = (CoreException) inexp;
				sb.append(String.format("{0}---------������Ϣ---------", level));
				for (Entry<Object, Object> kv : cexp.Data.entrySet()) {
					sb.append(String.format("{0} {1}: {2}", level, kv.getKey(), kv.getValue().toString()));
				}
				sb.append(String.format("{0}------------������ϢEND---------------", level));
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
