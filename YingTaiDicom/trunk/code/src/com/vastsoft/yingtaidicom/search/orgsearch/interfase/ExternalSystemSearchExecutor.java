package com.vastsoft.yingtaidicom.search.orgsearch.interfase;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;

/**
 * 外部系统检索执行器
 * 
 * @author jben
 * @since 2.0
 */
public abstract class ExternalSystemSearchExecutor {
	private SystemIdentity systemIdentity;
	private SessionFactory factory;

	/**
	 * 创建一个外部系统搜索执行器
	 * 
	 * @param systemIdentity
	 *            系统身份标识，必须指定
	 * @param factory
	 *            数据库连接工厂，必须指定
	 */
	public ExternalSystemSearchExecutor(SystemIdentity systemIdentity, SessionFactory factory) {
		super();
		this.systemIdentity = systemIdentity;
		this.factory = factory;
		if (systemIdentity == null)
			throw new RuntimeException("系统身份必须指定！");
		if (this.factory == null)
			throw new RuntimeException("数据连接池必须指定！");
	}

	/**
	 * 通过参数类型返回是否可以检索出信息<br>
	 * 此方法会被多次调用
	 * 
	 * @param paramType
	 *            参数类型
	 * @return 是否可识别此参数
	 */
	public abstract boolean canSearch(final RemoteParamsType paramType);

	/**
	 * 获取系统身份标识
	 * 
	 * @return
	 */
	public SystemIdentity getSystemIdentity() {
		return this.systemIdentity;
	}

	/**
	 * 执行病人数据搜索，根据病人数据查询尽可能多的数据<br>
	 * 当查询到数据时，应将数据添加到 rr参数中<br>
	 * rr参数中可以被任意读取，实现可根据需要从中获取需要的数据<br>
	 * 本方法应尽最大努力保证数据的准确性和完整性
	 * 
	 * @param searchResult
	 *            数据管理器，可以向里面添加或者读取数据
	 * @param remoteParamsManager
	 *            查询参数管理器，需要查询的参数都在里面
	 * @throws SearchExecuteException
	 */
	public abstract void searchPatientData(final SearchResult searchResult,
			final RemoteParamsManager remoteParamsManager) throws SearchExecuteException;

	/**
	 * 搜索最近的病例号码
	 * 
	 * @param result
	 *            返回值
	 * @param remoteParamsManager
	 *            查询客户端发过来的远程参数管理器
	 * @throws SearchExecuteException
	 *             搜索执行异常
	 */
	public abstract void searchLastCaseNums(final LastCaseNums result, final RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException;

	/**
	 * 读取缩略图，如果不能读取请返回null<br>
	 * 此方法提供给客户端二次获取指定影像的缩略图
	 * 
	 * @param remoteParamsManager
	 *            查询客户端发过来的远程参数管理器
	 * @param thumbnail_uid
	 *            缩略图的UID
	 * @return
	 * @throws SearchExecuteException
	 *             搜索执行异常
	 */
	public abstract byte[] readThumbnail(final RemoteParamsManager remoteParamsManager, final String thumbnail_uid)
			throws SearchExecuteException;

	public SqlSession getSession() {
		try {
			return this.factory.getSession();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void closeSession(SqlSession session) {
		if (session != null)
			this.factory.closeSession(session);
	}

	/**
	 * 通过病人姓名查询病人信息
	 * 
	 * @param remoteParamsManager
	 * @param patient_name
	 * @param identify_id 
	 * @return
	 * @throws SearchExecuteException
	 */
	public abstract List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(final RemoteParamsManager remoteParamsManager,
			final String patient_name, String identify_id) throws SearchExecuteException;

}
