package com.vastsoft.yingtai.module.user.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class UserChangeStatus extends SingleClassConstant {
	private static final Map<Integer, UserChangeStatus> mapUserChangeStatus = new HashMap<Integer, UserChangeStatus>();

	public static final UserChangeStatus VALID = new UserChangeStatus(1, "已通过");
	public static final UserChangeStatus INVALID = new UserChangeStatus(0, "无效");
	public static final UserChangeStatus APPROVING = new UserChangeStatus(11, "审核中");
	public static final UserChangeStatus REJECTED = new UserChangeStatus(12, "已拒绝");

	static {
		UserChangeStatus.mapUserChangeStatus.put(VALID.getCode(), VALID);
		UserChangeStatus.mapUserChangeStatus.put(INVALID.getCode(), INVALID);
		UserChangeStatus.mapUserChangeStatus.put(APPROVING.getCode(), APPROVING);
		UserChangeStatus.mapUserChangeStatus.put(REJECTED.getCode(), REJECTED);
	}

	protected UserChangeStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static UserChangeStatus parseCode(int iCode) {
		return UserChangeStatus.mapUserChangeStatus.get(iCode);
	}

	public static List<UserChangeStatus> getAllUserChangeStatus() {
		return new ArrayList<UserChangeStatus>(UserChangeStatus.mapUserChangeStatus.values());
	}
}
