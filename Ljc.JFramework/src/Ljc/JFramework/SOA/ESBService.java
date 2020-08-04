package Ljc.JFramework.SOA;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketEasy.Client.SessionClient;
import Ljc.JFramework.Utility.Func;
import Ljc.JFramework.Utility.Tuple;

public class ESBService extends SessionClient {
	private int _serviceNo;
	private boolean SupportTcpServiceRidrect = false;
	private boolean SupportUDPServiceRedirect = false;
	private ESBRedirectService RedirectTcpServiceServer = null;
	private String _serviceName;
	private String _endPointName;

	public int getServiceNo() {
		return this._serviceNo;
	}

	private void setServiceNo(int value) {
		this._serviceNo = value;
	}

	public ESBService(String serverip, int serverport, int sNo, boolean startSession, boolean supportTcpServiceRidrect,
			String serviceName, String endPointName) {
		super(serverip, serverport, startSession, false);
		// TODO Auto-generated constructor stub

		this.setServiceNo(sNo);
		this.OnClientReset.addEvent(this, "ESBService_OnClientReset", null);
		this._serviceName = serviceName;
		this._endPointName = endPointName;
	}

	public ESBService(int sNo, boolean supportTcpServiceRidrect, String serviceName, String endPointName)
			throws Exception {
		super(ESBConfig.ReadConfig().getESBServer(), ESBConfig.ReadConfig().getESBPort(), true, false);
		this.setServiceNo(sNo);
		this.OnClientReset.addEvent(this, "ESBService_OnClientReset", null);
		this._serviceName = serviceName;
		this._endPointName = endPointName;
		this.SupportTcpServiceRidrect = supportTcpServiceRidrect;
	}

	@Override
	protected final byte[] DoMessage(Message message) throws Exception {
		if (message.IsMessage(SOAMessageType.RegisterService.getVal())) {
			return message.getMessageBuffer();
		} else if (message.IsMessage(SOAMessageType.UnRegisterService.getVal())) {
			return message.getMessageBuffer();
		}
		return super.DoMessage(message);
	}

	/*
	 * public Object DoResponse(int funcId, byte[] Param) throws Exception { throw
	 * new Exception(String.format("未知的功能号:%d", funcId)); }
	 */

	public Object DoResponse(Tuple<Integer, byte[]> tup) throws Exception {
		throw new Exception(String.format("未知的功能号:%d", tup.GetItem1()));
	}

	@Override
	protected final void ReciveMessage(Message message) throws Exception {
		if (message.IsMessage(SOAMessageType.DoSOATransferRequest.getVal())) {
			SOATransferRequest request = null;
			try {
				Message responseMsg = new Message(SOAMessageType.DoSOATransferResponse.getVal());
				responseMsg.getMessageHeader().setTransactionID(message.getMessageHeader().getTransactionID());
				SOATransferResponse responseBody = new SOATransferResponse();
				request = message.GetMessageBody(SOATransferRequest.class);
				responseBody.setClientTransactionID(request.getClientTransactionID());
				responseBody.setClientId(request.getClientId());

				try {
					Object result = DoResponse(new Tuple<Integer, byte[]>(request.getFundId(), request.getParam()));
					responseBody.setResult(EntityBufCore.Serialize(result, true));
					responseBody.setIsSuccess(true);

				} catch (Exception ex) {
					responseBody.setIsSuccess(false);
					responseBody.setErrMsg(ex.getMessage());
					this.OnError(ex);
				}

				responseMsg.SetMessageBody(responseBody);

				this.SendMessage(responseMsg);

				return;
			} catch (Exception ex) {
				this.OnError(ex);
				return;
			}
		}

		super.ReciveMessage(message);
	}

	/// <summary>
	/// 启动服务
	/// </summary>
	public void StartService() throws Exception {
		while (!StartClient()) {
			Thread.sleep(1000);
		}
		Login(null, null);
	}

	public final boolean RegisterService() throws Exception {
		if (this.getServiceNo() < 0)
			throw new Exception("注册服务失败：服务号不能为负数");

		StartRedirectService();

		Message msg = new Message(SOAMessageType.RegisterService.getVal());
		msg.getMessageHeader().setTransactionID(SocketApplicationComm.GetSeqNum());
		RegisterServiceRequest req = new RegisterServiceRequest();
		req.setServiceNo(this.getServiceNo());
		if (SupportTcpServiceRidrect) {
			req.setRedirectTcpIps(RedirectTcpServiceServer.GetBindIps());
			req.setRedirectTcpPort(RedirectTcpServiceServer.GetBindTcpPort());
		}
		if (SupportUDPServiceRedirect) {
			// req.RedirectUdpIps = RedirectUpdServiceServer.GetBindIps();
			// req.RedirectUdpPort = RedirectUpdServiceServer.GetBindUdpPort();
		}

		msg.AddCustomData("ServiceName", this._serviceName);
		msg.AddCustomData("EndPointName", this._endPointName);

		msg.SetMessageBody(req);

		boolean boo = SendMessageAnsy(RegisterServiceResponse.class, msg, 30000).getIsSuccess();

		return boo;
	}

	public void UnRegisterService() throws Exception {
		Message msg = new Message(SOAMessageType.UnRegisterService.getVal());
		msg.getMessageHeader().setTransactionID(SocketApplicationComm.GetSeqNum());
		UnRegisterServiceRequest req = new UnRegisterServiceRequest();
		req.setServiceNo(this.getServiceNo());
		msg.SetMessageBody(req);

		boolean boo = SendMessageAnsy(UnRegisterServiceResponse.class, msg, 30000).getIsSuccess();

		if (boo) {
			System.out.println("取消成功");
		}

	}

	void ESBService_OnClientReset() {
		try {
			System.out.println("client is reset!");
			Login(null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void OnSessionResume() {
		super.OnSessionResume();

		try {
			Login(null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected final void OnLoginSuccess() {
		super.OnLoginSuccess();
		int trytimes = 0, maxtrytimes = 10;
		while (true) {
			try {
				if (RegisterService()) {
					System.out.println("注册服务成功");
					break;
				} else {
					System.out.println("注册服务失败");
				}
			} catch (Exception ex) {
				this.OnError(ex);
				// LogHelper.Error("注册服务失败", ex);
			}
			try {
				if (trytimes++ > maxtrytimes) {
					break;
				}
				Thread.sleep(trytimes * 100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void StartRedirectService() throws SocketException {
		List<InetAddress> bindips = Ljc.JFramework.Utility.NetWorkUtil.getIpV4Address();
		List<String> ips = new ArrayList<String>();
		for (InetAddress addr : bindips) {
			ips.add(addr.getHostAddress());
		}
		if (bindips.size() > 0) {
			int iport = 0;
			if (SupportTcpServiceRidrect && RedirectTcpServiceServer == null) {
				int trytimes = 0;
				while (true) {
					try {
						RedirectTcpServiceServer = new ESBRedirectService(ips.toArray(new String[ips.size()]), iport);
						Func<Tuple<Integer, byte[]>, Object> respfun = new Func<Tuple<Integer, byte[]>, Object>();
						respfun.addEvent(this, "DoResponse", Tuple.class);
						RedirectTcpServiceServer.DoResponseAction = respfun;

						RedirectTcpServiceServer.StartServer();
						break;
					} catch (Exception ex) {
						trytimes++;
						if (trytimes >= 10) {
							OnError(new Exception("启动tcp直连服务端口失败,已尝试" + trytimes + "次，端口:" + iport, ex));
							break;
						}
					}
				}
			}
		}
	}

}
