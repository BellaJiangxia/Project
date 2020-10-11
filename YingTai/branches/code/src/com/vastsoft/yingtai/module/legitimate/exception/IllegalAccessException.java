package com.vastsoft.yingtai.module.legitimate.exception;

import com.vastsoft.util.exception.BaseException;

public class IllegalAccessException extends BaseException {
	private static final long serialVersionUID = -4541527449562772045L;

	public IllegalAccessException(Throwable e) {
		super(e);
	}

	public IllegalAccessException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 11238;
	}

}
