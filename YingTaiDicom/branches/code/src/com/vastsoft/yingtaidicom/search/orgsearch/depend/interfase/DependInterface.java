package com.vastsoft.yingtaidicom.search.orgsearch.depend.interfase;

import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;

/** 数据翻译器，协调两个系统将的数据翻译工作 */
public interface DependInterface {
	/**
	 * 正序匹配,使用上级系统的数据转换成下级系统的数据
	 * 
	 * @param searchResult
	 * @param remoteParamsManager
	 * @throws SearchExecuteException
	 */
	public abstract void sequenceMatching(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException;

	/**
	 * 逆序匹配，使用下级系统的数据转换成上级系统的数据
	 * 
	 * @param searchResult
	 * @param remoteParamsManager
	 * @throws SearchExecuteException
	 */
	public abstract void reverseMatching(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException;

	/**
	 * 整理关联关系
	 * 
	 * @param searchResult
	 *            整理前的原数据
	 * @param remoteParamsManager
	 *            参数管理器
	 */
	public abstract void organizeSearchResult(SearchResult searchResult, RemoteParamsManager remoteParamsManager);

}
