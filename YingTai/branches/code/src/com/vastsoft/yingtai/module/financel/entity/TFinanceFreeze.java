package com.vastsoft.yingtai.module.financel.entity;

import java.util.Date;

public class TFinanceFreeze {
	private long id;
	private long account_id;
	private long diagnosis_id;
	private double price;
	private Date create_time;
	private int status;
	private double sys_deduct;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(long account_id) {
		this.account_id = account_id;
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

	public long getDiagnosis_id() {
		return diagnosis_id;
	}

	public void setDiagnosis_id(long diagnosis_id) {
		this.diagnosis_id = diagnosis_id;
	}

	public double getSys_deduct() {
		return sys_deduct;
	}

	public void setSys_deduct(double sys_deduct) {
		this.sys_deduct = sys_deduct;
	}
}
