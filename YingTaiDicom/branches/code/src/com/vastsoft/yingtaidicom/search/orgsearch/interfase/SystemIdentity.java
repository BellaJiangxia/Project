package com.vastsoft.yingtaidicom.search.orgsearch.interfase;

import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;

/** 系统身份标识 */
public class SystemIdentity {
	private AbstractExternalSystemVersion systemVersion;

	public SystemIdentity(AbstractExternalSystemVersion systemVersion) {
		super();
		this.systemVersion = systemVersion;
	}

	/**
	 * 获取系统类型
	 * 
	 * @return 返回执行器的系统类型
	 */
	public ExternalSystemType getSystemType() {
		return this.systemVersion.getSystem_brand().getSystem_type();
	}

	/**
	 * 获取外部系统厂商名称
	 * 
	 * @return 返回外部系统厂商名称
	 */
	public AbstractExternalSystemBrand getSystemBrand() {
		return this.systemVersion.getSystem_brand();
	}

	/**
	 * 获取外部系统版本号
	 * 
	 * @return 返回外部系统版本号
	 */
	public AbstractExternalSystemVersion getSystemVersion() {
		return this.systemVersion;
	}

	@Override
	public String toString() {
		return this.systemVersion.toString();
	}
}
