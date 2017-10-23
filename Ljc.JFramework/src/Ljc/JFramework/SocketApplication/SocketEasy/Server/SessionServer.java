package Ljc.JFramework.SocketApplication.SocketEasy.Server;

import java.util.HashMap;

import Ljc.JFramework.AutoReSetEventResult;
import Ljc.JFramework.Box;
import Ljc.JFramework.SocketApplication.LoginRequestMessage;
import Ljc.JFramework.SocketApplication.LoginResponseMessage;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.MessageType;
import Ljc.JFramework.SocketApplication.Session;
import Ljc.JFramework.SocketApplication.SocketApplicationException;

public class SessionServer extends ServerBase {
	protected HashMap<String, Session> appLoginSockets;

	private HashMap<String, AutoReSetEventResult> watingEvents;

	private boolean _serverModeNeedLogin = true;

	/// <summary>
	/// 如果是服务端模式，是否可以登录
	/// </summary>
	protected boolean getServerModeNeedLogin() {
		return _serverModeNeedLogin;
	}

	protected void setServerModeNeedLogin(boolean value) {
		_serverModeNeedLogin = value;
	}

	public SessionServer(String[] ips, int port) {
		super(ips, port);

		appLoginSockets = new HashMap<String, Session>();
		watingEvents = new HashMap<String, AutoReSetEventResult>();
	}

	public SessionServer(int port) {
		super(null, port);

		appLoginSockets = new HashMap<String, Session>();
		watingEvents = new HashMap<String, AutoReSetEventResult>();
	}

	private void App_HeatBeat(Message message, Session session) throws Exception {
		Message msg = new Message(MessageType.HEARTBEAT.getVal());
		msg.SetMessageBody(session.getSessionID());

		// s.Send(msg.ToBytes());
		session.setLastSessionTime(System.currentTimeMillis());
		session.SendMessage(msg);
	}

	/// <summary>
	/// 服务器处理客户端登陆请求
	/// </summary>
	/// <param name="user"></param>
	/// <param name="pwd"></param>
	/// <returns></returns>
	protected boolean OnUserLogin(String user, String pwd, Box<String> loginFailReson) {
		loginFailReson.setData("");
		return true;
	}

	private void App_Login(Message message, Session session) {
		try {
			Exception ex = null;
			LoginRequestMessage request = message.GetMessageBody(LoginRequestMessage.class);
			// string uid = message.Get<string>(FieldEnum.LoginID);
			// string pwd = message.Get<string>(FieldEnum.LoginPwd);

			Message LoginSuccessMessage = new Message(MessageType.LOGIN.getVal());
			LoginResponseMessage responsemsg = new LoginResponseMessage();

			Box<String> loginFailMsg = new Box<String>();
			boolean canLogin = false;
			try {
				canLogin = OnUserLogin(request.getLoginID(), request.getLoginPwd(), loginFailMsg);
			} catch (Exception e) {
				ex = e;
				loginFailMsg.setData("服务器出错");
			}
			if (canLogin) {
				responsemsg.setLoginResult(true);
				session.setIsLogin(true);
				session.setUserName(request.getLoginID());

				// session.Socket = s;
				// session.IPAddress =
				// ((System.Net.IPEndPoint)s.RemoteEndPoint).Address.ToString();
				synchronized (appLoginSockets) {
					if (appLoginSockets.containsKey(session.getSessionID())) {
						appLoginSockets.remove(session.getSessionID());
					}
					appLoginSockets.put(session.getSessionID(), session);
				}
			} else {
				responsemsg.setLoginResult(false);
			}

			int headBeatInt = 5000;

			responsemsg.setSessionID(session.getSessionID());
			responsemsg.setLoginID(request.getLoginID());
			responsemsg.setHeadBeatInterVal(headBeatInt);
			responsemsg.setSessionTimeOut(headBeatInt * 3);
			responsemsg.setLoginFailReson(loginFailMsg.getData());
			LoginSuccessMessage.SetMessageBody(responsemsg);
			session.SendMessage(LoginSuccessMessage);

			if (!canLogin) {
				session.Close();
			}

			if (ex != null) {
				throw ex;
			}
		} catch (Exception ex) {
			this.OnError(ex);
		}
	}

	private void App_LoginOut(Message message, Session session) {
		try {
			Message msg = new Message(MessageType.LOGOUT.getVal());

			session.SendMessage(msg);

			synchronized (appLoginSockets) {
				appLoginSockets.remove(session.getSessionID());
			}
			session.setIsValid(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.OnError(new SocketApplicationException("退出失败", e));
		}
	}

	protected void FormApp(Message message, Session session) {

		try {
			if (getServerModeNeedLogin() && !session.getIsLogin() && !message.IsMessage(MessageType.LOGIN.getVal())) {
				Message msg = new Message(MessageType.RELOGIN.getVal());
				session.SendMessage(msg);
				return;
			}

			if (message.IsMessage(MessageType.LOGIN.getVal())) {
				App_Login(message, session);
			} else if (message.IsMessage(MessageType.LOGOUT.getVal())) {
				App_LoginOut(message, session);
			} else if (message.IsMessage(MessageType.HEARTBEAT.getVal())) {
				App_HeatBeat(message, session);
			}
		} catch (Exception e) {
			this.OnError(e);
		}

		super.FormApp(message, session);
	}
}
