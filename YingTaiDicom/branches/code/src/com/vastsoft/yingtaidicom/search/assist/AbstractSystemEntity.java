package com.vastsoft.yingtaidicom.search.assist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public abstract class AbstractSystemEntity implements Cloneable {
	private String uid;
	private RemoteParamEntry remoteParamEntry;
	private SystemIdentity systemIdentity;

	private AbstractSystemEntity parentEntity;
	private Map<ExternalSystemType, Set<AbstractSystemEntity>> mapSubEntity = new HashMap<ExternalSystemType, Set<AbstractSystemEntity>>();

	public AbstractSystemEntity(RemoteParamEntry remoteParamEntry, SystemIdentity systemIdentity) {
		this.uid = StringTools.getUUID();
		this.remoteParamEntry = remoteParamEntry;
		if (remoteParamEntry == null)
			throw new RuntimeException("必须指定远程参数映射！");
		this.systemIdentity = systemIdentity;
		if (systemIdentity == null)
			throw new RuntimeException("必须指定创建者身份！");
	}

	public RemoteParamEntry getRemoteParamEntry() {
		return remoteParamEntry;
	}

	public String getUid() {
		return this.uid;
	}

	public void saveSubEntity(List<AbstractSystemEntity> abstractSystemEntitys) throws PatientDataException {
		this.saveSubEntity(ArrayTools.collectionToArray(abstractSystemEntitys,AbstractSystemEntity.class));
	}

	public void saveSubEntity(AbstractSystemEntity... abstractSystemEntitys) throws PatientDataException {
		if (ArrayTools.isEmpty(abstractSystemEntitys))
			throw new NullPointerException("指定的要添加的子数据不能为null！");
		for (AbstractSystemEntity abstractSystemEntity : abstractSystemEntitys) {
			if (abstractSystemEntity == null)
				continue;
			if (!this.systemIdentity.getSystemType()
					.equals(abstractSystemEntity.getSystemIdentity().getSystemType().getParentType()))
				throw new PatientDataException("指定的数据的系统类型不是当前数据所属系统类型的子类型！");
			Set<AbstractSystemEntity> setEntity = this.mapSubEntity
					.get(abstractSystemEntity.getSystemIdentity().getSystemType());
			if (CollectionTools.isEmpty(setEntity)) {
				setEntity = new HashSet<AbstractSystemEntity>();
				this.mapSubEntity.put(abstractSystemEntity.getSystemIdentity().getSystemType(), setEntity);
			}
			setEntity.add(abstractSystemEntity);
			abstractSystemEntity.setParentEntity(this);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void organizeEntity(RemoteResult remoteResult) throws PatientDataException {
		// 整理
		this.organize(remoteResult);
		if (CommonTools.isEmpty(this.mapSubEntity))
			return;
		for (Set<AbstractSystemEntity> setSubEntity : this.mapSubEntity.values()) {
			for (AbstractSystemEntity abstractSystemEntity : setSubEntity) {
				abstractSystemEntity.organizeEntity(remoteResult);
			}
		}
	}
	
	/**整理*/
	protected abstract void organize(RemoteResult remoteResult) throws PatientDataException;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		AbstractSystemEntity other = (AbstractSystemEntity) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	public SystemIdentity getSystemIdentity() {
		return systemIdentity;
	}

	public AbstractSystemEntity getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(AbstractSystemEntity parentEntity) {
		this.parentEntity = parentEntity;
	}
}
