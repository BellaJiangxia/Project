package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.FPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;

public interface PatientMapper {

	public TPatient selectPatientByIdentityIdForUpdate(String identity_id);

	public void insertPatient(TPatient patient);

	public void updatePatient(TPatient patient);

	public void updatePatientByIdentityId(TPatient patient);

	public int selectPatientOrgMapperCount(Map<String, Object> mapArg);

	public List<FPatientOrgMapper> selectPatientOrgMapper(Map<String, Object> mapArg);

	public TPatient selectPatientByOrgIdAndCardNum(TPatientOrgMapper tPatientOrgMapper);

	public int selectPatientCount(Map<String, Object> mapArg);

	public List<TPatient> selectPatient(Map<String, Object> mapArg);

	public TPatient selectPatientByIdForUpdate(long id);

	public void insertPatientOrgMapper(TPatientOrgMapper patientOrgMapper);

	public TPatientOrgMapper selectPatientOrgMapperByOrgIdAndPatientIdForUpdate(TPatientOrgMapper tPatientOrgMapper);

	public TPatient selectPatientByMobileForUpdate(String mobile);

	public void updatePatientOrgMapper(TPatientOrgMapper old_patient_org_mapper);
	
}
