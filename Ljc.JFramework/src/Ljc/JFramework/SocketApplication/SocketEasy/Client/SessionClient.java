package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.util.concurrent.ScheduledExecutorService;

import Ljc.JFramework.SocketApplication.Session;
import Ljc.JFramework.SocketApplication.SocketApplicationException;
import Ljc.JFramework.Utility.Action;

public class SessionClient extends ClientBase {
	ScheduledExecutorService timer;

	private Session SessionContext;
	/// <summary>
	/// 是否第一次报超时
	/// </summary>
	private boolean isFirstTimeOut = true;

	public Action SessionTimeOut;
	public Action SessionResume;
	public Action LoginFail;
	public Action LoginSuccess;

	public Action BeferLogout;

	private String uid;
	private String pwd;

	protected SocketApplicationException BuzException = null;

	public SessionClient(String serverip, int serverport, boolean startSession) {
		super(serverip, serverport, true);
		if (startSession) {
			StartSession();
		}
	}

	private void StartSession() {
		// TODO Auto-generated method stub

	}
}
