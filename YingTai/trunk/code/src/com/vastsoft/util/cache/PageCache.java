package com.vastsoft.util.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vastsoft.util.SizableArray;

/**
 * 支持分页功能的缓存
 * 
 * @author JoeRadar
 * 
 * @param <T>
 */
public class PageCache<T extends CachedItem> {
	// 缓存初始总大小，包含未使用的头部空间(INIT_HEAD_SIZE)
	private final static int INIT_SIZE = 32;

	// 缓存列表的头部一次性预留空间，此空间供新添数据使用
	private final static int INIT_HEAD_SIZE = 16;

	// 实际数据的起始位置
	private int iRealBeginIdx = INIT_HEAD_SIZE;

	private SizableArray<T> arrayData = new SizableArray<T>(INIT_SIZE);
	private HashMap<String, T> mapData = new HashMap<String, T>(INIT_SIZE);

	private DataLoader<T> loader = null;

	private boolean bOnceLoadInPage = true;

	/**
	 * 构造一个支持分页功能的缓存，需要外部实现一个数据加载器
	 * 
	 * @param loader
	 *            数据加载器
	 * @param bOnceLoadInPage
	 *            如果为true，当外部请求连续的数据时，从第一个缺失的数据开始，到最后一个缺失的数据都会被重新加载一遍，
	 *            即使其中部分数据已经被缓存。如果为false，则只加载缺失的部分数据，这可能会导致多次调用数据加载器进行数据加载。
	 */
	public PageCache(DataLoader<T> loader, boolean bOnceLoadInPage) {
		this.loader = loader;
		this.bOnceLoadInPage = bOnceLoadInPage;
	}

	public PageCache(DataLoader<T> loader) {
		this.loader = loader;
	}

	private List<T> getDataItemsOnceLoadInPage(int iBeginIdx, int iCount) {
		int ib = -1;
		int ie = -1;

		for (int i = 0; i < iCount; i++) {
			T obj = this.arrayData.get(this.iRealBeginIdx + iBeginIdx + i);

			if (obj == null && ib == -1)
				ib = i;

			if (obj == null)
				ie = i;
		}

		if (ib != -1) {
			int inb = iBeginIdx + ib;
			int inCnt = ie - ib + 1;

			List<T> listNewData = this.loader.loadData(inb, inCnt);

			if (listNewData == null)
				return null;

			if (listNewData.size() != 0) {
				if (this.arrayData.size() - this.iRealBeginIdx < iBeginIdx + iCount)
					this.arrayData.resize(this.iRealBeginIdx + iBeginIdx + iCount);
			}

			for (int i = 0; i < listNewData.size(); i++) {
				T objNew = listNewData.get(i);

				if (this.arrayData.get(this.iRealBeginIdx + inb + i) == null) {
					this.arrayData.set(this.iRealBeginIdx + inb + i, objNew);
					this.mapData.put(objNew.getGUID(), objNew);
				}
			}
		}

		ArrayList<T> list = new ArrayList<T>(iCount);
		this.arrayData.asList(this.iRealBeginIdx + iBeginIdx, iCount, list);
		int i;
		for (i = 0; i < list.size(); i++) {
			if (list.get(i) == null)
				break;
		}
		while (list.size() > i)
			list.remove(i);
		return list;
	}

	private List<T> getDataItemsMultiLoadInPage(int iBeginIdx, int iCount) {
		int ib = -1;
		int ie = -1;

		for (int i = 0; i < iCount; i++) {
			T obj = this.arrayData.get(this.iRealBeginIdx + iBeginIdx + i);

			if (obj == null && ib == -1)
				ib = i;

			if (obj == null)
				ie = i;

			if (obj != null && ie != -1) {
				int inb = iBeginIdx + ib;
				int inCnt = ie - ib + 1;

				List<T> listNewData = this.loader.loadData(inb, inCnt);

				if (listNewData == null)
					return null;

				if (listNewData.size() != 0) {
					if (this.arrayData.size() - this.iRealBeginIdx < iBeginIdx + iCount)
						this.arrayData.resize(this.iRealBeginIdx + iBeginIdx + iCount);
				}

				for (int j = 0; j < listNewData.size(); j++) {
					T objNew = listNewData.get(i);

					if (this.arrayData.get(this.iRealBeginIdx + inb + j) == null) {
						this.arrayData.set(this.iRealBeginIdx + inb + j, objNew);
						this.mapData.put(objNew.getGUID(), objNew);
					}
				}

				ib = -1;
				ie = -1;
			}
		}

		// 循环结束，加载最后一次缺失的数据。
		if (ib != -1) {
			int inb = iBeginIdx + ib;
			int inCnt = ie - ib + 1;

			List<T> listNewData = this.loader.loadData(inb, inCnt);

			if (listNewData == null)
				return null;

			for (int j = 0; j < listNewData.size(); j++) {
				T objNew = listNewData.get(j);

				if (this.arrayData.get(this.iRealBeginIdx + inb + j) == null) {
					this.arrayData.set(this.iRealBeginIdx + inb + j, objNew);
					this.mapData.put(objNew.getGUID(), objNew);
				}
			}
		}

		ArrayList<T> list = new ArrayList<T>(iCount);
		this.arrayData.asList(this.iRealBeginIdx + iBeginIdx, iCount, list);

		return list;
	}

	/**
	 * 根据GUID获取缓存的对象，如果请求的对象不存在，并不会导致从外部加载器加载数据，而是返回null。
	 * 
	 * @param strGUID
	 * @return
	 */
	public T checkDataItem(String strGUID) {
		return this.mapData.get(strGUID);
	}

	/**
	 * 请求连续的分页数据，如果请求的范围中有缺失的数据，会立刻从外部加载器(DataLoader)中加载。
	 * 
	 * @param iBeginIdx
	 * @param iCount
	 * @return
	 */
	public List<T> getCachedItems(int iBeginIdx, int iCount) {
		if (iBeginIdx < 0)
			return null;

		if (this.bOnceLoadInPage)
			return this.getDataItemsOnceLoadInPage(iBeginIdx, iCount);
		else
			return this.getDataItemsMultiLoadInPage(iBeginIdx, iCount);
	}

	/**
	 * 在缓存的前端增加一项
	 * 
	 * @param ci
	 */
	public void addTop(T t) {
		if (this.iRealBeginIdx == 0) {
			this.arrayData.resize(this.arrayData.size() + INIT_HEAD_SIZE, INIT_HEAD_SIZE);
			this.iRealBeginIdx = INIT_HEAD_SIZE;
		}

		this.iRealBeginIdx--;
		this.mapData.put(t.getGUID(), t);
		this.arrayData.set(this.iRealBeginIdx, t);
	}

	public void deleteItem(T t) {
	}

	public void reset() {
		this.arrayData.clear();
		this.iRealBeginIdx = INIT_HEAD_SIZE;
	}

	public static void main(String args[]) {
		PageCache<TestItem> pc = new PageCache<TestItem>(new TestLoader());

		List<TestItem> list = pc.getCachedItems(0, 3);
		System.out.println("from " + 5 + "  to " + 8);
		for (TestItem ti : list)
			System.out.println(ti.getGUID());

		pc.addTop(new TestItem());
		pc.addTop(new TestItem());
		pc.addTop(new TestItem());
		pc.addTop(new TestItem());

		pc.addTop(new TestItem());

		list = pc.getCachedItems(0, 10);
		System.out.println("from " + 0 + "  to " + 10);
		for (TestItem ti : list)
			System.out.println(ti.getGUID());
	}
}
