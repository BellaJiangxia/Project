package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist;

public class WPacsImageSearchParam extends AbstractWPacsSearchParam {
	private String stuuid;
	private String srsuid;
	
	public WPacsImageSearchParam(String[] aets_arr) {
		super(aets_arr);
	}

	public String getStuuid() {
		return stuuid;
	}

	public void setStuuid(String stuuid) {
		this.stuuid = stuuid;
	}

	public String getSrsuid() {
		return srsuid;
	}

	public void setSrsuid(String srsuid) {
		this.srsuid = srsuid;
	}

}
