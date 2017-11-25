package Ljc.JFramework.Net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

import Ljc.JFramework.Utility.StringUtil;

public class WebClient {
	// header���
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

	private final static Charset DefaultEncoding = Charset.forName("utf-8");
	// ����
	private Charset _encoding;

	/// <summary>
	/// ҳ�����
	/// </summary>
	public Charset getWebEncoding() {
		return _encoding;
	}

	public void setWebEncoding(Charset value) {
		_encoding = value;
	}

	private String _defaultEncoding = "utf-8";

	/// <summary>
	/// Ĭ�ϱ��룬����ȡ����ʧ��ʱ����Ĭ��ʹ�ô˱���
	/// </summary>
	public String getDefaultEncoding() {
		return _defaultEncoding;
	}

	public void setDefaultEncoding(String value) {
		_defaultEncoding = value;
	}

	private boolean _supportCompression = false;

	/// <summary>
	/// �Ƿ�֧��ѹ��
	/// </summary>
	public boolean getSupportCompression() {
		return _supportCompression;
	}

	public void setSupportCompression(boolean value) {
		_supportCompression = value;
	}

	// cookie���

	/// <summary>
	/// �����������ص�cookie
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
	/// ��ѯ���������ص�cookieֵ
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
	/// ��ʱʱ�䣬���뵥λ��Ĭ��120�룬���Ϊ0��������
	/// </summary>
	public int getTimeOut() {
		return _timeOut;
	}

	public void setTimeOut(int value) {
		if (value <= 0)
			_timeOut = 0;

		_timeOut = value * 1000;
	}

	/// <summary>
	/// post������������
	/// </summary>
	/// <param name="url"></param>
	/// <param name="data"></param>
	/// <param name="saveCookie">�Ƿ񱣴�cookie</param>
	/// <param name="getContent">�Ƿ��ȡ����</param>
	/// <returns></returns>
	public HttpResponseEx DoRequest(String url, String data, WebRequestMethodEnum method, boolean saveCookie,
			boolean getContent, String contentType) throws Exception {
		if (contentType == null) {
			contentType = "application/x-www-form-urlencoded;charset=UTF-8;";
		}

		byte[] buff = null;

		if (!StringUtil.isNullOrEmpty(data)) {
			buff = data.getBytes(this.getWebEncoding());
		}

		HttpResponseEx response = DoRequest(url, buff, method, saveCookie, getContent, contentType);

		if (!response.getSuccessed()) {
			return response;
		}

		Charset charset;
		try {
			charset = Charset.forName(response.getCharacterSet());
		} catch (Exception ex) {
			charset = DefaultEncoding;
		}

		if (response.getResponseBytes() != null && response.getResponseBytes().length > 0) {
			response.setResponseContent(new String(response.getResponseBytes(), charset));
		}
		return response;
	}

	// ����
	public HttpResponseEx DoRequest(String url, byte[] buff, WebRequestMethodEnum method, boolean saveCookie,
			boolean getContent, String contentType) throws Exception {
		HttpResponseEx ret = new HttpResponseEx();
		if (StringUtil.isNullOrEmpty(contentType)) {
			contentType = "application/x-www-form-urlencoded;charset=UTF-8;";
		}

		URL requestUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();

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
			conn.setDoOutput(true);
			OutputStream output = conn.getOutputStream();

			output.write(buff, 0, buff.length);
		}

		int responsecode = conn.getResponseCode();
		if (responsecode != 200 && responsecode != 302) {
			throw new IOException("�������:" + responsecode);
		}

		for (Entry<String, List<String>> kv : conn.getHeaderFields().entrySet()) {
			System.out.println(kv.getKey() + ":");
			for (String vv : kv.getValue()) {
				System.out.println(vv);
			}
		}

		ret.PraseHeader(conn);

		if (getContent) {
			InputStream input = conn.getInputStream();

			byte[] contentbuffer = Ljc.JFramework.Utility.StreamUtil.ReadStream(input, conn.getContentLength());
			ret.setResponseBytes(contentbuffer);

		}

		System.out.println("..." + conn.getContentEncoding());
		// System.out.println(String.valueOf(contentbuffer.length));
		// String str = new String(ret.getResponseBytes(), ret.getCharacterSet());
		// System.out.println(str);

		conn.disconnect();

		ret.setSuccessed(true);
		return ret;

	}

	/// <summary>
	/// �������������������Ҫ���ڶ�ȡ����ͼƬ��
	/// </summary>
	/// <param name="url"></param>
	/// <returns></returns>
	public InputStream GetStream(String url, boolean saveCookie) throws IOException {
		URL requestUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();

		conn.setRequestProperty("Method", WebRequestMethodEnum.GET.toString());

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

		if (!StringUtil.isNullOrEmpty(this._refer)) {
			conn.setRequestProperty("Referer", this._refer);
		}

		if (serverCookie != null)
			conn.setRequestProperty("Cookie", this.serverCookie.toString());
		else
			conn.setRequestProperty("Cookie", "");

		int responsecode = conn.getResponseCode();
		if (responsecode != 200 && responsecode != 302) {
			conn.disconnect();
			throw new IOException("�������:" + responsecode);
		}

		return conn.getInputStream();

	}

}
