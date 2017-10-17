package Ljc.JFramework.SOA;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.SocketApplicationComm;
import Ljc.JFramework.SocketApplication.SocketApplicationException;
import Ljc.JFramework.SocketApplication.SocketEasy.Client.SessionClient;

public class ESBClient extends SessionClient {
	private static ESBClientPoolManager _clientmanager;

	static {
		try {
			ESBClientPoolManager _clientmanager = new ESBClientPoolManager(0, null);
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

}
