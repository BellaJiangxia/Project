package com.vastsoft.yingtai.dataMove.db;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DataMoveSessionManager {
	private static SqlSessionFactory srcfactory = null;
	private static SqlSessionFactory destfactory = null;

	public static void loadProperties() throws IOException {
		Resources.setCharset(Charset.forName("UTF-8"));
		Reader src_batis_config = Resources.getResourceAsReader("com/vastsoft/yingtai/dataMove/config/resource_ibatis_config.xml");
		srcfactory = new SqlSessionFactoryBuilder().build(src_batis_config);
		Reader dest_batis_config = Resources.getResourceAsReader("com/vastsoft/yingtai/dataMove/config/destiantion_ibatis_config.xml");
		destfactory = new SqlSessionFactoryBuilder().build(dest_batis_config);
	}

	public static SqlSession getSrcSession(){
		if (srcfactory == null) {
			try {
				loadProperties();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return srcfactory.openSession();
	}
	
	public static SqlSession getDestSession(){
		if (destfactory == null) {
			try {
				loadProperties();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return destfactory.openSession();
	}
	
	public static void closeSession(SqlSession session){
		if (session != null) {
			session.close();
		}
	}
}
