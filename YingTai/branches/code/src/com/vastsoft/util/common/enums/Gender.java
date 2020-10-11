package com.vastsoft.util.common.enums;

import com.vastsoft.util.base.BaseEnum;

public enum Gender implements BaseEnum {
	/** 男 */
	MALE(1, "男"),
	/** 女 */
	FAMALE(2, "女");

	private Gender(int code, String name) {
		this.name = name;
	}

	private int code;
	private String name;

	public String getName() {
		return this.name;
	}

	public int getCode() {
		return this.code;
	}

	public static Gender parseCode(int code) {
		Gender[] gs = Gender.values();
		for (Gender gender : gs) {
			if (gender.getCode() == code)
				return gender;
		}
		return null;
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

	public static Gender[] getAll() {
		return Gender.values();
	}
}
