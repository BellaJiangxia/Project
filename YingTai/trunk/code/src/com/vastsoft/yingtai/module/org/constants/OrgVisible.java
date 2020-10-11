package com.vastsoft.yingtai.module.org.constants;

import java.util.HashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class OrgVisible extends SingleClassConstant {
	public static final OrgVisible SHOW=new OrgVisible(1, "可见");
	public static final OrgVisible HIDE=new OrgVisible(2, "隐藏");
	
	private static Map<Integer, OrgVisible> mapOrgVisible=new HashMap<Integer, OrgVisible>();
	
	static{
		mapOrgVisible.put(SHOW.getCode(), SHOW);
		mapOrgVisible.put(HIDE.getCode(), HIDE);
	}
	protected OrgVisible(int iCode, String strName) {
		super(iCode, strName);
	}
	public static OrgVisible parseCode(int iCode){
		return mapOrgVisible.get(iCode);
	}
}
