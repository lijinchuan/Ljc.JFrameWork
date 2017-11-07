package LJC.JFrameWork.Data.HBaseClient;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import Ljc.JFramework.Exception.ArgumentNullException;
import Ljc.JFramework.Utility.StringUtil;

public class HBaseClientUtil {

	public static String[] GetTables() throws IOException {
		// HBaseConfiguration
		Configuration configuration = HBaseConfiguration.create();
		configuration.addResource(new Path(
				"D:\\GitHub\\Ljc.JFrameWork\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\TestJFramework\\WEB-INF\\classes\\hbase-site.xml"));

		// configuration.set("hbase.zookeeper.quorum",
		// "192.168.0.110,192.168.0.111,192.168.0.112");
		configuration.set("hbase.rpc.timeout", "3000");
		configuration.set("hbase.client.retries.number", "3");
		// Configuration configuration = new HBaseConfiguration();
		// HTable htable = new HTable(configuration, "logs");
		HBaseAdmin hbaseadmin = new HBaseAdmin(configuration);
		String[] names = hbaseadmin.getTableNames();
		return names;
	}

	public static String[] GetTables2() throws IOException {
		HBaseConfiguration hbaseConfig = null;
		Configuration config = new Configuration();
		// config.set("hbase.master", "192.168.0.110:60000");
		config.set("hbase.zookeeper.quorum", "192.168.0.110,192.168.0.111,192.168.0.112");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		hbaseConfig = new HBaseConfiguration(config);

		// Configuration configuration = new HBaseConfiguration();
		// HTable htable = new HTable(configuration, "logs");
		HBaseAdmin hbaseadmin = new HBaseAdmin(hbaseConfig);
		String[] names = hbaseadmin.getTableNames();
		return names;
	}

	public static String[] GetTables3() throws IOException {
		HBaseConfiguration hbaseConfig = null;
		Configuration config = new Configuration();
		// config.set("hbase.master", "192.168.0.110:60000");
		config.set("hbase.zookeeper.quorum", "ljcserver:10110,ljcserver:10111,ljcserver:10112");
		config.set("hbase.rpc.timeout", "3000");
		config.set("hbase.client.retries.number", "3");

		// config.set("hbase.zookeeper.property.clientPort", "2181");
		hbaseConfig = new HBaseConfiguration(config);

		// Configuration configuration = new HBaseConfiguration();
		// HTable htable = new HTable(configuration, "logs");
		HBaseAdmin hbaseadmin = new HBaseAdmin(hbaseConfig);
		String[] names = hbaseadmin.getTableNames();
		return names;
	}

	public static boolean createTable(Configuration config, String tabname, String[] colFamilies)
			throws ArgumentNullException, IOException {
		if (StringUtil.isNullOrEmpty(tabname)) {
			throw new ArgumentNullException("tabname");
		}

		if (colFamilies == null || colFamilies.length == 0) {
			throw new ArgumentNullException("colFamilies");
		}

		Admin ad = ConnectionFactory.createConnection(config).getAdmin();

		return false;
	}
}
