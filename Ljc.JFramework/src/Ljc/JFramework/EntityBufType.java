package Ljc.JFramework;

class EntityBufType {
	private EntityType _entityType = EntityType.UNKNOWN;
	private Class _valueType = null;
	private Class _classType = null;
	private Object _defaultValue = null;

	public EntityType getEntityType() {
		return _entityType;
	}

	public void setEntityType(EntityType val) {
		this._entityType = val;
	}

	public void setValueType(Class cls) {
		this._valueType = cls;
	}

	/// <summary>
	/// 类名，可以是数组，类或者列表
	/// </summary>
	public Class getValueType() {
		return _valueType;
	}

	/// <summary>
	/// 类名，只是类
	/// </summary>
	public void setClassType(Class cls) {
		this._classType = cls;
	}

	public Class getClassType() {
		return this._classType;
	}

	public void setDefaultValue(Object val) {
		this._defaultValue = val;
	}

	public Object getDefaultValue() {
		return this._defaultValue;
	}

	private PropertyInfoEx _property = null;

	/// <summary>
	/// 属性
	/// </summary>
	public PropertyInfoEx getProperty() {
		return this._property;
	}

	public void setProperty(PropertyInfoEx prop) {
		this._property = prop;
	}
}
