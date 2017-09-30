package Ljc.JFramework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;

import Ljc.JFramework.TypeUtil.UInt16;
import Ljc.JFramework.Utility.BitConverter;
import Ljc.JFramework.Utility.ReflectUtil;
import Ljc.JFramework.Utility.StringUtil;

public class EntityBufCore {
	private final static short _defaultShort = 0;
	private final static short _defaultUShort = 0;
	private final static int _defaultInt = 0;
	private final static long _defaultLong = 0;
	private final static byte _defaultByte = 0;
	private final static char _defaultChar = '\0';
	private final static double _defaultDouble = 0;
	private final static float _defaultFloat = 0;
	private final static String _defaultString = "";
	private final static BigDecimal _defaultDecimal = BigDecimal.ZERO;
	private final static Boolean _defaultBool = Boolean.FALSE;

	private static EntityBufType MapBufType(Field field, Box<Boolean> isArray) {
		EntityBufType ebtype = new EntityBufType();
		ebtype.setValueType(field.getType());

		Class<?> type = field.getType();
		if (type.isArray()) {
			isArray.setData(true);
			ebtype.setClassType(type.getComponentType());
		} else {
			isArray.setData(false);
			ebtype.setClassType(type);
		}

		switch (type.getTypeName()) {
		case "short":
			ebtype.setEntityType(EntityType.SHORT);
			ebtype.setDefaultValue(_defaultShort);
			break;
		case "Ljc.JFramework.TypeUtil.UInt16":
		case "Ljc.JFramework.TypeUtil.UShort":
			ebtype.setEntityType(EntityType.USHORT);
			ebtype.setDefaultValue(_defaultUShort);
			break;
		case "int":
			ebtype.setEntityType(EntityType.INT32);
			ebtype.setDefaultValue(_defaultInt);
			break;
		case "long":
			ebtype.setEntityType(EntityType.INT64);
			ebtype.setDefaultValue(_defaultLong);
			break;
		case "byte":
			ebtype.setEntityType(EntityType.BYTE);
			ebtype.setDefaultValue(_defaultByte);
			break;
		case "char":
			ebtype.setEntityType(EntityType.CHAR);
			ebtype.setDefaultValue(_defaultChar);
			break;
		case "double":
			ebtype.setEntityType(EntityType.DOUBLE);
			ebtype.setDefaultValue(_defaultDouble);
			break;
		case "float":
			ebtype.setEntityType(EntityType.FLOAT);
			ebtype.setDefaultValue(_defaultFloat);
			break;
		case "java.lang.String":
			ebtype.setEntityType(EntityType.STRING);
			ebtype.setDefaultValue(_defaultString);
			break;
		case "Ljc.JFramework.TypeUtil.DateTime":
			ebtype.setEntityType(EntityType.DATETIME);
			break;
		case "java.math.BigDecimal":
			ebtype.setEntityType(EntityType.DECIMAL);
			ebtype.setDefaultValue(_defaultDecimal);
			break;
		case "java.lang.Boolean":
			ebtype.setEntityType(EntityType.BOOL);
			ebtype.setDefaultValue(_defaultBool);
			break;
		case "java.util.HashMap":
			ebtype.setEntityType(EntityType.DICTIONARY);
			break;
		case "java.util.List":
			ebtype.setEntityType(EntityType.LIST);
			break;
		default:
			if (isArray.getData()) {
				ebtype.setEntityType(EntityType.ARRAY);
			} else if (ebtype.getClassType().isEnum()) {
				ebtype.setEntityType(EntityType.ENUM);
			} else // if (ebtype.getClassType().isc.IsClass)
			{
				ebtype.setEntityType(EntityType.COMPLEX);
			}
			break;
		}

		return ebtype;

	}

	public static void TestSerialize(short val) throws IOException {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		MemoryStreamWriter ms = new MemoryStreamWriter(bs);
		ms.write(BitConverter.GetBytes(val));

		byte[] bytes = bs.toByteArray();
		for (byte b : bytes) {
			System.out.println(b);
		}
	}

	private static void SerializeSimple(Object val, Box<Boolean> arrayBox, EntityBufType bufType,
			MemoryStreamWriter msWriter) throws Exception {
		// if (bufType.EntityType == EntityType.COMPLEX)
		// {
		// throw new Exception("无法序列化复杂类型");
		// }

		Boolean isArray = arrayBox.getData();
		switch (bufType.getEntityType()) {
		case BYTE:
			if (isArray) {
				msWriter.WriteByteArray((byte[]) val);
			} else {
				msWriter.WriteByte((byte) val);
			}
			break;
		case STRING:
			if (isArray) {
				msWriter.WriteStringArray((String[]) val);
			} else {
				msWriter.WriteString((String) val);
			}
			break;
		case SHORT:
		case INT16:
			if (isArray) {
				msWriter.WriteInt16Array((short[]) val);
			} else {
				msWriter.WriteInt16((short) val);
			}
			break;
		case USHORT:
			if (isArray) {
				msWriter.WriteUInt16Array((UInt16[]) val);
			} else {
				msWriter.WriteUInt16((UInt16) val);
			}
			break;
		case INT32:
			if (isArray) {
				msWriter.WriteInt32Array((int[]) val);
			} else {
				msWriter.WriteInt32((int) val);
			}
			break;
		case DECIMAL:
			if (isArray) {
				msWriter.WriteDeciamlArray((BigDecimal[]) val);
			} else {
				msWriter.WriteDecimal((BigDecimal) val);
			}
			break;
		case DOUBLE:
			if (isArray) {
				msWriter.WriteDoubleArray((double[]) val);
			} else {
				msWriter.WriteDouble((double) val);
			}
			break;
		case INT64:
			if (isArray) {
				msWriter.WriteInt64Array((long[]) val);
			} else {
				msWriter.WriteInt64((long) val);
			}
			break;
		case DATETIME:
			if (isArray) {
				msWriter.WriteDateTimeArray((Date[]) val);
			} else {
				msWriter.WriteDateTime((Date) val);
			}
			break;
		case BOOL:
			if (isArray) {
				msWriter.WriteBoolArray((boolean[]) val);
			} else {
				msWriter.WriteBool((boolean) val);
			}
			break;
		case ENUM:
			if (isArray) {
				Object[] objarray = (Object[]) val;
				String[] strarr = new String[objarray.length];
				int i = 0;
				for (Object o : objarray) {
					strarr[i++] = o.toString();
				}
				msWriter.WriteStringArray(strarr);
			} else {
				msWriter.WriteString(val.toString());
			}
			break;
		/*
		 * case EntityType.DICTIONARY: if (isArray) { if (val == null) {
		 * msWriter.WriteInt32(-1); break; } var dicArray = (Array)val;
		 * msWriter.WriteInt32(dicArray.Length); for (int i = 0; i < dicArray.Length;
		 * i++) { Serialize(dicArray.GetValue(i), msWriter); } } else { if (val == null)
		 * { msWriter.WriteInt32(-1); break; } // IDictionary idic = (IDictionary)val;
		 * //写入长度 msWriter.WriteInt32(idic.Count); int i = 0; foreach (var kv in idic) {
		 * object k=kv.Eval("Key"); object v = kv.Eval("Value");
		 * 
		 * Serialize(k, msWriter); Serialize(v, msWriter); i++; } } break; case
		 * EntityType.LIST: if (isArray) { if (val == null) { msWriter.WriteInt32(-1);
		 * break; } var listarr = (Array)val; msWriter.WriteInt32(listarr.Length); for
		 * (int i = 0; i < listarr.Length; i++) { Serialize(listarr.GetValue(i),
		 * msWriter); } } else { if (val == null) { msWriter.WriteInt32(-1); break; }
		 * var list = (IList)val; msWriter.WriteInt32(list.Count); foreach (var item in
		 * list) { Serialize(item, msWriter); } } break; case EntityType.ARRAY: if
		 * (isArray) { if (val == null) { msWriter.WriteInt32(-1); break; } var listarr
		 * = (Array)val; msWriter.WriteInt32(listarr.Length); for (int i = 0; i <
		 * listarr.Length; i++) { Serialize(listarr.GetValue(i), msWriter); } } else {
		 * if (val == null) { msWriter.WriteInt32(-1); break; } var arr = (Array)val;
		 * msWriter.WriteInt32(arr.Length); foreach (var item in arr) { Serialize(item,
		 * msWriter); } } break;
		 */
		default:
			throw new Exception("序列化错误");
		}
	}

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
			System.out.println("----------------------------------");
			System.out.println(fieldname);
			System.out.println("是否是基础类型：" + f.getType().isPrimitive());
			System.out.println("是否数组:" + f.getType().isArray());
			System.out.println("类型名称:" + f.getType().getName());
			System.out.println("类型名称(getSimpleName):" + f.getType().getSimpleName());
			System.out.println("类型名称(getTypeName):" + f.getType().getTypeName());
			System.out.println("是否枚举:" + f.getType().isEnum());

			if (f.getType().isArray()) {
				System.out.println("数组类型:" + f.getType().getComponentType().getTypeName());
			}
		}

	}
}
