package TestService;

import Ljc.JFramework.WindowService.IService;

public class Service1 extends Ljc.JFramework.WindowService.ServiceDomain {

	static Service1 svc;

	@Override
	protected IService GetService() {
		// TODO Auto-generated method stub
		return new Servicexxx();
	}

	public static void StopService(String[] args) {
		svc.Stop(args);
	}

	/**
	 * �������񷽷�(�÷��������в��� String [] args)
	 * 
	 * @param args
	 */
	public static void StartService(String[] args) {
		svc = new Service1();
		svc.Start(args);
	}

}
