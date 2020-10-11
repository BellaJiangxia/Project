package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import java.util.Date;

public class FModifyReportRequest extends TModifyReportRequest {
	private String case_his_num;
	private String request_org_name;
	private String diagnosis_org_name;
	private String device_name;
	private Date report_time;

	public String getRequest_org_name() {
		return request_org_name;
	}

	public String getDevice_name() {
		return device_name;
	}

	public Date getReport_time() {
		return report_time;
	}
	public void setRequest_org_name(String request_org_name) {
		this.request_org_name = request_org_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public void setReport_time(Date report_time) {
		this.report_time = report_time;
	}

	public String getDiagnosis_org_name() {
		return diagnosis_org_name;
	}

	public void setDiagnosis_org_name(String diagnosis_org_name) {
		this.diagnosis_org_name = diagnosis_org_name;
	}

	public String getCase_his_num() {
		return case_his_num;
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}
	
}
