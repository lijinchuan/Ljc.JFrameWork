package LJC.JFrameWork.Data.Mongo;

import org.bson.BsonRegularExpression;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;

public class MongoQueryWarpper {
	BasicDBObject MongoQuery = null;

	public MongoQueryWarpper() {

	}

	public MongoQueryWarpper EQ(String name, Object val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(name, val);
			return this;
		}

		MongoQuery.append(name, val);
		return this;
	}

	public MongoQueryWarpper NE(String name, Object val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.NE, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.NE, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper All(String name, Object[] val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.ALL, new BasicDBObject().append(name, val));
			return this;
		}

		MongoQuery.append(QueryOperators.ALL, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper Exists(String name, boolean val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.EXISTS, new BasicDBObject().append(name, val));
			return this;
		}

		MongoQuery.append(QueryOperators.EXISTS, new BasicDBObject().append(name, val));

		return this;
	}

	public MongoQueryWarpper GT(String name, Object val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.GT, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.GT, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper GTE(String name, Object val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.GTE, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.GTE, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper In(String name, Object[] val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.IN, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.IN, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper LT(String name, Object val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.LT, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.LT, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper LTE(String name, Object val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.LTE, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.LTE, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper NotIn(String name, Object[] val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.NIN, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.NIN, new BasicDBObject().append(name, val));
		return this;
	}

	public MongoQueryWarpper Size(String name, int val) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.SIZE, new BasicDBObject().append(name, val));
			return this;
		}
		MongoQuery.append(QueryOperators.SIZE, new BasicDBObject().append(name, val));
		return this;
	}

	// public MongoQueryWarpper SizeGreaterThan(String name, int val) {
	// if (MongoQuery == null) {
	// MongoQuery = new BasicDBObject().append(QueryOperators.s, new
	// BasicDBObject().append(name, val));
	// return this;
	// }
	// MongoQuery = Query.And(MongoQuery, Query.SizeGreaterThan(name, val));
	// return this;
	// }
	//
	// public MongoQueryWarpper SizeGreaterThanOrEqual(string name, int val) {
	// if (MongoQuery == Query.Null) {
	// MongoQuery = Query.SizeGreaterThanOrEqual(name, val);
	// return this;
	// }
	// MongoQuery = Query.And(MongoQuery, Query.SizeGreaterThanOrEqual(name, val));
	// return this;
	// }
	//
	// public MongoQueryWarpper SizeLessThan(string name, int val) {
	// if (MongoQuery == Query.Null) {
	// MongoQuery = Query.SizeLessThan(name, val);
	// return this;
	// }
	// MongoQuery = Query.And(MongoQuery, Query.SizeLessThan(name, val));
	// return this;
	// }
	//
	// public MongoQueryWarpper SizeLessThanOrEqual(string name, int val) {
	// if (MongoQuery == Query.Null) {
	// MongoQuery = Query.SizeLessThanOrEqual(name, val);
	// return this;
	// }
	// MongoQuery = Query.And(MongoQuery, Query.SizeLessThanOrEqual(name, val));
	// return this;
	// }

	public MongoQueryWarpper And(MongoQueryWarpper query) {
		if (MongoQuery == null) {
			MongoQuery = query.MongoQuery;
			return this;
		}

		MongoQuery.append(QueryOperators.AND, query.MongoQuery);
		return this;
	}

	public MongoQueryWarpper Or(MongoQueryWarpper query) {
		if (MongoQuery == null) {
			MongoQuery = query.MongoQuery;
			return this;
		}
		MongoQuery.append(QueryOperators.OR, query.MongoQuery);
		return this;
	}

	public MongoQueryWarpper Matches(String name, String pattern) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append("$match",
					new BasicDBObject().append(name, new BsonRegularExpression(pattern)));
			return this;
		}
		MongoQuery.append("$match", new BasicDBObject().append(name, new BsonRegularExpression(pattern)));
		return this;
	}

	public MongoQueryWarpper ElemMatch(String name, MongoQueryWarpper query) {
		if (MongoQuery == null) {
			MongoQuery = new BasicDBObject().append(QueryOperators.ELEM_MATCH, query.MongoQuery);
			return this;
		}
		MongoQuery.append(QueryOperators.ELEM_MATCH, query.MongoQuery);
		return this;
	}
}
