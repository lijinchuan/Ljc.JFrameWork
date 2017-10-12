package Ljc.JFramework.SocketApplication;

public class SocketApplicationException extends Exception {
	public SocketApplicationException() {
		super();
	}

	public SocketApplicationException(String message) {
		super(message);
	}

	public SocketApplicationException(String message, Exception inner) {
		super(message, inner);
	}
}
