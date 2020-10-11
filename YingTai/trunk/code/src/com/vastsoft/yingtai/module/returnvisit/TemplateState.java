package com.vastsoft.yingtai.module.returnvisit;

import com.vastsoft.util.common.SingleClassConstant;

public class TemplateState extends SingleClassConstant
{

	protected TemplateState(int iCode, String strName)
	{
		super(iCode, strName);
	}

	public final static TemplateState INVALID = new TemplateState(-1, "作废");
	public final static TemplateState VALID = new TemplateState(1, "有效");

	public static TemplateState parseCode(int iCode)
	{
		if (iCode == -1)
			return TemplateState.INVALID;
		else if (iCode == 1)
			return TemplateState.VALID;
		else
			return null;
	}
}
