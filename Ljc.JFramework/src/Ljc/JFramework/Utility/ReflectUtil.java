package Ljc.JFramework.Utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
			 * 【3】如果是泛型参数的类型 { ParameterizedType pt = (ParameterizedType) fc;
			 * 
			 * Class genericClazz = (Class) pt.getActualTypeArguments()[0]; // 【4】
			 * 得到泛型里的class类型对象。
			 * 
			 * return genericClazz; }
			 */
		}

		return type;

	}

	public static Class<?> GetNestType(Field field) {
		Class<?> type = field.getType();
		if (type.isAssignableFrom(java.util.List.class)) {

			Type fc = field.getGenericType();
			if (fc instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) fc;

				Class genericClazz = (Class) pt.getActualTypeArguments()[0];
				return genericClazz;
			}
		} else if (type.isArray()) {
			return type.getComponentType();
		}
		return null;
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
	
	public static void test()
	{
		
	}
}
