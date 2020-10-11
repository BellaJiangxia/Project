package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportShareStatus;

public class TReportShare {
	private long id;
	private long report_id;
	private long share_org_id;
	private long share_user_id;
	private long close_share_user_id;
	private Date share_time;
	private Date close_share_time;
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShare_user_id() {
		return share_user_id;
	}

	public void setShare_user_id(long share_user_id) {
		this.share_user_id = share_user_id;
	}

	public Date getShare_time() {
		return share_time;
	}
	
	public void setShare_time(Date share_time) {
		this.share_time = share_time;
	}

	public Date getClose_share_time() {
		return close_share_time;
	}
	
	public String getClose_share_timeStr() {
		return  DateTools.dateToString(close_share_time);
	}

	public void setClose_share_time(Date close_share_time) {
		this.close_share_time = close_share_time;
	}

	public int getStatus() {
		return status;
	}
	public String getStatusStr() {
		return ReportShareStatus.parseCode(status).getName();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getClose_share_user_id() {
		return close_share_user_id;
	}

	public void setClose_share_user_id(long close_share_user_id) {
		this.close_share_user_id = close_share_user_id;
	}

	public long getReport_id() {
		return report_id;
	}

	public void setReport_id(long report_id) {
		this.report_id = report_id;
	}

	public long getShare_org_id() {
		return share_org_id;
	}

	public void setShare_org_id(long share_org_id) {
		this.share_org_id = share_org_id;
	}
}
