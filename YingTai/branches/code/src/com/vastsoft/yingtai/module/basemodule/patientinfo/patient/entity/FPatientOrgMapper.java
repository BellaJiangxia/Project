package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.user.constants.Gender;

public final class FPatientOrgMapper extends TPatientOrgMapper {
	private String org_name;
	private String patient_name;
	private String patient_identity_id;
	private Date patient_birthday;
	private int patient_gender;
	private String org_code;
	private long org_logo_file_id;
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public long getOrg_logo_file_id() {
		return org_logo_file_id;
	}
	public void setOrg_logo_file_id(long org_logo_file_id) {
		this.org_logo_file_id = org_logo_file_id;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public String getPatient_identity_id() {
		return patient_identity_id;
	}
	public void setPatient_identity_id(String patient_identity_id) {
		this.patient_identity_id = patient_identity_id;
	}
	public Date getPatient_birthday() {
		return patient_birthday;
	}
	public int getPatientAge() {
		return DateTools.getAgeByBirthday(patient_birthday);
	}
	public void setPatient_birthday(Date patient_birthday) {
		this.patient_birthday = patient_birthday;
	}
	public int getPatient_gender() {
		return patient_gender;
	}
	public String getPatient_genderStr() {
		Gender g= Gender.parseCode(patient_gender);
		return g==null?"":g.getName();
	}
	public void setPatient_gender(int patient_gender) {
		this.patient_gender = patient_gender;
	}
}
