package com.vastsoft.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供分页功能的列表类
 * 
 * @author JoeRadar
 * 
 */
public class PageList<T> {
	private List<T> listData;

	private int iPageSize;

	// public PageList()
	// {
	// }

	/**
	 * 构造函数，需要提供List来创建此对象
	 * 
	 * @param list
	 */
	public PageList(List<T> list) {
		this.listData = list;
	}

	/**
	 * 设置一页的大小
	 * 
	 * @param iPageSize
	 */
	public void setPageSize(int iPageSize) {
		this.iPageSize = iPageSize;
	}

	/**
	 * 获取一页的大小
	 * 
	 * @return
	 */
	public int getPageSize() {
		return this.iPageSize;
	}

	/**
	 * 获取总页数
	 * 
	 * @return
	 */
	public int getPageCount() {
		if (this.listData == null)
			return 0;
		else
			return this.listData.size() / this.iPageSize + ((this.listData.size() % this.iPageSize) > 0 ? 1 : 0);
	}

	/**
	 * 获取指定页的内容
	 * 
	 * @param iPageIdx
	 * @return
	 */
	public List<T> getPage(int iPageIdx) {
		int iPageCnt = this.getPageCount();

		if (iPageIdx >= iPageCnt)
			iPageIdx = iPageCnt - 1;

		ArrayList<T> list = new ArrayList<T>();

		int iBaseIdx = iPageIdx * this.iPageSize;

		for (int i = 0; i < this.iPageSize; i++) {
			try {
				list.add(this.listData.get(iBaseIdx + i));
			} catch (IndexOutOfBoundsException e) {
				return list;
			}
		}

		return list;
	}
}
