package com.vastsoft.yingtai.action.dicom.action;

import java.io.InputStream;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.utils.FileTools;

public class QueryThumbnailAction extends BaseYingTaiAction {
	private String thumbnail_uid;
	private String mark_char;
	private long dicom_img_id;
	private long affix_id;
	private InputStream inputStream;

	private InputStream takeDefaultStream() {
		return FileTools.getResourceAsStream("com/vastsoft/yingtai/action/dicom/action/unknown.png");
	}

	public String execute() {
		try {
			if ((!StringTools.contains(mark_char, "{{") && !StringTools.contains(thumbnail_uid, "{{"))
					&& (!StringTools.contains(mark_char, "}}") && !StringTools.contains(thumbnail_uid, "}}")))
				inputStream = DicomImgService.instance.readThumbnail(dicom_img_id, mark_char, thumbnail_uid, affix_id);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public InputStream getStream() {
		return inputStream == null ? takeDefaultStream() : inputStream;
	}

	public void setThumbnail_uid(String thumbnail_uid) {
		this.thumbnail_uid = filterParam(thumbnail_uid);
	}

	public void setAffix_id(String affix_id) {
		this.affix_id = (affix_id == null || affix_id.trim().isEmpty()) ? 0 : Long.valueOf(affix_id.trim());
	}

	public void setDicom_img_id(long dicom_img_id) {
		this.dicom_img_id = dicom_img_id;
	}

	public void setMark_char(String mark_char) {
		this.mark_char = filterParam(mark_char);
	}
}
