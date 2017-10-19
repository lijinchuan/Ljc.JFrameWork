package Core;

public final class GetPersonResponse {
	private int _id;
	private NewPersonInfo _info;

	public int getId() {
		return this._id;
	}

	public void setId(int value) {
		this._id = value;
	}

	public NewPersonInfo getInfo() {
		return this._info;
	}

	public void setInfo(NewPersonInfo value) {
		this._info = value;
	}
}
