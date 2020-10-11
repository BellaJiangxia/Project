package com.vastsoft.util.common.attributeUtils.annotation;

import java.lang.reflect.Method;

public class IgnoreAttrHandle {
	/** 获取是否忽略 */
	public static boolean needIgnore(Method method) {
		if (method == null)
			return false;
		try {
			IgnoreAttr ad = method.getAnnotation(IgnoreAttr.class);
			if (ad == null)
				return false;
			return true;
		} catch (Exception e) {
			return true;
		}
	}
}
