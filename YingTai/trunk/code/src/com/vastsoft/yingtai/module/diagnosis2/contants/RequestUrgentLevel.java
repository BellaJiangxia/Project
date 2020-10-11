package com.vastsoft.yingtai.module.diagnosis2.contants;

import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**
 * 申请紧急程度，code值请以紧急程度从低到高排列，越紧急code越大，牵涉到排序问题
 * 
 * @author jben
 *
 */
public class RequestUrgentLevel extends SingleClassConstant {
	/** 普通 */
	public static final RequestUrgentLevel NORMAL = new RequestUrgentLevel(10, "普通");
	/** 紧急 */
	public static final RequestUrgentLevel EMERGENCY = new RequestUrgentLevel(20, "紧急");

	protected RequestUrgentLevel(int code, String name) {
		super(code, name);
	}

	public static RequestUrgentLevel parseCode(int code) {
		Map<Integer, RequestUrgentLevel> listRequestUrgentLevel = SingleClassConstant.getMap(RequestUrgentLevel.class);
		return listRequestUrgentLevel.get(code);
	}

	public static List<RequestUrgentLevel> getAll() {
		return SingleClassConstant.getAll(RequestUrgentLevel.class);
	}
}
