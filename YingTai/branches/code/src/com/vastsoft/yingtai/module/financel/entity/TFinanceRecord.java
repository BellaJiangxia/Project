package com.vastsoft.yingtai.module.financel.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.financel.constants.AccountRecordType;

public class TFinanceRecord {
	private long id;
	private double price;
	private long request_org_id;
	private long diagnosis_org_id;
	private long account_id;
	private long operator_id;
	private int operat_type;
	private int re_type;
	private String description;
	private Date operat_time;
	private String note;
	
	private String v_opeaator_name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(long account_id) {
		this.account_id = account_id;
	}

	public long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getOperat_type() {
		return operat_type;
	}

	public void setOperat_type(int operat_type) {
		this.operat_type = operat_type;
	}

	public Date getOperat_time() {
		return operat_time;
	}
	public String getOperat_timeStr() {
		return DateTools.dateToString(operat_time);
	}
	public void setOperat_time(Date operat_time) {
		this.operat_time = operat_time;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getV_opeaator_name() {
		return v_opeaator_name;
	}

	public void setV_opeaator_name(String v_opeaator_name) {
		this.v_opeaator_name = v_opeaator_name;
	}
	
	public AccountRecordType getOpeartType(){
		return AccountRecordType.parseCode(this.operat_type);
	}
	
	public String getOpeartTypeStr(){
		AccountRecordType art= AccountRecordType.parseCode(this.operat_type);
		return art==null?"":art.getName();
	}
	public String getReTypeName() {
		AccountRecordType art= AccountRecordType.parseCode(this.operat_type);
		return art==null?"":art.getReType().getName();
	}
	public int getRe_type() {
		return re_type;
	}

	public void setRe_type(int re_type) {
		this.re_type = re_type;
	}

	public long getRequest_org_id() {
		return request_org_id;
	}

	public void setRequest_org_id(long request_org_id) {
		this.request_org_id = request_org_id;
	}

	public long getDiagnosis_org_id() {
		return diagnosis_org_id;
	}

	public void setDiagnosis_org_id(long diagnosis_org_id) {
		this.diagnosis_org_id = diagnosis_org_id;
	}

}
