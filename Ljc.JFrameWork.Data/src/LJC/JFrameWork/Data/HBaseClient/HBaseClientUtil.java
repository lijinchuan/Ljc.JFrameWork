package LJC.JFrameWork.Data.HBaseClient;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;

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
		config.set("hbase.master", "192.168.0.100:60000");
		config.set("hbase.zookeeper.quorum", "192.168.0.110,192.168.0.111,192.168.0.112");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		hbaseConfig = new HBaseConfiguration(config);

		// Configuration configuration = new HBaseConfiguration();
		// HTable htable = new HTable(configuration, "logs");
		HBaseAdmin hbaseadmin = new HBaseAdmin(hbaseConfig);
		String[] names = hbaseadmin.getTableNames();
		return names;
	}
}
