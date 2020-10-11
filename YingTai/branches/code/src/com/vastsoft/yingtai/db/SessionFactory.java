package com.vastsoft.yingtai.db;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * SqlSession工厂
 * 
 * @author JoeRadar
 * @date 2015-3-5
 */
public class SessionFactory{ 
	private static SqlSessionFactory factory = null;

	public static void loadProperties() throws IOException {
		Resources.setCharset(Charset.forName("UTF-8"));
		Reader batis_config = Resources.getResourceAsReader("ibatis_config.xml");
		SessionFactory.factory = new SqlSessionFactoryBuilder().build(batis_config);
	}

	public static SqlSession getSession(){
		if (SessionFactory.factory == null) {
			try {
				loadProperties();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SessionFactory.factory.openSession();
	}
	
	public static VsSqlSession getSession(String key){
		return new VsSqlSession(getSession(),key);
	}

	public static void closeSession(SqlSession session){
		if (session != null) {
			session.close();
		}
	}
	
	public static void closeSession(SqlSession session,String key){
		if (session != null) {
			if (session instanceof VsSqlSession){
				VsSqlSession vsSession=(VsSqlSession) session;
				vsSession.close(key);
			}
		}
	}
}
