package com.vastsoft.yingtai.module.org.exception;

import com.vastsoft.util.exception.BaseException;

public class OrgOperateException extends BaseException {
	private static final long serialVersionUID = 5945821696521649514L;

	public OrgOperateException(String strMessage) {
		super(strMessage);
	}

	public OrgOperateException(Throwable e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 99;
	}

}
