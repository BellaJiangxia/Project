package com.vastsoft.util.common;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.vastsoft.util.common.constants.Gender;
import com.vastsoft.util.common.inf.IterateReturnable;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.BreakIterateException;
import com.vastsoft.util.exception.ReturnIterateException;

public class CommonTools {
	// /**迭代枚举的所有值
	// * @throws IterateException */
	// public static <E extends Enum<E>> void foreachEnum(Class<E>
	// clazz,IterateRunnable<E> iterateRunnable) throws IterateException{
	// Set<E> setE = getAllEleFromEnum(clazz);
	// for (E e : setE)
	// try {
	// iterateRunnable.run(e);
	// } catch (BreakIterateException e1) {
	// }
	// }

	/**
	 * 此方法被移动了位置，等同于ArrayTools.collectionToArray()
	 * 
	 * @param list
	 * @param cla
	 * @return 返回此列表的数组表示形式，顺序与列表一致
	 */
	@Deprecated
	public static <T> T[] listToArray(List<T> list, Class<T> cla) {
		return ArrayTools.collectionToArray(list, cla);
	}

	/**
	 * 迭代枚举的所有值
	 * 
	 * @throws IterateException
	 * @throws BreakIterateException
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E foreachEnum(Class<E> clazz, IterateReturnable<E> iterateReturnable)
			throws BaseException {
		Set<E> setE = getAllEleFromEnum(clazz);
		for (E e : setE) {
			try {
				iterateReturnable.run(e);
			} catch (ReturnIterateException e1) {
				return (E) e1.getResult();
			} catch (BreakIterateException e2) {
				break;
			}
		}
		return null;
	}

	/**
	 * 获取枚举的全部元素
	 * 
	 * @param <E>
	 */
	public static <E extends Enum<E>> Set<E> getAllEleFromEnum(Class<E> clazz) {
		EnumSet<E> result = EnumSet.allOf(clazz);
		return result;
	}

	/** 从身份证号码提取性别 */
	public static Gender takeGenderByIdentityId(String identity_id, Gender default_gender) {
		if (!StringTools.wasIdentityId(identity_id))
			return default_gender;
		identity_id = identity_id.trim();
		if (identity_id.length() == 18) {
			int i = Integer.valueOf(identity_id.substring(16, 17));
			if (i % 2 == 1)
				return Gender.MALE;
			else
				return Gender.FAMALE;
		} else if (identity_id.length() == 15) {
			int i = Integer.valueOf(identity_id.substring(14));
			if (i % 2 == 1)
				return Gender.MALE;
			else
				return Gender.FAMALE;
		}
		return default_gender;
	}

	/** 从身份证号码提取性别 */
	public static com.vastsoft.util.common.enums.Gender takeGenderByIdentityId(String identity_id,
			com.vastsoft.util.common.enums.Gender default_gender) {
		if (!StringTools.wasIdentityId(identity_id))
			return default_gender;
		identity_id = identity_id.trim();
		if (identity_id.length() == 18) {
			int i = Integer.valueOf(identity_id.substring(16, 17));
			if (i % 2 == 1)
				return com.vastsoft.util.common.enums.Gender.MALE;
			else
				return com.vastsoft.util.common.enums.Gender.FAMALE;
		} else if (identity_id.length() == 15) {
			int i = Integer.valueOf(identity_id.substring(14));
			if (i % 2 == 1)
				return com.vastsoft.util.common.enums.Gender.MALE;
			else
				return com.vastsoft.util.common.enums.Gender.FAMALE;
		}
		return default_gender;
	}

	// /**
	// * 将一个对象拷贝到另外一个对象,通过public的get和set方法完成
	// *
	// * @param srcObj
	// * 源对象
	// * @param destObj
	// * 目标对象
	// * @param mapPropertyMapping
	// * 属性名映射，将首先使用此映射，如果映射中不存在将拷贝同名属性
	// * @param copyParentClass
	// * 是否拷贝父类中的属性
	// * @throws IllegalAccessException
	// * @throws IllegalArgumentException
	// * @throws InvocationTargetException
	// */
	// @Deprecated
	// public static void copyObject(Object srcObj, Object destObj, Map<String,
	// String> mapPropertyMapping,
	// boolean copyParentClass)
	// throws IllegalAccessException, IllegalArgumentException,
	// InvocationTargetException {
	// copyObject(srcObj, srcObj.getClass(), destObj, destObj.getClass(),
	// mapPropertyMapping, copyParentClass);
	// }

	// /**
	// * 将一个对象拷贝到另外一个对象,通过public的get和set方法完成
	// *
	// * @param srcObj
	// * 源对象
	// * @param srcObjCla
	// * 指定要复制的属性所在类
	// * @param destObj
	// * 目标对象
	// * @param destObjCla
	// * 目标对象属性所在类
	// * @param mapPropertyMapping
	// * 属性名映射，将首先使用此映射，如果映射中不存在将拷贝同名属性
	// * @param copyParentClass
	// * 是否拷贝父类中的属性
	// * @throws IllegalAccessException
	// * @throws IllegalArgumentException
	// * @throws InvocationTargetException
	// */
	// @Deprecated
	// public static void copyObject(Object srcObj, Class<?> srcObjCla, Object
	// destObj, Class<?> destObjCla,
	// Map<String, String> mapPropertyMapping, boolean copyParentClass)
	// throws IllegalAccessException, IllegalArgumentException,
	// InvocationTargetException {
	// List<Method> listGetMethod = takeGetMethods(srcObjCla);
	// List<Method> listSetMethod = takeSetMethods(destObjCla);
	// if (CommonTools.isEmpty(listGetMethod))
	// return;
	// if (CommonTools.isEmpty(listSetMethod))
	// return;
	// for (Method method : listGetMethod) {
	// if (!isPublic(method))
	// continue;
	// Object returnValue = method.invoke(srcObj);
	// String propertyName = method.getName().substring(3);
	// Method setMethod;
	// if (!CommonTools.isEmpty(mapPropertyMapping)) {
	// String setMethodName = mapPropertyMapping
	// .get(propertyName.substring(0, 1).toLowerCase() +
	// propertyName.substring(1));
	// if (StringTools.isEmpty(setMethodName))
	// setMethod = findMethodByName(listSetMethod, "set" + propertyName, null);
	// else
	// setMethod = findMethodByName(listSetMethod,
	// "set" + setMethodName.substring(0, 1).toUpperCase() +
	// setMethodName.substring(1), null);
	// } else
	// setMethod = findMethodByName(listSetMethod, "set" + propertyName, null);
	// if (setMethod == null)
	// continue;
	// if (!isPublic(setMethod))
	// continue;
	// setMethod.invoke(destObj, returnValue);
	// }
	// if (copyParentClass) {
	// if (destObjCla.getSuperclass() != Object.class)
	// copyObject(srcObj, srcObjCla, destObj, destObjCla.getSuperclass(),
	// mapPropertyMapping, copyParentClass);
	// if (srcObjCla.getSuperclass() != Object.class)
	// copyObject(srcObj, srcObjCla.getSuperclass(), destObj, destObjCla,
	// mapPropertyMapping, copyParentClass);
	// }
	// }

	// /** 检查方法是否是public */
	// @Deprecated
	// public static boolean isPublic(Method method) {
	// return Modifier.isPublic(method.getModifiers());
	// }
	//
	// /** 通过方法名和参数类型数组查询方法列表中的方法！如果参数列表为null标识不参与筛选 */
	// @Deprecated
	// public static Method findMethodByName(List<Method> listMethod, String
	// name, Class<?>[] paramterTypes) {
	// if (isEmpty(listMethod))
	// return null;
	// if (StringTools.isEmpty(name))
	// return null;
	// name = name.trim();
	// for (Method method : listMethod) {
	// if (name.equals(method.getName()))
	// if (paramterTypes == null)
	// return method;
	// else if (equals(paramterTypes, method.getParameterTypes()))
	// return method;
	// }
	// return null;
	// }

	// /** 获取一个类的所有的标准“set”方法 */
	// @Deprecated
	// public static List<Method> takeSetMethods(Class<?> clazz) {
	// @SuppressWarnings("unchecked")
	// List<Method> result = SetUniqueList.decorate(new ArrayList<Method>());
	// Method[] arrMothod = clazz.getDeclaredMethods();
	// if (isEmpty(arrMothod))
	// return result;
	// for (Method method : arrMothod) {
	// if (method.getName().startsWith("set")) {
	// Class<?>[] clas = method.getParameterTypes();
	// if (!isEmpty(clas) && clas.length == 1)
	// result.add(method);
	// }
	// }
	// return result;
	// }

	// /** 获取一个类的所有的标准“get”方法 */
	// @Deprecated
	// public static List<Method> takeGetMethods(Class<?> clazz) {
	// @SuppressWarnings("unchecked")
	// List<Method> result = SetUniqueList.decorate(new ArrayList<Method>());
	// Method[] arrMothod = clazz.getDeclaredMethods();
	// if (isEmpty(arrMothod))
	// return result;
	// for (Method method : arrMothod) {
	// if (method.getName().startsWith("get") ||
	// method.getName().startsWith("is"))
	// if (isEmpty(method.getParameterTypes()))
	// result.add(method);
	// }
	// return result;
	// }

	// /**
	// * 写入某对象的属性，包括私有属性,包含其父类的对象,返回是否成功<br>
	// * 此方法将首先调用set方法写入，如果失败才调用写入属性
	// */
	// @Deprecated
	// public static boolean writeProperty(Object obj, String property_name,
	// Object property_value) {
	// return writePropertyByClass(obj, obj.getClass(), property_name,
	// property_value);
	// }
	//
	// /**
	// * 写入某对象在指定类中的的属性，包括私有属性,包含其父类的对象，返回是否成功<br>
	// * 此方法将首先调用set方法写入，如果失败才调用写入属性
	// */
	// @Deprecated
	// public static boolean writePropertyByClass(Object obj, Class<?>
	// superClass, String property_name,
	// Object property_value) {
	// Class<?> property_value_cla = property_value.getClass();
	// try {
	// try {
	// String getMethodName = "set" + property_name.substring(0,
	// 1).toUpperCase() + property_name.substring(1);
	// Method method = superClass.getDeclaredMethod(getMethodName,
	// property_value_cla);
	// if (method != null) {
	// method.setAccessible(true);
	// method.invoke(obj, property_value);
	// return true;
	// }
	// } catch (Exception e) {
	// }
	// Field field = superClass.getDeclaredField(property_name);
	// field.setAccessible(true);
	// field.set(obj, property_value);
	// return true;
	// } catch (Exception e) {
	// }
	// Class<?> parentClass = superClass.getSuperclass();
	// if (parentClass == null)
	// return false;
	// if (parentClass.equals(Object.class))
	// return false;
	// return writePropertyByClass(obj, parentClass, property_name,
	// property_value);
	// }
	//
	// /**
	// * 读取某对象的属性，包括私有属性,如果不存在返回null<br>
	// * 此方法将首先调用get方法获取，如果失败才调用获取属性
	// */
	// @Deprecated
	// public static Object readProperty(Object obj, Class<?> superClass, String
	// property_name) {
	// Object result = null;
	// try {
	// try {
	// String getMethodName = "get" + property_name.substring(0,
	// 1).toUpperCase() + property_name.substring(1);
	// Method method = superClass.getDeclaredMethod(getMethodName);
	// if (method != null) {
	// method.setAccessible(true);
	// result = method.invoke(obj);
	// if (result != null)
	// return result;
	// }
	// } catch (Exception e) {
	// }
	// try {
	// String getMethodName = "is" + property_name.substring(0, 1).toUpperCase()
	// + property_name.substring(1);
	// Method method = superClass.getDeclaredMethod(getMethodName);
	// if (method != null) {
	// method.setAccessible(true);
	// result = method.invoke(obj);
	// if (result != null)
	// return result;
	// }
	// } catch (Exception e) {
	// }
	// Field field = superClass.getDeclaredField(property_name);
	// field.setAccessible(true);
	// return field.get(obj);
	// } catch (Exception e) {
	// }
	// Class<?> parentClass = superClass.getSuperclass();
	// if (parentClass == null)
	// return null;
	// if (parentClass.equals(Object.class))
	// return null;
	// return readProperty(obj, parentClass, property_name);
	// }
	//
	// /**
	// * 读取某对象的属性，包括私有属性,如果不存在返回null<br>
	// * 此方法将首先调用get方法获取，如果失败才调用获取属性
	// */
	// @Deprecated
	// public static Object readProperty(Object obj, String property_name) {
	// return readProperty(obj, obj.getClass(), property_name);
	// }

	// /** 将多个对象放进一个列表中 */
	// public static List objsToList(Object... objs) {
	// return arrayToList(objs);
	// }

	// /** 将String数组转换成long数组 */
	// public static long[] stringArrayToLongArray(String[] strs) {
	// if (strs == null)
	// return null;
	// long[] result = new long[strs.length];
	// for (int i = 0; i < result.length; i++) {
	// result[i] = Long.parseLong(strs[i]);
	// }
	// return result;
	// }

	/** 判断调用者是否是指定类,自身调用返回true */
	public static boolean judgeCaller(Method... method) {
		StackTraceElement[] arrayAtack = new Throwable().getStackTrace();
		if (arrayAtack.length <= 2)
			return true;
		StackTraceElement selfAtack = arrayAtack[1];
		StackTraceElement atack = arrayAtack[2];
		if (selfAtack.getClassName().equals(atack.getClassName()))
			return true;
		for (Method m : method) {
			if (atack.getClassName().equals(m.getDeclaringClass().getName()))
				if (atack.getMethodName().equals(m.getName()))
					return true;
		}
		return false;
	}

	/** 将两个Map合并。调用Map.putAll函数 */
	public static <K, V> Map<K, V> combine(Map<K, V> map1, Map<K, V> map2) {
		if (map1 == null)
			return map2;
		if (map2 == null)
			return map1;
		map1.putAll(map2);
		return map1;
	}

	public static class Entry<K, V> {
		private K key;
		private V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> buildMap(Entry<K, V>... entrys) {
		if (entrys == null)
			return null;
		Map<K, V> result = new HashMap<K, V>();
		if (entrys.length <= 0)
			return result;
		for (Entry<K, V> entry : entrys)
			result.put(entry.key, entry.value);
		return result;
	}
	//
	// public static <K, V> Map<K, V> buildMap(K k,V v) {
	// Map<K, V> result = new HashMap<K, V>();
	// result.put(k, v);
	// return result;
	// }

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.size() <= 0;
	}

	/** 将list转换为指定类型的list,只支持基本数据类型或String */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listCastType(List src, Class clazz) {
		if (src == null || src.size() <= 0)
			return null;
		List lis = new ArrayList();
		for (Object object : src) {
			if (object == null) {
				lis.add(object);
				continue;
			}
			if (clazz == Integer.class) {
				lis.add(Integer.valueOf(object.toString()));
				continue;
			}
			if (clazz == Byte.class) {
				lis.add(Byte.valueOf(object.toString()));
				continue;
			}
			if (clazz == Character.class) {
				lis.add(new Character(object.toString().charAt(0)));
				continue;
			}
			if (clazz == Boolean.class) {
				lis.add(Boolean.valueOf(object.toString()));
				continue;
			}
			if (clazz == Short.class) {
				lis.add(Short.valueOf(object.toString()));
				continue;
			}
			if (clazz == Float.class) {
				lis.add(Float.valueOf(object.toString()));
				continue;
			}
			if (clazz == Double.class) {
				lis.add(Double.valueOf(object.toString()));
				continue;
			}
			if (clazz == Long.class) {
				lis.add(Long.valueOf(object.toString()));
				continue;
			}
			if (clazz == String.class) {
				lis.add(object.toString());
				continue;
			}
		}
		return lis;
	}

	// /** 去掉list中的空元素 */
	// @Deprecated
	// public static void collectionTrim(Collection<?> src) {
	// if (src == null || src.size() <= 0)
	// return;
	// for (Iterator<?> iterator = src.iterator(); iterator.hasNext();) {
	// Object object = (Object) iterator.next();
	// if (object == null)
	// iterator.remove();
	// }
	// }

	/** 将数据转换成16进制字符串 */
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

	/**
	 * 判断两个或多个对象是否互相相等，<br>
	 * 仅仅只测试将其转换为文本之后是否相等<br>
	 * 会进行字符串去空
	 */
	public static boolean objEquals(Object obj, Object... objt) {
		if (obj == null)
			return false;
		String objstr = obj.toString().trim();
		for (Object object : objt) {
			if (object == null)
				return false;
			if (!objstr.equals(object.toString().trim()))
				return false;
		}
		return true;
	}

	/** 将任何类型已文本的形式转换为Long类型 */
	public static Long castToLong(Object obj) {
		if (obj == null)
			return null;
		String ss = obj.toString().trim();
		try {
			return new Long(ss);
		} catch (Exception e) {
			return null;
		}
	}

	/** 将对象列表转换为Long列表 */
	public static List<Long> listToLongList(List<?> listObj) {
		if (listObj == null)
			return null;
		List<Long> ls = new ArrayList<Long>();
		for (Object obj : listObj) {
			ls.add(castToLong(obj));
		}
		return ls;
	}

	/** 用于格式化用户名字 */
	public static String formatName(String name) {
		if (name == null)
			return null;
		int len = name.length();
		if (len <= 1)
			return name;
		return name.charAt(0) + "**";
	}

	/** 判断调用者是否是指定类,自身调用返回true */
	public static boolean judgeCaller(Class<?>... clazz) {
		StackTraceElement[] arrayAtack = new Throwable().getStackTrace();
		if (arrayAtack.length <= 2)
			return true;
		StackTraceElement selfAtack = arrayAtack[1];
		StackTraceElement atack = arrayAtack[2];
		if (selfAtack.getClassName().equals(atack.getClassName()))
			return true;
		for (Class<?> c : clazz) {
			if (atack.getClassName().equals(c.getName()))
				return true;
		}
		return false;
	}

	//
	// /** 将对象转换成json字符串 */
	// public static String toJsonString(Map mapValue) {
	// if (mapValue == null)
	// return "";
	// JSONObject obj = new JSONObject(mapValue);
	// return obj.toString();
	// }

	/** 对字符串MD5加密 */
	public static String MD5Encry(String src) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			return toHexString((md5.digest(src.getBytes("utf-8"))));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//
	// /** 判断对象是否是数组 */
	// @Deprecated
	// public static boolean isArray(Object obj) {
	// if (obj == null)
	// return false;
	// Class<?> clazz = obj.getClass();
	// return clazz.isArray();
	// }
	//
	// /** 将数组转换为list,不是数组返回null */
	// @Deprecated
	// public static List<Object> arrayToList(Object obj) {
	// if (!isArray(obj))
	// return null;
	// List<Object> list = new ArrayList<>();
	// int length = Array.getLength(obj);
	// if (length <= 0)
	// return list;
	// for (int i = 0; i < length; i++) {
	// Object ob = Array.get(obj, i);
	// list.add(ob);
	// }
	// return list;
	// }

	// /**
	// * 拆分字符串，拆分符可以是多个
	// * @param src
	// * 要拆分的字符串
	// * @param spCode
	// * 拆分符
	// * @return 拆分得到的字符数组
	// */
	// @Deprecated
	// public static List<String> splitStrAsList(String src, char... spCode){
	// return arrayToList(splitString(src, spCode));
	// }
	//
	// /**
	// * 拆分字符串，拆分符可以是多个,拆分结果不包含null或空串
	// * @param src
	// * 要拆分的字符串
	// * @param spCode
	// * 拆分符
	// * @return 拆分得到的字符数组
	// */
	// @Deprecated
	// public static String[] splitString(String src, char... spCode) {
	// if (src==null)return null;
	// src = src.trim();
	// if (src.equals(""))
	// return null;
	// List<String> strss = new ArrayList<String>();
	// Arrays.sort(spCode);
	// List<Character> css = arrayToList(spCode);
	// if (src.length() <= 1) {
	// strss.add(src);
	// } else {
	// int lastindex = 0;
	// for (int i = 0; i < src.length(); i++) {
	// char c = src.charAt(i);
	// if (css.contains(c)) {
	// String tmp = src.substring(lastindex, i);
	// if (tmp != null && !tmp.equals(""))
	// strss.add(tmp);
	// lastindex = i + 1;
	// }
	// if (i == src.length() - 1) {
	// String tmp = src.substring(lastindex, i + 1);
	// if (tmp != null && !tmp.equals(""))
	// strss.add(tmp);
	// lastindex = i + 1;
	// }
	// }
	// }
	// return (strss.size() > 0 ? strss.toArray(new String[] {}) : null);
	// }
	// /** 格式化字符串，将字符串中间的字符替换为指定字符 */
	// @Deprecated
	// public static String formatBetweenStr(String str, int beginLen, int
	// endLen, char flag) {
	// if (str == null)
	// return "";
	// str = str.trim();
	// if (str.length() <= beginLen + endLen)
	// return str;
	// StringBuilder sbb = new StringBuilder();
	// for (int i = 0; i < beginLen; i++) {
	// sbb.append(str.charAt(i));
	// }
	// for (int i = 0; i < str.length() - beginLen - endLen; i++) {
	// sbb.append(flag);
	// }
	// for (int i = str.length() - endLen; i < str.length(); i++) {
	// sbb.append(str.charAt(i));
	// }
	// return sbb.toString();
	// }

	// /**
	// * 格式化字符串src，返回 len长度的字符串，将在ֵsrc前面加指定填充符
	// *
	// * @param src
	// * 源字符串
	// * @param len
	// * 字符串长度
	// * @param flag
	// * 填充字符
	// * @return 返回得到的字符串
	// */
	// @Deprecated
	// public static String formatString(String src, int len, char flag) {
	// if (src == null)
	// src = "";
	// if (src.length() >= len)
	// return src;
	// int ll = len - src.length();
	// StringBuilder Strin = new StringBuilder();
	// for (int i = 0; i < ll; i++)
	// Strin.append(flag);
	// Strin.append(src);
	// return Strin.toString();
	// }

	/** 获取一个uuid */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 获取调用者的类,不包含自身类
	 * 
	 * @return 调用者的类
	 */
	public static Class<?> getCaller() {
		try {
			StackTraceElement[] arrayAtack = new Throwable().getStackTrace();
			if (arrayAtack == null || arrayAtack.length <= 2)
				return null;
			StackTraceElement selfAtack = arrayAtack[1];
			for (int i = 2; i < arrayAtack.length; i++) {
				StackTraceElement atack = arrayAtack[i];
				if (selfAtack.getClassName().equals(atack.getClassName()))
					continue;
				return Class.forName(atack.getClassName());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取调用者的类,包含自身类
	 * 
	 * @return 调用者的类
	 */
	public static Class<?> getCallerIncludeSelf() {
		try {
			StackTraceElement[] arrayAtack = new Throwable().getStackTrace();
			if (arrayAtack.length < 2)
				return null;
			if (arrayAtack.length == 2) {
				return Class.forName(arrayAtack[1].getClassName());
			}
			StackTraceElement atack = arrayAtack[2];
			return Class.forName(atack.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	// /** 检查是否是IP地址 */
	// @Deprecated
	// public static boolean isIpAddr(String addr) {
	// if (addr == null || addr.trim().isEmpty())
	// return false;
	// addr = addr.trim();
	// try {
	// String[] strs = StringTools.splitString(addr, ',');
	// if (strs == null || strs.length != 4)
	// return false;
	// for (int i = 0; i < strs.length; i++) {
	// String string = strs[i];
	// int appp = Integer.valueOf(string);
	// if (appp < 0 || appp > 255)
	// return false;
	// }
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }

	//
	// public static void main(String[] args) throws NoSuchMethodException,
	// SecurityException {
	// List<String> ls = new ArrayList<String>();
	// System.out.println(getComponentType(ls).getName());
	// }
	//
	// public static <T> Class getComponentType(List<T> list) throws
	// NoSuchMethodException, SecurityException{
	// if (list==null)
	// return null;
	// Method method = list.getClass().getMethod("get",int.class);
	// return method.getReturnType();
	// }
}
