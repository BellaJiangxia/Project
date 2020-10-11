package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity;

public class Image {
	private String instance_uid;
	private int series_uid_id;
	private String image_file;
	private String source_ae;
	private int rcvd_date;
	
	public Image() {
		super();
	}

	public Image(int series_uid_id, String source_ae) {
		super();
		this.series_uid_id = series_uid_id;
		this.source_ae = source_ae;
	}

	public String getInstance_uid() {
		return instance_uid;
	}

	public void setInstance_uid(String instance_uid) {
		this.instance_uid = instance_uid;
	}

	public int getSeries_uid_id() {
		return series_uid_id;
	}

	public void setSeries_uid_id(int series_uid_id) {
		this.series_uid_id = series_uid_id;
	}

	public String getImage_file() {
		return image_file;
	}

	public void setImage_file(String image_file) {
		this.image_file = image_file;
	}

	public String getSource_ae() {
		return source_ae;
	}

	public void setSource_ae(String source_ae) {
		this.source_ae = source_ae;
	}

	public int getRcvd_date() {
		return rcvd_date;
	}

	public void setRcvd_date(int rcvd_date) {
		this.rcvd_date = rcvd_date;
	}
}
