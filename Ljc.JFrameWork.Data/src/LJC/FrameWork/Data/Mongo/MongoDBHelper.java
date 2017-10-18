package LJC.FrameWork.Data.Mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import Ljc.JFramework.Box;
import Ljc.JFramework.Utility.StringUtil;

//下载最新驱动 http://mongodb.github.io/mongo-java-driver/
public class MongoDBHelper {
	/// <summary>
	/// MongoDB连接实体
	/// </summary>
	private static HashMap<String, MongoClient> mongClientPoolDict = new HashMap<String, MongoClient>();
	private static HashMap<String, MongoCollectionWarpper> CollectionDic = new HashMap<String, MongoCollectionWarpper>();
	private static Object lockObj = new Object();

	public static MongoClient CreateInstance(String path) {
		MongoClient result = null;

		if (!mongClientPoolDict.containsKey(path)) {
			synchronized (mongClientPoolDict) {
				if (!mongClientPoolDict.containsKey(path)) {
					result = new MongoClient(path);
					mongClientPoolDict.put(path, result);
				} else {
					result = mongClientPoolDict.get(path);
				}
			}

		} else {
			result = mongClientPoolDict.get(path);
		}

		return result;
	}

	public static MongoClient CreateInstanceUseConfig(String configname) throws Exception {
		String connstr = System.getProperty(configname);
		if (StringUtil.isNullOrEmpty(connstr)) {
			throw new Exception(String.format("mongo配置不存在或者为空值：%s", configname));
		}

		return CreateInstance(connstr);
	}

	private static <T> MongoCollectionWarpper GetCollecionInternal(Class<T> classt, String connectionName,
			String database, String collection) throws Exception {
		String collectionkey = String.format("%s:%s@%s", database, collection, connectionName);

		MongoCollectionWarpper collectionWarpper = CollectionDic.getOrDefault(collectionkey, null);
		if (collectionWarpper == null) {
			synchronized (CollectionDic) {
				collectionWarpper = CollectionDic.getOrDefault(collectionkey, null);
				if (collectionWarpper == null) {
					MongoCollection<T> mongocollection = CreateInstanceUseConfig(connectionName).getDatabase(database)
							.getCollection(collection, classt);
					if (mongocollection != null) {
						collectionWarpper = new MongoCollectionWarpper(mongocollection);
					}
					CollectionDic.put(collectionkey, collectionWarpper);
				}
			}
		}

		return collectionWarpper;
	}

	private static <T> MongoCollection<T> GetCollecion(Class<T> classt, String connectionName, String database,
			String collection) throws Exception {

		MongoCollectionWarpper collectionWarpper = GetCollecionInternal(classt, connectionName, database, collection);

		return collectionWarpper == null ? null : (MongoCollection<T>) collectionWarpper.MongoDBCollection;
	}

	public static <T> void Drop(Class<T> classt, String connectionName, String database, String collection)
			throws Exception {
		MongoCollection<T> mongocollection = GetCollecion(classt, connectionName, database, collection);
		mongocollection.drop();
	}

	public static <T> boolean Insert(Class<T> classt, String connectionName, String database, String collection,
			T entity) throws Exception {
		if (entity == null) {
			return false;
		}

		MongoCollection<T> mongocollection = GetCollecion(classt, connectionName, database, collection);

		mongocollection.insertOne(entity);

		return true;
	}

	public static <T> boolean InsertBatch(Class<T> classt, String connectionName, String database, String collection,
			List<T> entities) throws Exception {
		MongoCollection<T> mongocollection = GetCollecion(classt, connectionName, database, collection);

		mongocollection.insertMany(entities);

		return true;
	}

	public static <T> List<T> Find(Class<T> classt, String connectionName, String database, String collection,
			MongoQueryWarpper querys, int pageindex, int pagesize, String[] fields, MongoSortWarpper sorts,
			Box<Long> total) throws Exception {
		BasicDBObject mongoquery = querys == null ? null : querys.MongoQuery;
		MongoCollection<T> mongocollection = GetCollecion(classt, connectionName, database, collection);
		Bson mongosortby = sorts.MongoSortBy;
		int skip = (pageindex - 1) * pagesize;

		FindIterable<T> finditer = null;
		if (mongoquery != null) {
			finditer = mongocollection.find(mongoquery, classt);
		} else {
			mongocollection.find(classt);
		}
		if (mongosortby != null) {
			finditer = finditer.skip(skip).limit(pagesize).sort(mongosortby);
		} else {
			finditer = finditer.skip(skip).limit(pagesize);
		}

		if (fields != null && fields.length > 0) {
			// mongocursor = mongocursor.SetFields(fields);
		}

		List<T> list = new ArrayList<T>();
		MongoCursor<T> it = finditer.iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}
		if (list.size() < pagesize && list.size() > 0) {
			total.setData((long) (skip + list.size()));
		} else {
			total.setData(mongocollection.count(mongoquery));
		}
		return list;
	}

	public static <T> List<T> Find(Class<T> classt, String connectionName, String database, String collection,
			MongoQueryWarpper querys, int pageindex, int pagesize, String[] fields, MongoSortWarpper sorts)
			throws Exception {
		BasicDBObject mongoquery = querys == null ? null : querys.MongoQuery;
		MongoCollection<T> mongocollection = GetCollecion(classt, connectionName, database, collection);
		Bson mongosortby = sorts.MongoSortBy;
		int skip = (pageindex - 1) * pagesize;

		FindIterable<T> finditer = null;
		if (mongoquery != null) {
			finditer = mongocollection.find(mongoquery, classt);
		} else {
			mongocollection.find(classt);
		}
		if (mongosortby != null) {
			finditer = finditer.skip(skip).limit(pagesize).sort(mongosortby);
		} else {
			finditer = finditer.skip(skip).limit(pagesize);
		}

		if (fields != null && fields.length > 0) {
			// mongocursor = mongocursor.SetFields(fields);
		}

		List<T> list = new ArrayList<T>();
		MongoCursor<T> it = finditer.iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}

		return list;
	}

	public static <T> T FindOneByIdAs(Class<T> classt, String connectionName, String database, String collection,
			String id) throws Exception {
		ObjectId _id = new ObjectId(id);
		return FindOne(classt, connectionName, database, collection, new MongoQueryWarpper().EQ("_id", _id), null,
				null);
	}

	public static <T> T FindOne(Class<T> classt, String connectionName, String database, String collection,
			MongoQueryWarpper querys, String[] fields, MongoSortWarpper sorts) throws Exception {
		List<T> list = Find(classt, connectionName, database, collection, querys, 1, 1, fields, sorts);

		if (list.size() == 0) {
			return null;
		}

		return list.get(0);
	}

	public static <T> boolean Update(Class<T> classt,
	String connectionName, String database,
	String collection, MongoQueryWarpper querys,
	MongoUpdateWarpper updates, MongoUpdateFlagsWarpper flgs=null)
	{
        if (updates == null || updates.IsEmpty)
        {
            return false;
        }

        IMongoQuery mongoquery = querys == null ? Query.Null : querys.MongoQuery;

        MD.Builders.UpdateBuilder updateBuilder = updates.MongoUpdateBuilder;
        if (updateBuilder != null)
        {
            var mongocollection = GetCollecion<T>(connectionName, database, collection);

            if (flgs == null)
            {
                mongocollection.Update(mongoquery, updateBuilder);
            }
            else
            {
                mongocollection.Update(mongoquery, updateBuilder, flgs.MongoUpdateFlags);
            }
            return true;
        }

        return false;
    }

}
