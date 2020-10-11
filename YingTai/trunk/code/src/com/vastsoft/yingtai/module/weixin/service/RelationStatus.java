package com.vastsoft.yingtai.module.weixin.service;

import com.vastsoft.util.common.SingleClassConstant;

public class RelationStatus extends SingleClassConstant
{
	protected RelationStatus(int iCode, String strName)
	{
		super(iCode, strName);
	}

	public final static RelationStatus Subscribed = new RelationStatus(1, "已关注");
	public final static RelationStatus UnSubscribed = new RelationStatus(2, "取消关注");
	public final static RelationStatus Binded = new RelationStatus(9, "已绑定");

}
