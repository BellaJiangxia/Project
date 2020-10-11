package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.util.common.StringTools;

public class TReportShareSpeech {
	private long id;
	private long share_id;
	private long speech_user_id;
	private Date speech_time;
	private String content;
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShare_id() {
		return share_id;
	}

	public void setShare_id(long share_id) {
		this.share_id = share_id;
	}

	public long getSpeech_user_id() {
		return speech_user_id;
	}

	public void setSpeech_user_id(long speech_user_id) {
		this.speech_user_id = speech_user_id;
	}

	public Date getSpeech_time() {
		return speech_time;
	}
	
	public String getSpeech_timeStr(){
		return DateTools.dateToString(speech_time);
	}
	
	public void setSpeech_time(Date speech_time) {
		this.speech_time = speech_time;
	}
	
	@JSON(serialize=false)
	public String getContent() {
		return content;
	}
	
	public String[] getContent_lines() {
		return StringTools.stringToLines(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
