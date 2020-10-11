package com.vastsoft.yingtai.module.msg.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.msg.constants.MsgStatus;
import com.vastsoft.yingtai.module.msg.constants.MsgType;

public class TMessage {
	private long id;
	private String content;
	private String recv_mobile;//接收电话号码
	private long recv_user_id;
	private long diagnosis_id;
	private Date plan_time;//计划发送时间
	private Date sendtime;//实际发送时间
	private int msg_type;//短信类型
	private String uid;
	private int status;//状态

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}
	
	public String getContentsp() {
		return StringTools.cutStr(content, 10)+"...";
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendtime() {
		return sendtime;
	}
	
	public String getSendtimeStr() {
		return DateTools.dateToString(sendtime);
	}

	public void setSend_time(Date send_time) {
		this.sendtime = send_time;
	}

	public int getMsg_type() {
		return msg_type;
	}
	
	public String getMsg_typeStr() {
		MsgType mt=MsgType.parseCode(msg_type);
		return mt==null?"":mt.getName();
	}

	public void setMsg_type(int msg_type) {
		this.msg_type = msg_type;
	}

	public int getStatus() {
		return status;
	}
	
	public String getStatusStr() {
		MsgStatus ms=MsgStatus.parseCode(status);
		return ms==null?"":ms.getName();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRecv_mobile() {
		return recv_mobile;
	}

	public void setRecv_mobile(String recv_mobile) {
		this.recv_mobile = recv_mobile;
	}

	public long getRecv_user_id() {
		return recv_user_id;
	}

	public void setRecv_user_id(long recv_user_id) {
		this.recv_user_id = recv_user_id;
	}

	public Date getPlan_time() {
		return plan_time;
	}

	public void setPlan_time(Date plan_time) {
		this.plan_time = plan_time;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public long getDiagnosis_id() {
		return diagnosis_id;
	}

	public void setDiagnosis_id(long diagnosis_id) {
		this.diagnosis_id = diagnosis_id;
	}
}
