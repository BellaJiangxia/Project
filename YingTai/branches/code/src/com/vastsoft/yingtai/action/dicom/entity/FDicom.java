package com.vastsoft.yingtai.action.dicom.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImgCheckPro;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;

public class FDicom implements Serializable{
	private static final long serialVersionUID = -5115955212674491937L;
	
	private long imgId;
	private long affix_id;
	private String mark_char;
	private String ae_title;
	private String dev_type;
	private long device_type_id;
	private String part_type;
	private long part_type_id;
	private String checklist_num;
	private String thumbnail_uid;//缩略图图像ID
	private String study_date;
	private boolean newQuery;
	//
	private ArrayList<FDicomImgCheckPro> listCheckPro=new ArrayList<FDicomImgCheckPro>();

	public String getDev_type()
	{
		return dev_type;
	}

	public void setDev_type(String dev_type)
	{
		this.dev_type = dev_type;
	}

	public String getPart_type()
	{
		return part_type;
	}

	public void setPart_type(String part_type)
	{
		this.part_type = part_type;
	}

	public String getAe_title() {
		return ae_title;
	}

	public void setAe_title(String ae_title) {
		this.ae_title = ae_title;
	}

	public Date getCreate_time() {
		return DateTools.strToDate(this.study_date);
	}

	public String getChecklist_num() {
		return checklist_num;
	}

	public void setChecklist_num(String checklist_num) {
		this.checklist_num = checklist_num;
	}

	public long getImgId() {
		return imgId;
	}

	public void setImgId(long imgId) {
		this.imgId = imgId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ae_title == null) ? 0 : ae_title.hashCode());
		result = prime * result + (int) (affix_id ^ (affix_id >>> 32));
		result = prime * result + ((checklist_num == null) ? 0 : checklist_num.hashCode());
		result = prime * result + ((dev_type == null) ? 0 : dev_type.hashCode());
		result = prime * result + (int) (device_type_id ^ (device_type_id >>> 32));
		result = prime * result + (int) (imgId ^ (imgId >>> 32));
		result = prime * result + ((listCheckPro == null) ? 0 : listCheckPro.hashCode());
		result = prime * result + ((mark_char == null) ? 0 : mark_char.hashCode());
		result = prime * result + (newQuery ? 1231 : 1237);
		result = prime * result + ((part_type == null) ? 0 : part_type.hashCode());
		result = prime * result + (int) (part_type_id ^ (part_type_id >>> 32));
		result = prime * result + ((study_date == null) ? 0 : study_date.hashCode());
		result = prime * result + ((thumbnail_uid == null) ? 0 : thumbnail_uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FDicom other = (FDicom) obj;
		if (ae_title == null) {
			if (other.ae_title != null)
				return false;
		} else if (!ae_title.equals(other.ae_title))
			return false;
		if (affix_id != other.affix_id)
			return false;
		if (checklist_num == null) {
			if (other.checklist_num != null)
				return false;
		} else if (!checklist_num.equals(other.checklist_num))
			return false;
		if (dev_type == null) {
			if (other.dev_type != null)
				return false;
		} else if (!dev_type.equals(other.dev_type))
			return false;
		if (device_type_id != other.device_type_id)
			return false;
		if (imgId != other.imgId)
			return false;
		if (listCheckPro == null) {
			if (other.listCheckPro != null)
				return false;
		} else if (!listCheckPro.equals(other.listCheckPro))
			return false;
		if (mark_char == null) {
			if (other.mark_char != null)
				return false;
		} else if (!mark_char.equals(other.mark_char))
			return false;
		if (newQuery != other.newQuery)
			return false;
		if (part_type == null) {
			if (other.part_type != null)
				return false;
		} else if (!part_type.equals(other.part_type))
			return false;
		if (part_type_id != other.part_type_id)
			return false;
		if (study_date == null) {
			if (other.study_date != null)
				return false;
		} else if (!study_date.equals(other.study_date))
			return false;
		if (thumbnail_uid == null) {
			if (other.thumbnail_uid != null)
				return false;
		} else if (!thumbnail_uid.equals(other.thumbnail_uid))
			return false;
		return true;
	}

	public String getStudy_date() {
		return study_date;
	}

	public void setStudy_date(String study_date) {
		this.study_date = study_date;
	}

	public String getThumbnail_uid() {
		return thumbnail_uid;
	}

	public void setThumbnail_uid(String thumbnail_uid) {
		this.thumbnail_uid = thumbnail_uid;
	}

	public String getMark_char() {
		return mark_char;
	}

	public void setMark_char(String mark_char) {
		this.mark_char = mark_char;
	}

	public long getAffix_id() {
		return affix_id;
	}

	public void setAffix_id(long affix_id) {
		this.affix_id = affix_id;
	}

	public long getDevice_type_id() {
		return device_type_id;
	}

	public void setDevice_type_id(long device_type_id) {
		this.device_type_id = device_type_id;
	}

	public long getPart_type_id() {
		return part_type_id;
	}

	public void setPart_type_id(long part_type_id) {
		this.part_type_id = part_type_id;
	}

	public boolean isNewQuery() {
		return newQuery;
	}

	public void setNewQuery(boolean newQuery) {
		this.newQuery = newQuery;
	}

	public void buildListCheckProFromListDic(ArrayList<TDicValue> arrayList) {
		
	}

}
