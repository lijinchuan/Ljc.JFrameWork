package Ljc.JFramework.Utility;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

public class LocalCacheManager {
	private static HashMap<String, Object> _locks = new HashMap<String, Object>();
	private static HashMap<String, Object> _items = new HashMap<String, Object>();

	/**
	 * 
	 * @param key
	 * @param reflashFunc
	 * @param cachedMins
	 * @return
	 */
	public static <T> T Find(String key, Function<Object, T> reflashFunc,Object params, int cachedSecs) {
		if (StringUtil.isNullOrEmpty(key) || reflashFunc == null) {
			return null;
		}

		Object cachitem = _items.getOrDefault(key, null);
		CacheItem<T> val = null;
		if (cachitem != null) {
			val = (CacheItem<T>) cachitem;
			if (val.getExpired().after(new Date())) {
				return val.getItem();
			}
		}

		Object locker = _locks.getOrDefault(key, null);
		if (locker == null) {
			synchronized (_locks) {
				if ((locker = _locks.getOrDefault(key, null)) == null) {
					locker = new Object();
					_locks.put(key, locker);
				}
			}
		}

		synchronized (locker) {
			cachitem = _items.getOrDefault(key, null);
			if(cachitem!=null) {
				return ((CacheItem<T>)cachitem).getItem();
			}
			val = new CacheItem<T>();
			val.setItem(reflashFunc.apply(params));

			if (val.getItem() != null) {
				val.setExpired(new Date(System.currentTimeMillis() + cachedSecs * 1000));
				synchronized (_items) {
					_items.put(key, val);
				}
			}
			return val.getItem();
		}
	}
}
