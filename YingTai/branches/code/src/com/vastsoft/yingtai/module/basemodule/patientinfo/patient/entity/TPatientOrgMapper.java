package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity;

import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity.SharePatientOrgMapper;

public class TPatientOrgMapper extends SharePatientOrgMapper {
	
	public TPatientOrgMapper() {
		super();
	}

	public TPatientOrgMapper(long patient_id, long org_id) {
		super();
		super.setPatient_id(patient_id);
		super.setOrg_id(org_id);
	}

	public TPatientOrgMapper(long org_id, String card_num) {
		super();
		super.setOrg_id(org_id);
		super.setCard_num(card_num);
	}
}
