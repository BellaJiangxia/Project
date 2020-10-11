package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportModifyRequestStatus;

public class TModifyReportRequest {
	private long id;
	private long report_id;
	private long request_user_id;
	private long request_org_id;
	private long response_user_id;
	private long response_org_id;
	private String reason;
	private Date create_time;
	private Date response_time;
	private int status;
	private String answer;
	
	public TModifyReportRequest(long report_id, int status) {
		super();
		this.report_id = report_id;
		this.status = status;
	}
	public TModifyReportRequest() {
		super();
	}
	public long getId() {
		return id;
	}
	public long getRequest_user_id() {
		return request_user_id;
	}
	public long getResponse_user_id() {
		return response_user_id;
	}
	public String getReason() {
		return reason;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public Date getResponse_time() {
		return response_time;
	}
	public int getStatus() {
		return status;
	}
	public String getStatusStr() {
		ReportModifyRequestStatus sss=ReportModifyRequestStatus.parseCode(status);
		return sss==null?"":sss.getName();
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setRequest_user_id(long request_user_id) {
		this.request_user_id = request_user_id;
	}
	public void setResponse_user_id(long response_user_id) {
		this.response_user_id = response_user_id;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public void setResponse_time(Date response_time) {
		this.response_time = response_time;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getRequest_org_id() {
		return request_org_id;
	}
	public void setRequest_org_id(long request_org_id) {
		this.request_org_id = request_org_id;
	}
	public long getResponse_org_id() {
		return response_org_id;
	}
	public void setResponse_org_id(long response_org_id) {
		this.response_org_id = response_org_id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public long getReport_id() {
		return report_id;
	}
	public void setReport_id(long report_id) {
		this.report_id = report_id;
	}
	
}
