package com.vastsoft.util.common;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
	private Map<K, V> mapValue = new HashMap<K, V>();

	public MapBuilder() {
		super();
	}

	public MapBuilder(Map<K, V> mapValue) {
		super();
		this.mapValue = mapValue;
	}

	public MapBuilder(K key, V value) {
		super();
		this.mapValue.put(key, value);
	}

	public MapBuilder<K, V> put(K key, V value) {
		this.mapValue.put(key, value);
		return this;
	}

	public MapBuilder<K, V> putAll(Map<? extends K, ? extends V> m) {
		this.mapValue.putAll(m);
		return this;
	}

	public boolean isEmpty() {
		return this.mapValue.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.mapValue.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.mapValue.containsValue(value);
	}

	public MapBuilder<K, V> clear() {
		this.mapValue.clear();
		return this;
	}

	public Map<K, V> toMap() {
		return new HashMap<K, V>(this.mapValue);
	}
}
