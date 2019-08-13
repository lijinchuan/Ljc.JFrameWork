package Ljc.JFramework.Utility;

public class Func<T1, R1> implements Runnable {
	private EventHandler handler = null;
	private T1 params = null;

	public void setParams(T1 param) {
		this.params = param;
	}

	public Func() {
		handler = new EventHandler();
	}

	public R1 apply(T1 param) throws Exception {
		if (this.handler == null) {
			return null;
		}

		Object[] rs = this.handler.notifyX(param);
		if (rs == null || rs.length == 0) {
			return null;
		}
		return (R1) rs[0];
	}

	public boolean addEvent(Object object, String methodName, Class<?> tClass) {
		return this.handler.addEvent(object, methodName, tClass);
	}

	public boolean removeEvent(Object object, String methodName, Class<T1> tClass) {
		return this.handler.addEvent(object, methodName, tClass);
	}

	public R1 notifyEvent(T1 param) throws Exception {
		Object[] rs = this.handler.notifyX(param);
		if (rs == null || rs.length == 0) {
			return null;
		}
		return (R1) rs[0];
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
