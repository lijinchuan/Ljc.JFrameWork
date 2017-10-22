package Ljc.JFramework.SocketApplication.SocketEasy.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import Ljc.JFramework.SocketApplication.SocketBase;
import Ljc.JFramework.Utility.Action;

public class ServerBase extends SocketBase {

	private static final String IPAddress = null;

	protected ServerSocketChannel socketServer;

	protected String[] bindIpArray;
	protected int ipPort;
	protected Boolean isStartServer = false;

	/// <summary>
	/// ��������֮ǰ���¼�
	/// </summary>
	public Action BeforRelease;

	private int _maxPackageLength = 1024 * 1024 * 8;

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

	/// <summary>
	///
	/// </summary>
	/// <param name="ip"></param>
	/// <param name="port"></param>
	/// <param name="stop">���Ϊtrue,����Ͽ��Զ�����</param>
	public ServerBase(String[] bindips, int port) {
		this.bindIpArray = bindips;
		this.ipPort = port;
	}

	public ServerBase() {

	}

	// ������ŵ��Ѿ�׼�����˽����µĿͻ�������
	private void handleRead(SelectionKey key) throws IOException {
		SocketChannel clntChan = (SocketChannel) key.channel();
		// ��ȡ���ŵ��������ĸ���������Ϊ������
		ByteBuffer buf = (ByteBuffer) key.attachment();
		long bytesRead = clntChan.read(buf);
		// ���read������������-1��˵���ͻ��˹ر������ӣ���ô�ͻ����Ѿ����յ������Լ������ֽ�����ȵ����ݣ����԰�ȫ�عر�
		if (bytesRead == -1) {
			clntChan.close();
		} else if (bytesRead > 0) {
			// ����������ܶ��������ݣ��򽫸��ŵ�����Ȥ�Ĳ�������ΪΪ�ɶ���д
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
	}

	// �ͻ����ŵ��Ѿ�׼�����˽����ݴӻ�����д���ŵ�
	public void handleWrite(SelectionKey key) throws IOException {
		// ��ȡ����ŵ������Ļ�������������֮ǰ��ȡ��������
		ByteBuffer buf = (ByteBuffer) key.attachment();
		// ���û�������׼��������д���ŵ�
		buf.flip();
		SocketChannel clntChan = (SocketChannel) key.channel();
		// ������д�뵽�ŵ���
		clntChan.write(buf);
		if (!buf.hasRemaining()) {
			// ����������е������Ѿ�ȫ��д�����ŵ����򽫸��ŵ�����Ȥ�Ĳ�������Ϊ�ɶ�
			key.interestOps(SelectionKey.OP_READ);
		}
		// Ϊ�������������ڳ��ռ�
		buf.compact();
	}

	public Boolean StartServer() {
		try {
			if (socketServer == null) {
				socketServer = ServerSocketChannel.open();
				socketServer.configureBlocking(false); // ����serverSocketChannelΪ���������൱��linux��fcntl(socketfd,NO_BLOCKING);
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
			}

			if (!isStartServer) {
				Selector selector = Selector.open();
				socketServer.register(selector, SelectionKey.OP_ACCEPT);// ע������¼���poll epoll

				while (selector.select() > 0) {
					// �����¼�������channel����ִ�е������������ֱ��������һ��������channel
					// ��ȡ�����¼��б�ѭ��������ע�⣬һ��selectionKeyЯ����һ��channel������һ��channel�����ж����¼�
					// ���������Էֱ��жϣ������ڽ���ѭ��֮ǰ��ֻע����һ��serversocketchannel��accept�¼�����������key.isAcceptable()һ����serversocketchannel
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					for (SelectionKey key : selectionKeys) {
						if (key.isAcceptable()) {
							SocketChannel socket = socketServer.accept();
							socket.configureBlocking(false);
							socket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE,
									ByteBuffer.allocate(1024));
							continue;
						}
						if (key.isReadable()) {
							handleRead(key);
						}
						if (key.isWritable()) {
							// write(key.channel(), "������д������Ϣ");
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
}
