package Ljc.JFramework.Utility;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessTraceUtil {
	private static ConcurrentHashMap<Long, Queue<Tuple<String, Long>>> TraceDic = new ConcurrentHashMap<Long, Queue<Tuple<String, Long>>>();

	public static void StartTrace() {
		long threadid = Thread.currentThread().getId();
		Queue<Tuple<String, Long>> queue = TraceDic.get(threadid);
		if (queue == null) {
			queue = new LinkedBlockingQueue<Tuple<String, Long>>();
			TraceDic.put(threadid, queue);
		} else {
			queue.clear();
		}
		Trace("start");
	}

	public static void Trace(String message) {
		long threadid = Thread.currentThread().getId();
		Queue<Tuple<String, Long>> queue = TraceDic.get(threadid);
		if (queue != null) {
			queue.add(new Tuple<String, Long>(message, System.currentTimeMillis()));
		}
	}

	public static String PrintTrace() {

		long traceid = Thread.currentThread().getId();

		Queue<Tuple<String, Long>> queue = TraceDic.get(traceid);
		if (queue == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();

		long timeline = 0;
		Tuple<String, Long> item = null;
		while ((item = queue.poll()) != null) {
			if (timeline == 0) {
				timeline = item.GetItem2();
			}
			sb.append(String.format("%dms:%s %n", item.GetItem2() - timeline, item.GetItem1()));
		}

		return sb.toString();
	}
}
