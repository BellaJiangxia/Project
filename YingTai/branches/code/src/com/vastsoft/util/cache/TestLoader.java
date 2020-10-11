package com.vastsoft.util.cache;

import java.util.ArrayList;
import java.util.List;

public class TestLoader implements DataLoader<TestItem> {
	@Override
	public List<TestItem> loadData(int iBeginIdx, int iCount) {
		ArrayList<TestItem> listNew = new ArrayList<TestItem>();

		for (int i = 0; i < iCount; i++)
			listNew.add(new TestItem());

		return listNew;
	}

}
