package com.vastsoft.yingtai.module.org.product.entity;

public class FOrgProduct extends TOrgProduct{
	private String create_user_name;
	private String device_type_name;
	private String part_type_name;
	public String getCreate_user_name() {
		return create_user_name;
	}
	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}
	public String getDevice_type_name() {
		return device_type_name;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
	public String getPart_type_name() {
		return part_type_name;
	}
	public void setPart_type_name(String part_type_name) {
		this.part_type_name = part_type_name;
	}
}
