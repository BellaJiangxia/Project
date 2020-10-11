package com.vastsoft.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RulesValidator {
	public final static RulesValidator instance = new RulesValidator();

	private RulesValidator() {
	}

	/**
	 * 验证邮箱是否合法
	 * 
	 * @param strEmail
	 *            邮箱地址
	 * @return
	 */
	public synchronized boolean validateEmail(String strEmail) {
		try {
			Pattern regEmail = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$");
			Matcher matcher = regEmail.matcher(strEmail);

			return matcher.matches();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 验证手机是否合法
	 * 
	 * @param strMobilePhone
	 *            手机号码
	 * @return
	 */
	public synchronized boolean validateMobilePhone(String strMobilePhone) {
		try {
			Pattern regEmail = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
			Matcher m = regEmail.matcher(strMobilePhone);

			return m.matches();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 验证用户名是否合法
	 * 
	 * @param strAccountName
	 *            用户名
	 * @return
	 */
	public synchronized boolean validateAccountName(String strAccountName) {
		try {
			Pattern regAccount = Pattern.compile("^[a-zA-Z]{1}[0-9a-zA-Z_]{5,17}$");
			Matcher m = regAccount.matcher(strAccountName);

			return m.matches();
		} catch (Exception e) {
			return false;
		}
	}
}
