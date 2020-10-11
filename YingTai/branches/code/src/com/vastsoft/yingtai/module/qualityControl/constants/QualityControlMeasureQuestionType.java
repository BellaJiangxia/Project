package com.vastsoft.yingtai.module.qualityControl.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class QualityControlMeasureQuestionType extends SingleClassConstant {
	/**打分*/
	public static final QualityControlMeasureQuestionType SCORING = new  QualityControlMeasureQuestionType(10, "打分");
	/**文本*/
	public static final QualityControlMeasureQuestionType TEXT = new  QualityControlMeasureQuestionType(20, "文本");
	private static Map<Integer, QualityControlMeasureQuestionType> mapQualityControlMeasureQuestionType = new LinkedHashMap<Integer, QualityControlMeasureQuestionType>();
	
	static{
		mapQualityControlMeasureQuestionType.put(SCORING.getCode(), SCORING);
		mapQualityControlMeasureQuestionType.put(TEXT.getCode(), TEXT);
	}
	
	protected QualityControlMeasureQuestionType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static QualityControlMeasureQuestionType parseCode(int code) {
		return mapQualityControlMeasureQuestionType.get(code);
	}

	public static List<QualityControlMeasureQuestionType> getAll() {
		return new ArrayList<QualityControlMeasureQuestionType>(mapQualityControlMeasureQuestionType.values());
	}

}
