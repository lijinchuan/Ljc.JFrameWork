package Ljc.JFramework.Utility;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
	// ����һ��List
	private List<Event> objects;

	public EventHandler() {
		objects = new ArrayList<Event>();
	}

	// ���ĳ������Ҫִ�е��¼�������Ҫ�Ĳ���
	public void addEvent(Object object, String methodName, Class... clss) {
		objects.add(new Event(object, methodName, clss));
	}

	// ֪ͨ���еĶ���ִ��ָ�����¼�
	public void notifyX(Object... params) throws Exception {
		for (Event e : objects) {
			e.invoke(params);
		}
	}
}
