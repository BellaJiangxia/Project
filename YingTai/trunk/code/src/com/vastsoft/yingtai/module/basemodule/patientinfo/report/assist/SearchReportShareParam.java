package com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportShareStatus;

public class SearchReportShareParam extends AbstractSearchParam {
	private Long id;
	private Long report_id;
	private Long share_org_id;
	private Long share_user_id;
	private String case_his_num;
	private Long close_share_user_id;
	private String share_org_name;
	private String case_symptom;
	private Long case_his_id;
	private ReportShareStatus status;
	public Long getId() {
		return id;
	}
	public Long getReport_id() {
		return report_id;
	}
	public Long getShare_org_id() {
		return share_org_id;
	}
	public Long getShare_user_id() {
		return share_user_id;
	}
	public String getCase_his_num() {
		return case_his_num;
	}
	public Long getClose_share_user_id() {
		return close_share_user_id;
	}
	public String getShare_org_name() {
		return share_org_name;
	}
	public String getCase_symptom() {
		return case_symptom;
	}
	public Long getCase_his_id() {
		return case_his_id;
	}
	public ReportShareStatus getStatus() {
		return status;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setReport_id(Long report_id) {
		this.report_id = report_id;
	}
	public void setShare_org_id(Long share_org_id) {
		this.share_org_id = share_org_id;
	}
	public void setShare_user_id(Long share_user_id) {
		this.share_user_id = share_user_id;
	}
	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}
	public void setClose_share_user_id(Long close_share_user_id) {
		this.close_share_user_id = close_share_user_id;
	}
	public void setShare_org_name(String share_org_name) {
		this.share_org_name = share_org_name;
	}
	public void setCase_symptom(String case_symptom) {
		this.case_symptom = case_symptom;
	}
	public void setCase_his_id(Long case_his_id) {
		this.case_his_id = case_his_id;
	}
	public void setStatus(ReportShareStatus status) {
		this.status = status;
	}
}
