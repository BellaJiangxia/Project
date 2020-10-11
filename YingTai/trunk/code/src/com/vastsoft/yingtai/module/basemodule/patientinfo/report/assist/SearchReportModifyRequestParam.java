package com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportModifyRequestStatus;

public class SearchReportModifyRequestParam extends AbstractSearchParam {
	private Long id;
	private Long report_id;
	private Long request_user_id; 
	private Long request_org_id; 
	private Long response_user_id;
	private Long response_org_id;
	private ReportModifyRequestStatus status;
	
	public Long getId() {
		return id;
	}
	public ReportModifyRequestStatus getStatus() {
		return status;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setStatus(ReportModifyRequestStatus status) {
		this.status = status;
	}
	public Long getReport_id() {
		return report_id;
	}
	public void setReport_id(Long report_id) {
		this.report_id = report_id;
	}
	public Long getRequest_user_id() {
		return request_user_id;
	}
	public void setRequest_user_id(Long request_user_id) {
		this.request_user_id = request_user_id;
	}
	public Long getRequest_org_id() {
		return request_org_id;
	}
	public void setRequest_org_id(Long request_org_id) {
		this.request_org_id = request_org_id;
	}
	public Long getResponse_user_id() {
		return response_user_id;
	}
	public void setResponse_user_id(Long response_user_id) {
		this.response_user_id = response_user_id;
	}
	public Long getResponse_org_id() {
		return response_org_id;
	}
	public void setResponse_org_id(Long response_org_id) {
		this.response_org_id = response_org_id;
	}
}
