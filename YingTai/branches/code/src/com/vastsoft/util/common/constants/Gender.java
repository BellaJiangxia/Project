package com.vastsoft.util.common.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class Gender extends SingleClassConstant {
	/** 男 */
	public static final Gender MALE = new Gender(1, "男");
	/** 女 */
	public static final Gender FAMALE = new Gender(2, "女");

	private static final Map<Integer, Gender> mapUserGender = new LinkedHashMap<Integer, Gender>();
	static {
		mapUserGender.put(MALE.getCode(), MALE);
		mapUserGender.put(FAMALE.getCode(), FAMALE);
	}

	protected Gender(int iCode, String strName) {
		super(iCode, strName);
	}

	public static Gender parseCode(int iCode) {
		return Gender.mapUserGender.get(iCode);
	}

	public static List<Gender> getAll() {
		return new ArrayList<Gender>(Gender.mapUserGender.values());
	}

	public static Gender parseString(String string, Gender default_gender) {
		if (string == null)
			return default_gender;
		string = string.trim();
		if (string.equals("1"))
			return MALE;
		if (string.equals("2"))
			return FAMALE;
		if (string.equalsIgnoreCase("F"))
			return FAMALE;
		if (string.equalsIgnoreCase("M"))
			return MALE;
		if (string.equalsIgnoreCase("FAMALE"))
			return FAMALE;
		if (string.equalsIgnoreCase("MALE"))
			return MALE;
		if (string.equals("男"))
			return MALE;
		if (string.equals("女"))
			return FAMALE;
		return default_gender;
	}
}
