package com.vastsoft.util.common;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.vastsoft.util.common.inf.IterateReturnable;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.BreakIterateException;
import com.vastsoft.util.exception.IterateException;
import com.vastsoft.util.exception.ReturnIterateException;

/**
 * @author jben
 *
 */
public class ArrayTools {
	
	/**对数组进行去重，返回去重之后的数组，长度会发生改变
	 * @param <T>*/
	public static <T> T[] distinct(T[] arrayObj){
		int length = arrayLength(arrayObj);
		if (length<=0)
			return arrayObj;
		Class<?> clazz = getComponentType(arrayObj);
		@SuppressWarnings("unchecked")
		T[] result = (T[]) newInstance(clazz, 0);
		for (int i = 0; i < length; i++) {
			T obja = arrayObj[i];
			if (exist(result, obja))
				continue;
			result = add(result,obja);
		}
		return result;
	}
	
	/**对数组进行去重，返回去重之后的数组，长度会发生改变*/
	public static Object distinct(Object arrayObj){
		int length = arrayLength(arrayObj);
		if (length<=0)
			return arrayObj;
		Class<?> clazz = getComponentType(arrayObj);
		Object result = newInstance(clazz, 0);
		for (int i = 0; i < length; i++) {
			Object obja = get(arrayObj, i);
			if (exist(result, obja))
				continue;
			result = add(result,obja);
		}
		return result;
	}
	
	/**在数组的末尾添加一个元素。返回值的长度会增加1*/
	public static <T> T[] add(T[] arrayObj, T t) {
		int length = arrayLength(arrayObj);
		@SuppressWarnings("unchecked")
		T[] result = (T[]) newInstance(arrayObj.getClass().getComponentType(), length + 1);
		System.arraycopy(arrayObj, 0, result, 0, length);
		set(result, length, t);
		return result;
	}
	/**在数组的末尾添加一个元素。返回值的长度会增加1*/
	public static Object add(Object arrayObj, Object t) {
		int length = arrayLength(arrayObj);
		Object result = newInstance(arrayObj.getClass().getComponentType(), length + 1);
		System.arraycopy(arrayObj, 0, result, 0, length);
		set(result, length, t);
		return result;
	}
	/**获取数组所保存的数据的类型*/
	public static Class<?> getComponentType(Object arrayObj) {
		return arrayObj.getClass().getComponentType();
	}

	/**
	 * 删除数组当中所有的指定对象，使用equals测试
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	public static Object delete(Object arrayObj, int index) {
		int length = arrayLength(arrayObj);
		if (length <= 0)
			return arrayObj;
		if (index < 0 || index >= length)
			return arrayObj;
		Object result = newInstance(arrayObj.getClass().getComponentType(), length - 1);
		System.arraycopy(arrayObj, index + 1, result, index, length - index - 1);
		return result;
	}

	public static Object newInstance(Class<?> componentType, int length) {
		return Array.newInstance(componentType, length);
	}

	/**
	 * 删除数组当中所有的指定对象，使用equals测试
	 * 
	 * @param <T>
	 */
	public static <T> Object deleteAll(Object arrayObj, Object obj) {
		int length = arrayLength(arrayObj);
		if (length <= 0)
			return arrayObj;
		int len = 0;
		Object result = newInstance(arrayObj.getClass().getComponentType(), length);
		for (int i = 0; i < length; i++) {
			Object t = get(arrayObj, i);
			if (obj == null && t == null)
				continue;
			if (t == null) {
				set(result, len++, t);
				continue;
			}
			if (t.equals(obj))
				continue;
			set(result, len++, t);
		}
		Object result2 = newInstance(arrayObj.getClass().getComponentType(), len);
		System.arraycopy(result, 0, result2, 0, len);
		return result2;
	}

	public static void set(Object arrayObj, int index, Object obj) {
		int length = arrayLength(arrayObj);
		if (length <= 0)
			throw new IndexOutOfBoundsException();
		Array.set(arrayObj, index, obj);
	}
	
	public static Object get(Object arrayObj, int index) {
		int length = arrayLength(arrayObj);
		if (length <= 0)
			throw new IndexOutOfBoundsException();
		return Array.get(arrayObj, index);
	}

	/** 删除数组当中所有的指定对象，使用equals测试 */
	public static <T> T[] delete(T[] arrayObj, int index) {
		if (arrayObj == null || arrayObj.length <= 0)
			return arrayObj;
		if (index < 0 || index >= arrayObj.length)
			return arrayObj;
		T[] result = Arrays.copyOf(arrayObj, arrayObj.length - 1);
		System.arraycopy(arrayObj, index + 1, result, index, arrayObj.length - index - 1);
		return result;
	}

	/** 删除数组当中所有的指定对象，使用equals测试 */
	public static <T> T[] deleteAll(T[] arrayObj, T obj) {
		if (arrayObj == null || arrayObj.length <= 0)
			return arrayObj;
		int len = 0;
		T[] result = Arrays.copyOf(arrayObj, arrayObj.length);
		for (int i = 0; i < arrayObj.length; i++) {
			T t = arrayObj[i];
			if (obj == null && t == null)
				continue;
			if (t == null) {
				result[len++] = t;
				continue;
			}
			if (t.equals(obj))
				continue;
			result[len++] = t;
		}
		return Arrays.copyOf(result, len);
	}

	public static double[] transfer(Double[] ls) {
		if (ls == null)
			return null;
		double[] result = new double[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Double l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Double[] transfer(double[] ls) {
		if (ls == null)
			return null;
		Double[] result = new Double[ls.length];
		for (int i = 0; i < ls.length; i++) {
			double l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static float[] transfer(Float[] ls) {
		if (ls == null)
			return null;
		float[] result = new float[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Float l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Float[] transfer(float[] ls) {
		if (ls == null)
			return null;
		Float[] result = new Float[ls.length];
		for (int i = 0; i < ls.length; i++) {
			float l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static char[] transfer(Character[] ls) {
		if (ls == null)
			return null;
		char[] result = new char[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Character l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Character[] transfer(char[] ls) {
		if (ls == null)
			return null;
		Character[] result = new Character[ls.length];
		for (int i = 0; i < ls.length; i++) {
			char l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Boolean[] transfer(boolean[] ls) {
		if (ls == null)
			return null;
		Boolean[] result = new Boolean[ls.length];
		for (int i = 0; i < ls.length; i++) {
			boolean l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static boolean[] transfer(Boolean[] ls) {
		if (ls == null)
			return null;
		boolean[] result = new boolean[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Boolean l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static byte[] transfer(Byte[] ls) {
		if (ls == null)
			return null;
		byte[] result = new byte[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Byte l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Byte[] transfer(byte[] ls) {
		if (ls == null)
			return null;
		Byte[] result = new Byte[ls.length];
		for (int i = 0; i < ls.length; i++) {
			byte l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Short[] transfer(short[] ls) {
		if (ls == null)
			return null;
		Short[] result = new Short[ls.length];
		for (int i = 0; i < ls.length; i++) {
			short l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static short[] transfer(Short[] ls) {
		if (ls == null)
			return null;
		short[] result = new short[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Short l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static int[] transfer(Integer[] ls) {
		if (ls == null)
			return null;
		int[] result = new int[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Integer l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Integer[] transfer(int[] ls) {
		if (ls == null)
			return null;
		Integer[] result = new Integer[ls.length];
		for (int i = 0; i < ls.length; i++) {
			int l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static long[] transfer(Long[] ls) {
		if (ls == null)
			return null;
		long[] result = new long[ls.length];
		for (int i = 0; i < ls.length; i++) {
			Long l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static Long[] transfer(long[] ls) {
		if (ls == null)
			return null;
		Long[] result = new Long[ls.length];
		for (int i = 0; i < ls.length; i++) {
			long l = ls[i];
			result[i] = l;
		}
		return result;
	}

	public static <T> boolean exist(T[] arr, T tObj) {
		return contains(arr, tObj);
	}

	public static boolean exist(Object arrayObj, Object obj) {
		return contains(arrayObj, obj);
	}

	public static boolean contains(Object array, final Object obj) {
		if (isEmpty(array))
			return false;
		try {
			Boolean result = (Boolean)iterateArray(array, new IterateReturnable<Object>() {
				@Override
				public void run(Object t) throws ReturnIterateException, BreakIterateException, BaseException {
					if (t == null)
						return;
					if (t.equals(obj)) {
						throw new ReturnIterateException(true);
					}
				}
			});
			return result == null ? false : result;
		} catch (IterateException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断数组是不是为空，当对象为null或者长度为0返回true
	 * 
	 * @param <T>
	 */
	public static boolean isEmpty(Object arrayObj) {
		return arrayLength(arrayObj) <= 0;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] collectionToArray(Collection<T> list, Class<T> cla) {
		if (CollectionTools.isEmpty(list))
			return null;
		T[] newArr = (T[]) Array.newInstance(cla, list.size());
		return list.toArray(newArr);
	}

	/**
	 * 将a,b两个字节数组连接起来，截取数组b的长度为bLen,如果bLen>b.length，则bLen=b.length
	 * 
	 * @param a
	 * @param b
	 * @param bLen
	 *            截取数组b的长度为bLen,如果bLen>b.length，则bLen=b.length
	 * @return
	 */
	public static byte[] concat(byte[] a, byte[] b, int bLen) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		final int alen = a.length;
		final int blen = b.length;
		if (alen == 0)
			return b;
		if (blen == 0)
			return a;
		if (bLen > blen)
			bLen = blen;
		final byte[] result = new byte[alen + bLen];
		System.arraycopy(a, 0, result, 0, alen);
		System.arraycopy(b, 0, result, alen, bLen);
		return result;
	}

	public static byte[] concat(byte[] a, byte[] b) {
		return concat(a, b, b.length);
		// if (a == null)
		// return b;
		// if (b == null)
		// return a;
		// final int alen = a.length;
		// final int blen = b.length;
		// if (alen == 0)
		// return b;
		// if (blen == 0)
		// return a;
		// final byte[] result = new byte[alen + blen];
		// System.arraycopy(a, 0, result, 0, alen);
		// System.arraycopy(b, 0, result, alen, blen);
		// return result;
	}

	/** 将多个对象转换成数组 */
	public static long[] buildArray(long... obj) {
		return obj;
	}

	/** 将多个对象转换成数组 */
	@SuppressWarnings("unchecked")
	public static <T> T[] buildArray(T... obj) {
		return obj;
	}

	/***/
	public static Character[] arrayToObjArray(char[] arrChar) {
		if (arrChar == null)
			return null;
		Character[] result = new Character[arrChar.length];
		if (arrChar.length <= 0)
			return result;
		for (int i = 0; i < arrChar.length; i++) {
			result[i] = arrChar[i];
		}
		return result;
	}

	/***/
	public static Long[] arrayToObjArray(long[] arrLong) {
		if (arrLong == null)
			return null;
		Long[] result = new Long[arrLong.length];
		if (arrLong.length <= 0)
			return result;
		for (int i = 0; i < arrLong.length; i++) {
			result[i] = arrLong[i];
		}
		return result;
	}

	/** 判断两个数组是否相等，如果通过isEmpty测试都返回true,则才会返回true; */
	public static <T> boolean equals(T[] arr1, T[] arr2) {
		if (isEmpty(arr1) && isEmpty(arr2))
			return true;
		if (arr1 == null || arr2 == null)
			return false;
		return Arrays.equals(arr1, arr2);
	}

	/**
	 * 包含
	 * 
	 * @param <T>
	 */
	public static <T> boolean contains(T[] Ts, T t) {
		if (t == null)
			return false;
		if (isEmpty(Ts))
			return false;
		for (T tt : Ts) {
			if (t.equals(tt))
				return true;
		}
		return false;
	}

	/** 获取一个数组对象的长度，如果对象不为数组抛出异常 */
	public static int arrayLength(Object arrayObj) {
		if (arrayObj == null)
			return 0;
		if (!arrayObj.getClass().isArray())
			throw new RuntimeException("指定的对象不是数组！");
		return Array.getLength(arrayObj);
	}

	/**
	 * 迭代一个数组,如果数组为null或者长度为0不会调用迭代函数，如果想要打断循环请使用断言
	 * 
	 * @throws IterateException
	 */
	public static Object iterateArray(Object arrayObj, IterateReturnable<Object> run) throws IterateException {
		int arrLen = arrayLength(arrayObj);
		if (arrLen <= 0)
			return null;
		for (int i = 0; i < arrLen; i++) {
			Object obj = Array.get(arrayObj, i);
			try {
				run.run(obj);
			} catch (BreakIterateException e) {
				break;
			} catch (ReturnIterateException e) {
				return e.getResult();
			} catch (BaseException e) {
				e.printStackTrace();
				throw new IterateException(e);
			}
		}
		return null;
	}

	/**
	 * 判断数组是不是为空，当对象为null或者长度为0返回true
	 * 
	 * @param <T>
	 */
	public static <T> boolean isEmpty(T[] arrayObj) {
		arrayObj = arrayTrim(arrayObj);
		return isEmptyWithOutTrim(arrayObj);
	}

	/**
	 * 判断数组是不是为空，当对象为null或者长度为0返回true
	 * 
	 * @param <T>
	 */
	public static <T> boolean isEmptyWithOutTrim(T[] arrayObj) {
		if (arrayObj == null)
			return true;
		if (arrayObj.length <= 0)
			return true;
		return false;
	}

	/** 未数组去空 */
	public static <T> T[] arrayTrim(T[] arrayObj) {
		if (arrayObj == null || arrayObj.length <= 0)
			return arrayObj;
		ArrayList<T> listT = new ArrayList<T>();
		for (T t : arrayObj) {
			if (t != null)
				listT.add(t);
		}
		@SuppressWarnings("unchecked")
		T[] newArr = (T[]) Array.newInstance(arrayObj.getClass().getComponentType(), listT.size());
		return listT.toArray(newArr);
	}
}
