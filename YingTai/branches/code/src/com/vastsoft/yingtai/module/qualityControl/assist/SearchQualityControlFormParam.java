package com.vastsoft.yingtai.module.qualityControl.assist;

import java.util.List;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlEnforceable;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormState;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;

public class SearchQualityControlFormParam extends AbstractSearchParam {
	private Long id;
	private String name;
	private QualityControlTarget target_type;
	private List<QualityControlTarget> list_target_type;
//	private QualityControlOccasion question_occasion;
	private QualityControlEnforceable question_enforceable;
	private QualityControlFormState state;
	
	
	
	public QualityControlTarget getTarget_type() {
		return target_type;
	}
	public void setTarget_type(QualityControlTarget target_type) {
		this.target_type = target_type;
	}
	public QualityControlEnforceable getQuestion_enforceable() {
		return question_enforceable;
	}
	public void setQuestion_enforceable(QualityControlEnforceable question_enforceable) {
		this.question_enforceable = question_enforceable;
	}
	public QualityControlFormState getState() {
		return state;
	}
	public void setState(QualityControlFormState state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<QualityControlTarget> getList_target_type() {
		return list_target_type;
	}
	public void setList_target_type(List<QualityControlTarget> list_target_type) {
		this.list_target_type = list_target_type;
	}
	
}
