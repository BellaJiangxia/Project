package com.vastsoft.yingtai.module.sys.entity;

public class FReportTemplate extends TReportTemplate {
	private String device_type_name;
	private String part_type_name;
	private String user_name;
	
	public String getDevice_type_name() {
		return device_type_name;
	}
	public String getPart_type_name() {
		return part_type_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
	public void setPart_type_name(String part_type_name) {
		this.part_type_name = part_type_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	@Override
	public String toString() {
		return "FReportTemplate [" + (device_type_name != null ? "device_type_name=" + device_type_name + ", " : "")
				+ (part_type_name != null ? "part_type_name=" + part_type_name + ", " : "")
				+ (user_name != null ? "user_name=" + user_name : "") + "]";
	}
	
}
