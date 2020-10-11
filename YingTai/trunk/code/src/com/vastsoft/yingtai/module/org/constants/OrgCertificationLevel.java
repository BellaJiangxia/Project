package com.vastsoft.yingtai.module.org.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**机构认证级别
 * @author jiangyubin
 *
 */
public final class OrgCertificationLevel extends SingleClassConstant {
	private static final Map<Integer, OrgCertificationLevel> mapCertificationLevel = new HashMap<Integer, OrgCertificationLevel>();

	/**未认证*/
	public static final OrgCertificationLevel NORMAL = new OrgCertificationLevel(1, "未认证");
	/**已认证*/
	public static final OrgCertificationLevel CERTIFICATIONED = new OrgCertificationLevel(2, "已认证");

	public static final OrgCertificationLevel FAILED = new OrgCertificationLevel(3, "认证失败");
	
	protected OrgCertificationLevel(int code, String name) {
		super(code, name);
	}

	static {
		OrgCertificationLevel.mapCertificationLevel.put(NORMAL.getCode(), NORMAL);
		OrgCertificationLevel.mapCertificationLevel.put(CERTIFICATIONED.getCode(), CERTIFICATIONED);
		OrgCertificationLevel.mapCertificationLevel.put(FAILED.getCode(), FAILED);
	}

	public static OrgCertificationLevel parseCode(int iCode) {
		return OrgCertificationLevel.mapCertificationLevel.get(iCode);
	}

	public static List<OrgCertificationLevel> getAll() {
		return new ArrayList<OrgCertificationLevel>(OrgCertificationLevel.mapCertificationLevel.values());
	}

}
