package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.net.Socket;
import java.util.Date;

import Ljc.JFramework.SocketApplication.SocketBase;
import Ljc.JFramework.Utility.Action;

public class ClientBase extends SocketBase {
	protected Socket s;

	protected boolean isStartClient = false;
	protected boolean errorResume = true;
	protected String serverIp;
	protected int ipPort;
	private Date lastReStartClientTime;
	/// <summary>
	/// 断线重连时间间隔
	/// </summary>
	private int reConnectClientTimeInterval = 5000;

	/// <summary>
	/// 对象清理之前的事件
	/// </summary>
	public Action BeforRelease = new Action();

}
