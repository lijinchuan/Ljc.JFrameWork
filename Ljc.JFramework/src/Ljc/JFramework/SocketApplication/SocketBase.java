package Ljc.JFramework.SocketApplication;

import Ljc.JFramework.Utility.Action;

public class SocketBase {
	protected boolean stop = false;
	public Action<Exception> Error = new Action<Exception>();

	protected void OnError(Exception e) {
		if (Error != null) {
			try {
				Error.notifyEvent(e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
