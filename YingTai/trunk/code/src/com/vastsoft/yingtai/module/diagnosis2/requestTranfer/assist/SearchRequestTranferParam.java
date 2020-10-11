package com.vastsoft.yingtai.module.diagnosis2.requestTranfer.assist;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class SearchRequestTranferParam extends AbstractSearchParam {
	private Long id;
	private Long old_request_id;
	private Long new_request_id;
	private Long create_org_id;
	private Long create_user_id;
	private Long new_diagnosis_org_id;
	private DiagnosisStatus2 new_request_status;
	private Gender new_patient_gender;
	private Long new_device_type_id;
	private String new_patient_name;
	private String new_case_his_num;
	
	
	public Long getId() {
		return id;
	}
	public Long getOld_request_id() {
		return old_request_id;
	}
	public Long getNew_request_id() {
		return new_request_id;
	}
	public Long getCreate_org_id() {
		return create_org_id;
	}
	public Long getCreate_user_id() {
		return create_user_id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOld_request_id(Long old_request_id) {
		this.old_request_id = old_request_id;
	}
	public void setNew_request_id(Long new_request_id) {
		this.new_request_id = new_request_id;
	}
	public void setCreate_org_id(Long create_org_id) {
		this.create_org_id = create_org_id;
	}
	public void setCreate_user_id(Long create_user_id) {
		this.create_user_id = create_user_id;
	}
	public DiagnosisStatus2 getNew_request_status() {
		return new_request_status;
	}
	public void setNew_request_status(DiagnosisStatus2 new_request_status) {
		this.new_request_status = new_request_status;
	}
	public Long getNew_diagnosis_org_id() {
		return new_diagnosis_org_id;
	}
	public void setNew_diagnosis_org_id(Long new_diagnosis_org_id) {
		this.new_diagnosis_org_id = new_diagnosis_org_id;
	}
	public Gender getNew_patient_gender() {
		return new_patient_gender;
	}
	public void setNew_patient_gender(Gender new_patient_gender) {
		this.new_patient_gender = new_patient_gender;
	}
	public Long getNew_device_type_id() {
		return new_device_type_id;
	}
	public void setNew_device_type_id(Long new_device_type_id) {
		this.new_device_type_id = new_device_type_id;
	}
	public String getNew_patient_name() {
		return new_patient_name;
	}
	public void setNew_patient_name(String new_patient_name) {
		this.new_patient_name = new_patient_name;
	}
	public String getNew_case_his_num() {
		return new_case_his_num;
	}
	public void setNew_case_his_num(String new_case_his_num) {
		this.new_case_his_num = new_case_his_num;
	}
}
