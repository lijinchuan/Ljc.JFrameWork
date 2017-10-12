package Ljc.JFramework.Utility;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

	private static ThreadPoolExecutor _threadPool;
	private static BlockingQueue<Runnable> _poolqueues;
	private static final int minPoolSize = 10;
	private static final int maxPoolSize = 1000;
	private static final int keepAliveTime = 30;

	static {
		_poolqueues = new LinkedBlockingQueue<Runnable>();
		_threadPool = new ThreadPoolExecutor(minPoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, _poolqueues);
	}

	public static Boolean QueueUserWorkItem(Action run, Object param) throws InterruptedException {
		if (run == null) {
			return false;
		}
		run.setParams(param);
		// _poolqueues.put(run);
		_threadPool.execute(run);
		return true;
	}

	public static long GetTaskCount() {
		return _threadPool.getTaskCount();
	}

	public static long GetCompletedTaskCount() {
		return _threadPool.getCompletedTaskCount();
	}

	public static long GetLargestPoolSize() {
		return _threadPool.getLargestPoolSize();
	}

	public static long GetActiveCount() {
		return _threadPool.getActiveCount();
	}
}
