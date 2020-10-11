package com.vastsoft.yingtai.module.sys.exception;

import com.vastsoft.util.exception.BaseException;

public class SysOperateException extends BaseException {
	private static final long serialVersionUID = -2415621992928851638L;

	public SysOperateException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 220;
	}

}
