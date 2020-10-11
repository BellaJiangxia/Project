package com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity;

import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReport;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;

public class FRequestTranfer extends TRequestTranfer{
	private FDiagnosis old_request;
	private FDiagnosis new_request;
	private FReport new_request_report;
	private String create_user_name;
	private String create_org_name;
	
	public FDiagnosis getOld_request() {
		return old_request;
	}
	public FDiagnosis getNew_request() {
		return new_request;
	}
	public String getCreate_user_name() {
		return create_user_name;
	}
	public String getCreate_org_name() {
		return create_org_name;
	}
	public void setOld_request(FDiagnosis old_request) {
		this.old_request = old_request;
	}
	public void setNew_request(FDiagnosis new_request) {
		this.new_request = new_request;
	}
	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}
	public void setCreate_org_name(String create_org_name) {
		this.create_org_name = create_org_name;
	}
	public FReport getNew_request_report() {
		return new_request_report;
	}
	public void setNew_request_report(FReport new_request_report) {
		this.new_request_report = new_request_report;
	}
	
}
