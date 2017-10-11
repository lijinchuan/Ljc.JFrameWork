package Ljc.JFramework.Utility;

public class Action<T1> {
	EventHandler handler = null;

	public Action() {
		handler = new EventHandler();
	}

	public boolean addEvent(Object object, String methodName, Class<T1> tClass) {
		return this.handler.addEvent(object, methodName, tClass);
	}

	public boolean removeEvent(Object object, String methodName, Class<T1> tClass) {
		return this.handler.addEvent(object, methodName, tClass);
	}

	public void notifyX(T1 param) throws Exception {
		this.handler.notifyX(param);
	}
}
