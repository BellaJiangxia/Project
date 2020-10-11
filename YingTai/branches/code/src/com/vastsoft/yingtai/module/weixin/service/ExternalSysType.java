package com.vastsoft.yingtai.module.weixin.service;

import com.vastsoft.util.common.SingleClassConstant;

public class ExternalSysType extends SingleClassConstant
{
	protected ExternalSysType(int iCode, String strName)
	{
		super(iCode, strName);
	}

	public final static ExternalSysType Weixin = new ExternalSysType(1, "微信");
	public final static ExternalSysType QQ = new ExternalSysType(2, "腾讯QQ");
	public final static ExternalSysType Alipay = new ExternalSysType(2, "支付宝");

}
