package LJC.FrameWork.Data.Mongo;

import org.bson.Document;
import org.bson.types.ObjectId;

@SuppressWarnings("serial")
public class MongoDocumentObject extends Document {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectId __id;

	public ObjectId get_Id() {
		return (ObjectId) this.get("_id");
	}

	public void set_Id(ObjectId value) {

		this.put("_id", value);
	}
}
