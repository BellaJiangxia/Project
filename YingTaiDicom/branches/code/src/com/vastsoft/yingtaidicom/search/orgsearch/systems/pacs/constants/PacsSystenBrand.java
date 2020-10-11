package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.EpsSysAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.WPacsVersionAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.YingTaiPacsVersionAdapter;

/**PACS系统品牌常量*/
public final class PacsSystenBrand extends AbstractExternalSystemBrand{
	/**eps*/
	public static final PacsSystenBrand EPS = new PacsSystenBrand(10, "EPS", EpsSysAdapter.class, ExternalSystemType.PACS);
	/**影泰PACS*/
	public static final PacsSystenBrand YINGTAI = new PacsSystenBrand(20, "YINGTAI", YingTaiPacsVersionAdapter.class, ExternalSystemType.PACS);
	/**WPACS*/
	public static final PacsSystenBrand WPACS = new PacsSystenBrand(30, "WPACS", WPacsVersionAdapter.class, ExternalSystemType.PACS);
	
	private static Map<Integer, PacsSystenBrand> mapPacsSystenBrand = new HashMap<Integer, PacsSystenBrand>();
	static {
		mapPacsSystenBrand.put(EPS.getCode(), EPS);
		mapPacsSystenBrand.put(YINGTAI.getCode(), YINGTAI);
		mapPacsSystenBrand.put(WPACS.getCode(), WPACS);
	}
	
	protected PacsSystenBrand(int iCode, String strName,
			Class<? extends ExternalSystemAdapter> externalSystemAdapter_class, ExternalSystemType system_type) {
		super(iCode, strName, externalSystemAdapter_class, system_type);
	}

	public static PacsSystenBrand parseName(String name){
		for (Iterator<PacsSystenBrand> iterator = mapPacsSystenBrand.values().iterator(); iterator.hasNext();) {
			PacsSystenBrand type = (PacsSystenBrand) iterator.next();
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}
}
