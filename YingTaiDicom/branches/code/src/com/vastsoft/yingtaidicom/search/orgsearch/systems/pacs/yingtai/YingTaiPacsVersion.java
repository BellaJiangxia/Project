package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.constants.PacsSystenBrand;

public class YingTaiPacsVersion extends AbstractExternalSystemVersion {
	/** v1.0 */
	public static final YingTaiPacsVersion VER_1 = new YingTaiPacsVersion(10, "V1.0", PacsSystenBrand.YINGTAI,
			"com/vastsoft/yingtaidicom/search/orgsearch/systems/pacs/yingtai/ver1/config/pacs_eps_ver1_ibatis_config.xml");

	protected YingTaiPacsVersion(int iCode, String strName, AbstractExternalSystemBrand system_brand,
			String ibatis_config_path) {
		super(iCode, strName, system_brand, ibatis_config_path);
	}

	private static Map<Integer, YingTaiPacsVersion> mapYingTaiPascVersion = new HashMap<Integer, YingTaiPacsVersion>();
	static {
		mapYingTaiPascVersion.put(VER_1.getCode(), VER_1);
	}

	public static AbstractExternalSystemVersion parseName(String name) {
		for (Iterator<YingTaiPacsVersion> iterator = mapYingTaiPascVersion.values().iterator(); iterator.hasNext();) {
			YingTaiPacsVersion type = (YingTaiPacsVersion) iterator.next();
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}
}
