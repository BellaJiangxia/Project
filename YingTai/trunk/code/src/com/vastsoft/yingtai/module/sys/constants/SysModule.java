package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class SysModule extends SingleClassConstant {

	private static final Map<Integer, SysModule> mapSysModule = new HashMap<Integer, SysModule>();

	public static final SysModule SYS = new SysModule(1, "系统管理模块");
	public static final SysModule USER = new SysModule(2, "用户管理模块");
	public static final SysModule ORG = new SysModule(3, "机构管理模块");
	public static final SysModule DIAGNOSIS = new SysModule(4, "诊断管理模块");
	public static final SysModule MEDICAL = new SysModule(5, "病历管理模块");
	public static final SysModule FINANCEL = new SysModule(6, "资金结算模块");
	public static final SysModule MSG = new SysModule(7, "消息模块");
	public static final SysModule STAT = new SysModule(8, "统计模块");

	static {
		mapSysModule.put(SYS.getCode(), SYS);
		mapSysModule.put(USER.getCode(), USER);
		mapSysModule.put(ORG.getCode(), ORG);
		mapSysModule.put(DIAGNOSIS.getCode(), DIAGNOSIS);
		mapSysModule.put(MEDICAL.getCode(), MEDICAL);
		mapSysModule.put(FINANCEL.getCode(), FINANCEL);
		mapSysModule.put(MSG.getCode(), MSG);
		mapSysModule.put(STAT.getCode(), STAT);
	}

	protected SysModule(int iCode, String strName) {
		super(iCode, strName);
	}

	public static SysModule parseCode(int iCode) {
		return SysModule.mapSysModule.get(iCode);
	}

	public static List<SysModule> getAllDictionaryStatus() {
		return new ArrayList<SysModule>(SysModule.mapSysModule.values());
	}

}
