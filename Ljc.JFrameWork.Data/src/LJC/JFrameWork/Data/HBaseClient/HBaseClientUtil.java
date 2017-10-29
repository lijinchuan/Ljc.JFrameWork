package LJC.JFrameWork.Data.HBaseClient;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class HBaseClientUtil {
	public static String[] GetTables() throws IOException {
		// HBaseConfiguration
		Configuration configuration = HBaseConfiguration.create();

		// Configuration configuration = new HBaseConfiguration();
		// HTable htable = new HTable(configuration, "logs");
		HBaseAdmin hbaseadmin = new HBaseAdmin(configuration);
		String[] names = hbaseadmin.getTableNames();
		return names;
	}
}
