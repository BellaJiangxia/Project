package com.vastsoft.yingtai.module.sys.entity;

public class FSysLog extends TOperationRecord{
	private String operator_name;
	private String org_name;
	
	public String getOperator_name() {
		return operator_name;
	}
	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
}
