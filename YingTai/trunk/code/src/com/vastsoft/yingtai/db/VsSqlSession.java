package com.vastsoft.yingtai.db;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

/**具备SqlSession的所有功能，且可以执行临时接管操作;<br>
 * 接管之后，没有key不可以提交和回滚,以及关闭连接；<br>
 * 请在必要时慎重使用此类*/
@SuppressWarnings("rawtypes")
public class VsSqlSession implements SqlSession {
	private SqlSession session;
	private String key;
	
	public VsSqlSession(SqlSession session, String key) {
		this.session=session;
		if (key==null||key.trim().isEmpty())
			throw new RuntimeException("必须指定key!");
		this.key=key;
	}

	@Override
	public void close() {
		return;
	}
	
	public void close(String key) {
		if (this.key.equals(key))
			session.close();
	}

	@Override
	public void commit() {
	}
	
	public void commit(String key) {
		if (this.key.equals(key))
			session.commit();
	}
	
	@Override
	public void commit(boolean arg0) {
	}
	
	public void commit(String key,boolean arg0) {
		if (this.key.equals(key))
			session.commit(arg0);
	}

	@Override
	public int delete(String arg0) {
		return session.delete(arg0);
	}

	@Override
	public int delete(String arg0, Object arg1) {
		return session.delete(arg0, arg1);
	}

	@Override
	public Configuration getConfiguration() {
		return session.getConfiguration();
	}

	@Override
	public <T> T getMapper(Class<T> arg0) {
		return session.getMapper(arg0);
	}

	@Override
	public int insert(String arg0) {
		return session.insert(arg0);
	}

	@Override
	public int insert(String arg0, Object arg1) {
		return session.insert(arg0, arg1);
	}
	
	@Override
	public void rollback() {
	}

	public void rollback(String key) {
		if (this.key.equals(key))
			session.rollback();
	}

	@Override
	public void rollback(boolean arg0) {
	}
	
	public void rollback(boolean arg0,String key) {
		if (this.key.equals(key))
			session.rollback(arg0);
	}

	@Override
	public void select(String arg0, Object arg1, ResultHandler arg2) {
		session.select(arg0, arg1, arg2);
	}

	@Override
	public List selectList(String arg0) {
		return session.selectList(arg0);
	}

	@Override
	public List selectList(String arg0, Object arg1) {
		return session.selectList(arg0, arg1);
	}

	@Override
	public Object selectOne(String arg0) {
		return session.selectOne(arg0);
	}

	@Override
	public Object selectOne(String arg0, Object arg1) {
		return session.selectOne(arg0, arg1);
	}

	@Override
	public int update(String arg0) {
		return session.update(arg0);
	}

	@Override
	public int update(String arg0, Object arg1) {
		return session.update(arg0, arg1);
	}

	@Override
	public void clearCache() {
		this.session.clearCache();
	}

	@Override
	public List<BatchResult> flushStatements() {
		return this.session.flushStatements();
	}

	@Override
	public Connection getConnection() {
		return this.session.getConnection();
	}

	@Override
	public void select(String arg0, ResultHandler arg1) {
		this.session.select(arg0, arg1);
	}

	@Override
	public void select(String arg0, Object arg1, RowBounds arg2, ResultHandler arg3) {
		this.session.select(arg0, arg1, arg2, arg3);
	}

	@Override
	public <T> Cursor<T> selectCursor(String arg0) {
		return this.session.selectCursor(arg0);
	}

	@Override
	public <T> Cursor<T> selectCursor(String arg0, Object arg1) {
		return this.session.selectCursor(arg0, arg1);
	}

	@Override
	public <T> Cursor<T> selectCursor(String arg0, Object arg1, RowBounds arg2) {
		return this.session.selectCursor(arg0, arg1, arg2);
	}

	@Override
	public <E> List<E> selectList(String arg0, Object arg1, RowBounds arg2) {
		return this.selectList(arg0, arg1, arg2);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String arg0, String arg1) {
		return this.selectMap(arg0, arg1);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String arg0, Object arg1, String arg2) {
		return this.session.selectMap(arg0, arg1, arg2);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String arg0, Object arg1, String arg2, RowBounds arg3) {
		return this.session.selectMap(arg0, arg1, arg2, arg3);
	}
}
