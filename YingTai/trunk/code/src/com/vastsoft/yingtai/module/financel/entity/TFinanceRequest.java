package com.vastsoft.yingtai.module.financel.entity;

import java.util.Date;

public class TFinanceRequest {
	private long id;
	private long org_id;
	private int operation_type;
	private double price;
	private Date create_time;
	private int status;
	private String note;
	private long requsetor_id;
	private long confirm_id;
	private Date confirm_time;
	
	private String v_org_name;
	private String v_org_code;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public int getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(int operation_type) {
		this.operation_type = operation_type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getV_org_name() {
		return v_org_name;
	}

	public void setV_org_name(String v_org_name) {
		this.v_org_name = v_org_name;
	}

	public String getV_org_code() {
		return v_org_code;
	}

	public void setV_org_code(String v_org_code) {
		this.v_org_code = v_org_code;
	}

	public long getRequsetor_id() {
		return requsetor_id;
	}

	public void setRequsetor_id(long requsetor_id) {
		this.requsetor_id = requsetor_id;
	}

	public long getConfirm_id() {
		return confirm_id;
	}

	public void setConfirm_id(long confirm_id) {
		this.confirm_id = confirm_id;
	}

	public Date getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(Date confirm_time) {
		this.confirm_time = confirm_time;
	}

}
