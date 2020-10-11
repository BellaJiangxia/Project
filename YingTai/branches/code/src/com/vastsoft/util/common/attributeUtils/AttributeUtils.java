package com.vastsoft.util.common.attributeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeUtils {
	private List<ObjectUtils> listOU;
	private List<String> listAttrName;

	public static List<Map<String, Object>> SerializeList(List<?> listObj, String... attrs) {
		if (listObj == null)
			return null;
		AttributeUtils au = new AttributeUtils(attrs);
		au.addExtraAttr(listObj);
		return au.Serialize();
	}

	public static Map<String, Object> SerializeObj(Object obj, String... attrs) {
		if (obj == null)
			return null;
		AttributeUtils au = new AttributeUtils(attrs);
		au.addExtraAttr(obj);
		return au.Serialize().get(0);
	}

	private AttributeUtils() {
		super();
		listOU = new ArrayList<ObjectUtils>();
		listAttrName = new ArrayList<String>();
	}

	private AttributeUtils(String... attrs) {
		this();
		if (attrs != null && attrs.length > 0) {
			for (String string : attrs) {
				listAttrName.add(string);
			}
		}
	}

	/** 添加一个要格式化的对象 */
	private void addExtraAttr(List<?> listObj) {
		if (listObj == null || listObj.size() <= 0)
			return;
		for (Object object : listObj) {
			ObjectUtils ou = new ObjectUtils(object);
			ou.addAttributeNameForSerializable(listAttrName);
			listOU.add(ou);
		}
	}

	/** 添加一个要格式化的对象 */
	private void addExtraAttr(Object obj, Entry... entries) {
		ObjectUtils ou = new ObjectUtils(obj);
		ou.addAttributeNameForSerializable(listAttrName);
		if (entries != null && entries.length > 0) {
			for (Entry entry : entries) {
				ou.addExtraEntry(entry.getKey(), entry.getValue());
			}
		}
		listOU.add(ou);
	}

	private List<Map<String, Object>> Serialize() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (ObjectUtils ou : listOU) {
			if (ou == null)
				continue;
			result.add(ou.SerializeToMap());
		}
		return result;
	}

	public class Entry {
		private String key;
		private Object value;

		public Entry(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}
	}
}
