package com.vastsoft.yingtai.module.user.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**
 * @author jyb
 */
public class UserType extends SingleClassConstant {
	/**超级管理员*/
	public static final UserType SUPER_ADMIN = new UserType(9999, "超级管理员");
	/**普通管理员*/
	public static final UserType ADMIN = new UserType(1, "普通管理员");
	/**普通机构用户*/
	public static final UserType ORG_GENERAL = new UserType(2, "普通用户");
	/**医生用户*/
	public static final UserType ORG_DOCTOR = new UserType(3, "医生用户");
//	/**病人用户*/
//	public static final UserType PATIENT = new UserType(4, "病人用户");
	/**注册用户*/
	public static final UserType GUEST = new UserType(99, "注册用户");

	private static final Map<Integer, UserType> mapUserType = new HashMap<Integer, UserType>();

	static {
		UserType.mapUserType.put(SUPER_ADMIN.getCode(), SUPER_ADMIN);
		UserType.mapUserType.put(ADMIN.getCode(), ADMIN);
		UserType.mapUserType.put(ORG_GENERAL.getCode(), ORG_GENERAL);
		UserType.mapUserType.put(ORG_DOCTOR.getCode(), ORG_DOCTOR);
//		UserType.mapUserType.put(PATIENT.getCode(), PATIENT);
		UserType.mapUserType.put(GUEST.getCode(), GUEST);
	}

	public UserType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static UserType parseCode(int iCode) {
		return UserType.mapUserType.get(iCode);
	}

	public static List<UserType> getAll() {
		return new ArrayList<UserType>(UserType.mapUserType.values());
	}
	
	public static List<UserType> getRegType(){
		List<UserType> list=new ArrayList<UserType>();
		list.add(ORG_DOCTOR);
		list.add(ORG_GENERAL);
		return list;
	}
}
