package com.vastsoft.util.cache;

import java.util.List;

public interface DataLoader<T extends CachedItem> {
	public List<T> loadData(int iBeginIdx, int iCount);
}
