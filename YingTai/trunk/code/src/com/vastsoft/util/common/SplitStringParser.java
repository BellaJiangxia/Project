package com.vastsoft.util.common;

import com.vastsoft.util.common.inf.StringParser;

public class SplitStringParser<T> {
	private String splitStr;
	private char splitor=',';
	private StringParser<T> parser;
	/**默认是引文逗号分隔*/
	public SplitStringParser(String splitStr,StringParser<T> parser){
		this.splitStr = splitStr;
		this.parser = parser;
	}
	
	public SplitStringParser(String splitStr,char splitor,StringParser<T> parser){
		this(splitStr,parser);
		this.splitor = splitor;
	}
	
	public T[] parseAsArray(Class<T> clazz){
		String[] sp_strs = StringTools.splitString(splitStr, splitor);
		if (ArrayTools.isEmpty(sp_strs))
			return null;
		@SuppressWarnings("unchecked")
		T[] result = (T[]) ArrayTools.newInstance(clazz, sp_strs.length);
		for (int i = 0; i < sp_strs.length; i++) {
			String t = sp_strs[i];
			result[i] = parser.parse(t);
		}
		return result;
	}
	
	public static Long[] parseAsLongArray(String splitStr){
		return parseAsLongArray(splitStr, ',');
	}
	public static Long[] parseAsLongArray(String splitStr,char splitor){
		SplitStringParser<Long> rras = new SplitStringParser<Long>(splitStr, new StringParser<Long>() {
			@Override
			public Long parse(String str) {
				return Long.valueOf(str);
			}
		});
		return rras.parseAsArray(Long.class);
	}
	
	public static Integer[] parseAsIntegerArray(String splitStr){
		return parseAsIntegerArray(splitStr, ',');
	}
	public static Integer[] parseAsIntegerArray(String splitStr,char splitor){
		SplitStringParser<Integer> rras = new SplitStringParser<Integer>(splitStr, new StringParser<Integer>() {
			@Override
			public Integer parse(String str) {
				return Integer.valueOf(str);
			}
		});
		return rras.parseAsArray(Integer.class);
	}
	
	public static String[] parseAsStringArray(String splitStr){
		return parseAsStringArray(splitStr, ',');
	}
	public static String[] parseAsStringArray(String splitStr,char splitor){
		SplitStringParser<String> rras = new SplitStringParser<String>(splitStr, new StringParser<String>() {
			@Override
			public String parse(String str) {
				return str;
			}
		});
		return rras.parseAsArray(String.class);
	}
}
