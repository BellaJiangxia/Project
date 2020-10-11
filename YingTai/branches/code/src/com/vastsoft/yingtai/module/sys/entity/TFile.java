package com.vastsoft.yingtai.module.sys.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.sys.constants.DictionaryType;

public class TFile{
	private long id;
	private String path;
	private String thumbnail_path;
	private String ori_name;
	private Date create_time;
	private int status;
	private int file_type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOri_name() {
		return ori_name;
	}

	public void setOri_name(String ori_name) {
		this.ori_name = ori_name;
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

	public void setStatus(int status) {
		this.status = status;
	}

	public String getThumbnail_path() {
		return thumbnail_path;
	}

	public void setThumbnail_path(String thumbnail_path) {
		this.thumbnail_path = thumbnail_path;
	}

	public int getFile_type() {
		return file_type;
	}

	public void setFile_type(int file_type) {
		this.file_type = file_type;
	}
	
	public DictionaryType getFileType(){
		return DictionaryType.parseFrom(this.file_type);
	}
}
