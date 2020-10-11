package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist;

import com.vastsoft.util.common.SplitStringBuilder;
import com.vastsoft.util.db.AbstractSearchParam;

public abstract class AbstractWPacsSearchParam extends AbstractSearchParam {
	private String[] srcaet_arr;
	
	public AbstractWPacsSearchParam(String[] srcaet_arr) {
		super();
		this.srcaet_arr = srcaet_arr;
	}

	public String[] getAets_arr() {
		return srcaet_arr;
	}

	public void setAets_arr(String[] aets_arr) {
		this.srcaet_arr = aets_arr;
	}
	
	public String getAets() {
		return SplitStringBuilder.splitToString(this.srcaet_arr);
	}
}
