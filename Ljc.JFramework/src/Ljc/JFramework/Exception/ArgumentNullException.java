package Ljc.JFramework.Exception;

public class ArgumentNullException extends ArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String _errormsg = "����Ϊ��";

	public ArgumentNullException(String argument) {
		super(argument, _errormsg);
	}

}
