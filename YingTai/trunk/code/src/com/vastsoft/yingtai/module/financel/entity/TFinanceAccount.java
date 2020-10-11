package com.vastsoft.yingtai.module.financel.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.financel.constants.AccountStatus;

public class TFinanceAccount {
	private long id;
	private long org_id;
	private double amount;
	private String description;
	private Date create_Time;
	private String note;
	private int status;
	
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreate_Time() {
		return create_Time;
	}

	public void setCreate_Time(Date create_Time) {
		this.create_Time = create_Time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	
	public AccountStatus getAccountStatus(){
		return AccountStatus.parseCode(this.status);
	}
}
