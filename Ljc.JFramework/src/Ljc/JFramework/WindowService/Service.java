package Ljc.JFramework.WindowService;

public class Service implements IService {

	private volatile boolean _runFlag = true;

	public void doWork() {

	}

	@Override
	public final void run() {
		// TODO Auto-generated method stub
		System.out.println("�����߳̿�ʼ����");
		while (getRunFlag()) {
			// do something
		}
		System.out.println("�����߳̽�������");
	}

	@Override
	public final void setRunFlag(boolean runFlag) {
		// TODO Auto-generated method stub
		this._runFlag = runFlag;
	}

	@Override
	public final boolean getRunFlag() {
		// TODO Auto-generated method stub
		return this._runFlag;
	}

}
