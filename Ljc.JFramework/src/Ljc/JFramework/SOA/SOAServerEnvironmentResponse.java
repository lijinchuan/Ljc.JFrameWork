package Ljc.JFramework.SOA;

public class SOAServerEnvironmentResponse {
	private String _machineName;
	private String _oSVersion;
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
