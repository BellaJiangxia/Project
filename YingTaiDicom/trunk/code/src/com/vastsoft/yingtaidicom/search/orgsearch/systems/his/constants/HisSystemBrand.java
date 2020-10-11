package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.HisZhibangAdapter;

/** his系统厂商 */
public class HisSystemBrand extends AbstractExternalSystemBrand {
	/** 智邦HIS */
	public static final HisSystemBrand ZHIBANG = new HisSystemBrand(10, "智邦", HisZhibangAdapter.class,
			ExternalSystemType.HIS);

	private static Map<Integer, HisSystemBrand> mapHisSystemBrand = new HashMap<Integer, HisSystemBrand>();
	static {
		mapHisSystemBrand.put(ZHIBANG.getCode(), ZHIBANG);
	}

	protected HisSystemBrand(int iCode, String strName,
			Class<? extends ExternalSystemAdapter> externalSystemAdapter_class, ExternalSystemType system_type) {
		super(iCode, strName, externalSystemAdapter_class, system_type);
	}

	public static HisSystemBrand parseCode(int code) {
		return mapHisSystemBrand.get(code);
	}

	public static HisSystemBrand parseName(String name) {
		for (Iterator<HisSystemBrand> iterator = mapHisSystemBrand.values().iterator(); iterator.hasNext();) {
			HisSystemBrand type = (HisSystemBrand) iterator.next();
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}
}
