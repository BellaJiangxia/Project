package com.vastsoft.yingtaidicom.search.constants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class CaseHistoryType extends SingleClassConstant{
	/**门诊病例*/
	public static final CaseHistoryType MENZHEN = new CaseHistoryType(10, "门诊病例");
	/**住院病例*/
	public static final CaseHistoryType HOSPITALIZED = new CaseHistoryType(20, "住院病例");
	/**远诊病例*/
	public static final CaseHistoryType YUANZHEN = new CaseHistoryType(30, "远诊病例");
	/**其他病例*/
	public static final CaseHistoryType OTHER = new CaseHistoryType(99, "其他病例");
	
	private static Map<Integer, CaseHistoryType> mapCaseHistoryType=new LinkedHashMap<Integer, CaseHistoryType>();
	static{
		mapCaseHistoryType.put(MENZHEN.getCode(), MENZHEN);
		mapCaseHistoryType.put(HOSPITALIZED.getCode(), HOSPITALIZED);
		mapCaseHistoryType.put(YUANZHEN.getCode(), YUANZHEN);
		mapCaseHistoryType.put(OTHER.getCode(), OTHER);
	}
	protected CaseHistoryType(int iCode, String strName) {
		super(iCode, strName);
	}
	
	public static CaseHistoryType parseCode(int code){
		return mapCaseHistoryType.get(code);
	}
}