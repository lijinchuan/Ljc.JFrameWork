package LJC.JFrameWork.Data.Mongo;

import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Updates;

public class MongoUpdateWarpper {
	Bson MongoUpdateBuilder = null;

	private boolean _isempty = true;

	public boolean getIsEmpty() {
		return _isempty;
	}

	public MongoUpdateWarpper() {
	}

	public MongoUpdateWarpper Set(String name, Object val) {
		Bson newupdate = Updates.set(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper AddToSet(String name, Object val) {
		Bson newupdate = Updates.addToSet(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper AddToSetEach(String name, List<Object> val) {
		Bson newupdate = Updates.addEachToSet(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper BitwiseAnd(String name, long val) {
		Bson newupdate = Updates.bitwiseAnd(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper BitwiseOr(String name, long val) {
		Bson newupdate = Updates.bitwiseOr(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper BitwiseXor(String name, long val) {
		Bson newupdate = Updates.bitwiseOr(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Max(String name, Object val) {
		Bson newupdate = Updates.max(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Min(String name, Object val) {
		Bson newupdate = Updates.min(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Mul(String name, double val) {
		Bson newupdate = Updates.mul(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Mul(String name, long val) {
		Bson newupdate = Updates.mul(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper SetOnInsert(String name, Object val) {
		Bson newupdate = Updates.setOnInsert(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PopFirst(String name) {
		Bson newupdate = Updates.popFirst(name);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PopLast(String name) {
		Bson newupdate = Updates.popLast(name);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper UnSet(String name) {
		Bson newupdate = Updates.unset(name);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Incr(String name, long val) {
		Bson newupdate = Updates.inc(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Incr(String name, double val) {
		Bson newupdate = Updates.inc(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Pull(String name, Object val) {
		Bson newupdate = Updates.pull(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PullAll(String name, List<Object> val) {
		Bson newupdate = Updates.pullAll(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Push(String name, Object val) {
		Bson newupdate = Updates.push(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper PushEach(String name, List<Object> val) {
		// new PushOptions()
		Bson newupdate = Updates.pushEach(name, val);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Rename(String oldname, String newname) {
		Bson newupdate = Updates.rename(oldname, newname);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper CurrentDate(String name) {
		Bson newupdate = Updates.currentDate(name);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper CurrentTimestamp(String name) {
		Bson newupdate = Updates.currentTimestamp(name);
		if (MongoUpdateBuilder != null) {
			MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, newupdate);
		} else {
			MongoUpdateBuilder = newupdate;
		}
		if (_isempty) {
			_isempty = false;
		}
		return this;
	}

	public MongoUpdateWarpper Combine(MongoUpdateWarpper other) {
		if (other != null) {
			if (MongoUpdateBuilder != null) {
				MongoUpdateBuilder = Updates.combine(MongoUpdateBuilder, other.MongoUpdateBuilder);
			} else {
				MongoUpdateBuilder = other.MongoUpdateBuilder;
			}
		}
		if (_isempty) {
			_isempty = other != null && other.getIsEmpty();
		}
		return this;
	}
}
