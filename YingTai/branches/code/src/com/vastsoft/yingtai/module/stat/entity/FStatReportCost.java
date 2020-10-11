package com.vastsoft.yingtai.module.stat.entity;

public class FStatReportCost {
	private String device_name;
	private String part_name;
	private int print_count;
	private double price_count;
	public String getDevice_name() {
		return device_name;
	}
	public String getPart_name() {
		return part_name;
	}
	public int getPrint_count() {
		return print_count;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}
	public void setPrint_count(int print_count) {
		this.print_count = print_count;
	}
	public double getPrice_count() {
		return price_count;
	}
	public void setPrice_count(double price_count) {
		this.price_count = price_count;
	}
}
