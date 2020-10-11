package com.vastsoft.yingtaidicom.search.orgsearch.constants;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;

public abstract class AbstractSystemAdapterContant extends SingleClassConstant {
	private Class<? extends ExternalSystemAdapter> externalSystemAdapter_class;
	protected AbstractSystemAdapterContant(int iCode, String strName,
			Class<? extends ExternalSystemAdapter> externalSystemAdapter_class) {
		super(iCode, strName);
		this.externalSystemAdapter_class = externalSystemAdapter_class;
	}
	
//	public Class<? extends ExternalSystemAdapter> getExternalSystemAdapter_class() {
//		return externalSystemAdapter_class;
//	}
	
	public ExternalSystemAdapter getAdapter() throws InstantiationException, IllegalAccessException {
		return (ExternalSystemAdapter) externalSystemAdapter_class.newInstance();
	}
}
