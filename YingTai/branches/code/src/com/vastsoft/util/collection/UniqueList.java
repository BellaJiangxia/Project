package com.vastsoft.util.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 一个不重复的列表的实现，继承至 ArrayList,不重复使用equals方法测试
 * 
 * @author jben
 * @param <E>
 */
public class UniqueList<E> extends ArrayList<E> {
	private static final long serialVersionUID = -4511937598311824534L;
	private boolean allowNull;
	
	/**
	 * 默认不允许包含null元素
	 */
	public UniqueList() {
		super();
		this.allowNull = false;
	}
	/**
	 * 默认不允许包含null元素
	 */
	public UniqueList(Collection<? extends E> c) {
		super(c);
		this.allowNull = false;
	}
	/**
	 * 默认不允许包含null元素
	 */
	public UniqueList(int initialCapacity) {
		super(initialCapacity);
		this.allowNull = false;
	}
	
	/**
	 * @param c
	 * @param allowNull
	 */
	public UniqueList(Collection<? extends E> c,boolean allowNull) {
		super(c);
		this.allowNull = allowNull;
	}

	/**
	 * @param initialCapacity
	 * @param allowNull
	 */
	public UniqueList(int initialCapacity,boolean allowNull) {
		super(initialCapacity);
		this.allowNull = allowNull;
	}
	
	/**
	 * @param allowNull 是否允许null元素
	 */
	public UniqueList(boolean allowNull) {
		super();
		this.allowNull = allowNull;
	}

	/**
	 * 添加一个元素，返回是否成功，如果列表中已经存在，将返回true
	 * 
	 */
	@Override
	public boolean add(E e) {
		if (!allowNull && e == null)
			return false;
		if (this.contains(e))
			return true;
		return super.add(e);
	}

	@Override
	public void add(int index, E e) {
		if (!allowNull && e == null)
			return;
		if (this.contains(e))
			return;
		super.add(index, e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			if (!this.add(e))
				return false;
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		for (E e : c) {
			if (!allowNull && e == null)
				return false;
			if (this.contains(e))
				return false;
		}
		return super.addAll(index, c);
	}

	public boolean isAllowNull() {
		return allowNull;
	}
}
