package Test;

import java.io.IOException;

import Ljc.JFramework.Net.WebClient;
import Ljc.JFramework.Net.WebRequestMethodEnum;

public class TestWebClient {
	public static void main(String[] args) {
		WebClient wc = new WebClient();
		try {
			wc.DoRequest("https://www.cnblogs.com/zhuawang/archive/2012/12/08/2809380.html", null,
					WebRequestMethodEnum.GET, true, true, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
