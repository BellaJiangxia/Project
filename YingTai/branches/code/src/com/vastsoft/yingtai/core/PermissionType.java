package com.vastsoft.yingtai.core;

import com.vastsoft.util.common.SingleClassConstant;

public class PermissionType extends SingleClassConstant {

	protected PermissionType(int iCode, String strName) {
		super(iCode, strName);
	}

	public final static PermissionType UserPermissionType = new PermissionType(1, "用户权限");
	public final static PermissionType OrgPermissionType = new PermissionType(2, "机构权限");
}
