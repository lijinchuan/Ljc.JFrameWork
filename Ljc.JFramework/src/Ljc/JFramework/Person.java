package Ljc.JFramework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import Ljc.JFramework.Utility.ReflectUtil;
import Ljc.JFramework.Utility.StringUtil;

public class Person {
	private String _name = null;
	private int _age = 0;

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

	public List<Integer> getList() {
		return this._list;
	}

	public void SetList(List<Integer> val) {
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

				Method getMethod = ReflectUtil.GetDeclaredMethod(cls, "get" + fieldname, null);

				Method setMethod = ReflectUtil.GetDeclaredMethod(cls, "set" + fieldname, ReflectUtil.GetFieldType(f));

				if (getMethod == null || setMethod == null) {
					continue;
				}
				java.lang.Object oldval = getMethod.invoke(this, null);

				setMethod.invoke(newone, oldval);
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			return newone;
		}
	}
}
