package com.vastsoft.util;

import java.util.List;

public class SizableArray<T> {
	private Object array[] = null;

	public SizableArray(int sizeInit) {
		this.array = new Object[sizeInit];
	}

	/**
	 * 重置数组长度，如果指定的长度与当前数组长度一致，则不进行任何操作
	 * 
	 * @param iNewSize
	 */
	public void resize(int iNewSize) {
		this.resize(iNewSize, 0);
	}

	/**
	 * 重置数组长度，并且原来数据的位置从iPosDataBegin指定的位置开始存放。
	 * 
	 * @param iNewSize
	 *            数组的新尺寸
	 * @param iPosDataBegin
	 *            重置后的后的数组，数据开始的位置。
	 */
	public void resize(int iNewSize, int iPosDataBegin) {
		if (this.array.length < iNewSize) {
			int iCopyCnt = this.array.length;

			if (iNewSize - this.array.length < iPosDataBegin)
				iCopyCnt = this.array.length - (iPosDataBegin - (iNewSize - this.array.length));

			Object arrNew[] = new Object[iNewSize];
			System.arraycopy(this.array, 0, arrNew, iPosDataBegin, iCopyCnt);

			this.array = arrNew;
		} else if (this.array.length > iNewSize) {
			Object arrNew[] = new Object[iNewSize];
			System.arraycopy(this.array, 0, arrNew, iPosDataBegin, iNewSize - iPosDataBegin);

			this.array = arrNew;
		}
	}

	public void set(int iIdx, T t) throws ArrayIndexOutOfBoundsException {
		if (iIdx < this.array.length)
			this.array[iIdx] = t;
		else
			throw new ArrayIndexOutOfBoundsException();
	}

	@SuppressWarnings("unchecked")
	public T get(int iIdx) throws ArrayIndexOutOfBoundsException {
		if (iIdx < this.array.length)
			return (T) this.array[iIdx];
		else
			throw new ArrayIndexOutOfBoundsException();
	}

	public SizableArray<T> subArray(int iBeginIdx, int iCount) {
		if (iBeginIdx >= this.array.length)
			throw new ArrayIndexOutOfBoundsException();

		if (iBeginIdx + iCount >= this.array.length)
			throw new ArrayIndexOutOfBoundsException();

		Object arrRet[] = new Object[iCount];
		System.arraycopy(this.array, iBeginIdx, arrRet, 0, iCount);

		SizableArray<T> sa = new SizableArray<T>(iCount);

		System.arraycopy(this.array, iBeginIdx, sa.array, 0, iCount);

		return sa;
	}

	@SuppressWarnings("unchecked")
	public void asList(int iBeginIdx, int iCount, List<T> list) throws ArrayIndexOutOfBoundsException {
		if (iBeginIdx >= this.array.length)
			throw new ArrayIndexOutOfBoundsException();

		if (iBeginIdx + iCount > this.array.length)
			throw new ArrayIndexOutOfBoundsException();

		list.clear();

		for (int i = 0; i < iCount; i++)
			list.add((T) this.array[iBeginIdx + i]);
	}

	public void clear() {
		for (int i = 0; i < this.array.length; i++)
			this.array[i] = null;
	}

	public int size() {
		return this.array.length;
	}
}
