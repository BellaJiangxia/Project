package com.vastsoft.yingtai.module.stat.entity;

public class FStatDiagnosisDoctor {
	private long doctor_id;
	private String doctor_name;
	private int count;
	
	public long getDoctor_id() {
		return doctor_id;
	}
	public int getCount() {
		return count;
	}
	public void setDoctor_id(long doctor_id) {
		this.doctor_id = doctor_id;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDoctor_name() {
		return doctor_name;
	}
	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}
	
}
