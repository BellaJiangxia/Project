package com.vastsoft.yingtai.module.org.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class OrgChangeStatus extends SingleClassConstant {
	
	private static final Map<Integer, OrgChangeStatus> mapOrgChangeStatus = new HashMap<Integer, OrgChangeStatus>();

	public static final OrgChangeStatus VALID = new OrgChangeStatus(1, "已通过");
	public static final OrgChangeStatus INVALID = new OrgChangeStatus(0, "无效");
	public static final OrgChangeStatus APPROVING = new OrgChangeStatus(11, "审核中");
	public static final OrgChangeStatus REJECTED = new OrgChangeStatus(12, "已拒绝");

	static {
		OrgChangeStatus.mapOrgChangeStatus.put(VALID.getCode(), VALID);
		OrgChangeStatus.mapOrgChangeStatus.put(INVALID.getCode(), INVALID);
		OrgChangeStatus.mapOrgChangeStatus.put(APPROVING.getCode(), APPROVING);
		OrgChangeStatus.mapOrgChangeStatus.put(REJECTED.getCode(), REJECTED);
	}

	protected OrgChangeStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static OrgChangeStatus parseCode(int iCode) {
		return OrgChangeStatus.mapOrgChangeStatus.get(iCode);
	}

	public static List<OrgChangeStatus> getAllOrgChangeStatus() {
		return new ArrayList<OrgChangeStatus>(OrgChangeStatus.mapOrgChangeStatus.values());
	}
}
