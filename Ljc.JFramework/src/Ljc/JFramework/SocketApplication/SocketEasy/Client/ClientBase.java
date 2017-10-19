package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.MemoryStreamWriter;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.SocketApplicationException;
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
	/// ��������ʱ����
	/// </summary>
	private int reConnectClientTimeInterval = 5000;
	private byte[] _reciveBuffer = new byte[1024];

	/// <summary>
	/// ��������֮ǰ���¼�
	/// </summary>
	public Action BeforRelease = new Action();

	// region ���ð�����С
	private int _maxPackageLength = 10 * 1024 * 1024 * 8;

	public Action OnClientReset = new Action();

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
			if (socketClient != null && !socketClient.isClosed()) {
				socketClient.close();
			}
			isStartClient = false;
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	private boolean IsConnected() {
		return this.socketClient != null && this.socketClient.isConnected() && !this.socketClient.isClosed();
	}

	public Boolean StartClient() {
		try {
			if (this.IsConnected())
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
						String.format("���ӵ�Զ�̷�����%sʧ�ܣ��˿�:%d��ԭ��:%s", serverIp, ipPort, e.getMessage()));
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
					throw new Exception("����������ֽ�����" + this._maxPackageLength);
				}

				if (dataLen < 4) {
					throw new Exception("��С��4λ����" + this._maxPackageLength);
				}

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				MemoryStreamWriter ms = new MemoryStreamWriter(bs);

				// У��
				if (inputstream.read(buff4) != 4) {
					throw new Exception("��ȡ��λУ����ʧ�ܣ�" + this._maxPackageLength);
				}
				dataLen -= 4;

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

	@Override
	protected void OnError(Exception e) {
		super.OnError(e);

		if (!this.stop) {
			ClientBase client = this;
			SocketApplicationException ex = new SocketApplicationException("client����", e);

			if (socketClient != null && errorResume && (socketClient.isClosed() || !socketClient.isConnected())) {
				ex.Data.put("checksocket", "��Ҫ��������");

				try {
					Ljc.JFramework.Utility.ThreadPoolUtil.QueueUserWorkItem(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							client.StartClient();
						}

					});
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// new Action(() => StartClient()).BeginInvoke(null, null);
			} else {
				ex.Data.put("errorResume", errorResume);
				ex.Data.put("socketClient.Connected", socketClient.isConnected());
				ex.Data.put("checksocket", "����Ҫ��������");
			}
		}
	}
}
