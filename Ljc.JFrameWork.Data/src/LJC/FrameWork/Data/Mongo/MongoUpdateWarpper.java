package LJC.FrameWork.Data.Mongo;

import org.bson.BsonArray;
import org.bson.BsonValue;

import com.mongodb.BasicDBObject;

public class MongoUpdateWarpper {
	BasicDBObject MongoUpdateBuilder = new BasicDBObject();

	private boolean _isempty = true;

	public boolean getIsEmpty() {

		return _isempty;
	}

	public MongoUpdateWarpper() {
	}

	public MongoUpdateWarpper Set(String name, Object val) {
		MongoUpdateBuilder = MongoUpdateBuilder.append("$set", new BasicDBObject().put(name, val));
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper AddToSet(String name, Object val) {
		MongoUpdateBuilder = MongoUpdateBuilder.append("$addToSet ", new BasicDBObject().put(name, val));
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper AddToSetEach(string name, object[] val) {
		var bsonval = BsonArray.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.AddToSetEach(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper BitwiseAnd(string name, long val) {
		MongoUpdateBuilder = MongoUpdateBuilder.BitwiseAnd(name, val);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper BitwiseOr(string name, long val) {
		MongoUpdateBuilder = MongoUpdateBuilder.BitwiseOr(name, val);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper BitwiseXor(string name, long val) {
		MongoUpdateBuilder = MongoUpdateBuilder.BitwiseXor(name, val);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Max(string name, object val) {
		var bsonval = BsonValue.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.Max(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Min(string name, object val) {
		var bsonval = BsonValue.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.Min(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Mul(string name, double val) {
		MongoUpdateBuilder = MongoUpdateBuilder.Mul(name, val);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Mul(string name, long val) {
		MongoUpdateBuilder = MongoUpdateBuilder.Mul(name, val);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper SetOnInsert(string name, object val) {
		var bsonval = BsonValue.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.SetOnInsert(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}

		return this;
	}

	public MongoUpdateWarpper PopFirst(string name) {
		MongoUpdateBuilder = MongoUpdateBuilder.PopFirst(name);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PopLast(string name) {
		MongoUpdateBuilder = MongoUpdateBuilder.PopLast(name);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper UnSet(String name) {
		MongoUpdateBuilder = MongoUpdateBuilder.append("$unset", new BasicDBObject().put(name, Integer.valueOf(1)));
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Incr(String name, long val) {
		MongoUpdateBuilder = MongoUpdateBuilder.append("$inc", new BasicDBObject().put(name, val));
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Incr(String name, double val) {
		MongoUpdateBuilder = MongoUpdateBuilder.append("$inc", new BasicDBObject().put(name, val));
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Pull(string name, object val) {
		var bsonval = BsonValue.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.Pull(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PullAll(string name, object[] val) {
		var bsonval = BsonArray.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.PullAll(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Push(string name, object val) {
		var bsonval = BsonValue.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.Push(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PushAll(string name, object[] val) {
		var bsonval = BsonArray.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.PushAll(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PushEach(string name, object[] val) {
		var bsonval = BsonArray.Create(val);
		MongoUpdateBuilder = MongoUpdateBuilder.PushEach(name, bsonval);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Rename(string oldname, string newname) {
		MongoUpdateBuilder = MongoUpdateBuilder.Rename(oldname, newname);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper CurrentDate(string name) {
		MongoUpdateBuilder = MongoUpdateBuilder.CurrentDate(name);
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Combine(MongoUpdateWarpper other) {
		MongoUpdateBuilder = MongoUpdateBuilder.Combine(other.MongoUpdateBuilder);
		if (_isempty) {
			_isempty = other.IsEmpty;
		}
		return this;
	}
}
