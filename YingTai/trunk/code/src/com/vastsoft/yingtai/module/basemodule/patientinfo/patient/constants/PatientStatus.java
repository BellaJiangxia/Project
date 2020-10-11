package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.constants;

import com.vastsoft.util.common.SingleClassConstant;

public class PatientStatus extends SingleClassConstant {
	public static final PatientStatus NORMAL = new PatientStatus(10, "正常");
	
	protected PatientStatus(int iCode, String strName) {
		super(iCode, strName);
	}

}
