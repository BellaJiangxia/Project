package com.vastsoft.yingtaidicom.database.factory;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.database.exception.DataBaseException;

public class SessionFactory {
	// private String url;// url =
	// // jdbc:sqlserver://192.168.2.111:1433;databaseName=DICOMDB
	// private String username;// username = sa
	// private String password;
	private Properties properties;
	private String ibatis_config_path;
	private SqlSessionFactory sessionFactory;

	public SessionFactory(String url, String username, String password, String driver, String ibatis_config_path)
			throws DataBaseException {
		super();
		properties = new Properties();
		properties.setProperty("url", url);
		properties.setProperty("username", username);
		properties.setProperty("password", password);
		properties.setProperty("driver", driver);
		this.ibatis_config_path = ibatis_config_path;
	}

	public SessionFactory(Properties properties, String ibatis_config_path) throws DataBaseException {
		this.properties = properties;
		this.ibatis_config_path = ibatis_config_path;
	}

	public SqlSession getSession() throws IOException {
		if (this.sessionFactory == null) {
			Resources.setCharset(Charset.forName("UTF-8"));
			Reader batis_config = Resources.getResourceAsReader(ibatis_config_path);
			this.sessionFactory = new SqlSessionFactoryBuilder().build(batis_config, this.properties);
		}
		LoggerUtils.logger.info("获取数据库连接:" + this.properties.toString());
		return this.sessionFactory.openSession();
	}

	public void closeSession(SqlSession session) {
		if (session != null) {
			LoggerUtils.logger.info("关闭数据库连接!" + this.properties.toString());
			session.close();
		}
	}

	@Override
	public String toString() {
		return "SessionFactory [properties=" + properties + "]";
	}
}
