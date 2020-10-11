package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportMsgStatus;

public class TReportMsg {
	private long id;
	private long report_id;
	private long send_org_id;
	private long send_user_id;
	private long recv_org_id;
	private long recv_user_id;
	private String content;
	private Date send_time;
	private Date recv_time;
	private int type;
	private int status;
	
	
	
	public TReportMsg(long report_id, long recv_user_id, int status) {
		super();
		this.report_id = report_id;
		this.recv_user_id = recv_user_id;
		this.status = status;
	}

	public TReportMsg(long report_id, long send_org_id) {
		super();
		this.report_id = report_id;
		this.send_org_id = send_org_id;
	}

	public TReportMsg() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSend_org_id() {
		return send_org_id;
	}

	public void setSend_org_id(long send_org_id) {
		this.send_org_id = send_org_id;
	}

	public long getSend_user_id() {
		return send_user_id;
	}

	public void setSend_user_id(long send_user_id) {
		this.send_user_id = send_user_id;
	}

	public long getRecv_org_id() {
		return recv_org_id;
	}

	public void setRecv_org_id(long recv_org_id) {
		this.recv_org_id = recv_org_id;
	}

	public long getRecv_user_id() {
		return recv_user_id;
	}

	public void setRecv_user_id(long recv_user_id) {
		this.recv_user_id = recv_user_id;
	}

	public Date getSend_time() {
		return send_time;
	}
	
	public String getSend_timeStr() {
		return DateTools.dateToString(send_time);
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public Date getRecv_time() {
		return recv_time;
	}
	
	public String getRecv_timeStr() {
		return DateTools.dateToString(recv_time);
	}

	public void setRecv_time(Date recv_time) {
		this.recv_time = recv_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public String getStatusStr(){
		return ReportMsgStatus.parseFrom(status).getName();
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getReport_id() {
		return report_id;
	}

	public void setReport_id(long report_id) {
		this.report_id = report_id;
	}
}
