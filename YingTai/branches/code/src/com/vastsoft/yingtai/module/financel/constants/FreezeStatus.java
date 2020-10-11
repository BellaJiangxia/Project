package com.vastsoft.yingtai.module.financel.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class FreezeStatus extends SingleClassConstant {

	protected FreezeStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	/** 有效 */
	public static final FreezeStatus VALID = new FreezeStatus(1, "有效");
	/** 无效 */
	public static final FreezeStatus INVALID = new FreezeStatus(2, "无效");

	private static final Map<Integer, FreezeStatus> mapFreezeStatus = new HashMap<Integer, FreezeStatus>();

	static {
		mapFreezeStatus.put(VALID.getCode(), VALID);
		mapFreezeStatus.put(INVALID.getCode(), INVALID);
	}

	public static FreezeStatus parseCode(int iCode) {
		return FreezeStatus.mapFreezeStatus.get(iCode);
	}

	public static List<FreezeStatus> getAll() {
		return new ArrayList<FreezeStatus>(FreezeStatus.mapFreezeStatus.values());
	}
}
