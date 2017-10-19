package Ljc.JFramework.SOA;

import java.util.Random;
import java.util.function.Function;

import Ljc.JFramework.Utility.Func;

public class ESBClientPoolManager {
	private ESBClient[] Clients;

	public ESBClientPoolManager(Integer clientcount, Func<Integer, ESBClient> getClient) throws Exception {
		if (clientcount == 0) {
			clientcount = 5;
		}

		Clients = new ESBClient[clientcount];
		for (int i = 0; i < clientcount; i++) {
			ESBClient client = getClient.notifyEvent(i);
			if (client == null) {
				client = new ESBClient();
			}
			client.Error.addEvent(this, "client_Error", Exception.class);
			client.Login(null, null);
			Clients[i] = client;
		}
	}

	public ESBClientPoolManager(Integer clientcount, Function<Integer, ESBClient> getClient) throws Exception {
		if (clientcount == 0) {
			clientcount = 5;
		}

		Clients = new ESBClient[clientcount];
		for (int i = 0; i < clientcount; i++) {
			ESBClient client = getClient.apply(i);
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
		System.out.println("³ö´í:" + obj.getMessage());
	}
}
