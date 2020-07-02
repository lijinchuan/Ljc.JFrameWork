package Ljc.JFramework.SOA;

import java.util.Random;

import Ljc.JFramework.Utility.Action;
import Ljc.JFramework.Utility.Func3;

class ESBClientPoolManager {
	private ESBClient[] Clients;
	public Action<Exception> OnError = new Action<Exception>();

	public ESBClientPoolManager(Integer clientcount, String ip, RegisterServiceInfo info,
			Func3<Integer, String, RegisterServiceInfo, ESBClient> getClient) throws Exception {
		if (clientcount == 0) {
			clientcount = 5;
		}

		Clients = new ESBClient[clientcount];
		for (int i = 0; i < clientcount; i++) {
			ESBClient client = getClient.notifyEvent(i, ip, info);
			if (client == null) {
				client = new ESBClient();
			}
			client.Error.addEvent(this, "client_Error", Exception.class);
			client.StartSession();
			client.Login(null, null);
			Clients[i] = client;
		}
	}

	public ESBClient RandClient() {
		int idx = new Random(System.currentTimeMillis()).nextInt(Clients.length);

		ESBClient client = Clients[idx];

		return client;
	}

	void client_Error(Exception obj) {
		if (this.OnError != null) {
			try {
				this.OnError.notifyEvent(obj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("client_Error:" + obj.getMessage());
	}
}
