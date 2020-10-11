package com.vastsoft.yingtaidicom.search.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.list.SetUniqueList;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;

/** 远程参数管理器 */
public class RemoteParamsManager {
	private Map<RemoteParamEntry, RemoteParamEntry> mapParams = new HashMap<RemoteParamEntry, RemoteParamEntry>();

	public void addParam(RemoteParamsType paramType, String paramValue) {
		if (paramType == null)
			return;
		if (StringTools.isEmpty(paramValue))
			return;
		RemoteParamEntry paramEntry = new RemoteParamEntry(paramType, paramValue);
		this.mapParams.put(paramEntry, null);
	}

	public void addAboutParam(RemoteParamEntry paramEntry, RemoteParamEntry anotherParamEntry) {
		if (paramEntry == null || anotherParamEntry == null)
			throw new NullPointerException("关联参数不能为null");
		this.mapParams.put(paramEntry, anotherParamEntry);
		this.mapParams.put(anotherParamEntry, paramEntry);
	}

	public void addParams(RemoteParamsType paramType, String... paramValues) {
		if (paramType == null)
			return;
		if (ArrayTools.isEmpty(paramValues))
			return;
		for (String paramValue : paramValues) {
			this.addParam(paramType, paramValue);
		}
	}

	public List<String> getParamValuesByType(RemoteParamsType paramsType) {
		if (paramsType == null)
			return null;
		@SuppressWarnings("unchecked")
		List<String> result = SetUniqueList.decorate(new ArrayList<String>());
		for (RemoteParamEntry paramEntry : this.mapParams.keySet()) {
			if (paramsType.equals(paramEntry.getRemoteParamsType()))
				result.add(paramEntry.remoteParamValue);
		}
		return result;
	}

	@Override
	public String toString() {
		return "RemoteParamsManager [mapParams=" + mapParams + "]";
	}

	public Iterator<RemoteParamEntry> getKeyIterator() {
		return this.mapParams.keySet().iterator();
	}

	public static interface RemoteParamEntryForeach {
		public void doForeach(RemoteParamEntry paramEntry) throws SearchExecuteException;
	}

	public boolean isEmpty() {
		return this.mapParams.isEmpty();
	}

	public List<RemoteParamsType> getParamTypes() {
		@SuppressWarnings("unchecked")
		List<RemoteParamsType> result = SetUniqueList.decorate(new ArrayList<RemoteParamsType>());
		for (RemoteParamEntry paramEntry : this.mapParams.keySet()) {
			result.add(paramEntry.remoteParamsType);
		}
		return result;
	}

	public static class RemoteParamEntry {
		private RemoteParamsType remoteParamsType;
		private String remoteParamValue;

		public RemoteParamEntry(RemoteParamsType remoteParamsType, String remoteParamValue) {
			super();
			this.remoteParamsType = remoteParamsType;
			this.remoteParamValue = remoteParamValue;
			if (remoteParamsType == null || StringTools.isEmpty(remoteParamValue))
				throw new RuntimeException("参数类型和参数值不能为空！");
		}

		public RemoteParamsType getRemoteParamsType() {
			return remoteParamsType;
		}

		public String getRemoteParamValue() {
			return remoteParamValue;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((remoteParamValue == null) ? 0 : remoteParamValue.hashCode());
			result = prime * result + ((remoteParamsType == null) ? 0 : remoteParamsType.hashCode());
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
			RemoteParamEntry other = (RemoteParamEntry) obj;
			if (remoteParamValue == null) {
				if (other.remoteParamValue != null)
					return false;
			} else if (!remoteParamValue.equals(other.remoteParamValue))
				return false;
			if (remoteParamsType == null) {
				if (other.remoteParamsType != null)
					return false;
			} else if (!remoteParamsType.equals(other.remoteParamsType))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "RemoteParamEntry [remoteParamsType=" + remoteParamsType + ", remoteParamValue=" + remoteParamValue
					+ "]";
		}
	}
}
