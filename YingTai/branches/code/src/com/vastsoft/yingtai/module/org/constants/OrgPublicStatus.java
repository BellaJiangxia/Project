package com.vastsoft.yingtai.module.org.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class OrgPublicStatus extends SingleClassConstant {
	public static final OrgPublicStatus NO=new OrgPublicStatus(10, "都不可见");
	public static final OrgPublicStatus ONLY_ORG=new OrgPublicStatus(11, "机构信息可见");
	public static final OrgPublicStatus BOTH=new OrgPublicStatus(12, "机构信息、人员信息可见");
	
	private static Map<Integer, OrgPublicStatus> mapOrgVisible=new HashMap<Integer, OrgPublicStatus>();
	
	static{
		mapOrgVisible.put(ONLY_ORG.getCode(), ONLY_ORG);
		mapOrgVisible.put(BOTH.getCode(), BOTH);
		mapOrgVisible.put(NO.getCode(), NO);
	}
	
	protected OrgPublicStatus(int iCode, String strName) {
		super(iCode, strName);
	}
	
	public static OrgPublicStatus parseCode(int iCode){
		return mapOrgVisible.get(iCode);
	}
	
	public static List<OrgPublicStatus> getAllStatus(){
		return new ArrayList<OrgPublicStatus>(mapOrgVisible.values());
	}
}
