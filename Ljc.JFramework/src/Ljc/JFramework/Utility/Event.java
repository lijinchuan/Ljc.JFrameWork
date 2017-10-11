package Ljc.JFramework.Utility;

import java.lang.reflect.Method;

public class Event {
	// 要执行方法的对象
	private Object object;
	// 要执行的方法名称
	private String methodName;
	// 要执行方法的参数类型
	private Class[] paramTypes;
	private Method method = null;

	public Event() {

	}

	public Event(Object object, String methodName, Class... clss) {
		this.object = object;
		this.methodName = methodName;
		this.paramTypes = clss;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public Object getObject() {
		return object;
	}

	// 执行该 对象的该方法
	public void invoke(Object... params) throws Exception {
		if (this.method == null) {
			method = this.paramTypes == null ? object.getClass().getMethod(this.getMethodName())
					: object.getClass().getMethod(this.getMethodName(), this.paramTypes);
		}
		if (null == method) {
			return;
		}
		method.invoke(this.getObject(), params);
	}
}
