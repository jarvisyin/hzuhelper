package com.hzuhelper.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlHelper {
	// private String host;
	// private String port;
	private String username;
	private String password;
	private static String driverName = "com.mysql.jdbc.Driver";
	// private String dbUrl = "jdbc:mysql://";
	// private String serverName;
	//
	// private String databaseName;
	private String connName;

	private static MySqlHelper instance = null;

	private MySqlHelper() {
		// databaseName = "XInZTgRQuSPpqmDajqNv";
		// connName = dbUrl + serverName + databaseName;

		// bae连接
		// host = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_IP);
		// port = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_PORT);
		// username = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
		// password = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
		// databaseName = "XInZTgRQuSPpqmDajqNv";
		// serverName = host + ":" + port + "/";
		// connName = dbUrl + serverName + databaseName;

		// 本地连接
		username = "root";
		password = "919192";
		connName = "jdbc:mysql://127.0.0.1/hzuhelper?useUnicode=true&characterEncoding=utf8";

		// sae连接
		// host = "w.rdc.sae.sina.com.cn";
		// port = "3307";
		// username = "x3kokl3km4";
		// password = "h05031kh1w3m3m5z5lhyljm3z4yw2lh1kmwixkh3";
		// databaseName = "app_hzuhelper";
		// serverName = host + ":" + port + "/";
		// connName = dbUrl + serverName + databaseName;

	}

	public static MySqlHelper getInstance() {
		if (instance == null) {
			synchronized (MySqlHelper.class) {
				if (instance == null) {
					instance = new MySqlHelper();
				}
			}
		}
		return instance;
	}

	static {
		try {
			// DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Class.forName(driverName).newInstance();
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(connName, username, password);
	}

	public void free(ResultSet rs, Connection connection, Statement pst) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (connection != null)
						connection.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
