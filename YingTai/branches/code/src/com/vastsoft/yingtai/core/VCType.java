package com.vastsoft.yingtai.core;

import java.util.HashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class VCType extends SingleClassConstant {
	
	protected VCType(int iCode, String strName) {
		super(iCode, strName);
	}
	private static Map<Integer, VCType> mapVCType=new HashMap<Integer, VCType>();

	public final static VCType USER_REG = new VCType(1, "用户注册");
	public final static VCType USER_MODIFY = new VCType(2, "修改手机邮箱");
	public final static VCType USER_FIND = new VCType(3, "找回密码");
	
	static {
		mapVCType.put(USER_REG.getCode(), USER_REG);
		mapVCType.put(USER_MODIFY.getCode(), USER_MODIFY);
		mapVCType.put(USER_FIND.getCode(), USER_FIND);
	}

	public static VCType parseCode(int iCode) {
		return mapVCType.get(iCode);
	}

	
}
