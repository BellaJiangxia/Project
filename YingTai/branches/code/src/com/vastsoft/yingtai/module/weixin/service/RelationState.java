package com.vastsoft.yingtai.module.weixin.service;

import com.vastsoft.util.common.SingleClassConstant;

public class RelationState extends SingleClassConstant
{
	protected RelationState(int iCode, String strName)
	{
		super(iCode, strName);
	}

	public final static RelationState VALID = new RelationState(1, "有效");
	public final static RelationState INVALID = new RelationState(-1, "无效");

}
