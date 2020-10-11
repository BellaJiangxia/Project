package com.vastsoft.yingtai.module.financel.exception;

import com.vastsoft.util.exception.BaseException;

public class FinanceException extends BaseException {
	private static final long serialVersionUID = 8342431924670382355L;

	public FinanceException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 220;
	}

}
