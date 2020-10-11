package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class SysDataStatus extends SingleClassConstant {
	/** 有效 */
	public static final SysDataStatus VALID = new SysDataStatus(1, "有效");
	/** 无效 */
	public static final SysDataStatus INVALID = new SysDataStatus(2, "无效");

	private static Map<Integer, SysDataStatus> mapSysDataStatus = new HashMap<Integer, SysDataStatus>();

	static {
		mapSysDataStatus.put(VALID.getCode(), VALID);
		mapSysDataStatus.put(INVALID.getCode(), INVALID);
	}

	protected SysDataStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static SysDataStatus parseCode(int iCode) {
		return SysDataStatus.mapSysDataStatus.get(iCode);
	}

	public static List<SysDataStatus> getAllDictionaryStatus() {
		return new ArrayList<SysDataStatus>(SysDataStatus.mapSysDataStatus.values());
	}
}
