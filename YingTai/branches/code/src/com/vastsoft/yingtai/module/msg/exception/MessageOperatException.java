package com.vastsoft.yingtai.module.msg.exception;

import com.vastsoft.util.exception.BaseException;

public class MessageOperatException extends BaseException {
	private static final long serialVersionUID = 8227634234626273652L;

	public MessageOperatException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 3243;
	}
}
