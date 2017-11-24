package Ljc.JFramework.Net;

public class Cookie {
	private String _name;
	private String _value;
	private String _domain;
	private String _path;

	public Cookie(String cookiename, String cookievalue, String domain, String path) {
		this._name = cookiename;
		this._value = cookievalue;
		this._domain = domain;
		this._path = path;
	}

	public String getName() {
		return this._name;
	}

	public void setName(String value) {
		this._name = value;
	}

	public String getValue() {
		return this._value;
	}

	public void setValue(String value) {
		this._value = value;
	}

	public String getDomain() {
		return this._domain;
	}

	public void setDomain(String value) {
		this._domain = value;
	}

	public String getPath() {
		return this._path;
	}

	public void setPath(String value) {
		this._path = value;
	}
}
