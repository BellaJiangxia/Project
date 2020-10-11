package com.vastsoft.yingtai.module.diagnosis2.exception;

import com.vastsoft.util.exception.BaseException;

public class RequestException extends BaseException {
	private static final long serialVersionUID = 7667620369852341417L;

	public RequestException(String strMessage) {
		super(strMessage == null ? "诊断申请异常！" : strMessage);
	}

	public RequestException(Throwable e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 150;
	}

}
