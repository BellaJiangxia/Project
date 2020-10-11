package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.assist;

import java.util.Date;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class SearchCaseHistoryParam extends AbstractSearchParam{
	private Long id;
	private long[] containIds;
	private long[] excludeIds;
	private Long org_id;
	private Long patient_id;
	private String patient_name;
	private String patient_identity_id;
	private Gender patient_gender;
	private String case_his_num;
	private String hospitalized_num;
	private String symptom;
	private CaseHistoryStatus status;
	private Date createStart;
	private Date createEnd;
	private String reception_section;
	private CaseHistoryType type;
	private PatientDataSourceType source_type;
	
	private boolean needSortByEnnerTime;
	
	public long[] getContainIds() {
		return containIds;
	}
	public long[] getExcludeIds() {
		return excludeIds;
	}
	public Long getOrg_id() {
		return org_id;
	}
	public Long getPatient_id() {
		return patient_id;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public String getPatient_identity_id() {
		return patient_identity_id;
	}
	public Gender getPatient_gender() {
		return patient_gender;
	}
	public String getHospitalized_num() {
		return hospitalized_num;
	}
	public String getSymptom() {
		return symptom;
	}
	public CaseHistoryStatus getStatus() {
		return status;
	}
	public Date getCreateStart() {
		return createStart;
	}
	public Date getCreateEnd() {
		return createEnd;
	}
	public String getReception_section() {
		return reception_section;
	}
	public void setContainIds(long[] containIds) {
		this.containIds = containIds;
	}
	public void setExcludeIds(long[] excludeIds) {
		this.excludeIds = excludeIds;
	}
	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}
	public void setPatient_id(Long patient_id) {
		this.patient_id = patient_id;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public void setPatient_identity_id(String patient_identity_id) {
		this.patient_identity_id = patient_identity_id;
	}
	public void setPatient_gender(Gender patient_gender) {
		this.patient_gender = patient_gender;
	}
	public void setHospitalized_num(String hospitalized_num) {
		this.hospitalized_num = hospitalized_num;
	}
	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}
	public void setStatus(CaseHistoryStatus status) {
		this.status = status;
	}
	public void setCreateStart(Date createStart) {
		this.createStart = createStart;
	}
	public void setCreateEnd(Date createEnd) {
		this.createEnd = createEnd;
	}
	public void setReception_section(String reception_section) {
		this.reception_section = reception_section;
	}
	public String getCase_his_num() {
		return case_his_num;
	}
	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CaseHistoryType getType() {
		return type;
	}
	public void setType(CaseHistoryType type) {
		this.type = type;
	}
	public PatientDataSourceType getSource_type() {
		return source_type;
	}
	public void setSource_type(PatientDataSourceType source_type) {
		this.source_type = source_type;
	}
	public boolean isNeedSortByEnnerTime() {
		return needSortByEnnerTime;
	}
	public void setNeedSortByEnnerTime(boolean needSortByEnnerTime) {
		this.needSortByEnnerTime = needSortByEnnerTime;
	}
}
