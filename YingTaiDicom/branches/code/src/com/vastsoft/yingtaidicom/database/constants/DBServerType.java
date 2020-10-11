package com.vastsoft.yingtaidicom.database.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.util.common.StringTools;

public class DBServerType extends SingleClassConstant {
	public static final DBServerType MYSQL = new DBServerType(10, "MYSQL", "com.mysql.jdbc.Driver");
	public static final DBServerType SQL_SERVER = new DBServerType(20, "SQLSERVER",
			"com.microsoft.sqlserver.jdbc.SQLServerDriver");
	public static final DBServerType ORACLE = new DBServerType(30, "ORACLE", "oracle.jdbc.driver.OracleDriver");
	public static final DBServerType DB2 = new DBServerType(40, "DB2", "com.ibm.db2.jcc.DB2Driver");

	private String driver;

	private static Map<Integer, DBServerType> mapDBServerType = new HashMap<Integer, DBServerType>();
	static {
		mapDBServerType.put(MYSQL.getCode(), MYSQL);
		mapDBServerType.put(SQL_SERVER.getCode(), SQL_SERVER);
		mapDBServerType.put(ORACLE.getCode(), ORACLE);
		mapDBServerType.put(DB2.getCode(), DB2);
	}

	protected DBServerType(int iCode, String strName, String driver) {
		super(iCode, strName);
		this.driver = driver;
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl(String host, int post, String dbName) {
		switch (getCode()) {
		case 10:
			return "jdbc:mysql://" + host + ":" + post + "/" + dbName
					+ "?characterEncoding=UTF8&allowMultiQueries=true";
		case 20:
			return "jdbc:sqlserver://" + host + ":" + post + ";databaseName=" + dbName;
		case 30:
			return "jdbc:oracle:thin:@" + host + ":" + post + ":" + dbName;
		case 40:
			return "jdbc:db2://" + host + ":" + post + "/" + dbName;
		default:
			return null;
		}
	}

	public static List<DBServerType> getAll() {
		return new ArrayList<DBServerType>(mapDBServerType.values());
	}

	public static DBServerType parseName(String name) {
		if (StringTools.isEmpty(name))
			return null;
		for (DBServerType dbServerType : mapDBServerType.values()) {
			if (name.equalsIgnoreCase(dbServerType.getName()))
				return dbServerType;
		}
		return null;
	}
}
