package com.vastsoft.yingtai.module.qualityControl.assist;

import com.vastsoft.util.db.AbstractSearchParam;

public class SearchQualityControlMeasureParam extends AbstractSearchParam {
	private Long id;
	private Long form_id;
	private String content;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getForm_id() {
		return form_id;
	}

	public void setForm_id(Long form_id) {
		this.form_id = form_id;
	}
}
