package com.vastsoft.yingtai.module.sys.entity;

import java.util.Date;

public class TTemplateStat {
	private long id;
	private long template_id;
	private long user_id;
	private int use_times;
	private Date last_use_time;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(long template_id) {
		this.template_id = template_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public int getUse_times() {
		return use_times;
	}
	public void setUse_times(int use_times) {
		this.use_times = use_times;
	}
	public Date getLast_use_time() {
		return last_use_time;
	}
	public void setLast_use_time(Date last_use_time) {
		this.last_use_time = last_use_time;
	}
	
}
