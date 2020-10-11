package com.vastsoft.yingtai.module.qualityControl.assist;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlEnforceable;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormAnswerStatus;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;

public class SearchQualityControlFormAnswerParam extends AbstractSearchParam {
	private Long create_user_id;
	private Long create_org_id;
	private Long target_id;
	private Long form_id;
	private QualityControlTarget target_type;
//	private List<QualityControlTarget> listQualityControlTarget;
	private QualityControlFormAnswerStatus status;
	private QualityControlEnforceable form_question_enforceable;
	
	public Long getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(Long create_user_id) {
		this.create_user_id = create_user_id;
	}
	public Long getTarget_id() {
		return target_id;
	}
	public void setTarget_id(Long target_id) {
		this.target_id = target_id;
	}
	public QualityControlTarget getTarget_type() {
		return target_type;
	}
	public void setTarget_type(QualityControlTarget target_type) {
		this.target_type = target_type;
	}
	public QualityControlFormAnswerStatus getStatus() {
		return status;
	}
	public void setStatus(QualityControlFormAnswerStatus status) {
		this.status = status;
	}
//	public List<QualityControlTarget> getListQualityControlTarget() {
//		return listQualityControlTarget;
//	}
//	public void setListQualityControlTarget(List<QualityControlTarget> listQualityControlTarget) {
//		this.listQualityControlTarget = listQualityControlTarget;
//	}
	public Long getCreate_org_id() {
		return create_org_id;
	}
	public void setCreate_org_id(Long create_org_id) {
		this.create_org_id = create_org_id;
	}
	public Long getForm_id() {
		return form_id;
	}
	public void setForm_id(Long form_id) {
		this.form_id = form_id;
	}
	public QualityControlEnforceable getForm_question_enforceable() {
		return form_question_enforceable;
	}
	public void setForm_question_enforceable(QualityControlEnforceable form_question_enforceable) {
		this.form_question_enforceable = form_question_enforceable;
	}
	
}
