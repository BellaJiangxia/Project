package com.vastsoft.yingtai.module.user.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class OrgUserMapperStatus extends SingleClassConstant {

	private static final Map<Integer, OrgUserMapperStatus> mapOrgUserMapperStatus = new HashMap<Integer, OrgUserMapperStatus>();

	public static final OrgUserMapperStatus VALID = new OrgUserMapperStatus(1, "有效");
	public static final OrgUserMapperStatus APPROVING = new OrgUserMapperStatus(11, "审核中");
	public static final OrgUserMapperStatus REJECTED = new OrgUserMapperStatus(12, "已拒绝");

	static {
		OrgUserMapperStatus.mapOrgUserMapperStatus.put(VALID.getCode(), VALID);
		OrgUserMapperStatus.mapOrgUserMapperStatus.put(APPROVING.getCode(), APPROVING);
		OrgUserMapperStatus.mapOrgUserMapperStatus.put(REJECTED.getCode(), REJECTED);
	}

	protected OrgUserMapperStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static OrgUserMapperStatus parseCode(int iCode) {
		return OrgUserMapperStatus.mapOrgUserMapperStatus.get(iCode);
	}

	public static List<OrgUserMapperStatus> getAllOrgUserMapperStatus() {
		return new ArrayList<OrgUserMapperStatus>(OrgUserMapperStatus.mapOrgUserMapperStatus.values());
	}
}
