package com.vastsoft.utils.annotation;

import java.lang.reflect.Method;

public class IgnoreNullHandle {
	/**获取是否忽略null*/
	public static boolean getIgnoreNull(Method method) {
		if (method == null)
			return false;
		try {
			IgnoreNull ad = method.getAnnotation(IgnoreNull.class);
			if (ad==null)return false;
			return true;
		} catch (Exception e) {
			return true;
		}
	}
	
	/**获取是否忽略0*/
	public static boolean getIgnoreZero(Method method) {
		if (method == null)
			return false;
		try {
			IgnoreZero ad = method.getAnnotation(IgnoreZero.class);
			if (ad==null)return false;
			return true;
		} catch (Exception e) {
			return true;
		}
	}
}
