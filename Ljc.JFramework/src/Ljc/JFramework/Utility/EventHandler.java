package Ljc.JFramework.Utility;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
	// 是用一个List
	private List<Event> objects;

	public EventHandler() {
		objects = new ArrayList<Event>();
	}

	// 添加某个对象要执行的事件，及需要的参数
	public void addEvent(Object object, String methodName, Class... clss) {
		objects.add(new Event(object, methodName, clss));
	}

	// 通知所有的对象执行指定的事件
	public void notifyX(Object... params) throws Exception {
		for (Event e : objects) {
			e.invoke(params);
		}
	}
}
