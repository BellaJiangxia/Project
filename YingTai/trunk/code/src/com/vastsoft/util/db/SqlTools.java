package com.vastsoft.util.db;

import com.vastsoft.util.common.StringTools;

public class SqlTools {
	/**
	 * 将字符串组装成in结果集，不带括号
	 * 
	 * @param str
	 * @return
	 * @since 0.0005
	 */
	public static String buildINSqlStr(String... str) {
		if (str == null || str.length <= 0)
			return null;
		StringBuilder sbb = new StringBuilder();
		boolean wasAdd = false;
		for (String string : str) {
			if (StringTools.isEmpty(string))
				continue;
			if (wasAdd)
				sbb.append(',');
			sbb.append('\'').append(string).append('\'');
			wasAdd = true;
		}
		return sbb.toString();
	}

	/**
	 * 将字符串组装成in结果集，不带括号
	 * 
	 * @param str
	 * @return
	 * @since 0.0005
	 */
	public static String buildINSqlStr(long... str) {
		if (str == null || str.length <= 0)
			return null;
		StringBuilder sbb = new StringBuilder();
		boolean wasAdd = false;
		for (long lo : str) {
			if (wasAdd)
				sbb.append(',');
			sbb.append('\'').append(lo).append('\'');
			wasAdd = true;
		}
		return sbb.toString();
	}

	/**
	 * 将字符串组装成in结果集，不带括号
	 * 
	 * @param str
	 * @return
	 * @since 0.0005
	 */
	public static String buildINSqlStr(Long... str) {
		if (str == null || str.length <= 0)
			return null;
		StringBuilder sbb = new StringBuilder();
		boolean wasAdd = false;
		for (Long lo : str) {
			if (lo == null)
				continue;
			if (wasAdd)
				sbb.append(',');
			sbb.append('\'').append(lo).append('\'');
			wasAdd = true;
		}
		return sbb.toString();
	}
}
