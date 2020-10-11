package com.vastsoft.yingtai.module.diagnosis2.requestTranfer.excaption;

import com.vastsoft.util.exception.BaseException;

public class RequestTranferException extends BaseException {
	private static final long serialVersionUID = -5891989280244847908L;

	public RequestTranferException(String strMessage) {
		super(strMessage);
	}

	public RequestTranferException(Throwable e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 5723;
	}

}
