package Ljc.JFramework.Net;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WebHeaderCollection {
	private HashMap<String, String> _hashmap = new LinkedHashMap<String, String>();

	public String get(String name) {
		// TODO Auto-generated method stub
		return _hashmap.getOrDefault(name, null);
	}

}
