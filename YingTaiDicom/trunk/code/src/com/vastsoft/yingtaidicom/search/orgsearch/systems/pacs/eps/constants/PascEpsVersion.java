package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.constants.PacsSystenBrand;

public class PascEpsVersion extends AbstractExternalSystemVersion {
	/** v1.0 */
	public static final PascEpsVersion V_1 = new PascEpsVersion(10, "V1.0", PacsSystenBrand.EPS,
			"com/vastsoft/yingtaidicom/search/orgsearch/systems/pacs/eps/ver1/config/pacs_eps_ver1_ibatis_config.xml");
	
	private static Map<Integer, PascEpsVersion> mapPascEpsVersion = new HashMap<Integer, PascEpsVersion>();
	static {
		mapPascEpsVersion.put(V_1.getCode(), V_1);
	}

	protected PascEpsVersion(int iCode, String strName, AbstractExternalSystemBrand system_brand,
			String ibatis_config_path) {
		super(iCode, strName, system_brand, ibatis_config_path);
	}

	public static AbstractExternalSystemVersion parseName(String name) {
		for (Iterator<PascEpsVersion> iterator = mapPascEpsVersion.values().iterator(); iterator.hasNext();) {
			PascEpsVersion type = (PascEpsVersion) iterator.next();
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}
}
