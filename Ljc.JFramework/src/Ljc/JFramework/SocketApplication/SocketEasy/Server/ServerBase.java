package Ljc.JFramework.SocketApplication.SocketEasy.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.Session;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketApplicationException;
import Ljc.JFramework.SocketApplication.SocketBase;
import Ljc.JFramework.Utility.Action;
import Ljc.JFramework.Utility.BitConverter;
import Ljc.JFramework.Utility.ThreadPoolUtil;

public class ServerBase extends SocketBase {

	private static final String IPAddress = null;

	protected ServerSocketChannel socketServer;

	protected String[] bindIpArray;
	protected int ipPort;
	protected Boolean isStartServer = false;

	protected ConcurrentHashMap<String, Session> _connectSocketDic = new ConcurrentHashMap<String, Session>();

	/// <summary>
	/// 对象清理之前的事件
	/// </summary>
	public Action BeforRelease;

	private int _maxPackageLength = 1024 * 1024 * 8;

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

	/// <summary>
	///
	/// </summary>
	/// <param name="ip"></param>
	/// <param name="port"></param>
	/// <param name="stop">如果为true,不会断开自动重连</param>
	public ServerBase(String[] bindips, int port) {
		this.bindIpArray = bindips;
		this.ipPort = port;
	}

	public ServerBase() {

	}

	// 服务端信道已经准备好了接收新的客户端连接
	private void handleRead(SelectionKey key) throws Exception {
		SocketChannel clntChan = (SocketChannel) key.channel();
		Session session = (Session) key.attachment();
		try {
			if (session == null) {
				clntChan.close();
				return;
			}

			// 获取该信道所关联的附件，这里为缓冲区
			ByteBuffer buf = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
			long bytesRead = clntChan.read(buf);
			if (bytesRead == -1) {
				throw new SocketApplicationException("读取长度为-1");
			}

			int dataLen = BitConverter.GetInt(buf);

			if (dataLen > this._maxPackageLength) {
				throw new Exception("超过了最大字节数：" + this._maxPackageLength);
			}

			bytesRead = clntChan.read(buf);
			if (bytesRead == -1) {
				throw new SocketApplicationException("读取长度为-1");
			}

			dataLen -= 4;
			ByteBuffer bufbody = ByteBuffer.allocate(dataLen).order(ByteOrder.nativeOrder());
			int readLen = 0;

			while (readLen < dataLen) {
				bytesRead = clntChan.read(bufbody);
				if (bytesRead == -1) {
					if (bytesRead == -1) {
						throw new SocketApplicationException("读取长度为-1");
					}
				}
				readLen += bytesRead;
			}

			key.interestOps(SelectionKey.OP_READ);

			ThreadPoolUtil.QueueUserWorkItem(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message = null;
					Exception messageError = null;
					try {
						message = EntityBufCore.DeSerialize(Message.class, bufbody.array(), true);
					} catch (Exception ex) {
						messageError = ex;
					}

					session.setLastSessionTime(System.currentTimeMillis());
					if (messageError == null) {
						FormApp(message, session);
					} else {
						OnError(messageError);
					}
				}

			});

		} catch (Exception ex) {
			key.cancel();
			clntChan.close();
			this._connectSocketDic.remove(session.getSessionID());
		}
	}

	// 客户端信道已经准备好了将数据从缓冲区写入信道
	public void handleWrite(SelectionKey key) throws IOException {
		// 获取与该信道关联的缓冲区，里面有之前读取到的数据
		ByteBuffer buf = (ByteBuffer) key.attachment();
		// 重置缓冲区，准备将数据写入信道
		buf.flip();
		SocketChannel clntChan = (SocketChannel) key.channel();
		// 将数据写入到信道中
		clntChan.write(buf);
		if (!buf.hasRemaining()) {
			// 如果缓冲区中的数据已经全部写入了信道，则将该信道感兴趣的操作设置为可读
			key.interestOps(SelectionKey.OP_READ);
		}
		// 为读入更多的数据腾出空间
		buf.compact();
	}

	public Boolean StartServer() {
		try {
			if (socketServer == null) {
				socketServer = ServerSocketChannel.open();
				socketServer.configureBlocking(false); // 设置serverSocketChannel为非阻塞，相当于linux中fcntl(socketfd,NO_BLOCKING);
				// socketServer.SetSocketOption(SocketOptionLevel.Socket,
				// SocketOptionName.ReuseAddress, 1);
				if (bindIpArray == null) {
					InetSocketAddress sa = new InetSocketAddress(this.ipPort);
					socketServer.bind(sa);
				} else {
					for (String ip : bindIpArray) {
						socketServer.bind(new InetSocketAddress(ip, ipPort));
					}
				}

				if (this.ipPort == 0) {
					this.ipPort = socketServer.socket().getLocalPort();
				}
			}

			if (!isStartServer) {
				Selector selector = Selector.open();
				socketServer.register(selector, SelectionKey.OP_ACCEPT);// 注册监听事件，poll epoll

				while (selector.select() > 0) {
					// 基于事件驱动的channel，当执行到这里会阻塞，直到至少有一个就绪的channel
					// 获取就绪事件列表，循环迭代，注意，一个selectionKey携带了一个channel，但是一个channel可能有多种事件
					// 就绪，所以分别判断，由于在进入循环之前，只注册了一个serversocketchannel的accept事件，所以满足key.isAcceptable()一定是serversocketchannel
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					for (SelectionKey key : selectionKeys) {
						if (key.isAcceptable()) {
							SocketChannel socket = socketServer.accept();
							socket.configureBlocking(false);
							Session session = new Session();
							session.setIPAddress(socket.getRemoteAddress().toString());
							session.setIsValid(true);
							session.setSessionID(SocketApplicationComm.GetSeqNum());
							session.setSocketChannel(socket);
							// session.setPort(socket.);
							session.setConnectTime(new Date());
							socket.register(selector, SelectionKey.OP_READ, session);
							this._connectSocketDic.put(session.getSessionID(), session);
							continue;
						}
						if (key.isReadable()) {
							handleRead(key);
						}
						if (key.isWritable()) {
							// write(key.channel(), "这是我写出的消息");
							handleWrite(key);
						}
					}
				}
			}

			isStartServer = true;
			return true;
		} catch (Exception e) {
			OnError(e);
			return false;
		}
	}

	protected void FormApp(Message message, Session session) {

	}
}
