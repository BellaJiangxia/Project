package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist;

public class WPacsStudySearchParam extends AbstractWPacsSearchParam {
	private String pid;
	private String pname;
	
	public WPacsStudySearchParam(String[] aets_arr) {
		super(aets_arr);
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

}
