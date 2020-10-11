package com.vastsoft.utils.annotation;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public class ActionDescHandler {

	public static String getActionDescription(Class clazz, String strMethodName) {
		if (clazz == null)
			return null;
		try {
			@SuppressWarnings("unchecked")
			Method method = clazz.getMethod(strMethodName);
			ActionDesc ad = method.getAnnotation(ActionDesc.class);
			if (ad==null)return null;
			return ad.value();
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
}
