package com.vastsoft.util.common;

import com.vastsoft.util.base.BaseEnum;

public class EnumTools {
	/** 判断对象是否是枚举 */
	public static boolean isEnum(Object obj) {
		return ReflectTools.isEnum(obj.getClass());
	}

	/**
	 * @param cla
	 *            此类必须是枚举，否则始终返回null
	 * @param code
	 * @return
	 */
	public static <T extends BaseEnum> T parseCode(Class<T> cla, int code) {
		if (!ReflectTools.isEnum(cla))
			return null;
		T[] ts = cla.getEnumConstants();
		if (ArrayTools.isEmpty(ts))
			return null;
		for (T t : ts) {
			if (code == t.getCode())
				return t;
		}
		return null;
	}
}
