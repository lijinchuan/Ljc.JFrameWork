package Ljc.JFramework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Ljc.JFramework.TypeUtil.DateTime;
import Ljc.JFramework.TypeUtil.UInt16;
import Ljc.JFramework.Utility.ReflectUtil;
import Ljc.JFramework.Utility.StringUtil;

public class Person {
	private String _name = null;
	private int _age = 0;
	private short _short = 0;
	private UInt16 _uint16;
	private long _long = 0;
	private byte _byte = 0;
	private double _double = 0;
	private float _float = 0;
	private DateTime _dateTime;
	private BigDecimal _bigDecimal;
	private Boolean _bool = false;
	private char _char;
	private int[] _intArray;
	private ShortTypeEnum _enum;
	private HashMap<Integer, Integer> _dic;
	private Date _date;
	private Date[] _dateArray;

	private List<Integer> _list = null;
	private LinkedList<Integer> _intlist = null;

	public String getName() {
		return this._name;
	}

	public void setName(String val) {
		this._name = val;
	}

	public int getAge() {
		return this._age;
	}

	public void setAge(int val) {
		this._age = val;
	}

	public short getShort() {
		return this._short;
	}

	public void setShort(short val) {
		this._short = val;
	}

	public UInt16 getUint16() {
		return this._uint16;
	}

	public void setUint16(UInt16 val) {
		this._uint16 = val;
	}

	public long getLong() {
		return this._long;
	}

	public void setDouble(double val) {
		this._double = val;
	}

	public double getDouble() {
		return this._double;
	}

	public void setLong(long val) {
		this._long = val;
	}

	public byte getByte() {
		return this._byte;
	}

	public void setByte(byte val) {
		this._byte = val;
	}

	public float getFloat() {
		return this._float;
	}

	public void setFloat(float val) {
		this._float = val;
	}

	public DateTime getDateTime() {
		return this._dateTime;
	}

	public void setDateTime(DateTime val) {
		this._dateTime = val;
	}

	public BigDecimal getBigDecimal() {
		return this._bigDecimal;
	}

	public void setBigDecimal(BigDecimal val) {
		this._bigDecimal = val;
	}

	public Boolean getBool() {
		return this._bool;
	}

	public void setIntArray(int[] val) {
		this._intArray = val;
	}

	public int[] getIntArray() {
		return this._intArray;
	}

	public void setBool(Boolean val) {
		this._bool = val;
	}

	public char getChar() {
		return this._char;
	}

	public ShortTypeEnum getEnum() {
		return this._enum;
	}

	public void setEnum(ShortTypeEnum val) {
		this._enum = val;
	}

	public void setChar(char val) {
		this._char = val;
	}

	public HashMap<Integer, Integer> getDic() {
		return this._dic;
	}

	public void setDic(HashMap<Integer, Integer> val) {
		this._dic = val;
	}

	public void setDate(Date val) {
		this._date = val;
	}

	public Date getDate() {
		return this._date;
	}

	public void setDateArray(Date[] val) {
		this._dateArray = val;
	}

	public Date[] getDateArray() {
		return this._dateArray;
	}

	public List<Integer> getList() {
		return this._list;
	}

	public void setList(List<Integer> val) {
		this._list = val;
	}

	public Person serializeMe() {
		Person newone = null;
		try {
			Class cls = this.getClass();
			newone = (Person) cls.newInstance();

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
				java.lang.Object oldval = getMethod.invoke(this, null);

				setMethod.invoke(newone, oldval);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} finally {
			return newone;
		}
	}

	public static void TestEnumArray() {
		ArrayTypeFlag[] flags = new ArrayTypeFlag[] { ArrayTypeFlag.ShortLen, ArrayTypeFlag.ByteLen };
		for (Object f : (Object[]) ((Object) flags)) {
			System.out.println("aa " + f.toString());
		}
	}

	public static void applyHashMap(HashMap<String, Integer> map) {
		// 该方法可以为空
	}
}
