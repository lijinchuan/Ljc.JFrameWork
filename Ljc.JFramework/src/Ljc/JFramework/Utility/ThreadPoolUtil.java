package Ljc.JFramework.Utility;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

	private static ThreadPoolExecutor _threadPool;
	private static BlockingQueue<Runnable> _poolqueues;
	private static final int minPoolSize = 4;
	private static final int maxPoolSize = 100;
	private static final int keepAliveTime = 1;

	static {
		_poolqueues = new ArrayBlockingQueue<Runnable>(maxPoolSize);
		_threadPool = new ThreadPoolExecutor(minPoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, _poolqueues,
				Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
		_threadPool.allowCoreThreadTimeOut(true);
	}

	public static void SetMaxPoolSize(int value) {
		if (value > 0) {
			_threadPool.setMaximumPoolSize(value);
		}
	}

	public static void SetCorePoolSize(int value) {
		if (value > 0) {
			_threadPool.setCorePoolSize(value);
		}
	}

	public static Boolean QueueUserWorkItem(Action run, Object param) throws InterruptedException {
		if (run == null) {
			return false;
		}
		run.setParams(param);
		Print();
		// _poolqueues.put(run);
		_threadPool.execute(run);
		return true;
	}

	public static Boolean QueueUserWorkItem(Runnable run) throws InterruptedException {
		if (run == null) {
			return false;
		}
		// _poolqueues.put(run);
		Print();
		_threadPool.execute(run);
		return true;
	}

	public static long GetQueueCount() {
		return _threadPool.getQueue().size();
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

	public static void Print() {
		String log = "TaskCount:" + GetTaskCount();
		log += ",GetCompletedTaskCount:" + GetCompletedTaskCount();
		log += ",GetLargestPoolSize:" + GetLargestPoolSize();
		log += ",GetActiveCount:" + GetActiveCount();
		log += ",GetQueueCount:" + GetQueueCount();

		System.out.println(log);
	}
}
