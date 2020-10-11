package com.vastsoft.yingtai.module.reservation.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class CheckServiceStatus extends SingleClassConstant {

	protected CheckServiceStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, CheckServiceStatus> mapCheckServiceStatus = new HashMap<Integer, CheckServiceStatus>();

	public static final CheckServiceStatus REGISTER = new CheckServiceStatus(11, "未发布");
	public static final CheckServiceStatus PUBLISH = new CheckServiceStatus(12, "已发布");
	public static final CheckServiceStatus INVALIDATE = new CheckServiceStatus(10, "无效");

	static {
		CheckServiceStatus.mapCheckServiceStatus.put(PUBLISH.getCode(), PUBLISH);
		CheckServiceStatus.mapCheckServiceStatus.put(REGISTER.getCode(), REGISTER);
		CheckServiceStatus.mapCheckServiceStatus.put(INVALIDATE.getCode(), INVALIDATE);
	}

	public static CheckServiceStatus parseCode(int iCode) {
		return CheckServiceStatus.mapCheckServiceStatus.get(iCode);
	}

	public static List<CheckServiceStatus> getAll() {
		return new ArrayList<CheckServiceStatus>(CheckServiceStatus.mapCheckServiceStatus.values());
	}
}
