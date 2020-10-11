package com.vastsoft.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vastsoft.util.common.inf.EqualsFilter;

public class CollectionTools {
	public static <T> List<T> collectionToList(Collection<T> values) {
		return new ArrayList<T>(values);
	}

	/**
	 * 对列表去重,使用指定的相等过滤器测试是否相等，如果相等将留下相等的第一个对象
	 * 
	 * @param <T>
	 */
	public static <T> Collection<T> collectionDedup(Collection<T> listOrgAffix,EqualsFilter<T> equalsFilter) {
		if (listOrgAffix == null || listOrgAffix.size() <= 0)
			return listOrgAffix;
		List<T> listObj = new ArrayList<T>();
		for (Iterator<T> iterator = listOrgAffix.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			if (contains(listObj,t,equalsFilter))
				iterator.remove();
			else
				listObj.add(t);
		}
		return listOrgAffix;
	}
	
	public static <T> boolean contains(Collection<T> listObj,T tObj,EqualsFilter<T> equalsFilter){
		if (isEmpty(listObj))
			return false;
		for (T t : listObj) {
			if (equalsFilter.equals(t,tObj))
				return true;
		}
		return false;
	}
	
	/**
	 * 对列表去重,使用equals进行测试
	 * 
	 * @param <T>
	 */
	public static <T> Collection<T> collectionDedup(Collection<T> listOrgAffix) {
		if (listOrgAffix == null || listOrgAffix.size() <= 0)
			return listOrgAffix;
		List<T> listObj = new ArrayList<T>();
		for (Iterator<T> iterator = listOrgAffix.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			if (listObj.contains(t))
				iterator.remove();
			else
				listObj.add(t);
		}
		return listOrgAffix;
	}

	@SafeVarargs
	public static <T> List<T> buildList(boolean allowNull, T... ts) {
		if (ts == null || ts.length <= 0)
			return null;
		List<T> result = new ArrayList<T>();
		for (T t : ts) {
			if (!allowNull && t == null)
				continue;
			result.add(t);
		}
		return result;
	}

	@SafeVarargs
	public static <T> List<T> buildList(T... ts) {
		return buildList(false, ts);
	}

	/**
	 * 将数组转换为list,不是数组返回null
	 * 
	 * @param <T>
	 */
	public static <T> List<T> arrayToList(T[] obj) {
		if (obj == null)
			return null;
		List<T> list = new ArrayList<T>();
		if (obj.length <= 0)
			return list;
		for (T t : obj) {
			list.add(t);
		}
		return list;
	}

	/**
	 * 可一次性传入多个参数，返回这些参数组成的列表
	 * 
	 * @param <T>
	 * 
	 * @param feeIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> makeList(T... feeIds) {
		return arrayToList(feeIds);
	}

	/**
	 * 包含
	 * 
	 * @param <T>
	 */
	public static <E> boolean contains(Collection<E> Ts, E t) {
		if (t == null)
			return false;
		if (isEmpty(Ts))
			return false;
		return Ts.contains(t);
	}

	/** 去掉list中的空元素 */
	public static void collectionTrim(Collection<?> src) {
		if (src == null || src.size() <= 0)
			return;
		for (Iterator<?> iterator = src.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object == null)
				iterator.remove();
		}
	}

	public static boolean isEmptyWithOutTrim(Collection<?> list) {
		return list == null || list.size() <= 0;
	}

	public static boolean isEmpty(Collection<?> list) {
		collectionTrim(list);
		return isEmptyWithOutTrim(list);
	}

	/**
	 * 将两个集合合并，如果第一个参数不为null则返回的集合对象是第一个参数，此方法不去重
	 * 
	 * @param <E>
	 */
	public static <E> Collection<E> merge(Collection<E> co1, Collection<E> co2) {
		if (co1 == null)
			return co2;
		if (co2 == null)
			return co1;
		co1.addAll(co2);
		return co1;
	}

	/**
	 * 对两个Collection去重,去掉两个列表中重复的部分，并返回两个列表的集合列表
	 * 
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> discardDuplicate(Collection<? extends T> list1, Collection<? extends T> list2) {
		if (list1 == null || list1.size() <= 0)
			return (Collection<T>) list2;
		if (list2 == null || list2.size() <= 0)
			return (Collection<T>) list1;
		if (list1.size() > list2.size()) {
			Collection<T> result = new ArrayList<T>(list1);
			for (T object : list2) {
				if (!result.contains(object))
					result.add(object);
			}
			return result;
		} else {
			Collection<T> result = new ArrayList<T>(list2);
			for (T object : list1) {
				if (!result.contains(object))
					result.add(object);
			}
			return result;
		}
	}

	/**
	 * 判断长度是否等于指定值，当ce==null,则长度按0处理
	 * 
	 * @param <E>
	 */
	public static <E> boolean sizeAs(Collection<E> ce, int len) {
		if (ce == null)
			return len == 0;
		return ce.size() == len;
	}
}
