package LJC.JFrameWork.Data.Mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MsqylHelper {
	public static void TestConn() throws SQLException {
		System.out.println("Hello World!");
		Connection conn = null;
		String sql;

		String conn_str = "jdbc:mysql://127.0.0.1:3306/gubadata?"
				+ "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql����
			System.out.println("�ɹ�����MySQL��������");

			// һ��Connection����һ�����ݿ�����
			conn = DriverManager.getConnection(conn_str);
			Statement stmt = conn.createStatement();
			sql = "show tables;";
			ResultSet result = stmt.executeQuery(sql);
			if (result != null) {
				while (result.next()) {
					System.out.println(result.getString(1) + "\t");
				}
			}
		} catch (SQLException e) {
			System.out.println("MySQL��������");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
	}
}
