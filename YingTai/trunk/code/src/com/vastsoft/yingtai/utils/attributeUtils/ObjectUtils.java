package com.vastsoft.yingtai.utils.attributeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.utils.attributeUtils.annotation.IgnoreAttrHandle;

public class ObjectUtils {
	private Object value;
	private List<AttrEntry> listAttrNameEntry;
	private Map<String, Object> mapExtra;

	public void addExtraEntry(String name, Object value) {
		if (mapExtra == null)
			mapExtra = new HashMap<String, Object>();
		mapExtra.put(name, value);
	}

	public void addAttributeNameForSerializable(String... strName) {
		if (strName == null || strName.length <= 0)
			return;
		for (String string : strName) {
			if (string == null || string.trim().isEmpty())
				continue;
			listAttrNameEntry.add(new AttrEntry(string.trim()));
		}
	}

	public void addAttributeNameForSerializable(List<String> listName) {
		addAttributeNameForSerializable(listName.toArray(new String[listName.size()]));
	}

	/**
	 * 获取一个方法的属性名,根据指定前缀<br>
	 * 并去掉前缀，把去掉后的第一个字符变为小写<br>
	 */
	public static String takeAttributeNameByGetMethod(Method method, String... arrayPrifix) {
		if (method == null)
			return null;
		if (arrayPrifix == null || arrayPrifix.length <= 0)
			return null;
		for (String prifix : arrayPrifix) {
			if (!method.getName().startsWith(prifix))
				continue;
			if (method.getName().length() <= prifix.length())
				return null;
			String subName = method.getName().substring(prifix.length());
			return Character.toLowerCase(subName.charAt(0)) + (subName.length() <= 1 ? "" : subName.substring(1));
		}
		return null;
	}

	/** 获取一个类的所有属性名，不包含Object类中的属性 */
	public static List<String> getObjectAttrNames(Class<?> clazz, String... arrayPrifix) {
		if (clazz == null)
			return null;
		List<String> listAttrNames = new ArrayList<String>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getDeclaringClass().equals(Object.class))
				continue;
			String attrName = takeAttributeNameByGetMethod(method, arrayPrifix);
			if (attrName == null || attrName.trim().isEmpty())
				continue;
			listAttrNames.add(attrName);
		}
		return listAttrNames;
	}

	private AttrEntry attrNameContains(String attrName) {
		if (listAttrNameEntry == null || listAttrNameEntry.size() <= 0) {
			return null;
		}
		for (AttrEntry attrEntry : listAttrNameEntry) {
			if (attrEntry.getAttrName().equals(attrName)) {
				return attrEntry;
			}
		}
		return null;
	}

	public Map<String, Object> SerializeToMap() {
		Map<String, Object> mapAttr = new HashMap<String, Object>();
		Class<?> clazz = value.getClass();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getDeclaringClass().equals(Object.class))
				continue;
			if (!method.getName().startsWith("get") && !method.getName().startsWith("is"))
				continue;
			if (method.getReturnType() == null)
				continue;
			Class<?>[] params = method.getParameterTypes();
			if (params.length > 0)
				continue;
			String attributeName = ObjectUtils.takeAttributeNameByGetMethod(method, "get", "is");
			if (IgnoreAttrHandle.needIgnore(method))
				continue;
			AttrEntry ae = this.attrNameContains(attributeName);
			if (ae == null)
				continue;
			try {
				Object to = method.invoke(value);
				mapAttr.put(ae.getNewName() == null || ae.getNewName().trim().isEmpty() ? ae.getAttrName()
						: ae.getNewName(), to);
			} catch (Exception e) {
			}
		}
		if (mapExtra != null)
			mapAttr.putAll(mapExtra);
		return mapAttr;
	}

	public ObjectUtils(Object obj) {
		super();
		value = obj;
		listAttrNameEntry = new ArrayList<AttrEntry>();
	}
	
	/** 此方法支持patient.name.length格式的fieldName
	 * @param obj
	 * @param fieldName 此方法支持patient.name.length格式的fieldName
	 * @return
	 */
	public static Object getValueByPath(Object obj, String fieldName) {
		if (fieldName.contains(".")) {
			String[] fns = StringTools.splitString(fieldName, '.');
			if (ArrayTools.isEmpty(fns) || fns.length <= 1)
				return getValue(obj, fieldName);
			Object result = obj;
			for (String string : fns) {
				result = getValue(result, string);
			}
			return result;
		}else
			return getValue(obj, fieldName);
	}

	public static Object getValue(Object obj, String fieldName) {
		if (obj instanceof Map)
			return ((Map)obj).get(fieldName);
		Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getDeclaringClass().equals(Object.class))
				continue;
			if (!method.getName().startsWith("get") && !method.getName().startsWith("is"))
				continue;
			if (method.getReturnType() == null)
				continue;
			Class<?>[] params = method.getParameterTypes();
			if (params != null && params.length > 0)
				continue;
			String attributeName = ObjectUtils.takeAttributeNameByGetMethod(method, "get", "is");
			if (IgnoreAttrHandle.needIgnore(method))
				continue;
			if (!fieldName.equals(attributeName))
				continue;
			try {
				Object to = method.invoke(obj);
				return to;
			} catch (Exception e) {
			}
		}
		return null;
	}

	private class AttrEntry {
		private String attrName;
		private String newName;

		public AttrEntry(String attrName) {
			if (attrName.contains(">")) {
				String[] nns = attrName.split(">");
				if (nns.length >= 2) {
					this.attrName = nns[0];
					this.newName = nns[1];
					return;
				}
			}
			this.attrName = attrName;
		}

		public String getAttrName() {
			return attrName;
		}

		public String getNewName() {
			return newName;
		}
	}

	public static <T> Map<String, Object> objToMap(Class<T> cla, T obj)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapAttr = new HashMap<String, Object>();
		Field[] flds = cla.getDeclaredFields();
		for (Field field : flds) {
			field.setAccessible(true);
			Object result = field.get(obj);
			mapAttr.put(field.getName(), result);
			
		}
		return mapAttr;
	}
}
