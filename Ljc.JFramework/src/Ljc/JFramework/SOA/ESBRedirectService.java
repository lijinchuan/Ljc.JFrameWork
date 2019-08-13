package Ljc.JFramework.SOA;

import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.SocketApplication.Message;
import Ljc.JFramework.SocketApplication.Session;
import Ljc.JFramework.SocketApplication.SocketEasy.Server.SessionServer;
import Ljc.JFramework.Utility.Func;
import Ljc.JFramework.Utility.StringUtil;
import Ljc.JFramework.Utility.Tuple;

public class ESBRedirectService extends SessionServer {
	public Func<Tuple<Integer, byte[]>, Object> DoResponseAction;

	public ESBRedirectService(String[] ips, int port) {
		super(ips, port);
		this.setServerModeNeedLogin(false);
	}

	public String[] GetBindIps() {
		return this.bindIpArray;
	}

	public int GetBindTcpPort() {
		return this.ipPort;
	}

	@Override
	protected void FormApp(Message message, Session session) {
		if (message.IsMessage(SOAMessageType.DoSOARedirectRequest.getVal())) {
			try {
				if (DoResponseAction != null) {
					SOARedirectRequest reqbag = EntityBufCore.DeSerialize(SOARedirectRequest.class,
							message.getMessageBuffer(), true);
					Object obj = DoResponseAction
							.apply(new Tuple<Integer, byte[]>(reqbag.getFuncId(), reqbag.getParam()));

					if (obj instanceof Exception) {
						throw (Exception) obj;
					}

					if (!StringUtil.isNullOrEmpty(message.getMessageHeader().getTransactionID())) {
						Message retmsg = new Message(SOAMessageType.DoSOARedirectResponse.getVal());
						retmsg.getMessageHeader().setTransactionID(message.getMessageHeader().getTransactionID());
						SOARedirectResponse resp = new SOARedirectResponse();
						resp.setIsSuccess(true);
						resp.setResult(EntityBufCore.Serialize(obj, true));
						retmsg.SetMessageBody(resp);

						session.SendMessage(retmsg);
					} else {
						throw new Exception("服务未实现");
					}
				}
			} catch (Exception ex) {
				Message retmsg = new Message(SOAMessageType.DoSOARedirectResponse.getVal());
				retmsg.getMessageHeader().setTransactionID(message.getMessageHeader().getTransactionID());
				SOARedirectResponse resp = new SOARedirectResponse();
				resp.setIsSuccess(false);
				resp.setErrMsg(ex.toString());
				retmsg.SetMessageBody(resp);

				try {
					session.SendMessage(retmsg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					this.OnError(e);
				}
			}
		} else {
			super.FormApp(message, session);
		}
	}
}
