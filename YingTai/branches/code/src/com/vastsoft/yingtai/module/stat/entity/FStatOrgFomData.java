package com.vastsoft.yingtai.module.stat.entity;

public class FStatOrgFomData extends FStatData{
	private int f_count;//阴性数量
	private int m_count;//阳性数量
	private int none_count;
	
	public int getF_count() {
		return f_count;
	}
	public int getM_count() {
		return m_count;
	}
	public int getNone_count() {
		return none_count;
	}
	public void setF_count(int f_count) {
		this.f_count = f_count;
	}
	public void setM_count(int m_count) {
		this.m_count = m_count;
	}
	public void setNone_count(int none_count) {
		this.none_count = none_count;
	}
	public int getMScale(){
		return Math.round((this.m_count*100)/(float)this.getTotal());
	}
}
