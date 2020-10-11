package com.vastsoft.yingtai.dataMove.entity;

import java.util.Date;

public class TMedicalHis {
	private long id;
	private String medical_his_num;
	private String sicker_name;
	private String identity_id;
	private Date birthday;
	private int gender;
	private String symptom;
	private String born_site;
	private String live_site;
	private String work;
	private String sick_his;
	private long org_id;
	private int status;
	private Date create_time;
	private long creator_id;
	private String note;
	private String eaf_list;
	private String treatment;
	private String request_section;
	private long modify_user_id;
	private Date modify_time;

	public long getId() {
		return id;
	}

	public String getMedical_his_num() {
		return medical_his_num;
	}

	public String getSicker_name() {
		return sicker_name;
	}

	public String getIdentity_id() {
		return identity_id;
	}

	public Date getBirthday() {
		return birthday;
	}

	public int getGender() {
		return gender;
	}

	public String getSymptom() {
		return symptom;
	}

	public String getBorn_site() {
		return born_site;
	}

	public String getLive_site() {
		return live_site;
	}

	public String getWork() {
		return work;
	}

	public String getSick_his() {
		return sick_his;
	}

	public long getOrg_id() {
		return org_id;
	}

	public int getStatus() {
		return status;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public long getCreator_id() {
		return creator_id;
	}

	public String getNote() {
		return note;
	}

	public String getEaf_list() {
		return eaf_list;
	}

	public String getTreatment() {
		return treatment;
	}

	public String getRequest_section() {
		return request_section;
	}

	public long getModify_user_id() {
		return modify_user_id;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMedical_his_num(String medical_his_num) {
		this.medical_his_num = medical_his_num;
	}

	public void setSicker_name(String sicker_name) {
		this.sicker_name = sicker_name;
	}

	public void setIdentity_id(String identity_id) {
		this.identity_id = identity_id;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	public void setBorn_site(String born_site) {
		this.born_site = born_site;
	}

	public void setLive_site(String live_site) {
		this.live_site = live_site;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public void setSick_his(String sick_his) {
		this.sick_his = sick_his;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public void setCreator_id(long creator_id) {
		this.creator_id = creator_id;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setEaf_list(String eaf_list) {
		this.eaf_list = eaf_list;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public void setRequest_section(String request_section) {
		this.request_section = request_section;
	}

	public void setModify_user_id(long modify_user_id) {
		this.modify_user_id = modify_user_id;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

}
