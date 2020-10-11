package com.vastsoft.yingtai.module.diagnosis2.contants;

import java.util.HashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class RequestClass extends SingleClassConstant {
	/** 诊断申请 */
	public static final RequestClass DIAGNOSIS = new RequestClass(10, "诊断申请");
	/** 咨询申请 */
	public static final RequestClass CONSULT = new RequestClass(20, "咨询申请");
	
	private static Map<Integer, RequestClass> mapRequestClass = new HashMap<Integer, RequestClass>();
	static{
		mapRequestClass.put(DIAGNOSIS.getCode(), DIAGNOSIS);
		mapRequestClass.put(CONSULT.getCode(), CONSULT);
	}
	protected RequestClass(int code, String name) {
		super(code, name);
	}

	public static RequestClass parseCode(int code) {
		return mapRequestClass.get(code);
	}
}
