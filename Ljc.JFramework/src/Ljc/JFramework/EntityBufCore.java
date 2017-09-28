package Ljc.JFramework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import Ljc.JFramework.Utility.ReflectUtil;
import Ljc.JFramework.Utility.StringUtil;

public class EntityBufCore {
	public static void Serialize(Object o, MemoryStreamWriter ms) {
		Class<?> cls = o.getClass();

		for (Field f : cls.getDeclaredFields()) {
			String fieldname = f.getName();
			if (fieldname.startsWith("_")) {
				fieldname = fieldname.substring(1);
			}
			fieldname = StringUtil.captureName(fieldname);

			Method getMethod = ReflectUtil.GetMethod(cls, "get" + fieldname, null);
			Method setMethod = ReflectUtil.GetMethod(cls, "set" + fieldname, ReflectUtil.GetFieldType(f));

			if (getMethod == null || setMethod == null) {
				continue;
			}
			System.out.println(fieldname);
			System.out.println("�Ƿ��ǻ������ͣ�" + f.getType().isPrimitive());
			System.out.println("�Ƿ�����:" + f.getType().isArray());
			System.out.println("��������:" + f.getType().getName());
			System.out.println("�Ƿ�ö��:" + f.getType().isEnum());
		}
	}
}
