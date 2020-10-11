package com.vastsoft.yingtai.module.sys.entity;

import java.util.List;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.sys.constants.TemplateType;
import com.vastsoft.yingtai.utils.annotation.IgnoreNull;

public class TReportTemplate{
	private long id;
	private String template_name;
	private long device_type_id;
	private long part_type_id;
	private long user_id;
	private String pic_opinion;//影像所见
	private String pic_conclusion;//诊断意见
	private int type;
	private String note;
	@IgnoreNull
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTemplate_name() {
		return template_name;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
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
	@IgnoreNull
	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getPic_opinion() {
		return pic_opinion;
	}

	public String getPic_opinionLimit() {
		return pic_opinion==null?null:pic_opinion.length()>8?pic_opinion.substring(0, 9)+"...":pic_opinion;
	}
	
	public String getPic_opinionPreview() {
		return pic_opinion==null?null:pic_opinion.length()>200?pic_opinion.substring(0, 200)+"...":pic_opinion;
	}
	public List<String> getPic_conclusions() {
		if (pic_conclusion==null)
			return null;
		String pco=pic_conclusion.replace("\r\n", "<p>");
		pco=pco.replace("\n", "<p>");
		return StringTools.strArrayTrim(pco.split("<p>"));
	}
	public List<String> getPic_opinions() {
		if (pic_opinion==null)
			return null;
		String pco=pic_opinion.replace("\r\n", "<p>");
		pco=pco.replace("\n", "<p>");
		return StringTools.strArrayTrim(pco.split("<p>"));
	}
	public void setPic_opinion(String pic_opinion) {
		this.pic_opinion = pic_opinion;
	}

	@IgnoreNull
	public String getNote() {
		return note;
	}
	
	@IgnoreNull
	public String getNoteLimit() {
		return note==null?null:note.length()>8?note.substring(0, 9)+"...":note;
	}
	
	@IgnoreNull
	public String getNotePreview() {
		return note==null?null:note.length()>200?note.substring(0, 200)+"...":note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPic_conclusion() {
		return pic_conclusion;
	}
	
	public String getPic_conclusionLimit() {
		return pic_conclusion==null?null:pic_conclusion.length()>8?pic_conclusion.substring(0, 9)+"...":pic_conclusion;
	}

	public String getPic_conclusionPreview() {
		return pic_conclusion==null?null:pic_conclusion.length()>200?pic_conclusion.substring(0, 200)+"...":pic_conclusion;
	}
	
	public void setPic_conclusion(String pic_conclusion) {
		this.pic_conclusion = pic_conclusion;
	}

	public int getType() {
		return type;
	}
	
	public String getTypeStr() {
		return TemplateType.parseFrom(type).getName();
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TReportTemplate [id=" + id + ", "
				+ (template_name != null ? "template_name=" + template_name + ", " : "") + "device_type_id="
				+ device_type_id + ", part_type_id=" + part_type_id + ", user_id=" + user_id + ", "
				+ (pic_opinion != null ? "pic_opinion=" + pic_opinion + ", " : "")
				+ (pic_conclusion != null ? "pic_conclusion=" + pic_conclusion + ", " : "") + "type=" + type + ", "
				+ (note != null ? "note=" + note : "") + "]";
	}
	
}
