package com.vastsoft.yingtai.module.org.configs.exception;

import com.vastsoft.yingtai.module.org.exception.OrgOperateException;

public class OrgConfigsException extends OrgOperateException {
	private static final long serialVersionUID = -1900990483978528733L;

	public OrgConfigsException(String strMessage) {
		super(strMessage);
	}

	public OrgConfigsException(Throwable e) {
		super(e);
	}
	
}
