package com.vastsoft.yingtai.exception;

import com.vastsoft.util.exception.BaseException;

public class NotOrgPermissionException extends BaseException {
	private static final long serialVersionUID = -4022077568786613827L;

	public NotOrgPermissionException() {
		super("机构没有此权限，请联系系统管理员！");
	}

	@Override
	public int getCode() {
		return 200;
	}

}
