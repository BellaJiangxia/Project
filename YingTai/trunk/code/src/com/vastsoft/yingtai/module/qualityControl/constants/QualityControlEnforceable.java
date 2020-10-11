package com.vastsoft.yingtai.module.qualityControl.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**
 * 质控的填写的强制性
 * 
 * @author jben
 *
 */
public class QualityControlEnforceable extends SingleClassConstant {
	/** 强制 */
	public static final QualityControlEnforceable FORCE = new QualityControlEnforceable(10, "强制");
	/** 非强制 */
	public static final QualityControlEnforceable NOT_FORCE = new QualityControlEnforceable(20, "非强制");
	
	private static Map<Integer, QualityControlEnforceable> mapQualityControlEnforceable = new LinkedHashMap<Integer, QualityControlEnforceable>();
	static{
		mapQualityControlEnforceable.put(FORCE.getCode(), FORCE);
		mapQualityControlEnforceable.put(NOT_FORCE.getCode(), NOT_FORCE);
	}
	protected QualityControlEnforceable(int iCode, String strName) {
		super(iCode, strName);
	}

	public static QualityControlEnforceable parseCode(int code) {
		return mapQualityControlEnforceable.get(code);
	}

	public static List<QualityControlEnforceable> getAll() {
		return new ArrayList<QualityControlEnforceable>(mapQualityControlEnforceable.values());
	}
}
