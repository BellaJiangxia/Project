package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

/** 系统身份标识 */
public class ShareSystemIdentity {
	private AbstractShareExternalSystemVersion systemVersion;

	public ShareSystemIdentity(AbstractShareExternalSystemVersion systemVersion) {
		super();
		this.systemVersion = systemVersion;
	}

	/**
	 * 获取系统类型
	 * 
	 * @return 返回执行器的系统类型
	 */
	public ShareExternalSystemType getSystemType() {
		return this.systemVersion.getSystem_brand().getSystem_type();
	}

	/**
	 * 获取外部系统厂商名称
	 * 
	 * @return 返回外部系统厂商名称
	 */
	public AbstractShareExternalSystemBrand getSystemBrand() {
		return this.systemVersion.getSystem_brand();
	}

	/**
	 * 获取外部系统版本号
	 * 
	 * @return 返回外部系统版本号
	 */
	public AbstractShareExternalSystemVersion getSystemVersion() {
		return this.systemVersion;
	}

	@Override
	public String toString() {
		return this.systemVersion.toString();
	}
}
