package Ljc.JFramework;

import java.util.Properties;

class PropertyInfoEx {
	private Properties _propertyInfo;
	private Boolean _isSetSetValueMethed = false;

	public void setPropertyInfo(Properties prop) {
		this._propertyInfo = prop;
	}

	public Properties getPropertyInfo() {
		return _propertyInfo;

	}

	public PropertyInfoEx(Properties prop) {
		_propertyInfo = prop;
	}

	private void setIsSetSetValueMethed(Boolean boo) {
		this._isSetSetValueMethed = boo;
	}

	public Boolean getIsSetSetValueMethed() {
		return this._isSetSetValueMethed;
	}

	/*
	 * private Action<Object, Object> _setvaluemethed; public Action<object, object>
	 * SetValueMethed { get { return _setvaluemethed; } internal set {
	 * IsSetSetValueMethed = true; _setvaluemethed = value; } }
	 * 
	 * private Func<object, object> _getValueMethed; public Func<object, object>
	 * GetValueMethed { get { return _getValueMethed; } internal set {
	 * _getValueMethed = value; IsSetGetValueMethed = true; } }
	 */
}
