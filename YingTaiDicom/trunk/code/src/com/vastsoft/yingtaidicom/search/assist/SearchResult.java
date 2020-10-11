package com.vastsoft.yingtaidicom.search.assist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

/**
 * @author jben
 *
 */
public class SearchResult {
	private Map<ExternalSystemType, Map<String, AbstractSystemEntity>> mapSystemEntity = new HashMap<ExternalSystemType, Map<String, AbstractSystemEntity>>();

	public void saveObject(SystemIdentity systemIdentity, AbstractSystemEntity object)
			throws PatientDataException, CloneNotSupportedException {
		if (systemIdentity == null)
			throw new PatientDataException("系统身份标识必须指定！");
		if (object == null)
			throw new PatientDataException("必须指定要保存的对象！");
		if (!object.getSystemIdentity().getSystemType().equals(systemIdentity.getSystemType()))
			throw new PatientDataException("执行操作的执行器所属系统必须与要保存的数据来自于同一个系统！");
		Map<String, AbstractSystemEntity> mapTmp = this.mapSystemEntity.get(systemIdentity.getSystemType());
		if (mapTmp == null) {
			mapTmp = new HashMap<String, AbstractSystemEntity>();
			this.mapSystemEntity.put(systemIdentity.getSystemType(), mapTmp);
		}
		mapTmp.put(object.getUid(), (AbstractSystemEntity) object.clone());
	}

	public void saveSubObject(String parentUid, List<AbstractSystemEntity> abstractSystemEntities)
			throws PatientDataException {
		AbstractSystemEntity parentEntity = this.findObjectByUid(parentUid);
		if (parentEntity == null)
			throw new PatientDataException("指定的父数据未找到！");
		parentEntity.saveSubEntity(abstractSystemEntities);
	}

	public void saveSubObject(String parentUid, AbstractSystemEntity... abstractSystemEntities)
			throws PatientDataException {
		AbstractSystemEntity parentEntity = this.findObjectByUid(parentUid);
		if (parentEntity == null)
			throw new PatientDataException("指定的父数据未找到！");
		parentEntity.saveSubEntity(abstractSystemEntities);
	}

	public AbstractSystemEntity findObjectByUid(String objectUid) {
		if (StringTools.isEmpty(objectUid))
			return null;
		for (ExternalSystemType externalSystemType : this.mapSystemEntity.keySet()) {
			AbstractSystemEntity result = this.findObjectByUid(externalSystemType, objectUid);
			if (result != null)
				return result;
		}
		return null;
	}

	public AbstractSystemEntity findObjectByUid(ExternalSystemType externalSystemType, String objectUid) {
		if (externalSystemType == null || StringTools.isEmpty(objectUid))
			return null;
		Map<String, AbstractSystemEntity> mapTmp = this.mapSystemEntity.get(externalSystemType);
		if (CommonTools.isEmpty(mapTmp))
			return null;
		for (AbstractSystemEntity abstractSystemEntity : mapTmp.values()) {
			if (objectUid.equalsIgnoreCase(abstractSystemEntity.getUid()))
				return abstractSystemEntity;
		}
		return null;
	}

	public AbstractSystemEntity findEntityByRemoteParamEntry(ExternalSystemType externalSystemType,
			RemoteParamEntry remoteParamEntry) {
		if (externalSystemType == null || remoteParamEntry == null)
			return null;
		Iterator<AbstractSystemEntity> iterator = this.getIterator(externalSystemType);
		if (iterator == null)
			return null;
		while (iterator.hasNext()) {
			AbstractSystemEntity abstractSystemEntity = (AbstractSystemEntity) iterator.next();
			if (abstractSystemEntity == null)
				continue;
			if (abstractSystemEntity.getRemoteParamEntry().equals(remoteParamEntry))
				return abstractSystemEntity;
		}
		return null;
	}

	/** 获取指定系统保存的数据的迭代器 */
	public Iterator<AbstractSystemEntity> getIterator(ExternalSystemType externalSystemType) {
		Map<String, AbstractSystemEntity> mapTmp = this.mapSystemEntity.get(externalSystemType);
		if (CommonTools.isEmpty(mapTmp))
			return null;
		return mapTmp.values().iterator();
	}

	public boolean isEmpty() {
		if (CommonTools.isEmpty(this.mapSystemEntity))
			return true;
		for (Map<String, AbstractSystemEntity> mapTmp : this.mapSystemEntity.values()) {
			if (!CommonTools.isEmpty(mapTmp))
				return false;
		}
		return true;
	}
}
