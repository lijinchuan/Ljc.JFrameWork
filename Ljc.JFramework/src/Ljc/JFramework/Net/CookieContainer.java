package Ljc.JFramework.Net;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

public class CookieContainer {

	public int _perDomainCapacity = 25;

	private List<Cookie> _cookies = new LinkedList<Cookie>();

	public int getDomainCapacity() {
		return this._perDomainCapacity;
	}

	public void setDomainCapacity(int value) {
		this._perDomainCapacity = value;
	}

	public void add(Cookie cookie) {
		// TODO Auto-generated method stub
		_cookies.add(cookie);
	}

	public List<Cookie> getCookies() {
		return this._cookies;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for (Cookie c : _cookies) {
			sb.append(String.format("%s=%s;", c.getName(), URLEncoder.encode(c.getValue())));
		}
		return sb.toString();
	}

}
