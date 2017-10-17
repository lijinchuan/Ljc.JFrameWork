package Ljc.JFramework.SOA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketApplicationException;
import Ljc.JFramework.SocketApplication.SocketEasy.Client.SessionClient;
import Ljc.JFramework.Utility.Action;

public class ESBClient extends SessionClient {
	private static ESBClientPoolManager _clientmanager;
	private static HashMap<Integer, List<ESBClientPoolManager>> _esbClientDicManager = new HashMap<Integer, List<ESBClientPoolManager>>();

	static {
		try {
			ESBClientPoolManager _clientmanager = new ESBClientPoolManager(0, (i) -> null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ESBClient(String serverip, int serverport, boolean startSession) {
		super(serverip, serverport, startSession);
		// TODO Auto-generated constructor stub
	}

	public ESBClient() throws Exception {
		super(ESBConfig.ReadConfig().getESBServer(), ESBConfig.ReadConfig().getESBPort(),
				ESBConfig.ReadConfig().getAutoStart());
	}

	public String GetESBServer() {
		return this.serverIp;
	}

	public int GetESBPort() {
		return this.ipPort;
	}

	public <T> T DoRequest(Class<T> classt, int serviceno, int funcid, Object param) throws Exception {
		SOARequest request = new SOARequest();
		request.setServiceNo(serviceno);
		request.setFuncId(funcid);
		if (param == null) {
			request.setParam(null);
		} else {
			request.setParam(EntityBufCore.Serialize(param, true));
		}

		Message msg = new Message(SOAMessageType.DoSOARequest.getVal());
		msg.getMessageHeader().setTransactionID(SocketApplicationComm.GetSeqNum());
		msg.setMessageBuffer(EntityBufCore.Serialize(request, true));

		T result = SendMessageAnsy(classt, msg, 30000);
		return result;
	}

	public <T> T DoRequest(Class<T> classt, int funcid, Object param) throws Exception {
		SOARedirectRequest request = new SOARedirectRequest();
		request.setFuncId(funcid);
		if (param == null) {
			request.setParam(null);
		} else {
			request.setParam(EntityBufCore.Serialize(param, true));
		}

		Message msg = new Message(SOAMessageType.DoSOARedirectRequest.getVal());
		msg.getMessageHeader().setTransactionID(SocketApplicationComm.GetSeqNum());
		msg.setMessageBuffer(EntityBufCore.Serialize(request, true));

		T result = SendMessageAnsy(classt, msg, 30000);
		return result;
	}

	@Override
	protected byte[] DoMessage(Message message) throws Exception {
		if (message.IsMessage(SOAMessageType.DoSOAResponse.getVal())) {
			SOAResponse resp = message.GetMessageBody(SOAResponse.class);
			if (!resp.getIsSuccess()) {
				BuzException = new SocketApplicationException(resp.getErrMsg());
				// 这里最好抛出错误来
				throw BuzException;
			}
			return resp.getResult();
		} else if (message.IsMessage(SOAMessageType.DoSOARedirectResponse.getVal())) {
			SOARedirectResponse resp = message.GetMessageBody(SOARedirectResponse.class);
			if (!resp.getIsSuccess()) {
				BuzException = new SocketApplicationException(resp.getErrMsg());
				// 这里最好抛出错误来
				throw BuzException;
			}
			return resp.getResult();
		}
		return super.DoMessage(message);
	}

	public static <T> T DoSOARequest(Class<T> classt, int serviceId, int functionId, Object param) throws Exception {
		// using (var client = new ESBClient())
		// {
		// client.StartClient();
		// client.Error += client_Error;
		// var result = client.DoRequest<T>(serviceId, functionId, param);

		// return result;
		// }

		T result = _clientmanager.RandClient().DoRequest(classt, serviceId, functionId, param);

		return result;
	}

	private static String[] OrderIp(String[] ips) {
		String[] orderips = new String[6];
		for (String ip : ips) {
			if (ip.startsWith("192.168.0.")) {
				orderips[0] = ip;
			}

			else if (ip.startsWith("192.168.1.")) {
				orderips[1] = ip;
			}

			else if (ip.startsWith("192.168.")) {
				orderips[2] = ip;
			}

			else if (ip.startsWith("172.")) {
				orderips[3] = ip;
			}

			else if (ip.startsWith("10.")) {
				orderips[4] = ip;
			} else {
				orderips[5] = ip;
			}
		}

		// return orderips;

		return ips;
	}

	public static <T> T DoSOARequest2(Class<T> classt, int serviceId, int functionId, Object param) throws Exception {
		List<ESBClientPoolManager> tcpclientlist = _esbClientDicManager.getOrDefault(serviceId, null);
		if (tcpclientlist == null) {
			boolean takecleint = false;
			synchronized (_esbClientDicManager) {
				tcpclientlist = _esbClientDicManager.getOrDefault(serviceId, null);
				if (tcpclientlist == null) {
					takecleint = true;
					_esbClientDicManager.put(serviceId, null);
				}
				;
			}

			if (takecleint) {
				Action action = new Action();
				Ljc.JFramework.Utility.ThreadPoolUtil.QueueUserWorkItem(action, null);

				GetRegisterServiceInfoResponse respserviceinfo = DoSOARequest(GetRegisterServiceInfoResponse.class,
						Consts.ESBServerServiceNo, Consts.FunNo_GetRegisterServiceInfo,
						new GetRegisterServiceInfoRequest(serviceId));

				if (respserviceinfo.getInfos() != null && respserviceinfo.getInfos().length > 0) {
					List<ESBClientPoolManager> poollist = new ArrayList<ESBClientPoolManager>();
					for (RegisterServiceInfo info : respserviceinfo.getInfos()) {
						if (info.getRedirectTcpIps() != null) {

							for (String ip : OrderIp(info.getRedirectTcpIps())) {
								try {
									ESBClient client = new ESBClient(ip, info.getRedirectTcpPort(), false);
									/*
									 * client.Error.addEvent(object, methodName, tClass) { if (ex is
									 * System.Net.WebException) { client.CloseClient(); client.Dispose(); lock
									 * (_esbClientDicManager) { _esbClientDicManager.remove(serviceId); } } };
									 */
									if (client.StartSession()) {
										poollist.add(new ESBClientPoolManager(5, idx -> {
											if (idx == 0) {
												return client;
											}
											ESBClient newclient = new ESBClient(ip, info.getRedirectTcpPort(), false);
											newclient.StartSession();
											/*
											 * newclient.Error += (ex) => { if (ex is System.Net.WebException) {
											 * client.CloseClient(); client.Dispose(); lock (_esbClientDicManager) {
											 * _esbClientDicManager.Remove(serviceId); } } };
											 */
											return newclient;
										}));
										// LogHelper.Instance.Debug(string.Format("创建tcp客户端成功:{0},端口{1}", ip,
										// info.RedirectTcpPort));
										break;
									}
								} catch (Exception ex) {
									// LogHelper.Instance.Debug(string.Format("创建tcp客户端失败:{0},端口{1}", ip,
									// info.RedirectTcpPort));
								}
							}
						}
					}
					if (poollist.size() > 0) {
						synchronized (_esbClientDicManager) {
							_esbClientDicManager.put(serviceId, poollist);
						}
					}
				}
			}
		}

		List<ESBClientPoolManager> poolmanagerlist = _esbClientDicManager.getOrDefault(serviceId, null);
		if (poolmanagerlist != null && poolmanagerlist.size() > 0) {
			// Console.WriteLine("直连了");
			ESBClientPoolManager poolmanager = poolmanagerlist.size() == 1 ? poolmanagerlist.get(0)
					: poolmanagerlist.get(new Random().nextInt(poolmanagerlist.size()));

			ESBClient client = poolmanager.RandClient();
			// LogHelper.Instance.Debug("功能"+serviceId+"直连" + client.ipString + ":" +
			// client.ipPort);
			return client.DoRequest(classt, functionId, param);
		} else {
			return DoSOARequest(classt, serviceId, functionId, param);
		}
	}

}
