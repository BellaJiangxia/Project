package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.constants.HisSystemBrand;

public final class ZhibangHisVersion extends AbstractExternalSystemVersion {
	/** 版本1 */
	public static final ZhibangHisVersion ZHV_1 = new ZhibangHisVersion(10, "V1.0", HisSystemBrand.ZHIBANG,
			"com/vastsoft/yingtaidicom/search/orgsearch/systems/his/brand/Zhibang/ver1/config/his_zhibang_ver1_ibatis_config.xml");

	private static Map<Integer, ZhibangHisVersion> mapJinpanHisVersion = new HashMap<Integer, ZhibangHisVersion>();

	static {
		mapJinpanHisVersion.put(ZHV_1.getCode(), ZHV_1);
	}

	protected ZhibangHisVersion(int iCode, String strName, AbstractExternalSystemBrand system_brand,
			String ibatis_config_path) {
		super(iCode, strName, system_brand, ibatis_config_path);
	}
	// public ZhibangHisVersion getDedaultVersion() {
	// return ZhibangHisVersion.ZHV_1;
	// }

	// public static ZhibangHisVersion parseCode(int code) {
	// return mapJinpanHisVersion.get(code);
	// }

	public static ZhibangHisVersion parseName(String name) {
		for (Iterator<ZhibangHisVersion> iterator = mapJinpanHisVersion.values().iterator(); iterator.hasNext();) {
			ZhibangHisVersion type = (ZhibangHisVersion) iterator.next();
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}
}
