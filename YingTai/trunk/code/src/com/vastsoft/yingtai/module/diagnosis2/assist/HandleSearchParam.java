package com.vastsoft.yingtai.module.diagnosis2.assist;

import java.util.Date;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class HandleSearchParam extends AbstractSearchParam {
	private Long request_org_id;
	private Long dicom_img_device_type_id;
	private Gender patient_gender;
	private String patient_name;
	private String case_his_num;
	private Long diagnosis_id;
	private Long curr_user_id;
	private Long next_user_id;
	private Long org_id;
	private Date start;
	private Date end;
	private DiagnosisHandleStatus2 status;
	private DiagnosisHandleStatus2 [] contain_status;
	private DiagnosisHandleStatus2 [] exclude_status;
	private Long [] exclude_curr_user_ids;
	
	public Long [] getExclude_curr_user_ids(){
		return this.exclude_curr_user_ids;
	}
	public void setExclude_curr_user_ids(Long ... exclude_curr_user_ids){
		this.exclude_curr_user_ids = exclude_curr_user_ids;
	}
	public Long getRequest_org_id() {
		return request_org_id;
	}
	public Long getDiagnosis_id() {
		return diagnosis_id;
	}
	public Long getCurr_user_id() {
		return curr_user_id;
	}
	public Long getNext_user_id() {
		return next_user_id;
	}
	public Long getOrg_id() {
		return org_id;
	}
	public Date getStart() {
		return start;
	}
	public Date getEnd() {
		return end;
	}
	public DiagnosisHandleStatus2 getStatus() {
		return status;
	}
	public void setRequest_org_id(Long request_org_id) {
		this.request_org_id = request_org_id;
	}
	public void setDiagnosis_id(Long diagnosis_id) {
		this.diagnosis_id = diagnosis_id;
	}
	public void setCurr_user_id(Long curr_user_id) {
		this.curr_user_id = curr_user_id;
	}
	public void setNext_user_id(Long next_user_id) {
		this.next_user_id = next_user_id;
	}
	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public void setStatus(DiagnosisHandleStatus2 status) {
		this.status = status;
	}
	public Long getDicom_img_device_type_id() {
		return dicom_img_device_type_id;
	}
	public void setDicom_img_device_type_id(Long dicom_img_device_type_id) {
		this.dicom_img_device_type_id = dicom_img_device_type_id;
	}
	public Gender getPatient_gender() {
		return patient_gender;
	}
	public void setPatient_gender(Gender patient_gender) {
		this.patient_gender = patient_gender;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public String getCase_his_num() {
		return case_his_num;
	}
	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}
	public DiagnosisHandleStatus2 [] getContain_status() {
		return contain_status;
	}
	public void setContain_status(DiagnosisHandleStatus2... contain_status) {
		this.contain_status = contain_status;
	}
	public DiagnosisHandleStatus2 [] getExclude_status() {
		return exclude_status;
	}
	public void setExclude_status(DiagnosisHandleStatus2... exclude_status) {
		this.exclude_status = exclude_status;
	}
}
