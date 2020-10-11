package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class FReportShare extends TReportShare {
	private long case_his_id;
	private String case_his_num;
	private String case_symptom;
	private Date patient_birthday;
	private int patient_gender;
	private String share_org_name;
	private String share_user_name;
	
	public String getShare_org_name() {
		return share_org_name;
	}
	public void setShare_org_name(String share_org_name) {
		this.share_org_name = share_org_name;
	}
	public String getPatient_genderStr() {
		Gender ug=Gender.parseCode(patient_gender);
		return ug==null?"":ug.getName();
	}
	public String getShare_user_name() {
		return share_user_name;
	}
	public void setShare_user_name(String share_user_name) {
		this.share_user_name = share_user_name;
	}
	public long getCase_his_id() {
		return case_his_id;
	}
	public String getCase_his_num() {
		return case_his_num;
	}
	public String getCase_symptom() {
		return case_symptom;
	}
	public int getPatient_age(){
		return DateTools.getAgeByBirthday(patient_birthday);
	}
	public Date getPatient_birthday() {
		return patient_birthday;
	}
	public int getPatient_gender() {
		return patient_gender;
	}
	public void setCase_his_id(long case_his_id) {
		this.case_his_id = case_his_id;
	}
	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}
	public void setCase_symptom(String case_symptom) {
		this.case_symptom = case_symptom;
	}
	public void setPatient_birthday(Date patient_birthday) {
		this.patient_birthday = patient_birthday;
	}
	public void setPatient_gender(int patient_gender) {
		this.patient_gender = patient_gender;
	}
	
	
}
