package com.vastsoft.yingtai.module.diagnosis2.assist;

import java.util.Date;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class DiagnosisSearchParams2 extends AbstractSearchParam {
	private Long id = null;
	private String patient_name = null;
	private String sick_key_words = null;
	private Gender patient_gender = null;
	private String case_his_num = null;
	private Long case_his_id = null;
	private Long request_org_id = null;
	private Long request_user_id = null;
	private Long verify_user_id = null;
	private String dicom_img_mark_char = null;
	private Long diagnosis_org_id = null;
	private Long dicom_img_device_type_id = null;
//	private Long dicom_img_part_type_id = null;
	private Long diagnosis_product_id = null;
	private Long publish_report_org_id = null;
	private Date start = null;
	private Date end = null;
	private DiagnosisStatus2 status = null;
	private Long dicom_img_id = null;
	private Long curr_user_id;
	private boolean request_tranfer_flag=false;//是否包含流转的申请 true=包含 false=不包含

	public DiagnosisSearchParams2() {
	}

	public Long getId() {
		return id;
	}

	public Long getRequest_org_id() {
		return request_org_id;
	}

	public Long getRequest_user_id() {
		return request_user_id;
	}

	public Long getVerify_user_id() {
		return verify_user_id;
	}

	public Long getDiagnosis_org_id() {
		return diagnosis_org_id;
	}

	public Long getDiagnosis_product_id() {
		return diagnosis_product_id;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public DiagnosisStatus2 getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRequest_org_id(Long request_org_id) {
		this.request_org_id = request_org_id;
	}

	public void setRequest_user_id(Long request_user_id) {
		this.request_user_id = request_user_id;
	}

	public void setVerify_user_id(Long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public void setDiagnosis_org_id(Long diagnosis_org_id) {
		this.diagnosis_org_id = diagnosis_org_id;
	}

	public void setDiagnosis_product_id(Long diagnosis_product_id) {
		this.diagnosis_product_id = diagnosis_product_id;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setStatus(DiagnosisStatus2 status) {
		this.status = status;
	}

	public String getSick_key_words() {
		return sick_key_words;
	}

	public void setSick_key_words(String sick_key_words) {
		this.sick_key_words = sick_key_words;
	}

	public Long getDicom_img_id() {
		return dicom_img_id;
	}

	public void setDicom_img_id(Long dicom_img_id) {
		this.dicom_img_id = dicom_img_id;
	}

	public String getPatient_name() {
		return patient_name;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}

	public Gender getPatient_gender() {
		return patient_gender;
	}

	public void setPatient_gender(Gender patient_gender) {
		this.patient_gender = patient_gender;
	}

	public String getCase_his_num() {
		return case_his_num;
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}

	public Long getCase_his_id() {
		return case_his_id;
	}

	public void setCase_his_id(Long case_his_id) {
		this.case_his_id = case_his_id;
	}

	public String getDicom_img_mark_char() {
		return dicom_img_mark_char;
	}

	public void setDicom_img_mark_char(String dicom_img_mark_char) {
		this.dicom_img_mark_char = dicom_img_mark_char;
	}

	public Long getDicom_img_device_type_id() {
		return dicom_img_device_type_id;
	}

	public void setDicom_img_device_type_id(Long dicom_img_device_type_id) {
		this.dicom_img_device_type_id = dicom_img_device_type_id;
	}

//	public Long getDicom_img_part_type_id() {
//		return dicom_img_part_type_id;
//	}
//
//	public void setDicom_img_part_type_id(Long dicom_img_part_type_id) {
//		this.dicom_img_part_type_id = dicom_img_part_type_id;
//	}

	public Long getPublish_report_org_id() {
		return publish_report_org_id;
	}

	public void setPublish_report_org_id(Long publish_report_org_id) {
		this.publish_report_org_id = publish_report_org_id;
	}

	public Long getCurr_user_id() {
		return curr_user_id;
	}

	public void setCurr_user_id(Long curr_user_id) {
		this.curr_user_id = curr_user_id;
	}

	public boolean getRequest_tranfer_flag() {
		return request_tranfer_flag;
	}

	public void setRequest_tranfer_flag(boolean request_tranfer_flag) {
		this.request_tranfer_flag = request_tranfer_flag;
	}

}
