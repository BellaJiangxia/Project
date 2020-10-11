package com.vastsoft.yingtai.module.qualityControl.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlEnforceable;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormAnswerStatus;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;

public class TQualityControlFormAnswer {
	private long id;
	private long form_id;
	private String form_name;
	private int form_question_enforceable;// 回答强制性
	private String form_note;// 表格说明
	private long create_user_id;
	private long create_org_id;
	private Date create_time;
	private int status;
	private int target_type;
	private long target_id;
	// private int ignore_flag; //是否忽略，1标识是，其他标识否

	public TQualityControlFormAnswer() {
		super();
	}

	public TQualityControlFormAnswer(long form_id, long create_user_id, long create_org_id) {
		super();
		this.form_id = form_id;
		this.create_user_id = create_user_id;
		this.create_org_id = create_org_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getForm_id() {
		return form_id;
	}

	public void setForm_id(long form_id) {
		this.form_id = form_id;
	}

	public long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}

	public String getStatus_name() {
		QualityControlFormAnswerStatus qcfas = QualityControlFormAnswerStatus.parseCode(status);
		return qcfas == null ? "" : qcfas.getName();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreate_org_id() {
		return create_org_id;
	}

	public void setCreate_org_id(long create_org_id) {
		this.create_org_id = create_org_id;
	}

	public int getTarget_type() {
		return target_type;
	}

	public String getTarget_type_name() {
		QualityControlTarget qct = QualityControlTarget.parseCode(target_type);
		return qct == null ? "" : qct.getName();
	}

	public void setTarget_type(int target_type) {
		this.target_type = target_type;
	}

	// public int getIgnore_flag() {
	// return ignore_flag;
	// }
	//
	// public void setIgnore_flag(int ignore_flag) {
	// this.ignore_flag = ignore_flag;
	// }

	public long getTarget_id() {
		return target_id;
	}

	public void setTarget_id(long target_id) {
		this.target_id = target_id;
	}

	public String getForm_name() {
		return form_name;
	}

	public void setForm_name(String form_name) {
		this.form_name = form_name;
	}

	public int getForm_question_enforceable() {
		return form_question_enforceable;
	}

	public String getForm_question_enforceable_name() {
		QualityControlEnforceable qce = QualityControlEnforceable.parseCode(form_question_enforceable);
		return qce == null ? "" : qce.getName();
	}

	public void setForm_question_enforceable(int form_question_enforceable) {
		this.form_question_enforceable = form_question_enforceable;
	}

	public String getForm_note() {
		return form_note;
	}

	public void setForm_note(String form_note) {
		this.form_note = form_note;
	}

}
