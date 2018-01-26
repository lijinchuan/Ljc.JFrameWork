package Ljc.JFramework.Utility;

import java.util.Date;

public class CacheItem<T> {
	/// <summary>
	/// 值
	/// </summary>
	private T item;

	/// <summary>
	/// 过期时间
	/// </summary>
	private Date expired;

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}
}
