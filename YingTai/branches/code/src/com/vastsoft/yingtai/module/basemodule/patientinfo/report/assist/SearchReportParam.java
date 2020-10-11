package com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist;

import java.util.Date;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportFomType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReport;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class SearchReportParam extends AbstractSearchParam<FReport> {
	private Long id;
	private Long request_user_id;
	private Long request_org_id;
	private Long diagnosis_org_id;// 诊断机构ID
	private Long diagnosis_product_id;// 诊断产品ID
	private Long case_his_id;
	private Long request_id;// 诊断ID/申请ID
	private String case_his_num;
	private Long dicom_img_device_type_id; // 设备ID
	private Date start;
	private Date end;
	private Long publish_user_id;
	private String sick_key_words;
	private String patient_name;
	private Gender patient_gender;
	private String request_org_name;
	private String diagnosis_org_name;
	private String publish_report_org_name;
	private String publish_report_user_name;
	private String request_user_name;
	private ReportFomType f_o_m;
	private boolean with_dicom_series_list = true;// 报告是否携带影像序列信息
	private Integer viewed;//是否已被申请方查看 1=是，其他否 空为不过滤
	private ReportType type;
	private PatientDataSourceType source_type;

	public SearchReportParam(boolean with_dicom_series_list) {
		super();
		this.with_dicom_series_list = with_dicom_series_list;
	}

	public Long getRequest_user_id() {
		return request_user_id;
	}

	public Long getRequest_org_id() {
		return request_org_id;
	}

	public Long getDiagnosis_org_id() {
		return diagnosis_org_id;
	}

	public Long getDiagnosis_product_id() {
		return diagnosis_product_id;
	}

	public String getCase_his_num() {
		return case_his_num;
	}

	public Long getDicom_img_device_type_id() {
		return dicom_img_device_type_id;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public Long getPublish_user_id() {
		return publish_user_id;
	}

	public void setRequest_user_id(Long request_user_id) {
		this.request_user_id = request_user_id;
	}

	public void setRequest_org_id(Long request_org_id) {
		this.request_org_id = request_org_id;
	}

	public void setDiagnosis_org_id(Long diagnosis_org_id) {
		this.diagnosis_org_id = diagnosis_org_id;
	}

	public void setDiagnosis_product_id(Long diagnosis_product_id) {
		this.diagnosis_product_id = diagnosis_product_id;
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}

	public void setDicom_img_device_type_id(Long dicom_img_device_type_id) {
		this.dicom_img_device_type_id = dicom_img_device_type_id;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setPublish_user_id(Long publish_user_id) {
		this.publish_user_id = publish_user_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSick_key_words() {
		return sick_key_words;
	}

	public void setSick_key_words(String sick_key_words) {
		this.sick_key_words = sick_key_words;
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

	public Long getCase_his_id() {
		return case_his_id;
	}

	public void setCase_his_id(Long case_his_id) {
		this.case_his_id = case_his_id;
	}

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

//	public boolean isWith_report_msg_count() {
//		return with_report_msg_count;
//	}
//
//	public void setWith_report_msg_count(boolean with_report_msg_count) {
//		this.with_report_msg_count = with_report_msg_count;
//	}

	public String getRequest_org_name() {
		return request_org_name;
	}

	public void setRequest_org_name(String request_org_name) {
		this.request_org_name = request_org_name;
	}

	public String getDiagnosis_org_name() {
		return diagnosis_org_name;
	}

	public void setDiagnosis_org_name(String diagnosis_org_name) {
		this.diagnosis_org_name = diagnosis_org_name;
	}

	public String getPublish_report_org_name() {
		return publish_report_org_name;
	}

	public void setPublish_report_org_name(String publish_report_org_name) {
		this.publish_report_org_name = publish_report_org_name;
	}

	public String getRequest_user_name() {
		return request_user_name;
	}

	public void setRequest_user_name(String request_user_name) {
		this.request_user_name = request_user_name;
	}

	public ReportFomType getF_o_m() {
		return f_o_m;
	}

	public void setF_o_m(ReportFomType f_o_m) {
		this.f_o_m = f_o_m;
	}

	public String getPublish_report_user_name() {
		return publish_report_user_name;
	}

	public void setPublish_report_user_name(String publish_report_user_name) {
		this.publish_report_user_name = publish_report_user_name;
	}

	public boolean isWith_dicom_series_list() {
		return with_dicom_series_list;
	}

	public void setWith_dicom_series_list(boolean with_dicom_series_list) {
		this.with_dicom_series_list = with_dicom_series_list;
	}

	public Integer getViewed() {
		return viewed;
	}

	public void setViewed(Integer viewed) {
		this.viewed = viewed;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public PatientDataSourceType getSource_type() {
		return source_type;
	}

	public void setSource_type(PatientDataSourceType source_type) {
		this.source_type = source_type;
	}
}
