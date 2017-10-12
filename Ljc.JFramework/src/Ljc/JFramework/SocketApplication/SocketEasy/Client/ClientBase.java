package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Ljc.JFramework.SocketApplication.ClientReceiving;
import Ljc.JFramework.SocketApplication.SocketBase;
import Ljc.JFramework.Utility.Action;

public class ClientBase extends SocketBase {
	protected Socket socketClient;

	protected boolean isStartClient = false;
	protected boolean errorResume = true;
	protected String serverIp;
	protected int ipPort;
	private long lastReStartClientTime;
	/// <summary>
	/// ��������ʱ����
	/// </summary>
	private int reConnectClientTimeInterval = 5000;

	/// <summary>
	/// ��������֮ǰ���¼�
	/// </summary>
	public Action BeforRelease = new Action();

	// region ���ð�����С
	private int _maxPackageLength = 10 * 1024 * 1024 * 8;

	/// <summary>
	/// ÿ�������յ��ֽ���byte
	/// </summary>
	public int getMaxPackageLength() {

		return _maxPackageLength;
	}

	public void setMaxPackageLength(int value) {

		if (value <= 0) {
			return;
		}
		_maxPackageLength = value;
	}
	// endregion

	private ClientReceiving receiving;

	/// <summary>
	///
	/// </summary>
	/// <param name="serverip"></param>
	/// <param name="serverport"></param>
	/// <param name="stop">���Ϊtrue,����Ͽ��Զ�����</param>
	public ClientBase(String serverip, int serverport, Boolean errorResume) {
		this.serverIp = serverip;
		this.ipPort = serverport;
		this.errorResume = errorResume;
	}

	public ClientBase() {

	}

	public void CloseClient() {
		try {
			if (socketClient != null && socketClient.isConnected()) {
				socketClient.close();
			}
			isStartClient = false;
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	public Boolean StartClient() {
		try {
			if (socketClient != null && socketClient.isConnected())
				return true;

			if (System.currentTimeMillis() - lastReStartClientTime <= reConnectClientTimeInterval)
				return false;

			if (socketClient != null) {
				socketClient.close();
			}
			try {
				socketClient = new Socket(this.serverIp, this.ipPort);
				socketClient.setReceiveBufferSize(32000);
				socketClient.setSendBufferSize(32000);
				socketClient.setTcpNoDelay(true);
			} catch (UnknownHostException e) {
				Exception ne = new Exception(
						String.format("���ӵ�Զ�̷�����%sʧ�ܣ��˿�:%d��ԭ��:%s", serverIp, ipPort, e.getMessage()));
				throw ne;

			} catch (IOException e) {

				lastReStartClientTime = System.currentTimeMillis();
				throw e;
			}

			if (!isStartClient) {
				Thread threadClient = new Thread(receiving);
				threadClient.start();
			}

			isStartClient = true;
			return true;
		} catch (Exception e) {
			// OnError(e);
			return false;
		}
	}
}
