package Ljc.JFramework.SOA;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlTransient;

import Ljc.JFramework.CoreException;
import Ljc.JFramework.Utility.FileUtil;
import Ljc.JFramework.Utility.StringUtil;

//@XmlAccessorType(XmlAccessType.PROPERTY)
//@XmlRootElement(name = "ESBConfig")
public class ESBConfig {
	// @XmlElement(name = "ESBServer")
	private String eSBServer;
	// @XmlElement(name = "ESBPort")
	private int eSBPort;

	// @XmlElement(name = "AutoStart")
	private boolean autoStart;

	// @XmlElement(name = "Security")
	private boolean security;

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

	@XmlTransient
	public boolean getSecurity() {
		return this.security;
	}

	public void setSecurity(boolean val) {
		this.security = val;
	}

	private static String configfile = System.getProperty("user.home") + File.separatorChar + "ESBConfig.xml";
	private static ESBConfig _esbConfig = null;

	public static ESBConfig ReadConfig() throws Exception {
		if (_esbConfig != null)
			return _esbConfig;

		System.out.println(configfile);

		if (!new File(configfile).exists()) {
			String configfile2 = System.getProperty("user.dir") + File.separatorChar + "ESBConfig.xml";
			if (!new File(configfile2).exists()) {
				String configfile3 = System.getProperty("user.esbconfig") + File.separatorChar + "ESBConfig.xml";
				if (!new File(configfile3).exists()) {
					throw new Exception(
							String.format("未找到ESBConfig配置文件，路径：%s 、路径 %s、路径%s", configfile, configfile2, configfile3));
				} else {
					configfile = configfile3;
				}
			} else {
				configfile = configfile2;
			}
		}
		// _esbConfig =
		// Ljc.JFramework.Utility.SerializerUtil.DeSerializerFile(ESBConfig.class,
		// configfile);

		String configxml = FileUtil.readAllToString(configfile);
		_esbConfig = new ESBConfig();
		_esbConfig.setESBServer(StringUtil.getMiddleString(configxml, "<ESBServer>", "</ESBServer>"));
		_esbConfig.setAutoStart(
				Boolean.parseBoolean(StringUtil.getMiddleString(configxml, "<AutoStart>", "</AutoStart>")));
		_esbConfig.setESBPort(Integer.parseInt(StringUtil.getMiddleString(configxml, "<ESBPort>", "</ESBPort>")));
		_esbConfig
				.setSecurity(Boolean.parseBoolean(StringUtil.getMiddleString(configxml, "<Security>", "</Security>")));

		if (_esbConfig.getESBServer().indexOf('.') == -1 && _esbConfig.getESBServer().indexOf(':') == -1) {
			InetAddress[] ipaddress = InetAddress.getAllByName(_esbConfig.getESBServer());
			if (ipaddress == null || ipaddress.length == 0) {
				throw new Exception("配置服务地址无效。");
			}

			InetAddress ip4add = null;
			for (InetAddress add : ipaddress) {
				if (add instanceof Inet4Address) {
					ip4add = add;
					break;
				}
			}
			if (ip4add == null) {
				CoreException ex = new CoreException("查找机器失败");
				ex.Data.put("hostname", _esbConfig.getESBServer());
				throw ex;
			}
			_esbConfig.setESBServer(ip4add.getHostAddress());
		}

		return _esbConfig;
	}

	public static void WriteConfig(String esbServer, int esbPort, boolean autoStrat) throws JAXBException, IOException {
		ESBConfig config = new ESBConfig();
		config.setESBServer(esbServer);
		config.setESBPort(esbPort);
		config.setAutoStart(autoStrat);
		Ljc.JFramework.Utility.SerializerUtil.SerializerToXML(config, configfile);
	}
}
