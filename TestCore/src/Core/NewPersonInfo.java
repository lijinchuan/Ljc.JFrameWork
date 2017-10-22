package Core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NewPersonInfo {
	private String _name;
	private int _age;
	private Date _birth;
	private String[] _friends;
	private HashMap<String, NewPersonInfo> _friendsInfo;
	private List<String> _schools;

	public String getName() {
		return this._name;
	}

	public void setName(String value) {
		this._name = value;
	}

	public int getAge() {
		return this._age;
	}

	public void setAge(int value) {
		this._age = value;
	}

	public Date getBirth() {
		return this._birth;
	}

	public void setBirth(Date value) {
		this._birth = value;
	}

	public String[] getFriends() {
		return this._friends;
	}

	public void setFriends(String[] value) {
		this._friends = value;
	}

	public HashMap<String, NewPersonInfo> getFriendsInfo() {
		return this._friendsInfo;
	}

	public void setFriendsInfo(HashMap<String, NewPersonInfo> value) {
		this._friendsInfo = value;
	}

	public List<String> getSchools() {
		return this._schools;
	}

	public void setSchools(List<String> value) {
		this._schools = value;
	}

	public NewPersonInfo Fill() {
		return this;
	}
}
