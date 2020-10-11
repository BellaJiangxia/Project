package com.vastsoft.yingtai.module.org.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class OrgStatus extends SingleClassConstant {

	private static final Map<Integer, OrgStatus> mapDictionaryStatus = new HashMap<Integer, OrgStatus>();

	public static final OrgStatus VALID = new OrgStatus(1, "有效");
	public static final OrgStatus DISABLED = new OrgStatus(10, "禁用");
	public static final OrgStatus APPROVING = new OrgStatus(11, "审核中");
	public static final OrgStatus REJECTED = new OrgStatus(12, "已拒绝");
	public static final OrgStatus DELETED=new OrgStatus(15, "已删除");

	static {
		OrgStatus.mapDictionaryStatus.put(VALID.getCode(), VALID);
		OrgStatus.mapDictionaryStatus.put(DISABLED.getCode(), DISABLED);
		OrgStatus.mapDictionaryStatus.put(APPROVING.getCode(), APPROVING);
		OrgStatus.mapDictionaryStatus.put(REJECTED.getCode(), REJECTED);
		OrgStatus.mapDictionaryStatus.put(DELETED.getCode(), DELETED);
	}

	protected OrgStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static OrgStatus parseCode(int iCode) {
		return OrgStatus.mapDictionaryStatus.get(iCode);
	}

	public static List<OrgStatus> getAllDictionaryStatus() {
		return new ArrayList<OrgStatus>(OrgStatus.mapDictionaryStatus.values());
	}

}
