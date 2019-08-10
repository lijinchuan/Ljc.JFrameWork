package Ljc.JFramework.SocketApplication.SocketEasy.Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Ljc.JFramework.AutoResetEvent;
import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.MemoryStreamWriter;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.MessageType;
import Ljc.JFramework.SocketApplication.NegotiationEncryMessage;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketApplicationException;
import Ljc.JFramework.SocketApplication.SocketBase;
import Ljc.JFramework.Utility.Action;
import Ljc.JFramework.Utility.AesEncryHelper;
import Ljc.JFramework.Utility.BitConverter;
import Ljc.JFramework.Utility.RSACryptoServiceProvider;
import Ljc.JFramework.Utility.RsaEncryHelper;
import Ljc.JFramework.Utility.StringUtil;
import Ljc.JFramework.Utility.ThreadPoolUtil;
import Ljc.JFramework.Utility.Tuple;

public class ClientBase extends SocketBase {
	protected Socket socketClient;

	protected boolean isStartClient = false;
	protected boolean errorResume = true;

	/// <summary>
	/// 是否采用安全连接
	/// </summary>
	protected boolean isSecurity = false;
	protected String rsaPubKey = "";
	protected String rsaRrivateKey = "";
	/// <summary>
	/// 安全连接key
	/// </summary>
	protected String encryKey = "";

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
	private AutoResetEvent _startSign = new AutoResetEvent(false);

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
	public ClientBase(String serverip, int serverport, Boolean errorResume, boolean security) {
		this.serverIp = serverip;
		this.ipPort = serverport;
		this.errorResume = errorResume;
		this.isSecurity = security;
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

			if (!StringUtil.isNullOrEmpty(encryKey)) {
				encryKey = "";
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

			if (isSecurity) {
				Tuple<String, String> rsapair = RsaEncryHelper.GenPair();
				this.rsaPubKey = rsapair.GetItem1();
				this.rsaRrivateKey = rsapair.GetItem2();
				Message msg = new Message(MessageType.NEGOTIATIONENCRYR.getVal());
				encryKey = null;
				NegotiationEncryMessage negotiationEncryMessage = new NegotiationEncryMessage();
				negotiationEncryMessage.setPublicKey(rsapair.GetItem1());
				msg.SetMessageBody(negotiationEncryMessage);
				_startSign.reset();
				SocketApplicationComm.SendMessage(socketClient, msg, this.encryKey);
				_startSign.wait(30 * 1000);
				if (StringUtil.isNullOrEmpty(encryKey)) {
					throw new Exception("协商加密失败");
				}
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

				if (dataLen < 4) {
					throw new Exception("最小是4位数：" + this._maxPackageLength);
				}

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				MemoryStreamWriter ms = new MemoryStreamWriter(bs);

				// 校验
				if (inputstream.read(buff4) != 4) {
					throw new Exception("最取四位校验数失败：" + this._maxPackageLength);
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
			if (!StringUtil.isNullOrEmpty(this.encryKey)) {
				data = AesEncryHelper.AesDecrypt(data, this.encryKey);
			}
			Message message = EntityBufCore.DeSerialize(Message.class, data, true);
			if (message.IsMessage(MessageType.NEGOTIATIONENCRYR.getVal())) {
				NegotiationEncryMessage nmsg = message.GetMessageBody(NegotiationEncryMessage.class);
				byte[] ek = RSACryptoServiceProvider.decryptBASE64(nmsg.getEncryKey());
				this.encryKey = new String(RsaEncryHelper.decrypt(ek, this.rsaRrivateKey));
				this._startSign.set();
			} else {
				OnMessage(message);
			}
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
			SocketApplicationException ex = new SocketApplicationException("client出错", e);

			if (socketClient != null && errorResume && (socketClient.isClosed() || !socketClient.isConnected())) {
				ex.Data.put("checksocket", "需要发起重连");

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
				ex.Data.put("checksocket", "不需要发起重连");
			}
		}
	}
}
