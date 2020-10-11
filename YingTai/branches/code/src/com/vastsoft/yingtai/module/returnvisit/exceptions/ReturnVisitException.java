package com.vastsoft.yingtai.module.returnvisit.exceptions;

import com.vastsoft.util.exception.BaseException;

public class ReturnVisitException extends BaseException
{
	private static final long serialVersionUID = 8505202649569882167L;

	public ReturnVisitException(String strMessage)
	{
		super(strMessage);
	}

	@Override
	public int getCode()
	{
		return 701;
	}

}
