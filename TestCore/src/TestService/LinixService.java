package TestService;

import org.tanukisoftware.wrapper.WrapperListener;

import Ljc.JFramework.WindowService.ServiceDomain;

public class LinixService implements WrapperListener {

	private ServiceDomain _service;
	static LinixService Instance = null;

	public static void main(String[] args) {
		// ´òÓ¡²ÎÊý
		for (String arg : args)
			System.out.println(arg);
		Instance = new LinixService();
		Instance.start(args);
	}

	private LinixService() {
		this._service = new Service2();
	}

	@Override
	public void controlEvent(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer start(String[] arg0) {
		// TODO Auto-generated method stub
		if (_service != null) {
			_service.Start(arg0);
		}
		return null;
	}

	@Override
	public int stop(int arg0) {
		// TODO Auto-generated method stub
		if (_service != null) {
			_service.Stop(new String[] { String.valueOf(arg0) });
		}
		return 0;
	}
}
