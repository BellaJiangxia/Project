package com.vastsoft.utils;

public class NumberUtils {
	/**求平均数*/
	public static double average(int ... is){
		long count=0;
		for (int i : is)
			count+=i;
		return count/(double)is.length;
	}
}
