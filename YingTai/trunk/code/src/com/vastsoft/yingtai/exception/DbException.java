package com.vastsoft.yingtai.exception;

import com.vastsoft.util.exception.BaseException;

public class DbException extends BaseException {

	private static final long serialVersionUID = 9146153745673322891L;

	public DbException() {
		super("数据库操作异常");
	}

	@Override
	public int getCode() {
		return 102;
	}

}
