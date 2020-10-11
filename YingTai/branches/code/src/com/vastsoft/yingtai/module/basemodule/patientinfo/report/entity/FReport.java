package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import java.util.List;

import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FSeries;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosisHandle;
import com.vastsoft.yingtai.module.sys.entity.TFile;

public class FReport extends TReport {
	private long request_org_id;
	private String request_org_name;
	private long request_user_id;
	private String request_user_name;
	private long diagnosis_org_id;
	private String diagnosis_org_name;
	private String device_type_name;
	private String publish_report_org_name;
	private String about_case_ids;
	private String print_user_name;
	private String publish_user_name;
	private String accept_user_name;
	private long publish_user_sign_file_id;

	private String duty_org_name;// 责任机构名
	private String duty_report_user_name;// 责任报告员名
	private String duty_verify_user_name;// 责任审核员名
	private long duty_verify_user_sign_file_id;// 责任审核员签名图片ID
	private long duty_org_logo_file_id;//责任机构logo文件ID

	private List<TFile> eaf_file_list;
	private List<FCaseHistory> about_case_list;
	private List<FDiagnosisHandle> listDiagnosisHandle;
	private List<FSeries> listDicomImgSeries;
	private FDiagnosis request;

	public FReport() {
		super();
	}

	private int report_msg_count = 0;

	private int newMsgCount = 0;// 新诊断消息数量

	public String getRequest_org_name() {
		return request_org_name;
	}

	public String getDevice_type_name() {
		return device_type_name;
	}

	public void setRequest_org_name(String request_org_name) {
		this.request_org_name = request_org_name;
	}

	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}

	public String getPublish_report_org_name() {
		return publish_report_org_name;
	}

	public void setPublish_report_org_name(String publish_report_org_name) {
		this.publish_report_org_name = publish_report_org_name;
	}

	public String getAbout_case_ids() {
		return about_case_ids;
	}

	public void setAbout_case_ids(String about_case_ids) {
		this.about_case_ids = about_case_ids;
	}

	public List<TFile> getEaf_file_list() {
		return eaf_file_list;
	}

	public void setEaf_file_list(List<TFile> eaf_file_list) {
		this.eaf_file_list = eaf_file_list;
	}

	public List<FCaseHistory> getAbout_case_list() {
		return about_case_list;
	}

	public void setAbout_case_list(List<FCaseHistory> about_case_list) {
		this.about_case_list = about_case_list;
	}

	public List<FDiagnosisHandle> getListDiagnosisHandle() {
		return listDiagnosisHandle;
	}

	public void setListDiagnosisHandle(List<FDiagnosisHandle> listDiagnosisHandle) {
		this.listDiagnosisHandle = listDiagnosisHandle;
	}

	public long getRequest_org_id() {
		return request_org_id;
	}

	public void setRequest_org_id(long request_org_id) {
		this.request_org_id = request_org_id;
	}

	public long getDiagnosis_org_id() {
		return diagnosis_org_id;
	}

	public void setDiagnosis_org_id(long diagnosis_org_id) {
		this.diagnosis_org_id = diagnosis_org_id;
	}

	public String getDiagnosis_org_name() {
		return diagnosis_org_name;
	}

	public void setDiagnosis_org_name(String diagnosis_org_name) {
		this.diagnosis_org_name = diagnosis_org_name;
	}

	public String getPrint_user_name() {
		return print_user_name;
	}

	public void setPrint_user_name(String print_user_name) {
		this.print_user_name = print_user_name;
	}

	public String getPublish_user_name() {
		return publish_user_name;
	}

	public void setPublish_user_name(String publish_user_name) {
		this.publish_user_name = publish_user_name;
	}

	public long getRequest_user_id() {
		return request_user_id;
	}

	public void setRequest_user_id(long request_user_id) {
		this.request_user_id = request_user_id;
	}

	public String getRequest_user_name() {
		return request_user_name;
	}

	public void setRequest_user_name(String request_user_name) {
		this.request_user_name = request_user_name;
	}

	public int getNewMsgCount() {
		return newMsgCount;
	}

	public void setNewMsgCount(int newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	public long getPublish_user_sign_file_id() {
		return publish_user_sign_file_id;
	}

	public void setPublish_user_sign_file_id(long publish_user_sign_file_id) {
		this.publish_user_sign_file_id = publish_user_sign_file_id;
	}

	public String getAccept_user_name() {
		return accept_user_name;
	}

	public void setAccept_user_name(String accept_user_name) {
		this.accept_user_name = accept_user_name;
	}

	public int getReport_msg_count() {
		return report_msg_count;
	}

	public void setReport_msg_count(int report_msg_count) {
		this.report_msg_count = report_msg_count;
	}

	public List<FSeries> getListDicomImgSeries() {
		return listDicomImgSeries;
	}

	public void setListDicomImgSeries(List<FSeries> listDicomImgSeries) {
		this.listDicomImgSeries = listDicomImgSeries;
	}

	public String getDuty_org_name() {
		return duty_org_name;
	}

	public void setDuty_org_name(String duty_org_name) {
		this.duty_org_name = duty_org_name;
	}

	public String getDuty_report_user_name() {
		return duty_report_user_name;
	}

	public void setDuty_report_user_name(String duty_report_user_name) {
		this.duty_report_user_name = duty_report_user_name;
	}

	public String getDuty_verify_user_name() {
		return duty_verify_user_name;
	}

	public void setDuty_verify_user_name(String duty_verify_user_name) {
		this.duty_verify_user_name = duty_verify_user_name;
	}

	public long getDuty_verify_user_sign_file_id() {
		return duty_verify_user_sign_file_id;
	}

	public void setDuty_verify_user_sign_file_id(long duty_verify_user_sign_file_id) {
		this.duty_verify_user_sign_file_id = duty_verify_user_sign_file_id;
	}

	public long getDuty_org_logo_file_id() {
		return duty_org_logo_file_id;
	}

	public void setDuty_org_logo_file_id(long duty_org_logo_file_id) {
		this.duty_org_logo_file_id = duty_org_logo_file_id;
	}

	public FDiagnosis getRequest() {
		return request;
	}

	public void setRequest(FDiagnosis request) {
		this.request = request;
	}

}
