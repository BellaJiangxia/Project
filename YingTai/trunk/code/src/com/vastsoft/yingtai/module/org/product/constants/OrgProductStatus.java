package com.vastsoft.yingtai.module.org.product.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class OrgProductStatus extends SingleClassConstant {

	protected OrgProductStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, OrgProductStatus> mapOrgServiceStatus = new HashMap<Integer, OrgProductStatus>();

	public static final OrgProductStatus VALID = new OrgProductStatus(1, "有效");
	public static final OrgProductStatus INVALID = new OrgProductStatus(0, "无效");
	public static final OrgProductStatus DISABLE = new OrgProductStatus(2, "禁用");

	static {
		OrgProductStatus.mapOrgServiceStatus.put(VALID.getCode(), VALID);
		OrgProductStatus.mapOrgServiceStatus.put(INVALID.getCode(), INVALID);
		OrgProductStatus.mapOrgServiceStatus.put(DISABLE.getCode(), DISABLE);
	}

	public static OrgProductStatus parseCode(int iCode) {
		return OrgProductStatus.mapOrgServiceStatus.get(iCode);
	}

	public static List<OrgProductStatus> getAllDictionaryStatus() {
		return new ArrayList<OrgProductStatus>(OrgProductStatus.mapOrgServiceStatus.values());
	}

}
