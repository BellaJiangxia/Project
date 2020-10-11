package com.vastsoft.yingtai.module.financel.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class FinacelRequestType extends SingleClassConstant {
	/** 充值 */
	public static final FinacelRequestType CHARGE = new FinacelRequestType(1, "充值");
	/** 提现 */
	public static final FinacelRequestType TAKECASH = new FinacelRequestType(2, "提现");

	private static final Map<Integer, FinacelRequestType> mapFinacelRequestType = new HashMap<Integer, FinacelRequestType>();

	static {
		mapFinacelRequestType.put(CHARGE.getCode(), CHARGE);
		mapFinacelRequestType.put(TAKECASH.getCode(), TAKECASH);
	}

	protected FinacelRequestType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static FinacelRequestType parseCode(int iCode) {
		return FinacelRequestType.mapFinacelRequestType.get(iCode);
	}

	public static List<FinacelRequestType> getAll() {
		return new ArrayList<FinacelRequestType>(FinacelRequestType.mapFinacelRequestType.values());
	}
}
