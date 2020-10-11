package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class LogStatus extends SingleClassConstant {

	private static final Map<Integer, LogStatus> mapLogStatus = new HashMap<Integer, LogStatus>();
	/** 成功 */
	public static final LogStatus SUCCESS = new LogStatus(1, "成功");
	/** 失败 */
	public static final LogStatus FAIL = new LogStatus(2, "失败");

	static {
		LogStatus.mapLogStatus.put(SUCCESS.getCode(), SUCCESS);
		LogStatus.mapLogStatus.put(FAIL.getCode(), FAIL);
	}

	protected LogStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static LogStatus parseCode(int iCode) {
		return LogStatus.mapLogStatus.get(iCode);
	}

	public static List<LogStatus> getAll() {
		return new ArrayList<LogStatus>(LogStatus.mapLogStatus.values());
	}

}
