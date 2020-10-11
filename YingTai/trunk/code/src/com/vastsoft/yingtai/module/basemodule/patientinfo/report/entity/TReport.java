package com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity;

import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity.ShareReport;

public class TReport extends ShareReport {
	public TReport() {
		super();
	}

	public TReport(String dicom_img_mark_char, int source_type, int type) {
		super();
		super.setDicom_img_mark_char(dicom_img_mark_char);
		super.setSource_type(source_type);
		super.setType(type);
	}
}
