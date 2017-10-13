package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.util.concurrent.ScheduledExecutorService;

import Ljc.JFramework.SocketApplication.LoginRequestMessage;
import Ljc.JFramework.SocketApplication.LoginResponseMessage;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.MessageType;
import Ljc.JFramework.SocketApplication.Session;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
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

	private int sendMessageTryCountLimit = 3;

	public SessionClient(String serverip, int serverport, boolean startSession) {
		super(serverip, serverport, true);
		if (startSession) {
			StartSession();
		}
	}

	private final boolean StartSession() {
		// TODO Auto-generated method stub
		try {
			if (!isStartClient && !StartClient())
				return false;

			return true;
		} catch (Exception e) {
			OnError(e);

			return false;
		}
	}

	protected void OnLoginFail(String failMsg) {

	}

	public final void Login(String uid, String pwd) throws Exception {
		this.uid = uid;
		this.pwd = pwd;

		Message message = new Message(MessageType.LOGIN.getVal());
		LoginRequestMessage msg = new LoginRequestMessage();
		msg.setLoginID(this.uid);
		msg.setLoginPwd(this.pwd);
		message.SetMessageBody(msg);

		if (!SendMessage(message)) {
			OnLoginFail("发送请求失败");
			if (LoginFail != null) {
				LoginFail.notifyEvent(null);
			}
		}
	}

	public void Logout() throws Exception {
		try {
			if (BeferLogout != null) {
				BeferLogout.notifyEvent(null);
			}
		} catch (Exception ex) {

		}

		Message msg = new Message(MessageType.LOGOUT.getVal());

		stop = true;
		isStartClient = false;
		SendMessage(msg);
	}

	@Override
	protected final void OnMessage(Message message) {
		super.OnMessage(message);

		if (SessionContext != null) {
			SessionContext.setLastSessionTime(System.currentTimeMillis());
		}

		if (message.IsMessage(MessageType.LOGIN.getVal())) {
			LoginResponseMessage loginMsg = message.GetMessageBody(LoginResponseMessage.class);
			if (loginMsg.getLoginResult()) {
				StartHearteBeat(loginMsg);
				OnLoginSuccess();

				if (LoginSuccess != null) {
					LoginSuccess();
				}
			} else {
				OnLoginFail(loginMsg.LoginFailReson);
				if (LoginFail != null) {
					LoginFail();
				}
			}
		} else if (message.IsMessage(MessageType.HEARTBEAT)) {
			ReciveHeartBeat(message);
		} else if (message.IsMessage(MessageType.LOGOUT)) {
			SessionContext.IsLogin = false;
			SessionContext.IsValid = false;
			stop = true;
			isStartClient = false;
			this.timer.Stop();
		} else if (message.IsMessage(MessageType.RELOGIN)) {
			if (SessionContext == null) {
				throw new Exception("请先调用login方法。");
			}
			SessionContext.IsLogin = false;
			Login(uid, pwd);
		} else {
			ReciveMessage(message);
		}
	}

	public boolean SendMessage(Message message) throws Exception {
		try {
			int tryCount = 0;
			while (!socketClient.isConnected() && tryCount < sendMessageTryCountLimit) {
				tryCount++;
				StartClient();
			}

			if (!socketClient.isConnected()) {
				throw new Exception("发送失败，套接字连接失败。");
			}

			// byte[] data = EntityBufCore.Serialize(message);
			// byte[] len = BitConverter.GetBytes(data.Length);
			// socketClient.Send(len);
			// socketClient.Send(data);
			return SocketApplicationComm.SendMessage(socketClient, message) > 0;
		} catch (Exception e) {
			OnError(e);
			throw e;
		}
	}
}
