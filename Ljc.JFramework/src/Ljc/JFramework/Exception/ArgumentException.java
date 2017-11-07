package Ljc.JFramework.Exception;

import Ljc.JFramework.CoreException;

public class ArgumentException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArgumentException(String argumentName, String message) {
		super(String.format("²ÎÊý  %s ´íÎó:%s", argumentName, message));
	}

}
