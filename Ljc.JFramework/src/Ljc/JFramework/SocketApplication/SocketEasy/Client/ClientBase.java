package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.MemoryStreamWriter;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.SocketBase;
import Ljc.JFramework.Utility.Action;
import Ljc.JFramework.Utility.BitConverter;
import Ljc.JFramework.Utility.ThreadPoolUtil;

public class ClientBase extends SocketBase {
	protected Socket socketClient;

	protected boolean isStartClient = false;
	protected boolean errorResume = true;
	protected String serverIp;
	protected int ipPort;
	private long lastReStartClientTime;
	/// <summary>
	/// 断线重连时间间隔
	/// </summary>
	private int reConnectClientTimeInterval = 5000;
	private byte[] _reciveBuffer = new byte[1024];

	/// <summary>
	/// 对象清理之前的事件
	/// </summary>
	public Action BeforRelease = new Action();

	// region 设置包最大大小
	private int _maxPackageLength = 10 * 1024 * 1024 * 8;

	public Action OnClientReset = new Action();

	/// <summary>
	/// 每次最大接收的字节数byte
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

	/// <summary>
	///
	/// </summary>
	/// <param name="serverip"></param>
	/// <param name="serverport"></param>
	/// <param name="stop">如果为true,不会断开自动重连</param>
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

			boolean isResetClient = false;
			if (socketClient != null) {
				socketClient.close();
				isResetClient = true;
			}

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
						String.format("连接到远程服务器%s失败，端口:%d，原因:%s", serverIp, ipPort, e.getMessage()));
				throw ne;

			} catch (IOException e) {

				lastReStartClientTime = System.currentTimeMillis();
				throw e;
			}

			if (!isStartClient) {
				Action act = new Action();
				act.addEvent(this, "Receiving", null);
				Thread threadClient = new Thread(act);
				threadClient.start();
			}

			isStartClient = true;

			if (isResetClient && OnClientReset != null) {
				OnClientReset.notifyEvent(null);
			}

			return true;
		} catch (Exception e) {
			// OnError(e);
			return false;
		}
	}

	private void Receiving() {
		while (!stop/* && socketClient.Connected */) {
			try {
				byte[] buff4 = new byte[4];
				InputStream inputstream = socketClient.getInputStream();
				int count = inputstream.read(buff4);
				if (count != 4)
					break;
				int dataLen = BitConverter.GetInt(buff4);

				if (dataLen > this._maxPackageLength) {
					throw new Exception("超过了最大字节数：" + this._maxPackageLength);
				}

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				MemoryStreamWriter ms = new MemoryStreamWriter(bs);

				int readLen = 0;

				while (readLen < dataLen) {
					count = inputstream.read(_reciveBuffer, 0, Math.min(dataLen - readLen, _reciveBuffer.length));
					readLen += count;
					ms.write(_reciveBuffer, 0, count);
				}
				byte[] buffer = ms.GetBytes();
				ms.Close();
				Action<byte[]> act = new Action<byte[]>();
				act.addEvent(this, "ProcessMessage", byte[].class);
				act.setParams(buffer);
				ThreadPoolUtil.QueueUserWorkItem(act, buffer);
			} catch (IOException e) {
				break;
			} catch (Exception e) {
				OnError(e);
			}
		}

		try {
			socketClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ProcessMessage(byte[] data) {
		try {

			Message message = EntityBufCore.DeSerialize(Message.class, data, true);
			OnMessage(message);
		} catch (Exception e) {
			OnError(e);
		}
	}

	protected void OnMessage(Message message) {

	}
}
