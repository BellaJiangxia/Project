package com.vastsoft.yingtai.dataMove.entity;

import java.util.Date;

public class TDiagnosis_V1 {
	private long id;
	private long medical_his_id;
	private long request_user_id;
	private long request_org_id;
	private long diagnosis_org_id;
	private long diagnosis_product_id;
	private Date request_time;
	private Date handle_time;
	private int status;
	private Date create_time;
	private String note;
	private long device_type_id;
	private long part_type_id;
	private long accept_user_id;
	private long verify_user_id;
	private String pic_opinion;
	private Date report_time;
	private String pic_conclusion;
	private long publish_org_id;
	private long medical_his_img_id;
	private long curr_handle_id;
	private int f_o_m;
	private long print_user_id;
	private int print_times;
	private Date print_time;
	private long view_user_id;

	public long getId() {
		return id;
	}

	public long getMedical_his_id() {
		return medical_his_id;
	}

	public long getRequest_user_id() {
		return request_user_id;
	}

	public long getRequest_org_id() {
		return request_org_id;
	}

	public long getDiagnosis_org_id() {
		return diagnosis_org_id;
	}

	public long getDiagnosis_product_id() {
		return diagnosis_product_id;
	}

	public Date getRequest_time() {
		return request_time;
	}

	public Date getHandle_time() {
		return handle_time;
	}

	public int getStatus() {
		return status;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public String getNote() {
		return note;
	}

	public long getDevice_type_id() {
		return device_type_id;
	}

	public long getPart_type_id() {
		return part_type_id;
	}

	public long getAccept_user_id() {
		return accept_user_id;
	}

	public long getVerify_user_id() {
		return verify_user_id;
	}

	public String getPic_opinion() {
		return pic_opinion;
	}

	public Date getReport_time() {
		return report_time;
	}

	public String getPic_conclusion() {
		return pic_conclusion;
	}

	public long getPublish_org_id() {
		return publish_org_id;
	}

	public long getMedical_his_img_id() {
		return medical_his_img_id;
	}

	public long getCurr_handle_id() {
		return curr_handle_id;
	}

	public int getF_o_m() {
		return f_o_m;
	}

	public long getPrint_user_id() {
		return print_user_id;
	}

	public int getPrint_times() {
		return print_times;
	}

	public Date getPrint_time() {
		return print_time;
	}

	public long getView_user_id() {
		return view_user_id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMedical_his_id(long medical_his_id) {
		this.medical_his_id = medical_his_id;
	}

	public void setRequest_user_id(long request_user_id) {
		this.request_user_id = request_user_id;
	}

	public void setRequest_org_id(long request_org_id) {
		this.request_org_id = request_org_id;
	}

	public void setDiagnosis_org_id(long diagnosis_org_id) {
		this.diagnosis_org_id = diagnosis_org_id;
	}

	public void setDiagnosis_product_id(long diagnosis_product_id) {
		this.diagnosis_product_id = diagnosis_product_id;
	}

	public void setRequest_time(Date request_time) {
		this.request_time = request_time;
	}

	public void setHandle_time(Date handle_time) {
		this.handle_time = handle_time;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setDevice_type_id(long device_type_id) {
		this.device_type_id = device_type_id;
	}

	public void setPart_type_id(long part_type_id) {
		this.part_type_id = part_type_id;
	}

	public void setAccept_user_id(long accept_user_id) {
		this.accept_user_id = accept_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public void setPic_opinion(String pic_opinion) {
		this.pic_opinion = pic_opinion;
	}

	public void setReport_time(Date report_time) {
		this.report_time = report_time;
	}

	public void setPic_conclusion(String pic_conclusion) {
		this.pic_conclusion = pic_conclusion;
	}

	public void setPublish_org_id(long publish_org_id) {
		this.publish_org_id = publish_org_id;
	}

	public void setMedical_his_img_id(long medical_his_img_id) {
		this.medical_his_img_id = medical_his_img_id;
	}

	public void setCurr_handle_id(long curr_handle_id) {
		this.curr_handle_id = curr_handle_id;
	}

	public void setF_o_m(int f_o_m) {
		this.f_o_m = f_o_m;
	}

	public void setPrint_user_id(long print_user_id) {
		this.print_user_id = print_user_id;
	}

	public void setPrint_times(int print_times) {
		this.print_times = print_times;
	}

	public void setPrint_time(Date print_time) {
		this.print_time = print_time;
	}

	public void setView_user_id(long view_user_id) {
		this.view_user_id = view_user_id;
	}
}
