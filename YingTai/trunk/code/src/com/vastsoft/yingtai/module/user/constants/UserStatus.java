package com.vastsoft.yingtai.module.user.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**
 * @author jyb
 */
public final class UserStatus extends SingleClassConstant {
	/**审核中*/
	public static final UserStatus VERITIING = new UserStatus(1, "审核中");
	/**正常*/
	public static final UserStatus NORMAL = new UserStatus(2, "有效");
	/**已禁用*/
	public static final UserStatus DISABLE=new UserStatus(25, "已禁用");
	/**审核未通过*/
	public static final UserStatus VERITY_NOT_PASS = new UserStatus(3, "审核未通过");
	/**已删除*/
	public static final UserStatus DELETED = new UserStatus(4, "已删除");

	private static Map<Integer, UserStatus> mapUserStatus = new HashMap<Integer, UserStatus>();

	static {
		UserStatus.mapUserStatus.put(VERITIING.getCode(), VERITIING);
		UserStatus.mapUserStatus.put(NORMAL.getCode(), NORMAL);
		UserStatus.mapUserStatus.put(VERITY_NOT_PASS.getCode(), VERITY_NOT_PASS);
		UserStatus.mapUserStatus.put(DELETED.getCode(), DELETED);
		UserStatus.mapUserStatus.put(DISABLE.getCode(), DISABLE);
	}

	private UserStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static UserStatus parseCode(int iCode) {
		return UserStatus.mapUserStatus.get(iCode);
	}

	public static List<UserStatus> getAll() {
		return new ArrayList<UserStatus>(UserStatus.mapUserStatus.values());
	}

}
