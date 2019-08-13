package Ljc.JFramework.Utility;

public class Func3<T1, T2, T3, R1> implements Runnable {
	private EventHandler handler = null;
	private T1 params = null;
	private T2 params2 = null;
	private T3 params3 = null;

	public void setParams(T1 param, T2 param2, T3 param3) {
		this.params = param;
		this.params2 = param2;
		this.params3 = param3;
	}

	public Func3() {
		handler = new EventHandler();
	}

	public R1 apply(T1 param, T2 param2, T3 param3) throws Exception {
		if (this.handler == null) {
			return null;
		}

		Object[] rs = this.handler.notifyX(param, param2, param3);
		if (rs == null || rs.length == 0) {
			return null;
		}
		return (R1) rs[0];
	}

	public boolean addEvent(Object object, String methodName, Class<T1> tClass, Class<T2> t2Class, Class<T3> t3Class) {
		return this.handler.addEvent(object, methodName, tClass, t2Class, t3Class);
	}

	public boolean removeEvent(Object object, String methodName, Class<T1> tClass, Class<T2> t2Class,
			Class<T3> t3Class) {
		return this.handler.addEvent(object, methodName, tClass, t2Class, t3Class);
	}

	public R1 notifyEvent(T1 param, T2 param2, T3 param3) throws Exception {
		Object[] rs = this.handler.notifyX(param, param2, param3);
		if (rs == null || rs.length == 0) {
			return null;
		}
		return (R1) rs[0];
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.notifyEvent(params, params2, params3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
