package com.vastsoft.yingtai.module.qualityControl.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class QualityControlFormAnswerStatus extends SingleClassConstant {
	/** 初始状态 */
	public static final QualityControlFormAnswerStatus WRITING = new QualityControlFormAnswerStatus(20, "填写中");
	/** 完成 */
	public static final QualityControlFormAnswerStatus FINISH = new QualityControlFormAnswerStatus(30, "完成");

	private static Map<Integer, QualityControlFormAnswerStatus> mapQualityControlFormAnswerStatus = new HashMap<Integer, QualityControlFormAnswerStatus>();

	static {
		mapQualityControlFormAnswerStatus.put(WRITING.getCode(), WRITING);
		mapQualityControlFormAnswerStatus.put(FINISH.getCode(), FINISH);
	}

	protected QualityControlFormAnswerStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static QualityControlFormAnswerStatus parseCode(int code) {
		return mapQualityControlFormAnswerStatus.get(code);
	}

	public static List<QualityControlFormAnswerStatus> getAll() {
		return new ArrayList<QualityControlFormAnswerStatus>(mapQualityControlFormAnswerStatus.values());
	}

}
