package com.vastsoft.yingtaidicom.search.orgsearch.constants;

import com.vastsoft.util.common.SingleClassConstant;

public abstract class AbstractExternalSystemVersion extends SingleClassConstant {
	private AbstractExternalSystemBrand system_brand;
	private String ibatis_config_path;

	protected AbstractExternalSystemVersion(int iCode, String strName, AbstractExternalSystemBrand system_brand,
			String ibatis_config_path) {
		super(iCode, strName);
		this.system_brand = system_brand;
		this.ibatis_config_path = (ibatis_config_path);
	}

	public AbstractExternalSystemBrand getSystem_brand() {
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
