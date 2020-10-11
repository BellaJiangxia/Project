package com.vastsoft.yingtai.module.stat.entity;

import com.vastsoft.yingtai.module.financel.entity.TFinanceRecord;

public class FFinanceRecord extends TFinanceRecord {
	private String request_org_name;
	private String diagnosis_org_name;
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
}
