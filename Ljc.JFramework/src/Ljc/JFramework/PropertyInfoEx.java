package Ljc.JFramework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class PropertyInfoEx {
	private Field _propertyInfo;
	private Boolean _isSetSetValueMethod = false;

	private Method _setValueMethod = null;
	private Method _getValueMethod = null;

	public void setPropertyInfo(Field prop) {
		this._propertyInfo = prop;
	}

	public Field getPropertyInfo() {
		return _propertyInfo;

	}

	public PropertyInfoEx(Field prop) {
		_propertyInfo = prop;
	}

	private void setIsSetSetValueMethed(Boolean boo) {
		this._isSetSetValueMethod = boo;
	}

	public Boolean getIsSetSetValueMethod() {
		return this._isSetSetValueMethod;
	}

	public void setSetValueMethod(Method m) {
		this._setValueMethod = m;
	}

	public Method GetSetValueMethod() {
		return this._setValueMethod;
	}

	public void setGetValueMethod(Method m) {
		this._getValueMethod = m;
	}

	public Method getGetValueMethod() {
		return this._getValueMethod;
	}
}
