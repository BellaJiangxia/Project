package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.assist;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.constants.DicomImgStatus;

public class SearchDicomImgParam extends AbstractSearchParam {
	private Long imgId;
	private Long case_id;
	private Long device_type_id;
	private Long part_type_id;
	private Long affix_id;
	private String thumbnail_uid;
	private String mark_char;
	private String ae_title;
	private String checklist_num;
	private Date create_start;
	private Date create_end;
	private Date check_start;
	private Date check_end;
	private DicomImgStatus status;

	@Override
	public Map<String, Object> buildMap() {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("id", imgId);
		result.put("case_id", case_id);
		result.put("device_type_id", device_type_id);
		result.put("part_type_id", part_type_id);
		result.put("affix_id", affix_id);
		result.put("thumbnail_uid", thumbnail_uid);
		result.put("mark_char", mark_char);
		result.put("ae_title", ae_title);
		result.put("checklist_num", checklist_num);
		result.put("create_start", create_start);
		result.put("create_end", create_end);
		result.put("check_start", check_start);
		result.put("check_end", check_end);
		result.put("status", status==null?null:status.getCode());
		return result;
	}

	public Long getImgId() {
		return imgId;
	}

	public Long getCase_id() {
		return case_id;
	}

	public Long getDevice_type_id() {
		return device_type_id;
	}

	public Long getPart_type_id() {
		return part_type_id;
	}

	public Long getAffix_id() {
		return affix_id;
	}

	public String getThumbnail_uid() {
		return thumbnail_uid;
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

	public Date getCreate_start() {
		return create_start;
	}

	public Date getCreate_end() {
		return create_end;
	}

	public Date getCheck_start() {
		return check_start;
	}

	public Date getCheck_end() {
		return check_end;
	}

	public DicomImgStatus getStatus() {
		return status;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

	public void setCase_id(Long case_id) {
		this.case_id = case_id;
	}

	public void setDevice_type_id(Long device_type_id) {
		this.device_type_id = device_type_id;
	}

	public void setPart_type_id(Long part_type_id) {
		this.part_type_id = part_type_id;
	}

	public void setAffix_id(Long affix_id) {
		this.affix_id = affix_id;
	}

	public void setThumbnail_uid(String thumbnail_uid) {
		this.thumbnail_uid = thumbnail_uid;
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

	public void setCreate_start(Date create_start) {
		this.create_start = create_start;
	}

	public void setCreate_end(Date create_end) {
		this.create_end = create_end;
	}

	public void setCheck_start(Date check_start) {
		this.check_start = check_start;
	}

	public void setCheck_end(Date check_end) {
		this.check_end = check_end;
	}

	public void setStatus(DicomImgStatus status) {
		this.status = status;
	}
}
