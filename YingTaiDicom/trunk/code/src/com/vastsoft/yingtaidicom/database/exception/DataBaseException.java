package com.vastsoft.yingtaidicom.database.exception;

import com.vastsoft.util.exception.BaseException;

public class DataBaseException extends BaseException {
	private static final long serialVersionUID = -4061592276934269481L;

	public DataBaseException(String strMessage) {
		super("数据库异常！信息："+strMessage);
	}
	
	public DataBaseException(Exception e) {
		super("数据库异常！信息："+String.valueOf(e));
	}

	@Override
	public int getCode() {
		return 5896;
	}

}
