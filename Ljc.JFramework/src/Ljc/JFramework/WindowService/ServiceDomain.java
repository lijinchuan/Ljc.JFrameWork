package Ljc.JFramework.WindowService;

public abstract class ServiceDomain {
	private Thread thread = null;
	private IService service = null;

	protected abstract IService GetService();

	/**
	 * �˳����񷽷�(�÷��������в��� String [] args)
	 * 
	 * @param args
	 */
	public final void Stop(String[] args) {
		System.out.println("ֹͣ����");
		service.setRunFlag(false);
	}

	/**
	 * �������񷽷�(�÷��������в��� String [] args)
	 * 
	 * @param args
	 */
	public final void Start(String[] args) {
		System.out.println("��������");
		// ���������߳�
		service = this.GetService();
		thread = new Thread(service);
		try {
			// �������߳��趨Ϊ�û��̣߳��Ա���StartService�����������߳��˳�
			thread.setDaemon(false);
			if (!thread.isDaemon()) {
				System.out.println("�ɹ��趨�߳�Ϊ�û��̣߳�");
			}

			// ���������߳�
			thread.start();
		} catch (SecurityException se) {
			System.out.println("�߳������趨ʧ�ܣ�");
		}
	}
}
