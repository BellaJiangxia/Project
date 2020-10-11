package com.vastsoft.yingtaidicom.search.exception;

import com.vastsoft.util.exception.BaseException;

public class SearchExecuteException extends BaseException {
	private static final long serialVersionUID = 9068674674886582569L;

	public SearchExecuteException(String strMessage) {
		super("搜索执行失败！信息：" + strMessage);
	}

	public SearchExecuteException(Exception e) {
		super("搜索执行失败！信息：" + String.valueOf(e));
	}

	public static SearchExecuteException exceptionOf(Exception e) {
		if (e instanceof SearchExecuteException)
			return (SearchExecuteException) e;
		e.printStackTrace();
		return new SearchExecuteException(e.getMessage());
	}

	@Override
	public int getCode() {
		return 1100;
	}
}