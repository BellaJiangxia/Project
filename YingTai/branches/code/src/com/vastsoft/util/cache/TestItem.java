package com.vastsoft.util.cache;

import com.vastsoft.util.GUID;

public class TestItem implements CachedItem {
	private String strGUID = "";

	public TestItem() {
		this.strGUID = new GUID().toString(false);
	}

	@Override
	public String getGUID() {
		return this.strGUID;
	}

}
