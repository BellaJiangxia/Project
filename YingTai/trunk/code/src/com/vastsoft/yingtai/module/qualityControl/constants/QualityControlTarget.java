package com.vastsoft.yingtai.module.qualityControl.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**
 * 质控的对象
 * 
 * @author jben
 *
 */
public class QualityControlTarget extends SingleClassConstant {
	/** 影像 */
	public static final QualityControlTarget DICOM_IMG = new QualityControlTarget(10, "影像");
	/** 诊断报告 */
	public static final QualityControlTarget DIAGNOSIS_REPORT = new QualityControlTarget(20, "诊断报告");
	
	private static Map<Integer, QualityControlTarget> mapQualityControlClass = new LinkedHashMap<Integer, QualityControlTarget>();
	static{
		mapQualityControlClass.put(DICOM_IMG.getCode(), DICOM_IMG);
		mapQualityControlClass.put(DIAGNOSIS_REPORT.getCode(), DIAGNOSIS_REPORT);
	}
	protected QualityControlTarget(int iCode, String strName) {
		super(iCode, strName);
	}

	public static QualityControlTarget parseCode(int code) {
		return mapQualityControlClass.get(code);
	}

	public static List<QualityControlTarget> getAll() {
		return new ArrayList<>(mapQualityControlClass.values());
	}
}
