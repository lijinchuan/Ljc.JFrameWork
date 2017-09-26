package Ljc.JFramework;

import java.lang.reflect.Field;

import Ljc.JFramework.Utility.StringUtil;

public class Person {
	private String _name = null;
	private int _age = 0;

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

	public Person serializeMe() {
		Person newone = null;
		try {
			newone = this.getClass().newInstance();
			for (Field f : this.getClass().getDeclaredFields()) {
				String fieldname = StringUtil.captureName(f.getName().substring(1));
				java.lang.Object oldval = this.getClass().getMethod("get" + fieldname).invoke(this, null);

				System.out.println(oldval);
				this.getClass().getDeclaredMethod("set" + fieldname, f.getType()).invoke(newone, oldval);
				// this.getClass().getMethod("set" + fieldname).invoke(newone, oldval);
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			return newone;
		}
	}
}
