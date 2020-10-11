package com.vastsoft.yingtai.module.stat.entity;

public class FStatOrgMedicalHis {
	private long org_id;
	private String org_name;
	private int count;
	
	public long getOrg_id() {
		return org_id;
	}
	public String getOrg_name() {
		return org_name;
	}
	public int getCount() {
		return count;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
