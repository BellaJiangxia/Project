package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

import com.vastsoft.util.common.SingleClassConstant;

public abstract class AbstractShareExternalSystemBrand extends SingleClassConstant{
	private ShareExternalSystemType system_type;
	
	protected AbstractShareExternalSystemBrand(int iCode, String strName,ShareExternalSystemType system_type) {
		super(iCode, strName);
		this.system_type = system_type;
	}

	public ShareExternalSystemType getSystem_type() {
		return system_type;
	}

	@Override
	public String toString() {
		return this.system_type.toString()+" 品牌："+super.getName();
	}
}
