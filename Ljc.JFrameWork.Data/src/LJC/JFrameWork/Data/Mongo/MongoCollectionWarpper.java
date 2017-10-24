package LJC.JFrameWork.Data.Mongo;

import com.mongodb.client.MongoCollection;

class MongoCollectionWarpper {
	MongoCollection<?> MongoDBCollection = null;

	public MongoCollectionWarpper(MongoCollection<?> collection) {
		this.MongoDBCollection = collection;
	}

	private boolean _isCreateIndex = false;

	public boolean getIsCreateIndex() {
		return this._isCreateIndex;
	}

	public void setIsCreateIndex(boolean value) {
		this._isCreateIndex = value;
	}
}
