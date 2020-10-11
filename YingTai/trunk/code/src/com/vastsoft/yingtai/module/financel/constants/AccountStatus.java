package com.vastsoft.yingtai.module.financel.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class AccountStatus extends SingleClassConstant {
	/** 正常 */
	public static final AccountStatus NORMAL = new AccountStatus(1, "正常");
	/** 冻结 */
	public static final AccountStatus FREEZE = new AccountStatus(2, "冻结");

	private static Map<Integer, AccountStatus> mapAccountStatus = new HashMap<Integer, AccountStatus>();
	
	static {
		mapAccountStatus.put(NORMAL.getCode(), NORMAL);
		mapAccountStatus.put(FREEZE.getCode(), FREEZE);
	}

	protected AccountStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static AccountStatus parseCode(int iCode) {
		return AccountStatus.mapAccountStatus.get(iCode);
	}

	public static List<AccountStatus> getAll() {
		return new ArrayList<AccountStatus>(AccountStatus.mapAccountStatus.values());
	}
}
