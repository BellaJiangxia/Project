package com.vastsoft.yingtai.exception;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;

public class NotPermissionException extends BaseException
{

	private static final long serialVersionUID = -4572953886112915325L;

	public NotPermissionException()
	{
		super("权限不足");
	}
	
	

	public NotPermissionException(String strMsg)
	{
		super(strMsg);
	}

	public NotPermissionException(UserPermission orgAudit) {
		this("您缺少 “"+orgAudit.getName()+"”,不能执行此操作！");
	}



	@Override
	public int getCode()
	{
		return 101;
	}

}
