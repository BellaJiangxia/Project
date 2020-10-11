package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class PatientDataSourceType extends SingleClassConstant {
	public static final PatientDataSourceType YUANZHEN_SYS = new PatientDataSourceType(10, "远诊系统");

	public static final PatientDataSourceType REMOTE_HIS_SYS = new PatientDataSourceType(20, "HIS系统");

	public static final PatientDataSourceType REMOTE_PASC_SYS = new PatientDataSourceType(30, "PASC系统");

	public static final PatientDataSourceType REMOTE_RIS_SYS = new PatientDataSourceType(40, "RIS系统");

	public static final PatientDataSourceType REMOTE_EMR_SYS = new PatientDataSourceType(50, "EMR系统");

//	public static final PatientDataSourceType OTHER = new PatientDataSourceType(99, "其他来源");

	private static Map<Integer, PatientDataSourceType> mapCaseHistorySourceType = new LinkedHashMap<Integer, PatientDataSourceType>();
	static {
		mapCaseHistorySourceType.put(YUANZHEN_SYS.getCode(), YUANZHEN_SYS);
		mapCaseHistorySourceType.put(REMOTE_HIS_SYS.getCode(), REMOTE_HIS_SYS);
		mapCaseHistorySourceType.put(REMOTE_PASC_SYS.getCode(), REMOTE_PASC_SYS);
		mapCaseHistorySourceType.put(REMOTE_RIS_SYS.getCode(), REMOTE_RIS_SYS);
		mapCaseHistorySourceType.put(REMOTE_EMR_SYS.getCode(), REMOTE_EMR_SYS);
//		mapCaseHistorySourceType.put(OTHER.getCode(), OTHER);
	}

	protected PatientDataSourceType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static PatientDataSourceType parseCode(int code) {
		return mapCaseHistorySourceType.get(code);
	}

}
