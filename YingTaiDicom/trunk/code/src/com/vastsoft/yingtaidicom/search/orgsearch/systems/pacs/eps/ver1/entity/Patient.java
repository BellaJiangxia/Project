package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity;

public class Patient{
	private String ptn_id;
	private String ptn_name;
	private int ptn_id_id;
	private String sex;
	private long birth_date;
	
	public String getPtn_id() {
		return ptn_id;
	}

	public void setPtn_id(String ptn_id) {
		this.ptn_id = ptn_id;
	}

	public String getPtn_name() {
		return ptn_name;
	}

	public void setPtn_name(String ptn_name) {
		this.ptn_name = ptn_name;
	}

	public int getPtn_id_id() {
		return ptn_id_id;
	}

	public void setPtn_id_id(int ptn_id_id) {
		this.ptn_id_id = ptn_id_id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public long getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(long birth_date) {
		this.birth_date = birth_date;
	}

}
