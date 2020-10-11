package com.vastsoft.yingtai.module.stat.entity;

public class FStatDevicePartDiagnosis {
	private long device_type_id;
	private long part_type_id;
	private String device_type_name;
	private String part_type_name;
	private int count;
	public long getDevice_type_id() {
		return device_type_id;
	}
	public long getPart_type_id() {
		return part_type_id;
	}
	public String getDevice_type_name() {
		return device_type_name;
	}
	public String getPart_type_name() {
		return part_type_name;
	}
	public int getCount() {
		return count;
	}
	public void setDevice_type_id(long device_type_id) {
		this.device_type_id = device_type_id;
	}
	public void setPart_type_id(long part_type_id) {
		this.part_type_id = part_type_id;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
	public void setPart_type_name(String part_type_name) {
		this.part_type_name = part_type_name;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
