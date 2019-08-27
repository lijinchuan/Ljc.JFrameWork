package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Ljc.JFramework.AutoReSetEventResult;
import Ljc.JFramework.CoreException;
import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.TimeOutException;
import Ljc.JFramework.SocketApplication.LoginRequestMessage;
import Ljc.JFramework.SocketApplication.LoginResponseMessage;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.MessageType;
import Ljc.JFramework.SocketApplication.Session;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketApplicationException;
import Ljc.JFramework.TypeUtil.DateTime;
import Ljc.JFramework.Utility.Action;
import Ljc.JFramework.Utility.Converter;
import Ljc.JFramework.Utility.StringUtil;

public class SessionClient extends ClientBase {
	private Timer timer;

	private Session SessionContext;
	/// <summary>
	/// 是否第一次报超时
	/// </summary>
	private boolean isFirstTimeOut = true;

	public Action SessionTimeOut = new Action();
	public Action SessionResume = new Action();
	public Action<String> LoginFail = new Action<String>();
	public Action LoginSuccess = new Action();

	public Action BeferLogout = new Action();

	private String uid;
	private String pwd;

	protected SocketApplicationException BuzException = null;

	private HashMap<String, AutoReSetEventResult> watingEvents = new HashMap<String, AutoReSetEventResult>();

	private int sendMessageTryCountLimit = 3;

	public SessionClient(String serverip, int serverport, boolean startSession, boolean security) {
		super(serverip, serverport, true, security);
		if (startSession) {
			StartSession();
		}
	}

	public final boolean StartSession() {
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

	protected void OnSessionTimeOut() {

	}

	protected void OnLoginSuccess() {

	}

	protected void OnSessionResume() {

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
				LoginFail.notifyEvent("发送请求失败");
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

	private void StartHearteBeat(LoginResponseMessage message) {
		SessionContext = new Session();
		SessionContext.setEncryKey(this.encryKey);
		SessionContext.setUserName(message.getLoginID());
		SessionContext.setConnectTime(DateTime.Now());
		SessionContext.setSessionID(message.getSessionID());
		SessionContext.setSessionTimeOut(message.getSessionTimeOut());
		SessionContext.setHeadBeatInterVal(message.getHeadBeatInterVal());
		SessionContext.setIsLogin(true);
		SessionContext.setIsValid(true);

		if (timer == null) {
			timer = new Timer(true);
			timer.schedule(new TimerTask() {

				public void run() {
					// TODO Auto-generated method stub
					try {
						HeartBeat_Elapsed();
					} catch (Exception ex) {

					}
				}

			}, Math.max(SessionContext.getHeadBeatInterVal(), 10000),
					Math.max(SessionContext.getHeadBeatInterVal(), 10000));
		}
	}

	void HeartBeat_Elapsed() {
		try {
			if (System.currentTimeMillis() - SessionContext.getLastSessionTime() < 10000)
				return;

			if (SessionContext.IsTimeOut() && isFirstTimeOut) {
				isFirstTimeOut = false;
				SessionContext.setIsLogin(false);

				OnSessionTimeOut();

				if (SessionTimeOut != null) {
					SessionTimeOut.notifyEvent(null);
				}
			}

			Message msg = new Message(MessageType.HEARTBEAT.getVal());

			SendMessage(msg);
		} catch (Exception exp) {
			OnError(exp);
		}
	}

	private void ReciveHeartBeat(Message message) {
		if (SessionContext.IsTimeOut() || !isFirstTimeOut) {
			isFirstTimeOut = true;
			SessionContext.setIsLogin(true);

			OnSessionResume();

			if (SessionResume != null) {
				try {
					SessionResume.notifyEvent(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// SessionContext.LastSessionTime = message.MessageHeader.MessageTime;
		SessionContext.setLastSessionTime(System.currentTimeMillis());
	}

	protected void ReciveMessage(Message message) throws Exception {
		if (!StringUtil.isNullOrEmpty(message.getMessageHeader().getTransactionID())) {
			AutoReSetEventResult autoEvent = watingEvents.get(message.getMessageHeader().getTransactionID());

			if (autoEvent != null) {
				autoEvent.setWaitResult(DoMessage(message));
				autoEvent.setIsTimeOut(false);
				autoEvent.set();
				return;
			}
		}

	}

	@Override
	protected final void OnMessage(Message message) {
		super.OnMessage(message);

		if (SessionContext != null) {
			SessionContext.setLastSessionTime(System.currentTimeMillis());
		}

		try {
			if (message.IsMessage(MessageType.LOGIN.getVal())) {
				LoginResponseMessage loginMsg = message.GetMessageBody(LoginResponseMessage.class);
				if (loginMsg.getLoginResult()) {
					StartHearteBeat(loginMsg);
					OnLoginSuccess();

					if (LoginSuccess != null) {
						LoginSuccess.notifyEvent(null);
					}
				} else {
					OnLoginFail(loginMsg.getLoginFailReson());
					if (LoginFail != null) {
						LoginFail.notifyEvent(loginMsg.getLoginFailReson());
					}
				}
			} else if (message.IsMessage(MessageType.HEARTBEAT.getVal())) {
				ReciveHeartBeat(message);
			} else if (message.IsMessage(MessageType.LOGOUT.getVal())) {
				SessionContext.setIsLogin(false);
				SessionContext.setIsValid(false);
				stop = true;
				isStartClient = false;
				this.timer.cancel();
			} else if (message.IsMessage(MessageType.RELOGIN.getVal())) {
				if (SessionContext == null) {
					throw new Exception("请先调用login方法。");
				}
				SessionContext.setIsLogin(false);
				Login(uid, pwd);
			} else {
				ReciveMessage(message);
			}
		} catch (Exception ex) {
			OnError(ex);
		}
	}

	public <T> T SendMessageAnsy(Class<T> classt, Message message, int timeOut/* = 30000 */) throws Exception {
		String reqID = message.getMessageHeader().getTransactionID();

		if (StringUtil.isNullOrEmpty(reqID))
			throw new CoreException("消息没有设置唯一的序号。无法进行同步。");
		AutoReSetEventResult autoResetEvent = new AutoReSetEventResult(reqID);
		watingEvents.put(reqID, autoResetEvent);
		BuzException = null;

		SendMessage(message);
		// ThreadPool.QueueUserWorkItem(new WaitCallback(o => { SendMessage((Message)o);
		// }), message);
		// new Func<Message, bool>(SendMessage).BeginInvoke(message, null, null);

		autoResetEvent.waitOne(timeOut);
		// WaitHandle.WaitAny(new WaitHandle[] { autoResetEvent }, timeOut);

		watingEvents.remove(reqID);

		if (BuzException != null) {
			BuzException.Data.put("MessageType", message.getMessageHeader().getMessageType());
			BuzException.Data.put("TransactionID", reqID);
			throw BuzException;
		}

		if (autoResetEvent.getIsTimeOut()) {
			TimeOutException ex = new TimeOutException();
			ex.Data.put("errorsender", "LJC.JFrameWork.SocketEasy.Client.SesseionClient");
			ex.Data.put("MessageType", message.getMessageHeader().getMessageType());
			ex.Data.put("TransactionID", message.getMessageHeader().getTransactionID());
			ex.Data.put("serverIp", this.serverIp);
			ex.Data.put("ipPort", this.ipPort);
			if (message.getMessageBuffer() != null) {

				ex.Data.put("MessageBuffer", Converter.GetBase64(message.getMessageBuffer()));
			}
			ex.Data.put("resulttype", classt.getName());
			// LogManager.LogHelper.Instance.Error("SendMessageAnsy", ex);

			throw ex;
		} else {
			T result = EntityBufCore.DeSerialize(classt, (byte[]) autoResetEvent.getWaitResult(), true);
			return result;
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
			return SocketApplicationComm.SendMessage(socketClient, message, this.encryKey) > 0;
		} catch (Exception e) {
			OnError(e);
			throw e;
		}
	}

	/// <summary>
	/// 处理自定义消息
	/// </summary>
	/// <param name="message"></param>
	/// <returns></returns>
	protected byte[] DoMessage(Message message) throws Exception {
		return message.getMessageBuffer();
	}
}
