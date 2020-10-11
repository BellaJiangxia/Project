package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class DictionaryFlag extends SingleClassConstant {
	
	private static final Map<Integer, DictionaryFlag> mapDictionaryFlag = new HashMap<Integer, DictionaryFlag>();
	/** 不可修改，不可删除 */
	static final DictionaryFlag NULL_U_D = new DictionaryFlag(1, "不可修改，不可删除");
	/** 可修改，不能删除 */
	static final DictionaryFlag CAN_U_NOT_D = new DictionaryFlag(2, "可修改，不能删除");
	/** 可修改，可删除 */
	static final DictionaryFlag CAN_U_D = new DictionaryFlag(3, "可修改，可删除");

	static {
		mapDictionaryFlag.put(NULL_U_D.getCode(), NULL_U_D);
		mapDictionaryFlag.put(CAN_U_NOT_D.getCode(), CAN_U_NOT_D);
		mapDictionaryFlag.put(CAN_U_D.getCode(), CAN_U_D);
	}

	private DictionaryFlag(int iCode, String strName) {
		super(iCode, strName);
	}

	public static DictionaryFlag parseCode(int iCode) {
		return mapDictionaryFlag.get(iCode);
	}

	public static List<DictionaryFlag> getAll() {
		return new ArrayList<DictionaryFlag>(mapDictionaryFlag.values());
	}
}
