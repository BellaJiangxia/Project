package com.vastsoft.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vastsoft.util.common.inf.IterateReturnable;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.BreakIterateException;
import com.vastsoft.util.exception.IterateException;
import com.vastsoft.util.exception.ReturnIterateException;

/**
 * 间隔字符串生成器， 默认元素不能重复
 * 
 * @param <T>
 */
public class SplitStringBuilder<T> {
	private boolean canRepeat = false;// 是否允许元素重复，默认不能重复
	private String splitor;
	private List<T> objs = new ArrayList<T>();

	public SplitStringBuilder(T[] objs) {
		super();
		this.objs = CollectionTools.arrayToList(objs);
	}

	public SplitStringBuilder(Collection<T> objs) {
		super();
		this.objs = new ArrayList<T>(objs);
	}

	/** 默认使用‘,’分割 */
	public SplitStringBuilder() {
		super();
		splitor = ",";
	}

	/** 默认使用‘,’分割 */
	public SplitStringBuilder(boolean canRepeat) {
		super();
		splitor = ",";
		this.canRepeat = canRepeat;
	}

	public SplitStringBuilder(String splitor, List<T> objs) {
		super();
		this.splitor = splitor;
		this.objs = objs;
	}

	public SplitStringBuilder<T> addAll(T[] arrayObj) {
		if (ArrayTools.isEmpty(arrayObj))
			return this;
		for (T t : arrayObj)
			this.add(t);
		return this;
	}
	
	public SplitStringBuilder<T> addAll(Object arrayObj) {
		if (ArrayTools.isEmpty(arrayObj))
			return this;
		try {
			ArrayTools.iterateArray(arrayObj, new IterateReturnable<Object>(){
				@SuppressWarnings("unchecked")
				@Override
				public void run(Object t) throws ReturnIterateException, BreakIterateException, BaseException {
					SplitStringBuilder.this.add((T) t);
				}
			});
		} catch (IterateException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return this;
	}

	public SplitStringBuilder<T> addAll(Collection<T> collObj) {
		if (CollectionTools.isEmpty(collObj))
			return this;
		for (T t : collObj)
			this.add(t);
		return this;
	}

	public SplitStringBuilder<T> add(T obj) {
		if (obj == null)
			return this;
		if (!canRepeat) {
			if (this.objs.contains(obj))
				return this;
		}
		this.objs.add(obj);
		return this;
	}

	public String getSplitor() {
		return splitor;
	}

	public SplitStringBuilder<T> setSplitor(String splitor) {
		this.splitor = splitor;
		return this;
	}

	// public List<T> getObjs() {
	// return objs;
	// }

	// public void setObjs(List<T> objs) {
	// this.objs = objs;
	// }

	public SplitStringBuilder(String splitor) {
		super();
		this.splitor = splitor;
	}

	/** 是否此支付创建器重没有内容 */
	public boolean isEmpty() {
		return CollectionTools.isEmpty(this.objs);
	}

	public int size() {
		return this.objs == null ? 0 : this.objs.size();
	}

	public static <T> String splitToString(Collection<T> arrayObj, String splitor) {
		SplitStringBuilder<T> sss = new SplitStringBuilder<T>(splitor);
		sss.addAll(arrayObj);
		return sss.toString();
	}

	public static <T> String splitToString(Collection<T> arrayObj) {
		SplitStringBuilder<T> sss = new SplitStringBuilder<T>();
		sss.addAll(arrayObj);
		return sss.toString();
	}

	public static <T> String splitToString(T[] arrayObj) {
		SplitStringBuilder<T> sss = new SplitStringBuilder<T>();
		sss.addAll(arrayObj);
		return sss.toString();
	}

	public static <T> String splitToString(T[] arrayObj, String splitor) {
		SplitStringBuilder<T> sss = new SplitStringBuilder<T>(splitor);
		sss.addAll(arrayObj);
		return sss.toString();
	}
	
	public static <T> String splitToString(Object arrayObj) {
		SplitStringBuilder<T> sss = new SplitStringBuilder<T>();
		sss.addAll(arrayObj);
		return sss.toString();
	}

	public static <T> String splitToString(Object arrayObj, String splitor) {
		SplitStringBuilder<T> sss = new SplitStringBuilder<T>(splitor);
		sss.addAll(arrayObj);
		return sss.toString();
	}

	@Override
	public String toString() {
		if (CollectionTools.isEmpty(objs))
			return "";
		StringBuilder result = new StringBuilder();
		boolean isHas = false;
		for (Object object : objs) {
			if (object == null)
				continue;
			String con = String.valueOf(object);
			if (StringTools.isEmpty(con))
				continue;
			if (isHas)
				result.append(splitor);
			result.append(con);
			isHas = true;
		}
		return result.toString();
	}

}
