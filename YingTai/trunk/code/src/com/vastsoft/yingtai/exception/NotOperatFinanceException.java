package com.vastsoft.yingtai.exception;

import com.vastsoft.util.exception.BaseException;

public class NotOperatFinanceException extends BaseException {
	private static final long serialVersionUID = 294699216030004117L;

	public NotOperatFinanceException() {
		super("该模块没有结算系统调用权限！");
	}

	@Override
	public int getCode() {
		return 201;
	}

}
