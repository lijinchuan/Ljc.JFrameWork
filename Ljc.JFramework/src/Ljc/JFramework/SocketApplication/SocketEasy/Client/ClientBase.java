package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.net.Socket;
import java.util.Date;

import Ljc.JFramework.SocketApplication.SocketBase;

public class ClientBase extends SocketBase {
	protected Socket s;

	protected boolean isStartClient = false;
	protected boolean errorResume = true;
	protected String serverIp;
	protected int ipPort;
	private Date lastReStartClientTime;
	/// <summary>
	/// ��������ʱ����
	/// </summary>
	private int reConnectClientTimeInterval = 5000;

}
