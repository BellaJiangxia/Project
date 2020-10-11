package com.vastsoft.yingtai.module.stat.entity;

import com.vastsoft.util.common.DateTools;

import java.util.Calendar;
import java.util.Date;

public class FStatEntity {
	private int year;
	private int month;
	private int day;
	private int value;
	
	public String getYear() {
		return year+"年";
	}
	public String getMonth() {
		return month+"月";
	}
	public String getGlobalMonth() {
		return year+"年"+month+"月";
	}
	public String getDay() {
		return month+"月"+day+"日";
	}
	public String getGlobalDay() {
		return year+"年"+month+"月"+day+"日";
	}
	public int getValue() {
		return value;
	}
	public void setDate(Date date){
		this.year= DateTools.get(Calendar.YEAR, date);
		this.month=DateTools.get(Calendar.MONTH, date)+1;
		this.day=DateTools.get(Calendar.DAY_OF_MONTH, date);
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
