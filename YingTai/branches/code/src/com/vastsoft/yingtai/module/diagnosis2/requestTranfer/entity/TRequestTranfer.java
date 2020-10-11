package com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity;

import java.util.Date;

public class TRequestTranfer {
	private long id;
	private long old_request_id;
	private long new_request_id;
	private long create_org_id;
	private long create_user_id;
	private Date create_time;
	
	public long getId() {
		return id;
	}
	public long getOld_request_id() {
		return old_request_id;
	}
	public long getNew_request_id() {
		return new_request_id;
	}
	public long getCreate_user_id() {
		return create_user_id;
	}
	public long getCreate_org_id() {
		return create_org_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setOld_request_id(long old_request_id) {
		this.old_request_id = old_request_id;
	}
	public void setNew_request_id(long new_request_id) {
		this.new_request_id = new_request_id;
	}
	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}
	public void setCreate_org_id(long create_org_id) {
		this.create_org_id = create_org_id;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
}
