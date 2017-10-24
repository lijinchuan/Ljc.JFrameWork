package Ljc.JFramework.WindowService;

public abstract class ServiceDomain {
	private Thread thread = null;
	private IService service = null;

	protected abstract IService GetService();

	/**
	 * 退出服务方法(该方法必须有参数 String [] args)
	 * 
	 * @param args
	 */
	public final void Stop(String[] args) {
		System.out.println("停止服务");
		service.setRunFlag(false);
	}

	/**
	 * 启动服务方法(该方法必须有参数 String [] args)
	 * 
	 * @param args
	 */
	public final void Start(String[] args) {
		System.out.println("启动服务");
		// 产生服务线程
		service = this.GetService();
		thread = new Thread(service);
		try {
			// 将服务线程设定为用户线程，以避免StartService方法结束后线程退出
			thread.setDaemon(false);
			if (!thread.isDaemon()) {
				System.out.println("成功设定线程为用户线程！");
			}

			// 启动服务线程
			thread.start();
		} catch (SecurityException se) {
			System.out.println("线程类型设定失败！");
		}
	}
}
