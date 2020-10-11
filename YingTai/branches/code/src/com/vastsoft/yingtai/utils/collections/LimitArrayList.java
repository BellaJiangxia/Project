package com.vastsoft.yingtai.utils.collections;

import java.util.ArrayList;
import java.util.Collection;

/** 有固定容量的列表的一种实现，当列表达到容量限制时，将不可再添加 */
public class LimitArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = 3473181098543520329L;
	private int limit_size;

	public LimitArrayList(int limit_size) {
		super();
		this.limit_size = limit_size;
	}

	@Override
	public boolean add(E e) {
		if (this.full())
			return false;
		return super.add(e);
	}

	@Override
	public void add(int index, E element) {
		if (this.full())
			return;
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (this.size() + c.size() >= this.limit_size)
			return false;
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (this.size() + c.size() >= this.limit_size)
			return false;
		return super.addAll(index, c);
	}

	public boolean full() {
		return this.size() >= this.limit_size;
	}

	public int getLimit_size() {
		return limit_size;
	}
}
