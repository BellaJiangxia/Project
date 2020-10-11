package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.action;

import java.util.List;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.FPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class PatientAction extends BaseYingTaiAction {
	private long patient_id;
	private String patient_name, identity_id;
	private Gender gender;
	private SplitPageUtil spu;
	
	/**通过身份证号查询病人*/
	public String queryPatientByIdentityId(){
		try {
			TPatient patient = PatientService.instance.queryPatientByIdentityId(identity_id, null);
			addElementToData("patient", patient);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 查询被机构的病人，检索 */
	public String queryThisOrgPatientOrgMapper() {
		try {
			List<FPatientOrgMapper> listPatientOrgMapper = PatientService.instance
					.searchOrgPatient(takePassport().getOrgId(), patient_name, identity_id, gender, spu);
			addElementToData("listPatientOrgMapper", listPatientOrgMapper);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 通过ID查询病人 */
	public String queryPatientById() {
		try {
			TPatient patient = PatientService.instance.queryPatientById(patient_id);
			addElementToData("patient", patient);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setPatient_id(long patient_id) {
		this.patient_id = patient_id;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = filterParam(patient_name);
	}

	public void setIdentity_id(String identity_id) {
		this.identity_id = filterParam(identity_id);
	}

	public void setGender(int gender) {
		this.gender = Gender.parseCode(gender);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

}
