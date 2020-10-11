package com.vastsoft.yingtai.dataMove.entity;

import java.util.Date;

public class TMedicalHisImg {
	private long id;
	private long medical_his_id;
	private long device_type_id;
	private long part_type_id;
	private String mark_char;
	private String ae_title;
	private String checklist_num;
	private int status;
	private Date create_time;
	private Date check_time;
	private String thumbnail_uid;
	private long affix_id;
	private String check_pro;

	public long getId() {
		return id;
	}

	public long getMedical_his_id() {
		return medical_his_id;
	}

	public long getDevice_type_id() {
		return device_type_id;
	}

	public long getPart_type_id() {
		return part_type_id;
	}

	public String getMark_char() {
		return mark_char;
	}

	public String getAe_title() {
		return ae_title;
	}

	public String getChecklist_num() {
		return checklist_num;
	}

	public int getStatus() {
		return status;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public Date getCheck_time() {
		return check_time;
	}

	public String getThumbnail_uid() {
		return thumbnail_uid;
	}

	public long getAffix_id() {
		return affix_id;
	}

	public String getCheck_pro() {
		return check_pro;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMedical_his_id(long medical_his_id) {
		this.medical_his_id = medical_his_id;
	}

	public void setDevice_type_id(long device_type_id) {
		this.device_type_id = device_type_id;
	}

	public void setPart_type_id(long part_type_id) {
		this.part_type_id = part_type_id;
	}

	public void setMark_char(String mark_char) {
		this.mark_char = mark_char;
	}

	public void setAe_title(String ae_title) {
		this.ae_title = ae_title;
	}

	public void setChecklist_num(String checklist_num) {
		this.checklist_num = checklist_num;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public void setCheck_time(Date check_time) {
		this.check_time = check_time;
	}

	public void setThumbnail_uid(String thumbnail_uid) {
		this.thumbnail_uid = thumbnail_uid;
	}

	public void setAffix_id(long affix_id) {
		this.affix_id = affix_id;
	}

	public void setCheck_pro(String check_pro) {
		this.check_pro = check_pro;
	}

}
