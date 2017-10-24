package TestService;

import java.util.Date;

public class Servicexxx extends Ljc.JFramework.WindowService.Service {
	@Override
	public void doWork() {
		Ljc.JFramework.LogManager.Info(new Date().toString());

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}
}
