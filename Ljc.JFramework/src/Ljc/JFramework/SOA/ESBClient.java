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
import Ljc.JFramework.Utility.StringUtil;

public class ESBClient extends SessionClient {
	private static ESBClientPoolManager _clientmanager;
	private static HashMap<Integer, List<ESBClientPoolManager>> _esbClientDicManager = new HashMap<Integer, List<ESBClientPoolManager>>();

	static {
		try {
			ESBClientPoolManager _clientmanager = new ESBClientPoolManager(4, (i) -> null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int _serviceNo = -1;

	public ESBClient(String serverip, int serverport, boolean startSession) {
		super(serverip, serverport, startSession);
		// TODO Auto-generated constructor stub
	}

	public void SetServiceNo(int value) {
		this._serviceNo = value;
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

		String[] temporderips = new String[6 + ips.length];
		int offset = 6;
		for (String ip : ips) {
			int pos = 5;
			if (ip.startsWith("192.168.0.")) {
				pos = 0;
			}

			else if (ip.startsWith("192.168.1.")) {
				pos = 1;
			}

			else if (ip.startsWith("192.168.")) {
				pos = 2;
			}

			else if (ip.startsWith("172.")) {
				pos = 3;
			}

			else if (ip.startsWith("10.")) {
				pos = 4;
			} else {
				pos = 5;
			}
			if (!StringUtil.isNullOrEmpty(temporderips[pos])) {
				temporderips[offset++] = ip;
			} else {
				temporderips[pos] = ip;
			}
		}

		String[] orderips = new String[ips.length];
		int index = 0;
		for (String ip : temporderips) {
			if (!StringUtil.isNullOrEmpty(ip)) {
				orderips[index++] = ip;
			}
		}

		return orderips;
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
				Runnable act = new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						GetRegisterServiceInfoResponse respserviceinfo;
						try {
							respserviceinfo = DoSOARequest(GetRegisterServiceInfoResponse.class,
									Consts.ESBServerServiceNo, Consts.FunNo_GetRegisterServiceInfo,
									new GetRegisterServiceInfoRequest(serviceId));

							if (respserviceinfo.getInfos() != null && respserviceinfo.getInfos().length > 0) {
								List<ESBClientPoolManager> poollist = new ArrayList<ESBClientPoolManager>();
								for (RegisterServiceInfo info : respserviceinfo.getInfos()) {
									if (info.getRedirectTcpIps() != null) {

										for (String ip : OrderIp(info.getRedirectTcpIps())) {
											try {
												ESBClient client = new ESBClient(ip, info.getRedirectTcpPort(), false);
												client.SetServiceNo(info.getServiceNo());

												client.Error.addEvent(client, "Clent_Error", Exception.class);

												if (client.StartSession()) {
													poollist.add(new ESBClientPoolManager(5, idx -> {
														if (idx == 0) {
															return client;
														}
														ESBClient newclient = new ESBClient(ip,
																info.getRedirectTcpPort(), false);
														newclient.SetServiceNo(info.getServiceNo());
														newclient.StartSession();

														newclient.Error.addEvent(client, "Clent_Error",
																Exception.class);
														return newclient;
													}));
													// LogHelper.Instance.Debug(string.Format("创建tcp客户端成功:{0},端口{1}",
													// ip,
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
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				};
				Ljc.JFramework.Utility.ThreadPoolUtil.QueueUserWorkItem(act);
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

	private void Clent_Error(Exception ex) {

		this.CloseClient();
		// client.Dispose();
		synchronized (_esbClientDicManager) {
			_esbClientDicManager.remove(this._serviceNo);
		}
	}

}
