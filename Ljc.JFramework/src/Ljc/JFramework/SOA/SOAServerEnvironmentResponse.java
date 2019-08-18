package Ljc.JFramework.SOA;

import Ljc.JFramework.BeanFieldAnnotation;

public class SOAServerEnvironmentResponse {
	@BeanFieldAnnotation(order = 1)
	private String _machineName;
	@BeanFieldAnnotation(order = 2)
	private String _oSVersion;
	@BeanFieldAnnotation(order = 3)
	private int _processorCount;

	public String getMachineName() {
		return this._machineName;
	}

	public void setMachineName(String value) {
		this._machineName = value;
	}

	public String getOSVersion() {
		return this._oSVersion;
	}

	public void setOSVersion(String value) {
		this._oSVersion = value;
	}

	public int getProcessorCount() {
		return this._processorCount;
	}

	public void setProcessorCount(int value) {
		this._processorCount = value;
	}
}
