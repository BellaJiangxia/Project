package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.constants.PacsSystenBrand;

public class WPacsVersion extends AbstractExternalSystemVersion {
	/** v1.0 */
	public static final WPacsVersion VER_1 = new WPacsVersion(10, "V1.0", PacsSystenBrand.WPACS,
			"com/vastsoft/yingtaidicom/search/orgsearch/systems/pacs/wpacs/ver1/config/wpacs_ver1_ibatis_config.xml");

	protected WPacsVersion(int iCode, String strName, AbstractExternalSystemBrand system_brand,
			String ibatis_config_path) {
		super(iCode, strName, system_brand, ibatis_config_path);
	}

	private static Map<Integer, WPacsVersion> mapYingTaiPascVersion = new HashMap<Integer, WPacsVersion>();
	static {
		mapYingTaiPascVersion.put(VER_1.getCode(), VER_1);
	}

	public static AbstractExternalSystemVersion parseName(String name) {
		for (Iterator<WPacsVersion> iterator = mapYingTaiPascVersion.values().iterator(); iterator.hasNext();) {
			WPacsVersion type = (WPacsVersion) iterator.next();
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}
}
