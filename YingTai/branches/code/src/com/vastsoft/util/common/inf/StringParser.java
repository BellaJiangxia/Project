package com.vastsoft.util.common.inf;

public interface StringParser<T> {
	/**把字符串解析为对象*/
	public T parse(String str);
}
