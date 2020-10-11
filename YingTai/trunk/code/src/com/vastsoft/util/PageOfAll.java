package com.vastsoft.util;

import java.util.List;

public class PageOfAll<T> {
	private int iTotalCnt;
	private int iPageIdx;
	private List<T> listData;

	public PageOfAll(List<T> listData, int iPageIdx, int iTotalCnt) {
		this.iTotalCnt = iTotalCnt;
		this.iPageIdx = iPageIdx;
		this.listData = listData;
	}

	public int getPageIdx() {
		return this.iPageIdx;
	}

	public int getTotalCount() {
		return this.iTotalCnt;
	}

	public List<T> getPage() {
		return this.listData;
	}
}
