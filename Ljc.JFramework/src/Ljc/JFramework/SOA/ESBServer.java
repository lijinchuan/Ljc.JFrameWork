package Ljc.JFramework.SOA;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Ljc.JFramework.CoreException;
import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.Session;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketEasy.Server.SessionServer;
import Ljc.JFramework.TypeUtil.DateTime;
import Ljc.JFramework.Utility.EnvironmentUtil;
import Ljc.JFramework.Utility.Tuple;

public class ESBServer extends SessionServer {
	private static Object LockObj = new Object();
	protected List<ESBServiceInfo> ServiceContainer = new LinkedList<ESBServiceInfo>();
	ConcurrentMap<String, Session> ClientSessionList = new ConcurrentHashMap<String, Session>();

	public ESBServer(int port) {
		super(port);
		// TODO Auto-generated constructor stub

		this.setServerModeNeedLogin(false);
	}

	public Object DoRequest(int funcId, byte[] param) throws Exception {
		switch (funcId) {
		case Consts.FunNo_ECHO: {
			SOAServerEchoResponse obj = new SOAServerEchoResponse();
			obj.setOk(true);
			return obj;
		}
		case Consts.FunNo_Environment: {
			SOAServerEnvironmentResponse obj = new SOAServerEnvironmentResponse();
			obj.setMachineName(EnvironmentUtil.GetHostName());
			obj.setOSVersion(EnvironmentUtil.GetOSVersion());
			obj.setProcessorCount(Runtime.getRuntime().availableProcessors());
			return obj;
		}
		case Consts.FunNo_ExistsAServiceNo: {
			int serviceno = EntityBufCore.DeSerialize(int.class, param, true);
			for (ESBServiceInfo item : ServiceContainer) {
				if (item.getServiceNo() == serviceno) {
					return true;
				}
			}
			return false;
		}
		case Consts.FunNo_GetRegisterServiceInfo: {
			GetRegisterServiceInfoRequest req = EntityBufCore.DeSerialize(GetRegisterServiceInfoRequest.class, param,
					true);
			GetRegisterServiceInfoResponse resp = new GetRegisterServiceInfoResponse();

			resp.setServiceNo(req.getServiceNo());
			List<RegisterServiceInfo> list = new ArrayList<RegisterServiceInfo>();
			for (ESBServiceInfo item : ServiceContainer) {
				if (item.getServiceNo() == req.getServiceNo()) {
					RegisterServiceInfo info = new RegisterServiceInfo();
					info.setServiceNo(item.getServiceNo());
					info.setRedirectTcpIps(item.getRedirectTcpIps());
					info.setRedirectTcpPort(item.getRedirectTcpPort());
					info.setRedirectUdpIps(item.getRedirectUdpIps());
					info.setRedirectUdpPort(item.getRedirectUdpPort());
					list.add(info);
				}
			}
			resp.setInfos(list.toArray(new RegisterServiceInfo[list.size()]));

			return resp;
		}
		case Consts.FunNo_Test: {
			String req = EntityBufCore.DeSerialize(String.class, param, true);

			return req;
		}
		default: {
			throw new CoreException(String.format("未实现的功能:%d", funcId));
		}
		}
	}

	void DoTransferRequest(Session session, String msgTransactionID, SOARequest request) throws Exception {
		session.setBusinessTimeStamp(DateTime.Now());
		session.setTag(new Tuple<Integer, Integer>(request.getServiceNo(), request.getFuncId()));

		SOAResponse resp = new SOAResponse();
		Message msgRet = new Message(SOAMessageType.DoSOAResponse.getVal());
		msgRet.getMessageHeader().setTransactionID(msgTransactionID);
		resp.setIsSuccess(true);

		// 调用本地的方法
		if (request.getServiceNo() == 0) {
			try {
				Object obj = DoRequest(request.getFuncId(), request.getParam());
				resp.setResult(EntityBufCore.Serialize(obj, true));
			} catch (Exception ex) {
				resp.setIsSuccess(false);
				resp.setErrMsg(ex.getMessage());
			}

			msgRet.SetMessageBody(resp);
			session.SendMessage(msgRet);
		} else {
			// 查询服务
			List<ESBServiceInfo> serviceInfos = new ArrayList<ESBServiceInfo>();
			for (ESBServiceInfo item : ServiceContainer) {
				if (item.getServiceNo() == request.getServiceNo()) {
					serviceInfos.add(item);
				}
			}

			if (serviceInfos == null || serviceInfos.size() == 0) {
				resp.setIsSuccess(false);
				resp.setErrMsg(String.format("%d 服务未注册。", request.getServiceNo()));
			}

			if (resp.getIsSuccess()) {
				ESBServiceInfo serviceInfo = null;
				if (serviceInfos.size() == 1) {
					serviceInfo = serviceInfos.get(0);
				} else {
					Random rd = new Random();
					int idx = rd.nextInt(serviceInfos.size());
					serviceInfo = serviceInfos.get(idx);
				}

				try {
					if (System.currentTimeMillis() - serviceInfo.getSession().getLastSessionTime() > 30000) {
						synchronized (LockObj) {
							ServiceContainer.remove(serviceInfo);
							serviceInfo.getSession().Close();
						}

						throw new Exception(String.format("%d服务可能不可用,30秒无应答。", request.getServiceNo()));
					}

					String clientid = session.getSessionID();
					SOATransferRequest transferrequest = new SOATransferRequest();
					transferrequest.setClientId(clientid);
					transferrequest.setFundId(request.getFuncId());
					transferrequest.setParam(request.getParam());
					transferrequest.setClientTransactionID(msgTransactionID);

					Message msg = new Message(SOAMessageType.DoSOATransferRequest.getVal());
					msg.getMessageHeader().setTransactionID(SocketApplicationComm.GetSeqNum());
					msg.SetMessageBody(transferrequest);

					ClientSessionList.putIfAbsent(msg.getMessageHeader().getTransactionID(), session);

					if (serviceInfo.getSession().SendMessage(msg)) {
						// LogHelper.Instance.Debug(string.Format("发送SOA请求,请求序列:{0},服务号:{1},功能号:{2}",
						// msgTransactionID, request.ServiceNo, request.FuncId));
						return;
					} else {
						ClientSessionList.remove(clientid);
					}
					// var result = SendMessageAnsy<byte[]>(serviceInfo.Session, msg);

					// resp.Result = result;
				} catch (Exception ex) {
					OnError(ex);

					resp.setIsSuccess(false);
					resp.setErrMsg(ex.getMessage());
				}
			}

			if (!resp.getIsSuccess()) {
				msgRet.SetMessageBody(resp);
				session.SendMessage(msgRet);
			}
		}
	}

	void DoTransferResponse(SOATransferResponse response) {
		try {
			Session session = ClientSessionList.get(response.getClientTransactionID());

			if (session != null) {
				ClientSessionList.remove(response.getClientId());

				SOAResponse resp = new SOAResponse();
				Message msgRet = new Message(SOAMessageType.DoSOAResponse.getVal());
				msgRet.getMessageHeader().setTransactionID(response.getClientTransactionID());
				resp.setIsSuccess(response.getIsSuccess());
				resp.setErrMsg(response.getErrMsg());
				resp.setResult(response.getResult());

				msgRet.SetMessageBody(resp);
				session.SendMessage(msgRet);

				// Tuple toulp = (Tuple<Integer, Integer>) session.getTag();
			} else {
				CoreException ex = new CoreException(
						String.format("DoTransferResponse(SOATransferResponse response)失败,请求序列号:{0}",
								response.getClientTransactionID()));
				ex.Data.put("response.ClientId", response.getClientId());

				// LogHelper.Instance.Error("DoTransferResponse出错", ex);
			}
		} catch (CoreException ex) {
			ex.Data.put("请求序列号", response.getClientTransactionID());

			this.OnError(ex);
		}
	}

	@Override
	protected void FormApp(Message message, Session session) {
		try {
			if (message.IsMessage(SOAMessageType.DoSOARequest.getVal())) {
				SOARequest req = message.GetMessageBody(SOARequest.class);
				DoTransferRequest(session, message.getMessageHeader().getTransactionID(), req);
				return;
			} else if (message.IsMessage(SOAMessageType.DoSOATransferResponse.getVal())) {
				SOATransferResponse resp = message.GetMessageBody(SOATransferResponse.class);
				DoTransferResponse(resp);
				return;
			} else if (message.IsMessage(SOAMessageType.RegisterService.getVal())) {
				Message msg = new Message(SOAMessageType.RegisterService.getVal());
				msg.getMessageHeader().setTransactionID(message.getMessageHeader().getTransactionID());
				try {
					RegisterServiceRequest req = message.GetMessageBody(RegisterServiceRequest.class);

					if (req.getServiceNo() == 0)
						throw new CoreException("不允许使用服务号:0");

					synchronized (LockObj) {
						List<ESBServiceInfo> li = new ArrayList<ESBServiceInfo>();
						boolean isexists = false;
						for (ESBServiceInfo p : ServiceContainer) {
							if (p.getSession().getSessionID() == session.getSessionID()
									&& p.getSession().getIPAddress() == session.getIPAddress()
									&& p.getSession().getPort() == session.getPort()
									&& p.getServiceNo() == req.getServiceNo()) {
								li.add(p);
								continue;
							}

							if (!isexists && p.getServiceNo() == req.getServiceNo()
									&& p.getSession().getSessionID() == session.getSessionID()) {
								isexists = true;
							}
						}
						ServiceContainer.removeAll(li);

						if (!isexists) {
							ESBServiceInfo info = new ESBServiceInfo();
							info.setServiceNo(req.getServiceNo());
							info.setSession(session);
							info.setRedirectTcpIps(req.getRedirectTcpIps());
							info.setRedirectTcpPort(req.getRedirectTcpPort());
							info.setRedirectUdpIps(req.getRedirectUdpIps());
							info.setRedirectUdpPort(req.getRedirectUdpPort());

							ServiceContainer.add(info);
						}
					}

					RegisterServiceResponse resp = new RegisterServiceResponse();
					resp.setIsSuccess(true);
					msg.SetMessageBody(resp);
				} catch (Exception ex) {
					RegisterServiceResponse resp = new RegisterServiceResponse();
					resp.setIsSuccess(false);
					resp.setErrMsg(ex.getMessage());
					msg.SetMessageBody(resp);
				}

				session.SendMessage(msg);
				return;
			} else if (message.IsMessage(SOAMessageType.UnRegisterService.getVal())) {
				Message msg = new Message(SOAMessageType.UnRegisterService.getVal());
				msg.getMessageHeader().setTransactionID(message.getMessageHeader().getTransactionID());
				try {
					UnRegisterServiceRequest req = message.GetMessageBody(UnRegisterServiceRequest.class);
					synchronized (LockObj) {
						List<ESBServiceInfo> li = new ArrayList<ESBServiceInfo>();
						for (ESBServiceInfo p : ServiceContainer) {
							if (p.getSession().getIPAddress() == session.getIPAddress()
									&& p.getSession().getPort() == session.getPort()
									&& p.getServiceNo() == req.getServiceNo()) {
								li.add(p);
							}
						}
						ServiceContainer.removeAll(li);
					}
					UnRegisterServiceResponse resp = new UnRegisterServiceResponse();
					resp.setIsSuccess(true);
					msg.SetMessageBody(resp);
				} catch (Exception e) {
					UnRegisterServiceResponse resp = new UnRegisterServiceResponse();
					resp.setErrMsg(e.getMessage());
					msg.SetMessageBody(resp);
				}

				session.SendMessage(msg);
				return;
			}
		} catch (Exception ex) {
			this.OnError(ex);
		}

		super.FormApp(message, session);
	}
}
