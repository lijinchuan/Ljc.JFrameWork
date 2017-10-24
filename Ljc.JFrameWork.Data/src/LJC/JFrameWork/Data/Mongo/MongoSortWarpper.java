package LJC.JFrameWork.Data.Mongo;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Sorts;

public class MongoSortWarpper {
	Bson MongoSortBy = null;

	public MongoSortWarpper() {

	}

	public MongoSortWarpper Asc(String... keys) {
		if (MongoSortBy == null) {
			MongoSortBy = Sorts.ascending(keys);

		} else {
			MongoSortBy = Sorts.orderBy(MongoSortBy, Sorts.ascending(keys));
		}
		return this;
	}

	public MongoSortWarpper Desc(String... keys) {
		if (MongoSortBy == null) {
			MongoSortBy = Sorts.descending(keys);
		} else {
			MongoSortBy = Sorts.orderBy(MongoSortBy, Sorts.descending(keys));
		}
		return this;
	}
}
