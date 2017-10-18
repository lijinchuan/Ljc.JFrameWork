package LJC.FrameWork.Data.Mongo;

import org.bson.types.ObjectId;

public class MongoDocumentObject {
	private ObjectId __id;

	public ObjectId get_Id() {
		return this.__id;
	}

	public void set_Id(ObjectId value) {

		this.__id = value;
	}
}
