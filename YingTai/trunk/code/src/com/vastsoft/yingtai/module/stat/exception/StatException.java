package com.vastsoft.yingtai.module.stat.exception;

import com.vastsoft.util.exception.BaseException;

public class StatException extends BaseException {
	private static final long serialVersionUID = 1135234184414313792L;

	public StatException(String strMessage) {
		super(strMessage);
	}

    public StatException(Throwable e) {
        super(e);
    }

    @Override
	public int getCode() {
		return 513;
	}

}
