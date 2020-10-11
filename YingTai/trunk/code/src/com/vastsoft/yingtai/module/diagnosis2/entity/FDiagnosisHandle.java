package com.vastsoft.yingtai.module.diagnosis2.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class FDiagnosisHandle extends TDiagnosisHandle {
	private String case_his_id;
	private String case_his_num;
	private String patient_name;
	private Date patient_birthday;
	private int patient_gender;
	private long device_type_id;
	private String device_type_name;
//	private long part_type_id;
//	private String part_type_name;
	private String request_org_name;// 申请机构名
	private String curr_user_name;// 移交人名
	private String next_user_name;// 转交目标人名

	public String getDevice_type_name() {
		return device_type_name;
	}

//	public String getPart_type_name() {
//		return part_type_name;
//	}

	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}

//	public void setPart_type_name(String part_type_name) {
//		this.part_type_name = part_type_name;
//	}

	public String getRequest_org_name() {
		return request_org_name;
	}

	public void setRequest_org_name(String request_org_name) {
		this.request_org_name = request_org_name;
	}

	public String getCurr_user_name() {
		return curr_user_name;
	}

	public void setCurr_user_name(String curr_user_name) {
		this.curr_user_name = curr_user_name;
	}

	public String getNext_user_name() {
		return next_user_name;
	}

	public void setNext_user_name(String next_user_name) {
		this.next_user_name = next_user_name;
	}

	public String getCase_his_id() {
		return case_his_id;
	}

	public void setCase_his_id(String case_his_id) {
		this.case_his_id = case_his_id;
	}

	public String getCase_his_num() {
		return case_his_num;
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}

	public String getPatient_name() {
		return patient_name;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}

	public Date getPatient_birthday() {
		return patient_birthday;
	}

	public int getPatient_age() {
		return DateTools.getAgeByBirthday(patient_birthday);
	}

	public void setPatient_birthday(Date patient_birthday) {
		this.patient_birthday = patient_birthday;
	}

	public int getPatient_gender() {
		return patient_gender;
	}

	public String getPatient_genderStr() {
		Gender g = Gender.parseCode(patient_gender);
		return g == null ? "" : g.getName();
	}

	public void setPatient_gender(int patient_gender) {
		this.patient_gender = patient_gender;
	}

//	public long getPart_type_id() {
//		return part_type_id;
//	}
//
//	public void setPart_type_id(long part_type_id) {
//		this.part_type_id = part_type_id;
//	}

	public long getDevice_type_id() {
		return device_type_id;
	}

	public void setDevice_type_id(long device_type_id) {
		this.device_type_id = device_type_id;
	}

}
