package com.vastsoft.yingtai.module.returnvisit;

import com.vastsoft.util.common.SingleClassConstant;

public class BatchStatus extends SingleClassConstant
{
	protected BatchStatus(int iCode, String strName)
	{
		super(iCode, strName);
	}

	public final static BatchStatus WAIT_SEND = new BatchStatus(1, "待发送");
	public final static BatchStatus SENDED = new BatchStatus(2, "已发送");
	public final static BatchStatus HAS_NOT_SENDED = new BatchStatus(9, "有未发送成功");

	public static BatchStatus parseCode(int iCode)
	{
		if (iCode == 1)
			return BatchStatus.WAIT_SEND;
		else if (iCode == 2)
			return BatchStatus.SENDED;
		else if (iCode == 9)
			return BatchStatus.HAS_NOT_SENDED;
		else
			return null;
	}
}
