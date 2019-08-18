package Ljc.JFramework;

public class AutoResetEvent {
	private final Object _monitor = new Object();
	private volatile boolean _isOpen = false;

	public AutoResetEvent(boolean open) {
		_isOpen = open;
	}

	public void waitOne() throws InterruptedException {
		synchronized (_monitor) {
			while (!_isOpen) {
				_monitor.wait();
			}
			_isOpen = false;
		}
	}

	public boolean waitOne(long timeout) throws InterruptedException {
		boolean istimeout = false;
		synchronized (_monitor) {
			long t = System.currentTimeMillis();
			while (!_isOpen) {
				_monitor.wait(timeout);
				// Check for timeout
				if (System.currentTimeMillis() - t >= timeout) {
					istimeout = true;
					break;
				}
			}
			_isOpen = false;
		}

		return istimeout;
	}

	public void set() {
		synchronized (_monitor) {
			_isOpen = true;
			_monitor.notify();
		}
	}

	public void reset() {
		_isOpen = false;
	}
}
