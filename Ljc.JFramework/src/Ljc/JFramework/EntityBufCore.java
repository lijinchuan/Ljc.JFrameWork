package Ljc.JFramework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Ljc.JFramework.TypeUtil.UInt16;
import Ljc.JFramework.Utility.BitConverter;
import Ljc.JFramework.Utility.ReflectUtil;
import Ljc.JFramework.Utility.StringUtil;
import Ljc.JFramework.Utility.Tuple;

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

	private static Map<Integer, List<Tuple<EntityBufType, Boolean>>> EntityBufTypeDic = new HashMap<Integer, List<Tuple<EntityBufType, Boolean>>>();
	private static ReentrantReadWriteLock EntityBufTypeDicRWLock = new ReentrantReadWriteLock();
	private static Map<Integer, Tuple<EntityBufType, Boolean>> TypeBufTypeDic = new HashMap<Integer, Tuple<EntityBufType, Boolean>>();
	private static ReentrantReadWriteLock TypeBufTypeDicRWLock = new ReentrantReadWriteLock();

	private static EntityBufType MapBufType(Class<?> type, Box<Boolean> isArray) {
		EntityBufType ebtype = new EntityBufType();
		ebtype.setValueType(type);

		if (type.isArray()) {
			isArray.setData(true);
			ebtype.setClassType(type.getComponentType());
			ebtype.setValueType(type.getComponentType());
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
		case "java.lang.Integer":
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

	private static void SerializeSimple(Object val, boolean isArray, EntityBufType bufType, MemoryStreamWriter msWriter)
			throws Exception {
		// if (bufType.EntityType == EntityType.COMPLEX)
		// {
		// throw new Exception("无法序列化复杂类型");
		// }

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
		case CHAR:
			if (isArray) {
				msWriter.WriteCharArray((char[]) val);
			} else {
				msWriter.WriteChar((char) val);
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
		case FLOAT: {
			if (isArray) {
				msWriter.WriteFloatArray((float[]) val);
			} else {
				msWriter.WriteFloat((float) val);
			}
			break;
		}
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
				msWriter.WriteString((String) val);
			}
			break;
		case DICTIONARY:
			if (isArray) {
				if (val == null) {
					msWriter.WriteInt32(-1);
					break;
				}
				Object[] obs = (Object[]) val;
				msWriter.WriteInt32(obs.length);
				for (int i = 0; i < obs.length; i++) {
					Serialize(obs[i], msWriter);
				}
			} else {
				if (val == null) {
					msWriter.WriteInt32(-1);
					break;
				}
				//
				Map idic = (HashMap) val;
				// 写入长度
				msWriter.WriteInt32(idic.size());
				Iterator iter = idic.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object k = entry.getKey();
					Object v = entry.getValue();
					Serialize(k, msWriter);
					Serialize(v, msWriter);
				}
			}
			break;
		case LIST:
			if (isArray) {
				if (val == null) {
					msWriter.WriteInt32(-1);
					break;
				}
				Object[] listarr = (Object[]) val;
				msWriter.WriteInt32(listarr.length);
				for (int i = 0; i < listarr.length; i++) {
					Serialize(listarr[i], msWriter);
				}
			} else {
				if (val == null) {
					msWriter.WriteInt32(-1);
					break;
				}
				List listarr = (List) val;
				msWriter.WriteInt32(listarr.size());
				ListIterator it = listarr.listIterator();
				while (it.hasNext()) {
					Object obj = it.next();
					Serialize(obj, msWriter);
				}
			}
			break;
		case ARRAY:
			if (isArray) {
				if (val == null) {
					msWriter.WriteInt32(-1);
					break;
				}
				Object[] listarr = (Object[]) val;
				msWriter.WriteInt32(listarr.length);
				for (int i = 0; i < listarr.length; i++) {
					Serialize(listarr[i], msWriter);
				}
			} else {
				if (val == null) {
					msWriter.WriteInt32(-1);
					break;
				}
				Object[] arr = (Object[]) val;
				msWriter.WriteInt32(arr.length);
				for (Object item : arr) {
					Serialize(item, msWriter);
				}
			}
			break;
		default:
			throw new Exception("序列化错误:" + bufType.getEntityType().toString());
		}
	}

	private static void SerializeComplex(Object val, boolean isArray, EntityBufType bufType, MemoryStreamWriter ms)
			throws Exception {
		if (isArray) {
			Object[] vals = (Object[]) val;
			int len = -1;
			if (vals != null) {
				len = vals.length;
			}
			ms.WriteInt32(len);
			if (len > 0) {
				for (Object v : vals) {
					// 写入标志
					if (v != null) {
						EntityBufTypeFlag flag = EntityBufTypeFlag.Empty;
						ms.WriteByte((byte) flag.getVal());
						Serialize(v, ms);
					} else {
						EntityBufTypeFlag flag = EntityBufTypeFlag.VlaueNull;
						ms.WriteByte((byte) flag.getVal());
					}
				}
			}

		} else {
			if (val != null) {
				EntityBufTypeFlag flag = EntityBufTypeFlag.Empty;
				ms.WriteByte((byte) flag.getVal());
				Serialize(val, ms);
			} else {
				EntityBufTypeFlag flag = EntityBufTypeFlag.VlaueNull;
				ms.WriteByte((byte) flag.getVal());
			}
		}
	}

	public static Tuple<EntityBufType, Boolean> GetTypeBufType(Class<?> tp) {
		if (tp == null) {
			return null;
		}
		int key = tp.hashCode();
		Tuple<EntityBufType, Boolean> getval = null;
		if ((getval = TypeBufTypeDic.getOrDefault(key, null)) != null) {
			return getval;
		} else {
			try {
				TypeBufTypeDicRWLock.writeLock().lock();
				Box<Boolean> isArray = new Box<Boolean>();
				EntityBufType objType = MapBufType(tp, isArray);
				Tuple<EntityBufType, Boolean> tuple = new Tuple<EntityBufType, Boolean>(objType, isArray.getData());

				TypeBufTypeDic.put(key, tuple);

				return tuple;
			} finally {
				TypeBufTypeDicRWLock.writeLock().unlock();
			}
		}
	}

	public static List<Tuple<EntityBufType, Boolean>> GetTypeEntityBufType(Class<?> tp) throws ClassNotFoundException {
		if (tp == null)
			return null;

		int key = tp.hashCode();
		boolean isrealsereadlock = false;
		try {
			EntityBufTypeDicRWLock.readLock().lock();
			List<Tuple<EntityBufType, Boolean>> val;

			if ((val = EntityBufTypeDic.getOrDefault(key, null)) != null) {
				return val;
			}

			try {
				EntityBufTypeDicRWLock.readLock().unlock();
				isrealsereadlock = true;
				EntityBufTypeDicRWLock.writeLock().lock();

				List<Tuple<EntityBufType, Boolean>> list = new LinkedList<Tuple<EntityBufType, Boolean>>();

				Stack<Class<?>> parents = new Stack<Class<?>>();
				parents.add(tp);
				Class temptp = tp;
				while (true) {
					temptp = temptp.getSuperclass();
					if (temptp == null || temptp == Object.class) {
						break;
					}
					parents.add(temptp);
				}

				while (!parents.isEmpty() && (temptp = parents.pop()) != null) {
					for (Field f : temptp.getDeclaredFields()) {
						String fieldname = f.getName();
						if (fieldname.startsWith("_")) {
							fieldname = fieldname.substring(1);
						}
						fieldname = StringUtil.captureName(fieldname);

						Method getMethod = ReflectUtil.GetMethod(temptp, "get" + fieldname, null);
						Method setMethod = ReflectUtil.GetMethod(temptp, "set" + fieldname,
								ReflectUtil.GetFieldType(f));

						if (getMethod == null || setMethod == null) {
							continue;
						}
						Box<Boolean> bool = new Box<Boolean>();
						EntityBufType buftype = MapBufType(f.getType(), bool);
						PropertyInfoEx prop = new PropertyInfoEx(f);
						prop.setGetValueMethod(getMethod);
						prop.setSetValueMethod(setMethod);
						buftype.setProperty(prop);
						if (buftype.getEntityType() == EntityType.LIST) {
							buftype.setValueType(EntityBufCore.GetListValueType(buftype));
						}
						list.add(new Tuple<EntityBufType, Boolean>(buftype, bool.getData()));
					}
				}

				EntityBufTypeDic.put(key, list);

				return list;
			} finally {
				EntityBufTypeDicRWLock.writeLock().unlock();
			}
		} finally {
			if (!isrealsereadlock) {
				EntityBufTypeDicRWLock.readLock().unlock();
			}
		}
	}

	public static byte[] Serialize(Object o) throws Exception {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		try {

			MemoryStreamWriter ms = new MemoryStreamWriter(s);

			Serialize(o, ms);

			return ms.GetBytes();
		} finally {
			s.close();
		}
	}

	public static void Serialize(Object o, MemoryStreamWriter ms) throws Exception {
		if (o == null) {
			EntityBufTypeFlag flag = EntityBufTypeFlag.VlaueNull;
			ms.WriteByte((byte) flag.getVal());
			return;
		} else {
			EntityBufTypeFlag flag = EntityBufTypeFlag.Empty;
			ms.WriteByte((byte) flag.getVal());
		}

		Tuple<EntityBufType, Boolean> tuple = GetTypeBufType(o.getClass());

		if (tuple.GetItem1().getEntityType() != EntityType.COMPLEX) {
			SerializeSimple(o, tuple.GetItem2(), tuple.GetItem1(), ms);
			return;
		}

		boolean isArray;
		List<Tuple<EntityBufType, Boolean>> entitybuftypelist = GetTypeEntityBufType(o.getClass());
		Tuple<EntityBufType, Boolean> tp = null;
		for (int i = 0; i < entitybuftypelist.size(); i++) {
			tp = entitybuftypelist.get(i);
			isArray = tp.GetItem2();

			Object val = tp.GetItem1().getProperty().getGetValueMethod().invoke(o);

			if (tp.GetItem1().getEntityType() == EntityType.COMPLEX) {
				SerializeComplex(val, isArray, tp.GetItem1(), ms);
			} else {
				SerializeSimple(val, isArray, tp.GetItem1(), ms);
			}
		}

	}

	private static Class GetListValueType(EntityBufType listype) throws ClassNotFoundException {
		PropertyInfoEx pop = listype.getProperty();
		if (pop == null) {
			return null;
		}
		Method method = pop.GetSetValueMethod();
		if (method == null) {
			return null;
		}
		Type[] types = method.getGenericParameterTypes();
		if (types == null || types.length == 0) {
			return null;
		}
		ParameterizedType pt = (ParameterizedType) types[0];
		Type[] args = pt.getActualTypeArguments();
		if (args == null || args.length != 1) {
			return null;
		}
		return Class.forName(args[0].getTypeName());
	}

	private static Tuple<Class, Class> GetDirctionaryKeyValueType(EntityBufType iDicType)
			throws ClassNotFoundException {
		PropertyInfoEx pop = iDicType.getProperty();
		if (pop == null) {
			return null;
		}
		Method method = pop.GetSetValueMethod();
		if (method == null) {
			return null;
		}
		Type[] types = method.getGenericParameterTypes();
		if (types == null || types.length == 0) {
			return null;
		}
		ParameterizedType pt = (ParameterizedType) types[0];
		Type[] args = pt.getActualTypeArguments();
		if (args == null || args.length != 2) {
			return null;
		}
		return new Tuple<Class, Class>(Class.forName(args[0].getTypeName()), Class.forName(args[1].getTypeName()));
	}

	private static Object DeserializeSimple(EntityBufType buftype, boolean isArray, MemoryStreamReader msReader)
			throws Exception {
		if (buftype.getEntityType() == EntityType.COMPLEX) {
			throw new Exception("无法反序列化复杂类型");
		}

		if (buftype.getEntityType() == EntityType.UNKNOWN) {
			throw new Exception("无法反序列化未知类型");
		}

		switch (buftype.getEntityType()) {
		case BYTE:
			if (isArray) {
				return msReader.ReadByteArray();
			} else {
				return msReader.ReadByte();
			}
		case STRING:
			if (isArray) {
				return msReader.ReadStringArray();
			} else {
				return msReader.ReadString();
			}
		case CHAR:
			if (isArray) {
				return msReader.ReadCharArray();
			} else {
				return msReader.ReadRedirectChar();
			}
		case SHORT:
		case INT16:
			if (isArray) {
				return msReader.ReadInt16Array();
			} else {
				return msReader.ReadInt16();
			}
		case USHORT:
			if (isArray) {
				return msReader.ReadUInt16Array();
			} else {
				return msReader.ReadUInt16();
			}
		case INT32:
			if (isArray) {
				return msReader.ReadInt32Array();
			} else {
				return msReader.ReadInt32();
			}

		case INT64:
			if (isArray) {
				return msReader.ReadInt64Array();
			} else {
				return msReader.ReadInt64();
			}
		case DOUBLE:
			if (isArray) {
				return msReader.ReadDoubleArray();
			} else {
				return msReader.ReadRedirectDouble();
			}
		case FLOAT:
			if (isArray) {
				return msReader.ReadFloatArray();
			} else {
				return msReader.ReadRedirectFloat();
			}

		case DECIMAL:
			if (isArray) {
				return msReader.ReadDeciamlArray();
			} else {
				return msReader.ReadDecimal();
			}

		case DATETIME:
			if (isArray) {
				return msReader.ReadDateTimeArray();
			} else {
				return msReader.ReadDateTime();
			}
		case BOOL:
			if (isArray) {
				return msReader.ReadBoolArray();
			} else {
				return msReader.ReadBool();
			}
		case ENUM:
			if (isArray) {
				String[] strarray = msReader.ReadStringArray();

				Object[] arr = new Object[strarray.length];
				for (int i = 0; i < strarray.length; i++) {
					if (!StringUtil.isNullOrEmpty(strarray[i])) {
						arr[i] = Enum.valueOf(buftype.getClassType(), strarray[i]);
					}
				}
				return arr;
			} else {
				String str = msReader.ReadString();
				if (!StringUtil.isNullOrEmpty(str)) {
					return Enum.valueOf(buftype.getClassType(), msReader.ReadString());
				} else {
					return null;
				}
			}
		case DICTIONARY:
			if (isArray) {
				int arrlen = msReader.ReadInt32();
				if (arrlen == -1)
					return null;

				Map[] dicarr = new HashMap[arrlen];
				for (int i = 0; i < arrlen; i++) {
					dicarr[i] = (Map) DeSerialize(buftype.getClassType(), msReader);
				}

				return dicarr;
			} else {
				int dicLen = msReader.ReadInt32();
				if (dicLen == -1) {
					return null;
				}

				Map idic = (Map) buftype.getClassType().newInstance();
				Tuple<Class, Class> keyvaluetype = GetDirctionaryKeyValueType(buftype);

				for (int i = 0; i < dicLen; i++) {
					idic.put(DeSerialize(keyvaluetype.GetItem1(), msReader),
							DeSerialize(keyvaluetype.GetItem2(), msReader));
				}

				return idic;
			}
		case LIST:
			if (isArray) {
				int listarrlen = msReader.ReadInt32();
				if (listarrlen == -1)
					return null;
				List[] listArray = new LinkedList[listarrlen];
				for (int i = 0; i < listarrlen; i++) {
					listArray[i] = (List) DeSerialize(buftype.getClassType(), msReader);
				}
				return listArray;
			} else {
				int listlen = msReader.ReadInt32();
				if (listlen == -1)
					return null;
				List list = new LinkedList();
				for (int i = 0; i < listlen; i++) {
					list.add(DeSerialize(buftype.getValueType(), msReader));
				}
				return list;
			}
		case ARRAY:
			if (isArray) {
				int listarrlen = msReader.ReadInt32();
				if (listarrlen == -1)
					return null;
				Array listArray = (Array) Array.newInstance(buftype.getValueType(), listarrlen);
				for (int i = 0; i < listarrlen; i++) {
					Array.set(listArray, i, DeSerialize(buftype.getClassType(), msReader));
				}
				return listArray;
			} else {
				int arrlen = msReader.ReadInt32();
				if (arrlen == -1)
					return null;
				Array arr = (Array) Array.newInstance(buftype.getValueType(), arrlen);
				for (int i = 0; i < arrlen; i++) {
					Array.set(arr, i, DeSerialize(buftype.getValueType(), msReader));
				}
				return arr;
			}
		default:
			throw new Exception("反序列化错误:" + buftype.getEntityType().toString());
		}
	}

	private static Object DeSerialize(Class DestType, MemoryStreamReader msReader) throws Exception {

		int firstByte = (int) msReader.ReadByte();
		if ((firstByte & EntityBufTypeFlag.VlaueNull.getVal()) == EntityBufTypeFlag.VlaueNull.getVal()) {
			return null;
		}

		// EntityBufType destTypeBufType = MapBufType(DestType, out isArray);
		Tuple<EntityBufType, Boolean> touple = GetTypeBufType(DestType);
		if (touple.GetItem1().getEntityType() != EntityType.COMPLEX) {
			return DeserializeSimple(touple.GetItem1(), touple.GetItem2(), msReader);
		}

		boolean isArray;
		Object ret = DestType.newInstance();
		// PropertyInfo[] props = DestType.GetProperties();
		List<Tuple<EntityBufType, Boolean>> buftypelist = GetTypeEntityBufType(DestType);
		for (Tuple<EntityBufType, Boolean> buftype : buftypelist) {
			// EntityBufType buftype = MapBufType(prop.PropertyType, out isArray);
			isArray = buftype.GetItem2();
			if (buftype.GetItem1().getEntityType() == EntityType.COMPLEX) {
				if (isArray) {
					int len = msReader.ReadInt32();
					if (len == -1) {
						// ret.SetValue(buftype.Item1.Property, null);
						// ret.SetValueDrect(buftype.Item1.Property, null);
						continue;
					} else {
						Object[] objs = (Object[]) Array.newInstance(buftype.GetItem1().getClassType(), len);

						for (int i = 0; i < len; i++) {
							// 读下标志
							int flag = msReader.ReadByte();
							if ((flag & EntityBufTypeFlag.VlaueNull.getVal()) == EntityBufTypeFlag.VlaueNull.getVal()) {
								objs[i] = null;
							} else {

								objs[i] = DeSerialize(buftype.GetItem1().getClassType(), msReader);
							}
						}
						if (objs != null && !objs.equals(buftype.GetItem1().getDefaultValue())) {
							// ret.SetValue(buftype.Item1.Property, objs);
							buftype.GetItem1().getProperty().GetSetValueMethod().invoke(ret, objs);
						}
					}
				} else { // 读下标志
					int flag = msReader.ReadByte();
					if ((flag & EntityBufTypeFlag.VlaueNull.getVal()) == EntityBufTypeFlag.VlaueNull.getVal()) {
						// ret.SetValue(buftype.Item1.Property, null);
						buftype.GetItem1().getProperty().GetSetValueMethod().invoke(ret, null);
						continue;
					} else {
						Object val = DeSerialize(buftype.GetItem1().getClassType(), msReader);
						if (val != null && !val.equals(buftype.GetItem1().getDefaultValue())) {
							buftype.GetItem1().getProperty().GetSetValueMethod().invoke(ret, val);
						}
					}
				}
			} else {
				Object val = DeserializeSimple(buftype.GetItem1(), isArray, msReader);
				if (val != null && !val.equals(buftype.GetItem1().getDefaultValue())) {
					// ret.SetValue(buftype.Item1.Property, val);
					buftype.GetItem1().getProperty().GetSetValueMethod().invoke(ret, val);
				}
			}
		}

		return ret;
	}

	public static <T> T DeSerialize(Class<T> c, byte[] bytes, boolean compress) throws Exception {
		java.io.ByteArrayInputStream bs = null;
		try {

			bs = new java.io.ByteArrayInputStream(bytes);
			Ljc.JFramework.MemoryStreamReader reader = new Ljc.JFramework.MemoryStreamReader(bs);
			Object obj = DeSerialize(c, reader);
			return (T) obj;
		} finally {
			if (bs != null) {
				bs.close();
			}
		}
	}
}
