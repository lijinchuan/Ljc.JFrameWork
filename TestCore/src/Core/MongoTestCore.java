package Core;

import java.util.List;

import org.bson.types.ObjectId;

import Entity.BlogType;
import Ljc.JFramework.Box;

public class MongoTestCore {
	public List<BlogType> GetBlogTypes() throws Exception {
		Box<Long> total = new Box<Long>();
		List<BlogType> list = LJC.JFrameWork.Data.Mongo.MongoDBHelper.Find(BlogType.class, "blogdb", BlogType.DBName,
				BlogType.CollectionName, null, 1, 100, null, null, total);
		ObjectId id = list.get(0).get_Id();
		return list;
	}

}
