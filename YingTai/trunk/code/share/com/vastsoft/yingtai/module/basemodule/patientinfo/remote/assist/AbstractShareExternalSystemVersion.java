package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

import com.vastsoft.util.common.SingleClassConstant;

public abstract class AbstractShareExternalSystemVersion extends SingleClassConstant {
	private AbstractShareExternalSystemBrand system_brand;
	private String ibatis_config_path;

	protected AbstractShareExternalSystemVersion(int iCode, String strName, AbstractShareExternalSystemBrand system_brand,
			String ibatis_config_path) {
		super(iCode, strName);
		this.system_brand = system_brand;
		this.ibatis_config_path = (ibatis_config_path);
	}

	public AbstractShareExternalSystemBrand getSystem_brand() {
		return system_brand;
	}

	@Override
	public String toString() {
		return system_brand.toString() + " 版本：" + super.getName();
	}

	public String getIbatis_config_path() {
		return ibatis_config_path;
	}
}
