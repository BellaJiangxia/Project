package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

public class FReportMsg extends TReportMsg {
	private String send_org_name;
	private String send_user_name;
	private String recv_org_name;
	private String recv_user_name;
	public String getSend_org_name() {
		return send_org_name;
	}
	public String getSend_user_name() {
		return send_user_name;
	}
	public String getRecv_org_name() {
		return recv_org_name;
	}
	public String getRecv_user_name() {
		return recv_user_name;
	}
	public void setSend_org_name(String send_org_name) {
		this.send_org_name = send_org_name;
	}
	public void setSend_user_name(String send_user_name) {
		this.send_user_name = send_user_name;
	}
	public void setRecv_org_name(String recv_org_name) {
		this.recv_org_name = recv_org_name;
	}
	public void setRecv_user_name(String recv_user_name) {
		this.recv_user_name = recv_user_name;
	}
	
}
