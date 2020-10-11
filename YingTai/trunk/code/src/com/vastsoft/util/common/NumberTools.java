package com.vastsoft.util.common;

import java.math.BigDecimal;
import java.util.List;

public class NumberTools {
	/** 将字符串转换成long */
	public static long parseLong(String src, long defVal) {
		try {
			return Long.parseLong(src);
		} catch (Exception e) {
			return defVal;
		}
	}
	
	/**将字符串使用','作为分隔符拆分后转成long数组返回
	 * @param arrayStr 指定字符串
	 * @return
	 */
	public static long[] stringToLongArray(String arrayStr){
		return stringToLongArray(arrayStr, ',');
	}
	
	/**将字符串使用指定的分隔符拆分后转成long数组返回
	 * @param arrayStr 指定字符串
	 * @param splitor 指定分隔符，可以是多个
	 * @return
	 */
	public static long[] stringToLongArray(String arrayStr,char... splitor){
		String[] lonArrStrs = StringTools.splitString(arrayStr, splitor);
		if (ArrayTools.isEmpty(lonArrStrs))
			return null;
		long[] result = new long[lonArrStrs.length];
		for (int i = 0; i < lonArrStrs.length; i++) {
			String string = lonArrStrs[i];
			result[i] = Long.parseLong(string);
		}
		return result;
	}

	/**
	 * 约等于，判断两个小数是否大约相等
	 * 
	 * @param d1
	 * @param d2
	 * @param amount
	 *            表示精确到小数位后的位数,将以四舍五入法执行截取位数
	 * @return
	 */
	public static boolean aroundEquals(double d1, double d2, int amount) {
		return cutDouble(d1, amount) == cutDouble(d2, amount);
	}

	/** 计算两个数字之差,返回值总是正数 */
	public static long distance(long l1, long l2) {
		return Math.abs(l1 - l2);
	}

	/** 计算两个数字之差,返回值总是正数 */
	public static int distance(int l1, int l2) {
		return Math.abs(l1 - l2);
	}

	/** 计算两个数字之差,返回值总是正数 */
	public static float distance(float l1, float l2) {
		return Math.abs(l1 - l2);
	}

	/** 计算两个数字之差 ,返回值总是正数 */
	public static double distance(double l1, double l2) {
		return Math.abs(l1 - l2);
	}

	/** 将字符串转换成数值，去掉不是数字的字符。此方法已过时，请使用takeInteger() 方法替代
	 * @see NumberTools.takeInteger()
	 *  */
	@Deprecated
	public static long takeNumber(String str, long default_val) {
		if (StringTools.isEmpty(str))
			return default_val;
		str = StringTools.cutNotNumChar(str);
		if (StringTools.isEmpty(str))
			return default_val;
		return Long.valueOf(str).longValue();
	}
	
	/** 将字符串转换成整数，去掉不是数字的字符 */
	public static long takeInteger(String str, long default_val) {
		if (StringTools.isEmpty(str))
			return default_val;
		str = StringTools.cutNotNumChar(str);
		if (StringTools.isEmpty(str))
			return default_val;
		return Double.valueOf(str).longValue();
	}
	
	/** 将字符串转换成小数，去掉不是数字的字符 */
	public static double takeFloat(String str, double default_val) {
		if (StringTools.isEmpty(str))
			return default_val;
		str = StringTools.cutNotNumChar(str);
		if (StringTools.isEmpty(str))
			return default_val;
		return Double.valueOf(str).doubleValue();
	}

	public static long[] ListToLongArray(List<Long> list) {
		if (CollectionTools.isEmpty(list))
			return null;
		long[] result = new long[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Long l = list.get(i);
			result[i] = l.longValue();
		}
		return result;
	}

	public static int[] listToIntArray(List<Integer> list) {
		if (CollectionTools.isEmpty(list))
			return null;
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Integer l = list.get(i);
			result[i] = l.intValue();
		}
		return result;
	}

	/** 获取数字数组中的最小值 */
	public static int minInt(int... objt) {
		if (objt.length == 1)
			return objt[0];
		int aa = objt[0];
		for (int i = 1; i < objt.length; i++) {
			int tt = objt[i];
			aa = tt > aa ? aa : tt;
		}
		return aa;
	}

	public static final Class<?>[] NUMBER_TYPE = { byte.class, short.class, int.class, long.class,
			Byte.class, Short.class, Integer.class, Long.class };
	
	public static final Class<?>[] FLOAT_TYPE = { byte.class, short.class, int.class, long.class,
			double.class, float.class, Byte.class, Short.class, Integer.class, Long.class,
			Double.class, Float.class };

	/**
	 * 判断对象是否是整数类型，及4总基本数据类型，不包含boolean类型和char类型<br>
	 * 在常量数组“NUMBER_TYPE”中包含的返回true
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNumber(Object obj) {
		if (obj == null)
			throw new NullPointerException("参数Obj为空！");
		return ArrayTools.contains(NUMBER_TYPE, obj.getClass());
	}
	
	/**
	 * 判断对象是否是整数或者小数类型，及6总基本数据类型，不包含boolean类型和char类型<br>
	 * 在常量数组“FLOAT_TYPE”中包含的返回true
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isFloat(Object obj) {
		if (obj == null)
			throw new NullPointerException("参数Obj为空！");
		return ArrayTools.contains(FLOAT_TYPE, obj.getClass());
	}

	/** 四舍五入取指定位小数 */
	public static double cutDouble(double d, int amount) {
		BigDecimal b = new BigDecimal(d);
		return b.setScale(amount, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/** 求平均数 */
	public static double average(int... is) {
		long count = 0;
		for (int i : is)
			count += i;
		return count / (double) is.length;
	}

	/**
	 * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
	 */
	public static String digitUppercase(double d) {
		BigDecimal numberOfMoney = new BigDecimal(d);
		return NumberToCN.number2CNMontrayUnit(numberOfMoney);
	}

	/**
	 * 数字转换为汉语中人民币的大写<br>
	 * 
	 * @author hongten
	 * @contact hongtenzone@foxmail.com
	 * @create 2013-08-13
	 */
	private static class NumberToCN {
		/**
		 * 汉语中数字大写
		 */
		private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		/**
		 * 汉语中货币单位大写，这样的设计类似于占位符
		 */
		private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
				"拾", "佰", "仟", "兆", "拾", "佰", "仟" };
		/**
		 * 特殊字符：整
		 */
		private static final String CN_FULL = "整";
		/**
		 * 特殊字符：负
		 */
		private static final String CN_NEGATIVE = "负";
		/**
		 * 金额的精度，默认值为2
		 */
		private static final int MONEY_PRECISION = 2;
		/**
		 * 特殊字符：零元整
		 */
		private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

		/**
		 * 把输入的金额转换为汉语中人民币的大写
		 * 
		 * @param numberOfMoney
		 *            输入的金额
		 * @return 对应的汉语大写
		 */
		public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
			StringBuilder sb = new StringBuilder();
			int signum = numberOfMoney.signum();
			// 零元整的情况
			if (signum == 0)
				return CN_ZEOR_FULL;
			// 这里会进行金额的四舍五入
			long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
			// 得到小数点后两位值
			long scale = number % 100;
			int numUnit = 0;
			int numIndex = 0;
			boolean getZero = false;
			// 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
			if (!(scale > 0)) {
				numIndex = 2;
				number = number / 100;
				getZero = true;
			}
			if ((scale > 0) && (!(scale % 10 > 0))) {
				numIndex = 1;
				number = number / 10;
				getZero = true;
			}
			int zeroSize = 0;
			while (true) {
				if (number <= 0)
					break;
				// 每次获取到最后一个数
				numUnit = (int) (number % 10);
				if (numUnit > 0) {
					if ((numIndex == 9) && (zeroSize >= 3))
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
					if ((numIndex == 13) && (zeroSize >= 3))
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					sb.insert(0, CN_UPPER_NUMBER[numUnit]);
					getZero = false;
					zeroSize = 0;
				} else {
					++zeroSize;
					if (!(getZero))
						sb.insert(0, CN_UPPER_NUMBER[numUnit]);
					if (numIndex == 2) {
						if (number > 0)
							sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					} else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0))
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					getZero = true;
				}
				// 让number每次都去掉最后一个数
				number = number / 10;
				++numIndex;
			}
			// 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
			if (signum == -1)
				sb.insert(0, CN_NEGATIVE);
			// 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
			if (!(scale > 0))
				sb.append(CN_FULL);
			return sb.toString();
		}
	}
}