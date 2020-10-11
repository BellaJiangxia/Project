package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class CaseHistoryStatus extends SingleClassConstant {
	private static final Map<Integer, CaseHistoryStatus> mapMedicalStatus = new HashMap<Integer, CaseHistoryStatus>();
	/**正常*/
	public static final CaseHistoryStatus NORMAL = new CaseHistoryStatus(1, "正常");// 可以修改
	/**已提交诊断*/
	public static final CaseHistoryStatus COMMITED = new CaseHistoryStatus(2, "已提交诊断");//
	/**已诊断*/
	public static final CaseHistoryStatus DIAGNOSISED = new CaseHistoryStatus(3, "已诊断");

	static {
		CaseHistoryStatus.mapMedicalStatus.put(NORMAL.getCode(), NORMAL);
		CaseHistoryStatus.mapMedicalStatus.put(COMMITED.getCode(), COMMITED);
		CaseHistoryStatus.mapMedicalStatus.put(DIAGNOSISED.getCode(), DIAGNOSISED);
	}

	protected CaseHistoryStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static CaseHistoryStatus parseCode(int iCode) {
		return CaseHistoryStatus.mapMedicalStatus.get(iCode);
	}

	public static List<CaseHistoryStatus> getAllMedicalStatus() {
		return new ArrayList<CaseHistoryStatus>(mapMedicalStatus.values());
	}

}
