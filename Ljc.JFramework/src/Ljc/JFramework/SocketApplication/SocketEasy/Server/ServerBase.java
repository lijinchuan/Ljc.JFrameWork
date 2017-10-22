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
	private void handleRead(SelectionKey key) throws IOException {
		SocketChannel clntChan = (SocketChannel) key.channel();
		// 获取该信道所关联的附件，这里为缓冲区
		ByteBuffer buf = (ByteBuffer) key.attachment();
		long bytesRead = clntChan.read(buf);
		// 如果read（）方法返回-1，说明客户端关闭了连接，那么客户端已经接收到了与自己发送字节数相等的数据，可以安全地关闭
		if (bytesRead == -1) {
			clntChan.close();
		} else if (bytesRead > 0) {
			// 如果缓冲区总读入了数据，则将该信道感兴趣的操作设置为为可读可写
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
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
							socket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE,
									ByteBuffer.allocate(1024));
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
}
