package com.vastsoft.yingtai.module.user.exception;

import com.vastsoft.util.exception.BaseException;

/**
 * @author jyb
 */
public class UserOperateException extends BaseException {
	private static final long serialVersionUID = 6546204719780951053L;

	public UserOperateException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 1000;
	}

}
