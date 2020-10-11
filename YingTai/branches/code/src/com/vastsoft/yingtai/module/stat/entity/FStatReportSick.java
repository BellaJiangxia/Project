package com.vastsoft.yingtai.module.stat.entity;

import java.util.List;

public class FStatReportSick {
	private String sick_name;
	private int count;
	private int total;
	
	public String getSick_name() {
		return sick_name;
	}
	public int getCount() {
		return count;
	}
	public int getScale() {
		return Math.round(count*100/((float)total));
	}
	public void setSick_name(String sick_name) {
		this.sick_name = sick_name;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public static List<FStatReportSick> setSick_nameForList(List<FStatReportSick> listSick,String sick_name){
		if (listSick==null)return null;
		for (FStatReportSick fStatReportSick : listSick)
			fStatReportSick.setSick_name(sick_name);
		return listSick;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
