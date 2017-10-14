package Ljc.JFramework;

public class TimeOutException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeOutException() {
		super();
	}

	public TimeOutException(String message) {
		super(message);
	}

	public TimeOutException(String message, Exception inner) {
		super(message, inner);
	}
}
