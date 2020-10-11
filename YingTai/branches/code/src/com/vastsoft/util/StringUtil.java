package com.vastsoft.util;

public class StringUtil {

	public static String toHexString(byte value[]) {
		String newString = "";
		for (int i = 0; i < value.length; i++) {
			byte b = value[i];
			String str = Integer.toHexString(b);
			if (str.length() > 2)
				str = str.substring(str.length() - 2);
			if (str.length() < 2)
				str = "0" + str;
			newString = newString + str;
		}
		return newString.toLowerCase();
	}
	/***/
	public static boolean isValidString(String str, int length) {
		boolean flag = true;
		if (str != null && !str.isEmpty() && length > 0) {
			if (str.length() > length)
				flag = false;
		}
		return flag;
	}
}
