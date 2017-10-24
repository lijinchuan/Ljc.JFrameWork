package Ljc.JFramework.WindowService;

public interface IService extends Runnable {
	void setRunFlag(boolean runFlag);

	boolean getRunFlag();
}
