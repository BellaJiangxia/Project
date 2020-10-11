package com.vastsoft.yingtai.module.financel.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class AccountRecordType extends SingleClassConstant {
	private AccountRecordRandEType reType=null;
	/** 充值 */
	public static final AccountRecordType CHARGE = new AccountRecordType(1, "充值",AccountRecordRandEType.CASH_IN);
	/** 提现 */
	public static final AccountRecordType CASH_OUT = new AccountRecordType(2, "提现",AccountRecordRandEType.CASH_OUT);
	/** 诊断冻结 */
	public static final AccountRecordType DIAGNOSIS_FREEZE = new AccountRecordType(3, "诊断冻结",AccountRecordRandEType.CASH_OUT);
	/** 诊断划入 */
	public static final AccountRecordType DIAGNOSIS_CASH_ID = new AccountRecordType(4, "诊断划入",AccountRecordRandEType.CASH_IN);
	/** 系统账户收入 */
	public static final AccountRecordType SYSTEM_CASH_ID = new AccountRecordType(5, "系统账户收入",AccountRecordRandEType.CASH_IN);
	/** 创建一个机构账户 */
	public static final AccountRecordType CREATE_ORG_ACCOUNT = new AccountRecordType(6, "创建一个机构账户",AccountRecordRandEType.OTHER);
	/** 冻结一个机构账户 */
	public static final AccountRecordType FREEZE_ORG_ACCOUNT = new AccountRecordType(7, "冻结一个机构账户",AccountRecordRandEType.OTHER);
	/** 解冻一个机构账户 */
	public static final AccountRecordType UNFREEZE_ORG_ACCOUNT = new AccountRecordType(8, "解冻一个机构账户",AccountRecordRandEType.OTHER);
	/** 撤销一个申请冻结 */
	public static final AccountRecordType UNFREEZE_DIAGNOSIS = new AccountRecordType(9, "撤销一个申请冻结",AccountRecordRandEType.OTHER);
	
	private static Map<Integer, AccountRecordType> mapAccountRecordType = new HashMap<Integer, AccountRecordType>();

	static {
		mapAccountRecordType.put(CHARGE.getCode(), CHARGE);
		mapAccountRecordType.put(CASH_OUT.getCode(), CASH_OUT);
		mapAccountRecordType.put(DIAGNOSIS_FREEZE.getCode(), DIAGNOSIS_FREEZE);
		mapAccountRecordType.put(DIAGNOSIS_CASH_ID.getCode(), DIAGNOSIS_CASH_ID);
		mapAccountRecordType.put(SYSTEM_CASH_ID.getCode(), SYSTEM_CASH_ID);
		mapAccountRecordType.put(CREATE_ORG_ACCOUNT.getCode(), CREATE_ORG_ACCOUNT);
		mapAccountRecordType.put(FREEZE_ORG_ACCOUNT.getCode(), FREEZE_ORG_ACCOUNT);
		mapAccountRecordType.put(UNFREEZE_ORG_ACCOUNT.getCode(), UNFREEZE_ORG_ACCOUNT);
		mapAccountRecordType.put(UNFREEZE_DIAGNOSIS.getCode(), UNFREEZE_DIAGNOSIS);
	}

	protected AccountRecordType(int iCode, String strName,AccountRecordRandEType type) {
		super(iCode, strName);
		this.reType=type;
	}

	public static AccountRecordType parseCode(int iCode) {
		return AccountRecordType.mapAccountRecordType.get(iCode);
	}

	public static List<AccountRecordType> getAll() {
		return new ArrayList<AccountRecordType>(AccountRecordType.mapAccountRecordType.values());
	}

	public AccountRecordRandEType getReType() {
		return reType;
	}
}
