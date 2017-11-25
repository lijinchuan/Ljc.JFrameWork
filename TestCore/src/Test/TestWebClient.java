package Test;

import java.io.IOException;

import Ljc.JFramework.Net.WebClient;
import Ljc.JFramework.Net.WebRequestMethodEnum;

public class TestWebClient {
	public static void main(String[] args) throws Exception {
		WebClient wc = new WebClient();
		try {
			wc.DoRequest("http://blog.csdn.net/heyu158/article/details/23342533", null, WebRequestMethodEnum.GET, true,
					true, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
