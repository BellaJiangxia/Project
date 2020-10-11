package com.vastsoft.yingtai.module.org.realtion.constants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class OrgRelationPatientInfoShareType extends SingleClassConstant {
	/** 不共享病例库 */
	public static final OrgRelationPatientInfoShareType NONE = new OrgRelationPatientInfoShareType(0, "不共享");
	/** 非完全共享 */
	public static final OrgRelationPatientInfoShareType INCOMPLETE_SHARED = new OrgRelationPatientInfoShareType(1, "非完全共享");
	/** 完全共享 */
	public static final OrgRelationPatientInfoShareType SHARED = new OrgRelationPatientInfoShareType(2, "完全共享");

	private static Map<Integer, OrgRelationPatientInfoShareType> mapOrgRelationPatientInfoShareType = new LinkedHashMap<Integer, OrgRelationPatientInfoShareType>();

	static {
		mapOrgRelationPatientInfoShareType.put(NONE.getCode(), NONE);
		mapOrgRelationPatientInfoShareType.put(INCOMPLETE_SHARED.getCode(), INCOMPLETE_SHARED);
		mapOrgRelationPatientInfoShareType.put(SHARED.getCode(), SHARED);
	}

	protected OrgRelationPatientInfoShareType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static OrgRelationPatientInfoShareType parseCode(int code) {
		OrgRelationPatientInfoShareType r = mapOrgRelationPatientInfoShareType.get(code);
		return r == null ? NONE : r;
	}

}
