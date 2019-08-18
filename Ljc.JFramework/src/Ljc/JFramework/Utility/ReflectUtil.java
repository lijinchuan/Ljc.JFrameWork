package Ljc.JFramework.Utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Ljc.JFramework.BeanFieldAnnotation;

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

	public static List<Field> getDeclaredFieldsOrdered(Class<?> type) {
		// 用来存放所有的属性域
		List<Field> fieldList = new ArrayList<Field>();
		// 过滤带有注解的Field
		for (Field f : type.getDeclaredFields()) {
			fieldList.add(f);
		}

		Collections.sort(fieldList, new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				// TODO Auto-generated method stub
				BeanFieldAnnotation bf1 = o1.getAnnotation(BeanFieldAnnotation.class);
				int sort1 = 0;
				if (bf1 != null) {
					sort1 = bf1.order();
				}
				BeanFieldAnnotation bf2 = o2.getAnnotation(BeanFieldAnnotation.class);
				int sort2 = 0;
				if (bf2 != null) {
					sort2 = bf2.order();
				}
				if (sort1 <= sort2) {
					return -1;
				} else
					return 1;
			}

		});
		return fieldList;
	}
}
