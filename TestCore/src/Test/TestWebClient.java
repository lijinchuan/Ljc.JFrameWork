package Test;

import java.io.IOException;

import Ljc.JFramework.Net.WebClient;
import Ljc.JFramework.Net.WebRequestMethodEnum;

public class TestWebClient {
	public static void main(String[] args) throws Exception {
		WebClient wc = new WebClient();
		try {
			wc.DoRequest("http://sns-qas.lvb.eastmoney.com/lvb//api/user/SearchUser",
					"reqtype=Server&key_word=2468102&page=1&count=20".getBytes(), WebRequestMethodEnum.POST, true, true,
					null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
