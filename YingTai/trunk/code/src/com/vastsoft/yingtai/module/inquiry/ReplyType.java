package com.vastsoft.yingtai.module.inquiry;

import com.vastsoft.util.common.SingleClassConstant;

public class ReplyType extends SingleClassConstant
{
	protected ReplyType(int iCode, String strName)
	{
		super(iCode, strName);
	}

	public final static ReplyType REPLY_Q = new ReplyType(10, "问");
	public final static ReplyType REPLY_A = new ReplyType(11, "答");

	public static ReplyType parseCode(int iCode)
	{
		if (iCode == 10)
			return ReplyType.REPLY_Q;
		else if (iCode == 11)
			return ReplyType.REPLY_A;
		else
			return null;
	}
}
