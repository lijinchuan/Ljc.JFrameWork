package Ljc.JFramework.SOA;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketEasy.Client.SessionClient;
import Ljc.JFramework.Utility.Tuple;

public class ESBService extends SessionClient {
	private int _serviceNo;
	private boolean SupportTcpServiceRidrect = false;
	private boolean SupportUDPServiceRedirect = false;
	private ESBRedirectService RedirectTcpServiceServer = null;

	public int getServiceNo() {
		return this._serviceNo;
	}

	private void setServiceNo(int value) {
		this._serviceNo = value;
	}

	public ESBService(String serverip, int serverport, int sNo, boolean startSession,
			boolean supportTcpServiceRidrect) {
		super(serverip, serverport, startSession);
		// TODO Auto-generated constructor stub

		this.setServiceNo(sNo);
	}

	public ESBService(int sNo, boolean supportTcpServiceRidrect) throws Exception {
		super(ESBConfig.ReadConfig().getESBServer(), ESBConfig.ReadConfig().getESBPort(), true);
		this.setServiceNo(sNo);
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
	 * new Exception(String.format("δ֪�Ĺ��ܺ�:%d", funcId)); }
	 */

	public Object DoResponse(Tuple<Integer, byte[]> tup) throws Exception {
		throw new Exception(String.format("δ֪�Ĺ��ܺ�:%d", tup.GetItem1()));
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
	/// ��������
	/// </summary>
	public void StartService() throws Exception {
		while (!StartClient()) {
			Thread.sleep(1000);
		}
		Login(null, null);
	}

	public final boolean RegisterService() throws Exception {
		if (this.getServiceNo() < 0)
			throw new Exception("ע�����ʧ�ܣ�����Ų���Ϊ����");

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
			System.out.println("ȡ���ɹ�");
		}

	}

	@Override
	protected void OnSessionResume() {
		super.OnSessionResume();

		while (true) {
			try {
				if (RegisterService()) {
					// LogHelper.Instance.Info("���ӻָ���ע�����ɹ�");
					break;
				} else {
					// LogHelper.Instance.Info("���ӻָ���ע�����ʧ��");
				}
			} catch (Exception ex) {
				// LogHelper.Instance.Error("���ӻָ���ע�����ʧ��", ex);
				this.OnError(ex);
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected final void OnLoginSuccess() {
		super.OnLoginSuccess();
		while (true) {
			try {
				if (RegisterService()) {
					System.out.println("ע�����ɹ�");
					break;
				} else {
					System.out.println("ע�����ʧ��");
				}
			} catch (Exception ex) {
				this.OnError(ex);
				// LogHelper.Error("ע�����ʧ��", ex);
			}
			try {
				Thread.sleep(3000);
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
						RedirectTcpServiceServer.DoResponseAction = s -> {
							try {
								return this.DoResponse(s);
							} catch (Exception e) {
								return e;
							}

						};
						RedirectTcpServiceServer.StartServer();
						break;
					} catch (Exception ex) {
						trytimes++;
						if (trytimes >= 10) {
							OnError(new Exception("����tcpֱ������˿�ʧ��,�ѳ���" + trytimes + "�Σ��˿�:" + iport, ex));
							break;
						}
					}
				}
			}
		}
	}

}
