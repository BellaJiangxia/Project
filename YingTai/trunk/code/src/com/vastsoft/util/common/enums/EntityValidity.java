package com.vastsoft.util.common.enums;

import com.vastsoft.util.base.BaseEnum;

public enum EntityValidity implements BaseEnum {
	/**有效*/
	VALID(1,"有效"),
	/**已删除*/
	DELETED(99,"已删除");
	private int code;
	private String name;
	
	private EntityValidity(int code,String name){
		this.name = name;
		this.code = code;
	}
	
	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public static EntityValidity parseCode(int code){
		return parseCode(code, null);
	}
	
	public static EntityValidity parseCode(int code,EntityValidity default_val){
		if (code == 1)
			return VALID;
		if (code == 99)
			return DELETED;
		return default_val;
	}

}
