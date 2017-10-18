package LJC.FrameWork.Data.Mongo;

import com.mongodb.client.model.UpdateOptions;

public class MongoUpdateFlagsWarpper {
	UpdateOptions MongoUpdateFlags = new UpdateOptions();

	private boolean _isMul = true;

	public boolean getIsMulti() {
		return this._isMul;
	}

	public MongoUpdateFlagsWarpper SetMulti(boolean multi) {
		// MongoUpdateFlags
		this._isMul = multi;
		return this;
	}

	public MongoUpdateFlagsWarpper SetUpsert(boolean upset) {
		MongoUpdateFlags.upsert(upset);
		return this;
	}
}
