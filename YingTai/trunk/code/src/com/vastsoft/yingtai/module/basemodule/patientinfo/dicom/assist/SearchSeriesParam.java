package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.assist;

import com.vastsoft.util.db.AbstractSearchParam;

public class SearchSeriesParam extends AbstractSearchParam {
	private Long id;
	private Long dicom_img_id;
	private String mark_char;
	private Long part_type_id;

	public Long getDicom_img_id() {
		return dicom_img_id;
	}

	public void setDicom_img_id(Long dicom_img_id) {
		this.dicom_img_id = dicom_img_id;
	}

	public String getMark_char() {
		return mark_char;
	}

	public void setMark_char(String mark_char) {
		this.mark_char = mark_char;
	}

	public Long getPart_type_id() {
		return part_type_id;
	}

	public void setPart_type_id(Long part_type_id) {
		this.part_type_id = part_type_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
