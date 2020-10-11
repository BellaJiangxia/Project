package com.vastsoft.yingtaidicom.search.orgsearch.constants;

import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;

public abstract class AbstractExternalSystemBrand extends AbstractSystemAdapterContant {
	private ExternalSystemType system_type;
	
	protected AbstractExternalSystemBrand(int iCode, String strName,
			Class<? extends ExternalSystemAdapter> externalSystemAdapter_class,
			ExternalSystemType system_type) {
		super(iCode, strName, externalSystemAdapter_class);
		this.system_type = system_type;
	}

	public ExternalSystemType getSystem_type() {
		return system_type;
	}

	@Override
	public String toString() {
		return this.system_type.toString()+" 品牌："+super.getName();
	}
}
