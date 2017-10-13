package Ljc.JFramework.Utility;

public final class Action<T1> implements Runnable {
	private EventHandler handler = null;
	private T1 params = null;

	public void setParams(T1 param) {
		this.params = param;
	}

	public Action() {
		handler = new EventHandler();
	}

	public boolean addEvent(Object object, String methodName, Class<T1> tClass) {
		return this.handler.addEvent(object, methodName, tClass);
	}

	public boolean removeEvent(Object object, String methodName, Class<T1> tClass) {
		return this.handler.addEvent(object, methodName, tClass);
	}

	public void notifyEvent(T1 param) throws Exception {
		this.handler.notifyX(param);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.notifyEvent(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
