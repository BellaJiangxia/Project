package com.vastsoft.yingtai.module.financel.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;
/**账户记录收支类型*/
public class AccountRecordRandEType extends SingleClassConstant {
	/** 收入*/
	public static final AccountRecordRandEType CASH_IN = new AccountRecordRandEType(1, "收入");
	/** 指出 */
	public static final AccountRecordRandEType CASH_OUT = new AccountRecordRandEType(2, "支出");
	/** 其他 */
	public static final AccountRecordRandEType OTHER = new AccountRecordRandEType(3, "其他");
	
	private static Map<Integer, AccountRecordRandEType> mapAccountRecordRandEType = new HashMap<Integer, AccountRecordRandEType>();

	static {
		mapAccountRecordRandEType.put(CASH_IN.getCode(), CASH_IN);
		mapAccountRecordRandEType.put(CASH_OUT.getCode(), CASH_OUT);
		mapAccountRecordRandEType.put(OTHER.getCode(), OTHER);
	}

	protected AccountRecordRandEType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static AccountRecordRandEType parseCode(int iCode) {
		return AccountRecordRandEType.mapAccountRecordRandEType.get(iCode);
	}

	public static List<AccountRecordRandEType> getAll() {
		return new ArrayList<AccountRecordRandEType>(AccountRecordRandEType.mapAccountRecordRandEType.values());
	}

	public static String typesToString(List<AccountRecordRandEType> listReType) {
		if (listReType==null||listReType.size()<=0)return null;
		StringBuilder sbb=new StringBuilder();
		sbb.append(listReType.get(0).getCode());
		if (listReType.size()>1) {
			for (int i = 1; i < listReType.size(); i++) {
				AccountRecordRandEType tt=listReType.get(i);
				sbb.append(",").append(tt.getCode());
			}
		}
		return sbb.toString();
	}
}
