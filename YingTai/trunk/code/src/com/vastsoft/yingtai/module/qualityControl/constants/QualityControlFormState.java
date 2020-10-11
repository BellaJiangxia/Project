package com.vastsoft.yingtai.module.qualityControl.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class QualityControlFormState extends SingleClassConstant {
	/**正常*/
	public static final QualityControlFormState NORMAL = new QualityControlFormState(10, "正常");
	/**禁用*/
	public static final QualityControlFormState DISABLED = new QualityControlFormState(20, "禁用");
//	/**已删除*/
//	public static final QualityControlFormState FINISH = new QualityControlFormState(-1, "已删除");
	private static Map<Integer, QualityControlFormState> mapQualityControlFormState = new LinkedHashMap<>();
	static{
		mapQualityControlFormState.put(NORMAL.getCode(), NORMAL);
		mapQualityControlFormState.put(DISABLED.getCode(), DISABLED);
//		mapQualityControlFormState.put(FINISH.getCode(), FINISH);
	}
	protected QualityControlFormState(int iCode, String strName) {
		super(iCode, strName);
	}

	public static QualityControlFormState parseCode(int code) {
		return mapQualityControlFormState.get(code);
	}

	public static List<QualityControlFormState> getAll() {
		return new ArrayList<QualityControlFormState>(mapQualityControlFormState.values());
	}

}
