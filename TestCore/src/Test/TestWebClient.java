package Test;

import Ljc.JFramework.Net.WebClient;

public class TestWebClient {
	public static void main(String[] args) throws Exception {
		WebClient wc = new WebClient();
		wc.DownloadFile("http://106.14.193.150/publish/c7d2bbbc3cf941f8aacd28f6592ee18a.zip",
				"D:\\GitHub\\Ljc.JFrameWork\\TestCore\\111.zip");
	}
}
