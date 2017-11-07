package LJC.JFrameWork.Data.HBaseClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import Ljc.JFramework.Exception.ArgumentNullException;
import Ljc.JFramework.Utility.StringUtil;

public class HBaseClientWrapper {
	private String _hbase_zookeeper_quorum;
	private int _hbase_zookeeper_property_clientPort = 0;
	private int _hbase_rpc_timeout = 5000;
	private int _hbase_client_retries_number = 3;
	private Connection _hbaseConnection;

	private void init() throws IOException {
		Configuration config = new Configuration();
		config.set("hbase.zookeeper.quorum", this._hbase_zookeeper_quorum);
		if (_hbase_zookeeper_property_clientPort > 0) {
			config.set("hbase.zookeeper.property.clientPort",
					String.valueOf(this._hbase_zookeeper_property_clientPort));
		}
		config.set("hbase.rpc.timeout", String.valueOf(this._hbase_rpc_timeout));
		config.set("hbase.client.retries.number", String.valueOf(this._hbase_client_retries_number));

		this._hbaseConnection = ConnectionFactory.createConnection(config);
	}

	public HBaseClientWrapper(String hbase_zookeeper_quorum) throws IOException {
		this._hbase_zookeeper_quorum = hbase_zookeeper_quorum;

		init();
	}

	public HBaseClientWrapper(String hbase_zookeeper_quorum, int hbase_zookeeper_property_clientPort)
			throws IOException {
		this._hbase_zookeeper_quorum = hbase_zookeeper_quorum;
		this._hbase_zookeeper_property_clientPort = hbase_zookeeper_property_clientPort;
		init();
	}

	public HBaseClientWrapper(String hbase_zookeeper_quorum, int hbase_rpc_timeout, int hbase_client_retries_number)
			throws IOException {
		this._hbase_zookeeper_quorum = hbase_zookeeper_quorum;
		this._hbase_rpc_timeout = hbase_rpc_timeout;
		this._hbase_client_retries_number = hbase_client_retries_number;

		init();
	}

	public TableName[] GetTables() throws IOException {

		TableName[] tablenames = this._hbaseConnection.getAdmin().listTableNames();
		// String[] names = _hbaseConnection..getTableNames();

		return tablenames;
	}

	public void createTable(String tabname, String... colFamilies) throws Exception {
		if (StringUtil.isNullOrEmpty(tabname)) {
			throw new ArgumentNullException("tabname");
		}

		Admin admin = this._hbaseConnection.getAdmin();
		TableName tablename = TableName.valueOf(tabname);
		if (admin.tableExists(tablename)) {
			throw new Exception(String.format("表已存在:%s", tabname));
		}

		// creating table descriptor
		HTableDescriptor table = new HTableDescriptor(tablename);

		for (String cf : colFamilies) {
			// creating column family descriptor
			HColumnDescriptor family = new HColumnDescriptor(Bytes.toBytes(cf));
			// adding coloumn family to HTable
			table.addFamily(family);
		}
		admin.createTable(table);
	}

	public void addColFamily(String tabname, String... colFamilies) throws Exception {
		if (StringUtil.isNullOrEmpty(tabname)) {
			throw new ArgumentNullException("tabname");
		}

		Admin admin = this._hbaseConnection.getAdmin();
		TableName tablename = TableName.valueOf(tabname);
		if (!admin.tableExists(tablename)) {
			throw new Exception(String.format("表不存在:%s", tabname));
		}

		for (String cf : colFamilies) {
			admin.addColumn(tablename, new HColumnDescriptor(cf));
		}
	}

	public void delColFamily(String tabname, String... colFamilies) throws Exception {
		if (StringUtil.isNullOrEmpty(tabname)) {
			throw new ArgumentNullException("tabname");
		}

		Admin admin = this._hbaseConnection.getAdmin();
		TableName tablename = TableName.valueOf(tabname);
		if (!admin.tableExists(tablename)) {
			throw new Exception(String.format("表不存在:%s", tabname));
		}

		for (String cf : colFamilies) {
			admin.deleteColumn(tablename, Bytes.toBytes(cf));
		}
	}

	public void deleteTable(String tabname) throws Exception {
		Admin admin = this._hbaseConnection.getAdmin();
		TableName tablename = TableName.valueOf(tabname);
		if (!admin.tableExists(tablename)) {
			throw new Exception(String.format("表不存在:%s", tabname));
		}
		if (admin.isTableEnabled(tablename)) {
			admin.disableTable(tablename);
		}
		admin.deleteTable(tablename);
	}

	public void enableTable(String tabname) throws Exception {
		Admin admin = this._hbaseConnection.getAdmin();
		TableName tablename = TableName.valueOf(tabname);
		if (!admin.tableExists(tablename)) {
			throw new Exception(String.format("表不存在:%s", tabname));
		}
		admin.enableTable(tablename);
	}

	public void disableTable(String tabname) throws Exception {
		Admin admin = this._hbaseConnection.getAdmin();
		TableName tablename = TableName.valueOf(tabname);
		if (!admin.tableExists(tablename)) {
			throw new Exception(String.format("表不存在:%s", tabname));
		}
		admin.disableTable(tablename);
	}

	public void put(String tabname, String rowKey, String family, String qualifier, String value) throws IOException {
		TableName tablename = TableName.valueOf(tabname);
		Table table = this._hbaseConnection.getTable(tablename);
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
		table.put(put);
	}

	public void put(String tabname, String rowKey, String family, String qualifier, byte[] value) throws IOException {
		TableName tablename = TableName.valueOf(tabname);
		Table table = this._hbaseConnection.getTable(tablename);
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), value);
		table.put(put);
	}

	public byte[] getString(String tabname, String rowKey, String family, String qualifier) throws IOException {
		TableName tablename = TableName.valueOf(tabname);
		Table table = this._hbaseConnection.getTable(tablename);
		Get get = new Get(Bytes.toBytes(rowKey));

		byte[] familybytes = Bytes.toBytes(family);
		byte[] qualifierbytes = Bytes.toBytes(qualifier);
		get.addColumn(familybytes, qualifierbytes);
		Result result = table.get(get);

		return result.value();
	}

	public List<byte[]> scan(String tabname, String family, String qualifier, String startRow, String stopRow)
			throws IOException {

		List<byte[]> ret = new ArrayList<byte[]>();

		TableName tablename = TableName.valueOf(tabname);
		Table table = this._hbaseConnection.getTable(tablename);
		Scan scan = new Scan();

		if (!StringUtil.isNullOrEmpty(family)) {
			if (!StringUtil.isNullOrEmpty(qualifier)) {
				scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
			} else {
				scan.addFamily(Bytes.toBytes(family));
			}
		}

		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		ResultScanner scanner = table.getScanner(scan);
		for (Result result = scanner.next(); result != null; result = scanner.next()) {
			ret.add(result.value());

			System.out.println(result);
		}

		return ret;
	}

}
