package com.vastsoft.yingtai.module.org.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class OrgProperty extends SingleClassConstant {

	private static final Map<Integer, OrgProperty> mapOrgProperty = new HashMap<Integer, OrgProperty>();

	public static final OrgProperty ORG = new OrgProperty(11, "单位机构");
	public static final OrgProperty TEAM = new OrgProperty(12, "医生团队");

	static {
		OrgProperty.mapOrgProperty.put(ORG.getCode(), ORG);
		OrgProperty.mapOrgProperty.put(TEAM.getCode(), TEAM);
	}

	protected OrgProperty(int iCode, String strName) {
		super(iCode, strName);
	}

	public static OrgProperty parseCode(int iCode) {
		return OrgProperty.mapOrgProperty.get(iCode);
	}

	public static List<OrgProperty> getAllOrgProperty() {
		return new ArrayList<OrgProperty>(OrgProperty.mapOrgProperty.values());
	}

}
