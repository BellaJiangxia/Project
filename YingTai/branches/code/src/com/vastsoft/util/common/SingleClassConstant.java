package com.vastsoft.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.vastsoft.util.common.inf.Constants;

public abstract class SingleClassConstant implements Constants {
	private static final Map<Class<? extends SingleClassConstant>, LinkedHashMap<Integer,SingleClassConstant>> mapConstants = 
			new HashMap<Class<? extends SingleClassConstant>, LinkedHashMap<Integer,SingleClassConstant>>();

	private int code;
	private String name;

	protected SingleClassConstant(int code, String name) {
		this.code = code;
		this.name = name;
		if (StringTools.isEmpty(name))
			throw new NullPointerException("name不能为空！");
		SingleClassConstant.putConstants(this.getClass(), this);
	}

	private static <T extends SingleClassConstant> boolean putConstants(Class<T> clazz, SingleClassConstant constant) {
		LinkedHashMap<Integer,SingleClassConstant> values = SingleClassConstant.mapConstants.get(clazz);
		if (values == null) {
			values = new LinkedHashMap<Integer,SingleClassConstant>();
			SingleClassConstant.mapConstants.put(clazz, values);
		}
		SingleClassConstant scc = values.get(constant.getCode());
		if (scc != null)
			throw new IllegalArgumentException("重复的常量CODE："+constant);
		values.put(constant.getCode(), constant);
		return true;
	}

	public int getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "SingleClassConstant [code=" + code + ", name=" + name + ", getClass()=" + getClass() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleClassConstant other = (SingleClassConstant) obj;
		if (code != other.code)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * 获取指定类的全部条目，注意，默认不包含其子类的条目
	 * 
	 * @param constantType
	 * @return
	 */
	public static <T extends SingleClassConstant> Map<Integer, T> getMap(Class<T> constantType) {
		return getMap(constantType, false);
	}

	/**
	 * 获取指定类的全部条目
	 * 
	 * @param constantType
	 *            指定类
	 * @param containChildClass
	 *            是否包含子类条目
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends SingleClassConstant> Map<Integer, T> getMap(Class<T> constantType,
			boolean containChildClass) {
		Map<Integer, T> result = new LinkedHashMap<Integer, T>();
		for (Class<? extends SingleClassConstant> ct : SingleClassConstant.mapConstants.keySet()) {
			if (constantType.equals(ct) || ReflectTools.isChildClassOf(constantType, ct)) {
				LinkedHashMap<Integer, SingleClassConstant> setscc = SingleClassConstant.mapConstants.get(ct);
				if (!CommonTools.isEmpty(setscc)) {
					for (SingleClassConstant singleClassConstant : setscc.values()) {
						result.put(((SingleClassConstant) singleClassConstant).code, (T) singleClassConstant);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 注意，默认不包含其子类的条目
	 * 
	 * @param constantType
	 * @return
	 */
	public static <T extends SingleClassConstant> List<T> getAll(Class<T> constantType) {
		return getAll(constantType, false);
	}

	/**
	 * @param constantType
	 * @param containChildClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends SingleClassConstant> List<T> getAll(Class<T> constantType, boolean containChildClass) {
		List<T> result = new ArrayList<T>();
		for (Class<? extends SingleClassConstant> ct : SingleClassConstant.mapConstants.keySet()) {
			if (constantType.equals(ct) || ReflectTools.isChildClassOf(constantType, ct)) {
				LinkedHashMap<Integer, SingleClassConstant> setscc = SingleClassConstant.mapConstants.get(ct);
				result.addAll((Collection<? extends T>) setscc.values());
			}
		}
		return result;
	}
}
