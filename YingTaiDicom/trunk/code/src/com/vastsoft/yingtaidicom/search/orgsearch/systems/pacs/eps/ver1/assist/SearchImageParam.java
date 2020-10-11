package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.assist;

import com.vastsoft.util.db.AbstractSearchParam;

public class SearchImageParam extends AbstractSearchParam {
	private String instance_uid;
	private Integer series_uid_id;
	private String source_ae;

	
	public SearchImageParam() {
		super();
	}

	public SearchImageParam(String instance_uid) {
		this.instance_uid = instance_uid;
	}

	public Integer getSeries_uid_id() {
		return series_uid_id;
	}

	public void setSeries_uid_id(Integer series_uid_id) {
		this.series_uid_id = series_uid_id;
	}

	public String getSource_ae() {
		return source_ae;
	}

	public void setSource_ae(String source_ae) {
		this.source_ae = source_ae;
	}

	public String getInstance_uid() {
		return instance_uid;
	}

	public void setInstance_uid(String instance_uid) {
		this.instance_uid = instance_uid;
	}
}
