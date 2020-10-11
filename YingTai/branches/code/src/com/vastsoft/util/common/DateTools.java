package com.vastsoft.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.inf.DayDoSomeThing;
import com.vastsoft.util.common.inf.MonthDoSomeThing;
import com.vastsoft.util.common.inf.YearDoSomeThing;

/**
 * 日期和时间的工具类，注意，此类的月份以1开始
 * 
 * @author jben
 *
 */
public class DateTools {
	/** 一分钟的毫秒数 */
	public static final long MINUTE_MILLISECONDS = 60 * 1000;
	/** 小时的毫秒数 */
	public static final long HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;
	/** 一天时间的毫秒数 */
	public static final long DAY_MILLISECONDS = 24 * HOUR_MILLISECONDS;
	
	public static Date getBirthdayByAge(int age){
		return getAfterYear(new Date(), -Math.abs(age));
	}
	
	/**
	 * 对指定的两个时间间按天数自增，每天调用一次DayDoSomeThing ，时间界限是包前不包后
	 * 
	 * @throws Exception
	 */
	public static void everyYear(Date start_month, Date end_month, YearDoSomeThing doSomeThing) throws Exception {
		while (true) {
			if (!start_month.before(end_month))
				break;
			if (isSameDay(start_month, end_month))
				break;
			doSomeThing.doSomething(getYear(start_month));
			start_month = getAfterYear(start_month, 1);
		}
	}

	/**
	 * 对指定的两个时间间按天数自增，每天调用一次DayDoSomeThing ，时间界限是包前不包后
	 * 
	 * @throws Exception
	 */
	public static void everyDay(Date start_month, Date end_month, DayDoSomeThing doSomeThing) throws Exception {
		while (true) {
			if (!start_month.before(end_month))
				break;
			if (isSameDay(start_month, end_month))
				break;
			doSomeThing.doSomething(getYear(start_month), getMonth(start_month), getDayOfMonth(start_month));
			start_month = getAfterDay(start_month, 1);
		}
	}

	/**
	 * 对指定的两个时间间按月数自增，每月调用一次MonthDoSomeThing ，时间界限是包前不包后
	 * 
	 * @throws Exception
	 */
	public static void everyMonth(Date start_month, Date end_month, MonthDoSomeThing doSomeThing) throws Exception {
		while (true) {
			if (!start_month.before(end_month))
				break;
			if (isSameMonth(start_month, end_month))
				break;
			doSomeThing.doSomething(getYear(start_month), getMonth(start_month));
			start_month = getAfterMonths(start_month, 1);
		}
	}

	public static String rangeToString(long millisSecond) {
		StringBuilder sbb = new StringBuilder();
		long range = millisSecond;
		if (range > DAY_MILLISECONDS) {
			sbb.append(range / DAY_MILLISECONDS).append("天");
		}
		range = range % DAY_MILLISECONDS;
		if (range > HOUR_MILLISECONDS) {
			sbb.append(range / HOUR_MILLISECONDS).append("小时");
		}
		range = range % HOUR_MILLISECONDS;
		if (range > MINUTE_MILLISECONDS) {
			sbb.append(range / MINUTE_MILLISECONDS).append("分钟");
		}
		range = range % MINUTE_MILLISECONDS;
		if (range > 1000) {
			sbb.append(range / 1000).append("秒");
		}
		range = range % 1000;
		if (range > 0)
			sbb.append(range).append("毫秒");
		return sbb.toString();
	}

	public static class TimeRange {
		private long start;
		private long end;

		public TimeRange() {
			this.start = System.currentTimeMillis();
		}

		public void start() {
			this.start = System.currentTimeMillis();
		}

		public void stop() {
			this.end = System.currentTimeMillis();
		}

		public String rangeToString() {
			long range = this.end - this.start;
			return DateTools.rangeToString(range);
		}
	}

	/** 从身份证号中查询出生日 */
	public static Date takeBirthdayByIdentityId(String identityId, Date default_date) {
		if (StringTools.isEmpty(identityId))
			return default_date;
		identityId = identityId.trim();
		if (!StringTools.wasIdentityId(identityId))
			return default_date;
		if (identityId.length() == 15) {
			String birthdayStr = "19" + identityId.substring(6, 12);
			return DateTools.strToDate(birthdayStr);
		} else if (identityId.length() == 18) {
			String birthdayStr = identityId.substring(6, 14);
			return DateTools.strToDate(birthdayStr);
		}
		return default_date;
	}

	/**
	 * 判断指定的时间是否是指定的年月日
	 * 
	 * @param date
	 *            指定的时间
	 * @param year
	 *            指定的年
	 * @param month
	 *            指定的月
	 * @param day
	 *            指定的日
	 * @return 返回是否是同一天
	 */
	public static boolean equals(Date date, int year, int month, int day) {
		return equals(date, getDate(year, month, day), TimeExactType.DAY);
	}

	/** 判断两个时间是否在指定的精度下相等 */
	public static boolean equals(Date date1, Date date2, TimeExactType tet) {
		if (date1 == null || date2 == null)
			return false;
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		switch (tet) {
		case YEAR:
			return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		case MONTH:
			if (!equals(date1, date2, TimeExactType.YEAR))
				return false;
			return cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		case DAY:
			if (!equals(date1, date2, TimeExactType.MONTH))
				return false;
			return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
		case HOUR:
			if (!equals(date1, date2, TimeExactType.DAY))
				return false;
			return cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY);
		case MINUTE:
			if (!equals(date1, date2, TimeExactType.HOUR))
				return false;
			return cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE);
		case SECOND:
			if (!equals(date1, date2, TimeExactType.MINUTE))
				return false;
			return cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND);
		case MILLISECOND:
			return date1.getTime() == date2.getTime();
		default:
			return date1.equals(date2);
		}
	}

	/** 检查是否是合法的生日 */
	public static boolean isBirthday(Date birthday) {
		if (birthday == null)
			return false;
		return birthday.before(new Date());
	}

	/** 获取两个时间的间隔天数 */
	public static int daysBetween(Date d1, Date d2) {
		if (isSameDay(d1, d2))
			return 0;
		long t1 = d1.getTime();
		long t2 = d2.getTime();
		if (t1 == t2)
			return 0;
		Date beginDate = null;
		Date endDate = null;
		if (t1 > t2) {
			beginDate = d2;
			endDate = d1;
		} else {
			beginDate = d1;
			endDate = d2;
		}
		int result = 0;
		while (true) {
			if (isSameDay(beginDate, endDate))
				return result;
			result++;
			beginDate = getAfterDay(beginDate, 1);
		}
	}

	/** 判断是否是同一年 */
	public static boolean isSameYear(Date d1, Date d2) {
		return equals(d1, d2, TimeExactType.YEAR);
	}

	/** 判断是否是同一个月 */
	public static boolean isSameMonth(Date d1, Date d2) {
		return equals(d1, d2, TimeExactType.MONTH);
	}

	/** 判断两个时间是否是同一天 */
	public static boolean isSameDay(Date d1, Date d2) {
		return equals(d1, d2, TimeExactType.DAY);
	}

	public static Date getYestoday() {
		return getYestoday(new Date());
	}

	public static Date getYestoday(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_MONTH, -1);
		return ca.getTime();
	}

	/** 获取两个时间间隔的毫秒数 */
	public static long timesBetweenAsMillisecond(Date time1, Date time2) {
		long ttt1 = time1.getTime();
		long ttt2 = time2.getTime();
		if (ttt1 == ttt2)
			return 0;
		if (ttt1 > ttt2)
			return ttt1 - ttt2;
		return ttt2 - ttt1;
	}

	/** 获取两个时间间隔的秒数,此方法会向下取整 */
	public static long timesBetweenAsSecond(Date time1, Date time2) {
		long ttt1 = timesBetweenAsMillisecond(time1, time2);
		return ttt1 / 1000;
	}

	/** 获取两个时间间隔的分钟,此方法会向下取整 */
	public static long timesBetweenAsMinute(Date time1, Date time2) {
		long ttt1 = timesBetweenAsMillisecond(time1, time2);
		return ttt1 / MINUTE_MILLISECONDS;
	}

	/** 获取两个时间间隔的小时数,此方法会向下取整 */
	public static long timesBetweenAsHour(Date time1, Date time2) {
		long ttt1 = timesBetweenAsMillisecond(time1, time2);
		return ttt1 / HOUR_MILLISECONDS;
	}

	/** 获取两个时间间隔的小时数,此方法会向下取整 */
	public static long timesBetweenAsDay(Date time1, Date time2) {
		long ttt1 = timesBetweenAsMillisecond(time1, time2);
		return ttt1 / DAY_MILLISECONDS;
	}

	/** 获取指定日期的月最后一天的最后一毫秒 注意：已知使用此方法生成的时间插入MYSQL会得到+1天的结果*/
	@Deprecated
	public static Date getEndTimeOfMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DAY_OF_MONTH), 23, 59, 59,
				999);
	}
	
	/** 获取指定日期的月最后一天的最后一毫秒 注意：已知使用此方法生成的时间插入MYSQL会得到+1天的结果*/
	public static Date getEndSecondOfMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DAY_OF_MONTH), 23, 59, 59,
				0);
	}
	
	/** 获取指定日期的月最后一天的最后一毫秒 注意：已知使用此方法生成的时间插入MYSQL会得到+1天的结果*/
	public static Date getEndMillisecondOfMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DAY_OF_MONTH), 23, 59, 59,
				999);
	}
	
	/** 获取指定日期的月最后一天的开始时间*/
	public static Date takeEndDayOfMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	/**
	 * 时间精确度
	 * 
	 * @author jben
	 *
	 */
	public static enum TimeExactType {
		/** 精确到年 */
		YEAR("yyyy"),
		/** 精确到月 */
		MONTH("yyyy'-'MM"),
		/** 精确到日 */
		DAY("yyyy'-'MM'-'dd"),
		/** 精确到时 */
		HOUR("yyyy'-'MM'-'dd' 'HH"),
		/** 精确到分 */
		MINUTE("yyyy'-'MM'-'dd' 'HH':'mm"),
		/** 精确到秒 */
		SECOND("yyyy'-'MM'-'dd' 'HH':'mm':'ss"),
		/** 精确到毫秒 */
		MILLISECOND("yyyy'-'MM'-'dd' 'HH':'mm':'ss' 'SSS");

		private String format;

		private TimeExactType(String format) {
			this.format = format;
		}

		public String getFormat() {
			return format;
		}
	}

	/** 获取指定天的最后一毫秒 */
	public static Date getEndTimeByDay(Date sd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(sd);
		return getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY) + 1, cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59,
				999);
	}

	/** 获取指定日期的年的最后一毫秒 */
	public static Date getEndTimeByYear(Date sd) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sd);
		int year = calendar.get(Calendar.YEAR);
		int lmonth = calendar.getActualMaximum(Calendar.MONTH);
		calendar.set(Calendar.MONTH, lmonth);
		int lday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return DateTools.getDate(year, lmonth + 1, lday, 23, 59, 59, 999);
	}

	/**
	 * 通过年月日获取时间,参数月以1开始，时分秒均为0
	 * 
	 * @param year
	 *            整数
	 * @param month
	 *            1-12
	 * @param day
	 *            1-31
	 */
	public static Date getDate(int year, int month, int day) {
		return getDate(year, month, day, 0, 0, 0);
	}

	/** 获取当前年 */
	public static int getCurrYear() {
		return getYear(new Date());
	}

	/** 获取指定时间的年 */
	public static int getYear(Date date) {
		return get(Calendar.YEAR, date);
	}

	/** 获取当前月，以1开始 */
	public static int getCurrMonth() {
		return getMonth(new Date());
	}

	/** 获取指定时间到现在的月数,以1-12开始 */
	public static int getMonth(Date date) {
		return get(Calendar.MONTH, date) + 1;
	}

	/**
	 * 获取指定时间之后的分钟数,负数表示之前
	 * 
	 * @param date
	 * @param i
	 *            之后的多少分钟，负数表示之前
	 * @return
	 */
	public static Date getAfterMinute(Date date, int i) {
		if (date == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, i);
		return calendar.getTime();
	}

	/**
	 * 获取指定时间之后的小时数,负数表示之前
	 * 
	 * @param date
	 * @param i
	 *            之后的多少小时，负数表示之前
	 * @return
	 */
	public static Date getAfterHour(Date date, int i) {
		if (date == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, i);
		return calendar.getTime();
	}

	/**
	 * 获取指定时间的指定个年数之后的时间，负数表示之前
	 * 
	 * @param date
	 * @param amount
	 *            之后的多少年，负数表示之前
	 * @return
	 */
	public static Date getAfterYear(Date date, int amount) {
		if (date == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, amount);
		return calendar.getTime();
	}

	/** 获取指定时间的指定个月之后的时间，负数表示之前 */
	public static Date getAfterMonths(Date date, int amount) {
		if (date == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, amount);
		return calendar.getTime();
	}

	/** 获取当前时间的一个月之前的时间 */
	public static Date getBeforeOneMonth() {
		return getAfterMonths(new Date(), -1);
	}

	/** 获取今天的日期 */
	public static int getCurrDayOfMonth() {
		return get(Calendar.DAY_OF_MONTH, new Date());
	}

	/** 获取当前月的最后一天的日期 */
	public static int getEndDayOfCurrMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/** 获取指定时间的日期 */
	public static int getDayOfMonth(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/** 获取指定时间的月的最后一天的日期 */
	public static int getEndDayOfMonth(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/** 获取i天之后的当前时间,当I为负数则表示之前 */
	public static Date getAfterDay(Date time, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.DAY_OF_YEAR, i);
		return cal.getTime();
	}

	/** 获取指定时间天的开始的时间 */
	public static Date getStartTimeByDay(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		return getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	/** 获取指定时间所在月份的开始时间 */
	public static Date getStartTimeByMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
	}

	/** 获取指定时间所在年的开始时间 */
	public static Date getStartTimeByYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getDate(cal.get(Calendar.YEAR), 1, 1, 0, 0, 0);
	}

	/** 获取明天的开始时间 */
	public static Date getBeginTimeOfTomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	/** 获取最近指定天数开始的时间 */
	public static List<Date> getLastDays(int days) {
		if (days <= 0)
			return null;
		List<Date> arrayDates = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1); // 要先+1,才能把本月的算进去
		for (int i = 0; i < days; i++) {
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1); // 逐次往前推1个月
			arrayDates.add(0, getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0));
		}
		return arrayDates;
	}

	/** 获取,详见Calendar类 */
	public static int get(int field, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(field);
	}

	/**
	 * 获取最近12个月，经常用于统计图表的X轴
	 */
	public static Date[] getLast12Months() {
		Date[] last12Months = new Date[12];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); // 要先+1,才能把本月的算进去
		for (int i = 0; i < 12; i++) {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); // 逐次往前推1个月
			last12Months[11 - i] = getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
		}
		return last12Months;
	}

	/** 获取指定日期的月最后一天的最后一毫秒 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DAY_OF_MONTH), 23, 59, 59,
				999);
	}

	/** 通过年月日时分秒获取时间,注意，月以1开始 */
	public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
		return getDate(year, month, day, hour, minute, second, 0);
	}

	/** 通过年月日时分秒获取时间,注意，月以1开始(1-12) */
	public static Date getDate(int year, int month, int day, int hour, int minute, int second, int milliSecond) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hour, minute, second);
		cal.set(Calendar.MILLISECOND, milliSecond);
		return cal.getTime();
	}

	/**
	 * 根据用户生日计算年龄
	 */
	public static int getAgeByBirthday(Date birthday) {
		if (birthday == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthday))
			return -1;
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if (monthNow > monthBirth)
			return age;
		if (monthNow < monthBirth)
			return --age;
		if (dayOfMonthNow < dayOfMonthBirth)
			return --age;
		return age;
	}

	/**
	 * 获取指定时间的格式字符串<br>
	 * 默认精确到秒
	 */
	public static String dateToString(Date d) {
		try {
			return new SimpleDateFormat(TimeExactType.SECOND.getFormat()).format(d);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取指定时间的格式字符串<br>
	 * 默认精确到秒
	 */
	public static String dateToString(Date d, String format) {
		try {
			return new SimpleDateFormat(format).format(d);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据精确度获取指定时间的格式字符串<br>
	 * 默认精确到秒
	 */
	public static String dateToString(Date d, TimeExactType format) {
		try {
			if (format != null)
				return new SimpleDateFormat(format.getFormat()).format(d);
			return new SimpleDateFormat(TimeExactType.SECOND.getFormat()).format(d);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 检查日期字符串是否是指定格式
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param formatStr
	 *            日期格式
	 * @return 是否是指定日期形式
	 */
	public static boolean checkDateFormatAndValite(String dateStr, String formatStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			Date ndate = sdf.parse(dateStr);
			String str = sdf.format(ndate);
			if (str.equals(dateStr))
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/** 将字符串按默认的时间格式化自动转换为最相近的格式，如果不能转换返回NULL */
	public static Date strToDate(String dstr, Date defaultValue, String... formats) {
		Date result = strToDate(dstr, formats);
		if (result == null)
			return defaultValue;
		return result;
	}

	/** 将字符串按默认的时间格式化自动转换为最相近的格式，如果不能转换返回NULL */
	public static Date strToDate(String dstr, String... formats) {
		if (dstr == null)
			return null;
		String[] format = { "yyyy'-'MM'-'dd", "yyyy'-'MM'-'dd' 'HH':'mm':'ss", "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'",
				"yyyy'/'MM'/'dd", "yyyyMMdd", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMMddHHmmssSSS" };
		List<String> listFormat = null;
		if (formats != null && formats.length > 0) {
			listFormat = CollectionTools.arrayToList(formats);
		}

		if (listFormat == null) {
			listFormat = CollectionTools.arrayToList(format);
		} else {
			listFormat.addAll(CollectionTools.arrayToList(format));
		}
		try {
			for (String fm : listFormat) {
				if (checkDateFormatAndValite(dstr, fm)) {
					return new SimpleDateFormat(fm).parse(dstr);
				}
			}
			for (String fm : listFormat) {
				int fml = fm.replace("'", "").length();
				if (dstr.length() >= fml) {
					String ss = dstr.substring(0, fml);
					if (checkDateFormatAndValite(ss, fm)) {
						return new SimpleDateFormat(fm).parse(ss);
					}
				}
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 获取时间的年，以字符串形式返回 */
	public static String YearToStr(Date d) {
		return new SimpleDateFormat("yyyy").format(d);
	}

	/** 获取时间的月和日，以字符串的形式返回 */
	public static String MonthDayToStr(Date d) {
		return new SimpleDateFormat("MMdd").format(d);
	}

	/** 获取过去的最近n个年份的开始时间 */
	public static Date[] getLastYear(int n) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		Date[] arrayDate = new Date[n];
		for (int i = 1; i <= n; i++) {
			arrayDate[i - 1] = DateTools.getDate(year - n + i, 1, 1, 0, 0, 0);
		}
		return arrayDate;
	}

	/** 获取本月开始到今天的所有天数 */
	public static Date[] getAllDaysCurrMonth() {
		Calendar cal = Calendar.getInstance();
		int currDay = cal.get(Calendar.DAY_OF_MONTH);
		Date[] arrayDay = new Date[currDay];
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		for (int i = 1; i <= currDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i);
			arrayDay[i - 1] = cal.getTime();
		}
		return arrayDay;
	}

	/** 获取指定天的最后一秒 */
	public static Date getLastTimeByDay(Date sd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(sd);
		return getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
	}

	/** 获取指定日期的年的最后一秒 */
	public static Date getLastTimeByYear(Date sd) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sd);
		int year = calendar.get(Calendar.YEAR);
		int lmonth = calendar.getActualMaximum(Calendar.MONTH);
		calendar.set(Calendar.MONTH, lmonth);
		int lday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return DateTools.getDate(year, lmonth + 1, lday, 23, 59, 59);
	}
}
