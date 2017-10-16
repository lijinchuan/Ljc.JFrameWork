package Ljc.JFramework.Utility;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
	// 是用一个List
	private List<Event> objects;

	public EventHandler() {
		objects = new ArrayList<Event>();
	}

	private Event findEvt(Object object, String methodName, Class<?>... clss) {
		Event evt = null;
		for (Event e : objects) {
			if (EqualsUtil.Equals(e.getObject(), object) && EqualsUtil.Equals(e.getMethodName(), methodName)) {

				if (e.getParamTypes() != null) {
					if (clss == null || clss.length != e.getParamTypes().length) {
						continue;
					}
					for (int i = 0; i < clss.length; i++) {
						if (!e.getParamTypes()[i].equals(clss[i])) {
							continue;
						}
					}
				} else {
					if (clss != null) {
						continue;
					}
				}

				evt = e;
				break;
			}
		}

		return evt;
	}

	// 添加某个对象要执行的事件，及需要的参数
	public boolean addEvent(Object object, String methodName, Class<?>... clss) {
		Event evt = this.findEvt(object, methodName, clss);
		if (evt != null) {
			return false;
		}
		return objects.add(new Event(object, methodName, clss));
	}

	public boolean removeEvent(Object object, String methodName, Class<?>... clss) {
		Event evt = this.findEvt(object, methodName, clss);

		if (evt != null) {
			return objects.remove(evt);
		}

		return false;
	}

	// 通知所有的对象执行指定的事件
	public Object[] notifyX(Object... params) throws Exception {
		if (objects == null || objects.size() == 0) {
			return null;
		}

		Object[] results = new Object[objects.size()];
		int i = 0;
		for (Event e : objects) {
			results[i++] = e.invoke(params);
		}

		return results;
	}
}
