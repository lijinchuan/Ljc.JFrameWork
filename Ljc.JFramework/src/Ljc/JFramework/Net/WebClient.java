package Ljc.JFramework.Net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import Ljc.JFramework.Utility.StringUtil;

public class WebClient {
	// header相关
	private String _accept = "text/html, application/xhtml+xml, */*";

	public String getAccept() {
		return _accept;
	}

	public void setAccept(String value) {
		this._accept = value;
	}

	private String _userAgent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)";

	public String getUserAgent() {

		return _userAgent;

	}

	public void setUserAgent(String value) {

		this._userAgent = value;

	}

	private String _acceptLanguage = "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3";

	public String getAcceptLanguage() {
		return _acceptLanguage;
	}

	public void setAcceptLanguage(String value) {
		this._acceptLanguage = value;
	}

	private String _refer = "";

	public String getReferer() {
		return _refer;
	}

	public void setReferer(String value) {
		_refer = value;
	}

	// 编码
	private String _encoding = "utf-8";

	/// <summary>
	/// 页面编码
	/// </summary>
	public String getWebEncoding() {
		return _encoding;
	}

	public void setWebEncoding(String value) {
		_encoding = value;
	}

	private String _defaultEncoding = "utf-8";

	/// <summary>
	/// 默认编码，当获取编码失败时，会默认使用此编码
	/// </summary>
	public String getDefaultEncoding() {
		return _defaultEncoding;
	}

	public void setDefaultEncoding(String value) {
		_defaultEncoding = value;
	}

	private boolean _supportCompression = true;

	/// <summary>
	/// 是否支持压缩
	/// </summary>
	public boolean getSupportCompression() {
		return _supportCompression;
	}

	public void setSupportCompression(boolean value) {
		_supportCompression = value;
	}

	// cookie相关

	/// <summary>
	/// 服务器给本地的cookie
	/// </summary>
	private CookieContainer serverCookie;

	public void AppendCookie(String cookieName, String cookieValue, String domain, String path) {
		if (serverCookie == null) {
			serverCookie = new CookieContainer();
			serverCookie.setDomainCapacity(255);
		}
		serverCookie.add(new Cookie(cookieName, cookieValue, domain, path));
	}

	public List<Cookie> getCookies() {
		return serverCookie.getCookies();
	}

	/// <summary>
	/// 查询服务器返回的cookie值
	/// </summary>
	/// <param name="cookieName"></param>
	/// <returns></returns>
	public String GetCookieValue(String cookieName) {
		for (Cookie c : this.getCookies()) {
			if (c.getName() == cookieName) {
				return c.getValue();
			}
		}

		return "";
	}

	private int _timeOut = 120 * 1000;

	/// <summary>
	/// 超时时间，以秒单位，默认120秒，如果为0，则不限制
	/// </summary>
	public int getTimeOut() {
		return _timeOut;
	}

	public void setTimeOut(int value) {
		if (value <= 0)
			_timeOut = 0;

		_timeOut = value * 1000;
	}

	// 方法
	public HttpResponseEx DoRequest(String url, byte[] buff, WebRequestMethodEnum method, boolean saveCookie,
			boolean getContent, String contentType) throws IOException {
		HttpResponseEx ret = new HttpResponseEx();
		if (StringUtil.isNullOrEmpty(contentType)) {
			contentType = "application/x-www-form-urlencoded;charset=UTF-8;";
		}

		URL requestUrl = new URL(url);
		URLConnection conn = requestUrl.openConnection();

		if (buff != null && buff.length > 0 && method == WebRequestMethodEnum.GET)
			conn.setRequestProperty("Method", WebRequestMethodEnum.POST.toString());
		else
			conn.setRequestProperty("Method", method.toString());

		conn.setRequestProperty("Accept", this._accept);

		// webRequest.AllowAutoRedirect = true;
		// Accept-Language:zh-CN,zh;q=0.8
		conn.setRequestProperty("Accept-Language", this._acceptLanguage);
		// webRequest.KeepAlive = true;
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setUseCaches(false);
		// webRequest.CachePolicy = new
		// System.Net.Cache.RequestCachePolicy(System.Net.Cache.RequestCacheLevel.NoCacheNoStore);
		// webRequest.UserAgent = UserAgent;
		conn.setRequestProperty("User-Agent", this._userAgent);
		if (_timeOut > 0) {
			// webRequest.Timeout = _timeOut;
			conn.setConnectTimeout(_timeOut);
		}
		if (_supportCompression) {
			// webRequest.Headers.Add(HttpRequestHeader.AcceptEncoding, "gzip, deflate");
			// webRequest.Headers.Add(HttpRequestHeader.AcceptEncoding, "gzip");
			conn.setRequestProperty("Accept-Encoding", "gzip");
		}
		if (!StringUtil.isNullOrEmpty(this._refer)) {
			conn.setRequestProperty("Referer", this._refer);
		}

		if (serverCookie != null)
			conn.setRequestProperty("Cookie", this.serverCookie.toString());
		else
			conn.setRequestProperty("Cookie", "");

		if (buff != null && buff.length > 0) {
			// byte[] buff = this.WebEncoding.GetBytes(data);
			// webRequest.ContentType = "application/x-www-form-urlencoded;charset=UTF-8;";

			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Content-Length", String.valueOf(buff.length));

			OutputStream output = conn.getOutputStream();
			output.write(buff, 0, buff.length);
		}

		Map<String, List<String>> headers = conn.getHeaderFields();

		return null;

	}

}
