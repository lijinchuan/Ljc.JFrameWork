package Ljc.JFramework.Utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class ReflectUtil {
	public static Class GetFieldType(Field field) {
		if (field == null) {
			return null;
		}

		Type type = field.getType();
		if (type == List.class) {
			return field.getGenericType().getClass();
		}

		return (Class) type;

	}

	public static Method GetDeclaredMethod(Class cls, String methodname, Class type) {

		try {
			if (type == null) {
				return cls.getDeclaredMethod(methodname);
			}
			return cls.getDeclaredMethod(methodname, type);
		} catch (NoSuchMethodException ex) {
			return null;
		} catch (SecurityException ex) {
			return null;
		}
	}
}
