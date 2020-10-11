package com.vastsoft.yingtai.module.financel.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class FinacelRequestStatus extends SingleClassConstant {
	/** 未处理 */
	public static final FinacelRequestStatus UNHANDLE = new FinacelRequestStatus(1, "未处理");
	/** 已处理 */
	public static final FinacelRequestStatus HANDLED = new FinacelRequestStatus(2, "已处理");
	/** 已拒绝 */
	public static final FinacelRequestStatus REJECT = new FinacelRequestStatus(3, "已拒绝");

	private static final Map<Integer, FinacelRequestStatus> mapFinacelRequestStatus = new HashMap<Integer, FinacelRequestStatus>();

	static {
		mapFinacelRequestStatus.put(UNHANDLE.getCode(), UNHANDLE);
		mapFinacelRequestStatus.put(HANDLED.getCode(), HANDLED);
		mapFinacelRequestStatus.put(REJECT.getCode(), REJECT);
	}

	protected FinacelRequestStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static FinacelRequestStatus parseCode(int iCode) {
		return FinacelRequestStatus.mapFinacelRequestStatus.get(iCode);
	}

	public static List<FinacelRequestStatus> getAll() {
		return new ArrayList<FinacelRequestStatus>(FinacelRequestStatus.mapFinacelRequestStatus.values());
	}
}
