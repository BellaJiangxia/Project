package com.vastsoft.util.db;

import java.io.Serializable;

/**
 * 分页模型
 * 
 * @author jyb
 */
public class SplitPageUtil implements Serializable {
	private static final long serialVersionUID = 3744243970457063079L;

	/** 总行数 */
	private long totalRow = 0;
	/** 当前页数 */
	private long currPage = 1;
	/**
	 * 每页总行数,及期望每行总行数<br>
	 * 注意： 实际本页总行数可能小于此数字
	 */
	private int perPageCount = 10;

	public SplitPageUtil() {
		super();
	}

	/**
	 * 构建一个新的分页模型
	 * 
	 * @param currPage
	 *            当前页，以1为开始
	 * @param perPageCount
	 *            每页行数，默认为10
	 */
	public SplitPageUtil(int currPage, int perPageCount) {
		super();
		this.currPage = currPage;
		this.perPageCount = perPageCount;
	}

	/**
	 * 构建一个新的分页模型
	 * 
	 * @param currPage
	 *            当前页，以1为开始
	 * @param perPageCount
	 *            每页行数，默认为10
	 * @param totalRow
	 *            总行数
	 */
	public SplitPageUtil(int currPage, int perPageCount, int totalRow) {
		super();
		this.currPage = currPage;
		this.perPageCount = perPageCount;
		this.totalRow = totalRow;
	}

	/** 返回本分页是否是盛满状态 */
	public boolean isFull() {
		return getCurrPageCount() >= perPageCount;
	}

	/** 重置本工具，回到初始状态 */
	public void reSet() {
		totalRow = 0;
		// totalPage=0;
		currPage = 1;
		perPageCount = 10;
	}

	public void copyTo(SplitPageUtil spu) {
		spu.setCurrPage(currPage);
		spu.setPerPageCount(perPageCount);
		spu.setTotalRow(totalRow);
	}

	@Override
	public String toString() {
		return "SplitPageUtil [totalRow=" + totalRow + ", currPage=" + currPage + ", perPageCount=" + perPageCount
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (currPage ^ (currPage >>> 32));
		result = prime * result + perPageCount;
		result = prime * result + (int) (totalRow ^ (totalRow >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SplitPageUtil other = (SplitPageUtil) obj;
		if (currPage != other.currPage)
			return false;
		if (perPageCount != other.perPageCount)
			return false;
		if (totalRow != other.totalRow)
			return false;
		return true;
	}

	/** 获取总行数 */
	public long getTotalRow() {
		return totalRow;
	}

	/** 设置总行数 */
	public void setTotalRow(long totalRow) {
		if (totalRow < 0)
			totalRow = 0;
		this.totalRow = totalRow;
		long totalpage = getTotalPage();
		if (currPage > totalpage)
			currPage = totalpage;
	}

	/** 当前页的最小行，被当前页包括 */
	public long getCurrMinRowNum() {
		if (currPage < 1)
			return 1;
		long cc = (currPage - 1) * perPageCount + 1;
		cc = cc > totalRow ? 0 : cc;
		return cc;
	}

	/** 当前页最大行，不被当前页包括 */
	public long getCurrMaxRowNum() {
		long currmr = getCurrMinRowNum();
		currmr = currmr + perPageCount;
		return currmr;
	}

	public long getCurrPage() {
		return currPage;
	}

	/** 设置当前页码 */
	public void setCurrPage(long currPage) {
		if (currPage <= 0)
			currPage = 1;
		this.currPage = currPage;
	}

	public int getPerPageCount() {
		return perPageCount;
	}

	/** 设置每页条数 */
	public void setPerPageCount(int perPageCount) {
		if (perPageCount <= 0)
			return;
		this.perPageCount = perPageCount;
	}

	public long getCurrPageCount() {
		if (totalRow <= 0)
			return 0;
		if (perPageCount <= 0)
			return 0;
		if (perPageCount * currPage <= totalRow) {
			return perPageCount;
		}
		return totalRow - ((currPage - 1) * perPageCount);
	}

	public long getTotalPage() {
		if (totalRow <= 0)
			return 0;
		if (perPageCount <= 0)
			return 0;
		return totalRow % perPageCount == 0 ? totalRow / perPageCount : totalRow / perPageCount + 1;
	}

	public boolean isHasNextPage() {
		return currPage < this.getTotalPage();
	}

	/** 转换到下一页，返回是否转换成功 */
	public boolean nextPage() {
		if (!this.isHasNextPage())
			return false;
		this.currPage += 1;
		return true;
	}

	public boolean isHasPrevPage() {
		return currPage > 1;
	}

	public boolean prevPage() {
		if (!this.isHasPrevPage())
			return false;
		this.currPage--;
		return true;
	}
}
