package com.vastsoft.yingtaidicom.search.constants;

import com.vastsoft.util.common.SingleClassConstant;

public class CaseHistoryState extends SingleClassConstant {
	public static final CaseHistoryState NORMAL=new CaseHistoryState(10, "正常");
	public static final CaseHistoryState REMOVED=new CaseHistoryState(99, "已删除/无效");
	
	protected CaseHistoryState(int iCode, String strName) {
		super(iCode, strName);
		// TODO Auto-generated constructor stub
	}

}
