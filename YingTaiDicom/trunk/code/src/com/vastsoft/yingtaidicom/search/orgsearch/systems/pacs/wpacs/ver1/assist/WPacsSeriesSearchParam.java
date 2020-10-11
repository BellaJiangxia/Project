package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist;

public class WPacsSeriesSearchParam extends AbstractWPacsSearchParam {
	private String stuuid;
	
	public WPacsSeriesSearchParam(String[] aets_arr) {
		super(aets_arr);
	}

	public String getStuuid() {
		return stuuid;
	}

	public void setStuuid(String stuuid) {
		this.stuuid = stuuid;
	}

}
