package Ljc.JFramework.Utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectUtil {
	public static Class GetFieldType(Field field) {
		if (field == null) {
			return null;
		}

		Class<?> type = field.getType();
		if (type.isAssignableFrom(java.util.List.class)) {

			return List.class;
			/*
			 * Type fc = field.getGenericType(); if (fc instanceof ParameterizedType) //
			 * ��3������Ƿ��Ͳ��������� { ParameterizedType pt = (ParameterizedType) fc;
			 * 
			 * Class genericClazz = (Class) pt.getActualTypeArguments()[0]; // ��4��
			 * �õ��������class���Ͷ���
			 * 
			 * return genericClazz; }
			 */
		}

		return type;

	}

	public static Method GetMethod(Class cls, String methodname, Class type) {

		try {
			if (type == null) {

				return cls.getMethod(methodname);
			}
			return cls.getMethod(methodname, type);
		} catch (NoSuchMethodException ex) {
			return null;
		} catch (SecurityException ex) {
			return null;
		}
	}
}
