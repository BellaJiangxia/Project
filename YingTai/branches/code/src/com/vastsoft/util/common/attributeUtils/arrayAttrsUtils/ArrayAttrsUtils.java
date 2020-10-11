package com.vastsoft.util.common.attributeUtils.arrayAttrsUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.attributeUtils.ObjectUtils;
import com.vastsoft.util.common.attributeUtils.annotation.IgnoreAttrHandle;

@SuppressWarnings("rawtypes")
public class ArrayAttrsUtils {
	private List listObject;
	private List<AttrEntry> listAttrNames;

	public ArrayAttrsUtils(String[] attrs) {
		listAttrNames = new ArrayList<AttrEntry>();
		for (String string : attrs) {
			listAttrNames.add(new AttrEntry(string));
		}
	}

	/** 将数据序列化成指定个数组，取指定的属性 */
	public static Map<String, List<Object>> SerializeSingleArrayToList(List listObj, String... attrs) {
		if (listObj == null)
			return null;
		ArrayAttrsUtils au = new ArrayAttrsUtils(attrs);
		au.addExtraAttr(listObj);
		return au.Serialize();
	}

	private Map<String, List<Object>> Serialize() {
		Map<String, List<Object>> result = new HashMap<String, List<Object>>();
		if (listAttrNames != null && listAttrNames.size() > 0 && listObject != null && listObject.size() > 0) {
			for (Object obj : listObject) {
				if (obj == null)
					continue;
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
					if (params.length > 0)
						continue;
					String attributeName = ObjectUtils.takeAttributeNameByGetMethod(method, "get", "is");
					if (IgnoreAttrHandle.needIgnore(method))
						continue;
					AttrEntry ae = this.attrNameContains(attributeName);
					if (ae == null)
						continue;
					try {
						Object to = method.invoke(obj);
						String applyAttrName = ae.newName == null || ae.getNewName().trim().isEmpty() ? ae.getAttrName()
								: ae.getNewName();
						List<Object> listArray = result.get(applyAttrName);
						if (listArray == null) {
							listArray = new ArrayList<Object>();
							result.put(applyAttrName, listArray);
						}
						listArray.add(to);
					} catch (Exception e) {
					}
				}
			}
			return result;
		}
		return result;
	}

	private AttrEntry attrNameContains(String attributeName) {
		for (AttrEntry attrEntry : listAttrNames) {
			if (attrEntry.getAttrName().equals(attributeName)) {
				return attrEntry;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void addExtraAttr(List listObj) {
		if (listObject == null)
			listObject = new ArrayList<Object>();
		listObject.addAll(listObj);
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
}
