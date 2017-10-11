package Ljc.JFramework.Utility;

import java.lang.reflect.Method;

public class Event {
	// Ҫִ�з����Ķ���
	private Object object;
	// Ҫִ�еķ�������
	private String methodName;
	// Ҫִ�з����Ĳ�������
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

	// ִ�и� ����ĸ÷���
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
