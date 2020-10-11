package com.vastsoft.yingtai.module.weixin.service.exceptions;

import com.vastsoft.util.exception.BaseException;

public class ReleationException extends BaseException
{

	private static final long serialVersionUID = -1573436855760883466L;

	public ReleationException(String strMsg)
	{
		super(strMsg);
	}

	@Override
	public int getCode()
	{
		return 123;
	}

}
