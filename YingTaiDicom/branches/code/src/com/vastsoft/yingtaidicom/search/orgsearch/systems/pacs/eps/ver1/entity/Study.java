package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.list.SetUniqueList;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.entity.TSeries;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public class Study {
	private String study_uid;
	private int study_uid_id;
	private int ptn_id_id;
	private int study_date;
	private double study_time;
	private String study_id;
	private String accession_number;
	private String physician_name;
	private String study_desc;
	private int user_group;
	private int status_viewimage_useruid;
	private String status_viewimage_username;
	private int status_viewimage_date;
	private int status_viewimage_time;
	private String request_service;
	private String request_physician;

	@SuppressWarnings("unchecked")
	private List<Series> listSeries = SetUniqueList.decorate(new ArrayList<Series>());// 序列列表

	public String getStudy_uid() {
		return study_uid;
	}

	public void setStudy_uid(String study_uid) {
		this.study_uid = study_uid;
	}

	public int getStudy_uid_id() {
		return study_uid_id;
	}

	public void setStudy_uid_id(int study_uid_id) {
		this.study_uid_id = study_uid_id;
	}

	public int getPtn_id_id() {
		return ptn_id_id;
	}

	public void setPtn_id_id(int ptn_id_id) {
		this.ptn_id_id = ptn_id_id;
	}

	public int getStudy_date() {
		return study_date;
	}

	public void setStudy_date(int study_date) {
		this.study_date = study_date;
	}

	public double getStudy_time() {
		return study_time;
	}

	public void setStudy_time(double study_time) {
		this.study_time = study_time;
	}

	public String getStudy_id() {
		return study_id;
	}

	public void setStudy_id(String study_id) {
		this.study_id = study_id;
	}

	public String getAccession_number() {
		return accession_number;
	}

	public void setAccession_number(String accession_number) {
		this.accession_number = accession_number;
	}

	public String getPhysician_name() {
		return physician_name;
	}

	public void setPhysician_name(String physician_name) {
		this.physician_name = physician_name;
	}

	public String getStudy_desc() {
		return study_desc;
	}

	public void setStudy_desc(String study_desc) {
		this.study_desc = study_desc;
	}

	public int getUser_group() {
		return user_group;
	}

	public void setUser_group(int user_group) {
		this.user_group = user_group;
	}

	public int getStatus_viewimage_useruid() {
		return status_viewimage_useruid;
	}

	public void setStatus_viewimage_useruid(int status_viewimage_useruid) {
		this.status_viewimage_useruid = status_viewimage_useruid;
	}

	public String getStatus_viewimage_username() {
		return status_viewimage_username;
	}

	public void setStatus_viewimage_username(String status_viewimage_username) {
		this.status_viewimage_username = status_viewimage_username;
	}

	public int getStatus_viewimage_date() {
		return status_viewimage_date;
	}

	public void setStatus_viewimage_date(int status_viewimage_date) {
		this.status_viewimage_date = status_viewimage_date;
	}

	public int getStatus_viewimage_time() {
		return status_viewimage_time;
	}

	public void setStatus_viewimage_time(int status_viewimage_time) {
		this.status_viewimage_time = status_viewimage_time;
	}

	public String getRequest_service() {
		return request_service;
	}

	public void setRequest_service(String request_service) {
		this.request_service = request_service;
	}

	public String getRequest_physician() {
		return request_physician;
	}

	public void setRequest_physician(String request_physician) {
		this.request_physician = request_physician;
	}

	public List<Series> getListSeries() {
		return listSeries;
	}

	public void addSeries(List<Series> listSeries) {
		if (CollectionTools.isEmpty(listSeries))
			return;
		this.listSeries.addAll(listSeries);
	}

	public List<TSeries> organize(SystemIdentity systemIdentity) throws PatientDataException {
		if (CollectionTools.isEmpty(this.listSeries))
			throw new PatientDataException("此次检查没有序列！");
		List<TSeries> result = new ArrayList<>(this.listSeries.size());
		for (Series series : listSeries) {
			TSeries tSeries = new TSeries(systemIdentity);
			tSeries.setPart_name(StringTools.isEmpty(series.getBody_part())
					? (StringTools.isEmpty(series.getProtocol_name()) ? "未指定" : series.getProtocol_name().trim())
					: series.getBody_part().trim());
			tSeries.setMark_char(series.getSeries_uid());
			tSeries.setThumbnail_uid(series.getThrumbnail_uid());
			tSeries.setThumbnail_data(series.getThumbnail_data());
			tSeries.setExpose_times(series.getExpose_times());
			result.add(tSeries);
		}
		return result;
	}

	public String getAe_title() {
		if (CollectionTools.isEmpty(this.listSeries))
			return "";
		return this.listSeries.get(0).getSource_ae();
	}

	public String getDevice_name() {
		if (CollectionTools.isEmpty(this.listSeries))
			return "";
		for (Series series : listSeries) {
			if (StringTools.isEmpty(series.getModality()))
				continue;
			return series.getModality().trim();
		}
		return "";
	}

	public String getPart_name() {
		if (CollectionTools.isEmpty(this.listSeries))
			return "";
		for (Series series : listSeries) {
			if (!StringTools.isEmpty(series.getProtocol_name()))
				return series.getProtocol_name().trim();
		}
		return "";
	}

}
