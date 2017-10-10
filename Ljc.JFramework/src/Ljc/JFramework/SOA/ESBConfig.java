package Ljc.JFramework.SOA;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ESBConfig {
	private String eSBServer;
	private int eSBPort;
	private boolean autoStart;

	public String getESBServer() {
		return this.eSBServer;
	}

	public void setESBServer(String val) {
		this.eSBServer = val;
	}

	public int getESBPort() {
		return this.eSBPort;
	}

	public void setESBPort(int val) {
		this.eSBPort = val;
	}

	public Boolean getAutoStart() {
		return this.autoStart;
	}

	public void setAutoStart(boolean val) {
		this.autoStart = val;
	}
}
