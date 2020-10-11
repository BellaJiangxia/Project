package com.vastsoft.yingtaidicom.search.exception;

import com.vastsoft.util.exception.BaseException;

public class DataPriorityException extends BaseException {
	private static final long serialVersionUID = -5612608815931293839L;

	public DataPriorityException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 1282;
	}
}
