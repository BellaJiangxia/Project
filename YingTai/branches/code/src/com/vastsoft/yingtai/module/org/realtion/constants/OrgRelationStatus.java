package com.vastsoft.yingtai.module.org.realtion.constants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class OrgRelationStatus extends SingleClassConstant {
	/**有效*/
	public static final OrgRelationStatus VALID = new OrgRelationStatus(1, "有效");
//	public static final OrgRelationStatus DISABLED = new OrgRelationStatus(10, "禁用");
	/**审核中*/
	public static final OrgRelationStatus APPROVING = new OrgRelationStatus(11, "审核中");
	/**已拒绝*/
	public static final OrgRelationStatus REJECTED = new OrgRelationStatus(12, "已拒绝");
//	public static final OrgRelationStatus DELETED=new OrgRelationStatus(15, "已删除");
	
	private static Map<Integer, OrgRelationStatus> mapOrgRelationStatus = new LinkedHashMap<Integer, OrgRelationStatus>();
	
	static{
		mapOrgRelationStatus.put(VALID.getCode(), VALID);
//		mapOrgRelationStatus.put(DISABLED.getCode(), DISABLED);
		mapOrgRelationStatus.put(APPROVING.getCode(), APPROVING);
		mapOrgRelationStatus.put(REJECTED.getCode(), REJECTED);
//		mapOrgRelationStatus.put(DELETED.getCode(), DELETED);
	}
	
	protected OrgRelationStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static OrgRelationStatus parseCode(int friendStatus) {
		return mapOrgRelationStatus.get(friendStatus);
	}

}
