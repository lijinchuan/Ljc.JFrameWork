package Ljc.JFramework.Utility;

import java.lang.reflect.Method;

public class Event {
	// Ҫִ�з����Ķ���
	private Object object;
	// Ҫִ�еķ�������
	private String methodName;
	// Ҫִ�з����Ĳ�������
	private Class[] paramTypes = null;
	private Method method = null;

	public Event() {

	}

	public Event(Object object, String methodName, Class... clss) {
		this.object = object;
		this.methodName = methodName;
		if (!MethodUtil.ParamsIsNullEmpty(clss)) {
			this.paramTypes = clss;
		}
	}

	public String getMethodName() {
		return this.methodName;
	}

	public Object getObject() {
		return object;
	}

	public Class[] getParamTypes() {
		return this.paramTypes;
	}

	// ִ�и� ����ĸ÷���
	public Object invoke(Object... params) throws Exception {
		if (this.method == null) {
			Class tp = object.getClass();
			while (true) {

				try {
					method = (this.paramTypes == null) ? tp.getDeclaredMethod(this.getMethodName())
							: tp.getDeclaredMethod(this.getMethodName(), this.paramTypes);

					method.setAccessible(true);

					break;
				} catch (NoSuchMethodException ex) {
					if (tp == Object.class) {
						throw ex;
					}
					tp = tp.getSuperclass();
					if (tp == null) {
						throw ex;
					}

				}

			}
		}
		if (null == method) {
			return null;
		}
		if (this.getParamTypes() == null) {
			return method.invoke(this.getObject());
		} else {
			return method.invoke(this.getObject(), params);
		}
	}
}
