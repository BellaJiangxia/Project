package com.vastsoft.util.thread;

import java.util.Hashtable;

import com.vastsoft.util.common.StringTools;

/**
 * 线程保管员，保管线程变量
 * 
 * @author jben
 *
 */
public class ThreadStoreman {
	private static final ThreadLocal<Hashtable<String, Object>> threadLocal = new ThreadLocal<Hashtable<String, Object>>();

	public static Object takeData(String key) {
		if (StringTools.isEmpty(key))
			throw new NullPointerException("key 不可以为空，或者空字符串！");
		Hashtable<String, Object> mapData = threadLocal.get();
		if (mapData == null)
			return null;
		return mapData.get(key);
	}

	public static void putData(String key, Object value) {
		if (StringTools.isEmpty(key))
			throw new NullPointerException("key 不可以为空，或者空字符串！");
		Hashtable<String, Object> mapData = threadLocal.get();
		if (mapData == null) {
			mapData = new Hashtable<String, Object>();
			threadLocal.set(mapData);
		}
		mapData.put(key, value);
	}
}
