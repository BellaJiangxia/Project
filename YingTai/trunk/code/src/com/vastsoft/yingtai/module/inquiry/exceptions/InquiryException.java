package com.vastsoft.yingtai.module.inquiry.exceptions;

import com.vastsoft.util.exception.BaseException;

public class InquiryException extends BaseException
{
	private static final long serialVersionUID = 8505202649569882167L;

	public InquiryException(String strMessage)
	{
		super(strMessage);
	}

	@Override
	public int getCode()
	{
		return 901;
	}

}
