package com.vastsoft.yingtai.core;

import java.util.LinkedHashSet;
import java.util.Set;

import com.vastsoft.util.common.SplitStringBuilder;
import com.vastsoft.yingtai.module.user.constants.UserType;

public class PermissionsSerializer {
	/**
	 * 将用户权限列表转换成字符串
	 * 
	 * @param listUserPermission
	 *            要转换的用户权限列表
	 * @return 权限列表的字符串表示形式
	 */
	public static String toUserPermissionString(Set<UserPermission> listUserPermission) {
		SplitStringBuilder<Integer> strPerms = new SplitStringBuilder<Integer>();
		for (UserPermission userPermission : listUserPermission) {
			strPerms.add(userPermission.getCode());
		}
		return strPerms.toString();
	}

	/**
	 * 解析权限的字符串标识形式得到用户权限列表
	 * 
	 * @param strData
	 *            要转换的字符串
	 * @param userType
	 *            用户类型，管理员或者机构用户
	 * @return 返回权限列表
	 */
	public static Set<UserPermission> fromUserPermissionString(String strPermission) {
		if (strPermission == null || strPermission.isEmpty())
			return new LinkedHashSet<UserPermission>();

		String[] strPerms = strPermission.split(",");
		Set<UserPermission> listUserPermission = new LinkedHashSet<UserPermission>();
		if (strPerms == null || strPerms.length == 0)
			return listUserPermission;
		for (String strCode : strPerms) {
			UserPermission userPerm = UserPermission.parseCode(Integer.parseInt(strCode));
			if (userPerm != null)
				listUserPermission.add(userPerm);
		}
		return listUserPermission;
	}

	/**
	 * 解析权限的字符串标识形式得到用户权限列表
	 * 
	 * @param strData
	 *            要转换的字符串
	 * @param userType
	 *            用户类型，管理员或者机构用户
	 * @return 返回权限列表
	 */
	public static Set<UserPermission> fromUserPermissionString(String strData, UserType userType) {
		String[] strPerms = strData.split(",");
		Set<UserPermission> listUserPermission = new LinkedHashSet<UserPermission>();
		if (strPerms == null || strPerms.length == 0)
			return listUserPermission;
		for (String strCode : strPerms) {
			if (strCode == null || strCode.trim().isEmpty())
				continue;
			try {
				UserPermission userPerm;
				if (userType.equals(UserType.ADMIN)) {
					userPerm = UserPermission.parseAdminCode(Integer.parseInt(strCode));
				} else {
					userPerm = UserPermission.parseUserCode(Integer.parseInt(strCode));
				}
				if (userPerm != null)
					listUserPermission.add(userPerm);
			} catch (Exception e) {
			}
		}
		return listUserPermission;
	}

	// /**
	// * 将机构权限列表转换成字符串表示形式
	// *
	// * @param listOrgPerimission
	// * 要转换的机构权限列表
	// * @return 机构权限列表字符串表示形式
	// */
	// public static String toOrgPermissionString(List<OrgPermission>
	// listOrgPerimission) {
	// StringBuilder strPerms = new StringBuilder();
	// for (OrgPermission orgPermission : listOrgPerimission) {
	// strPerms.append(orgPermission.getCode()).append(',');
	// }
	// return strPerms.toString();
	// }

	// /**
	// * 将机构权限列表的字符串表示形式转换为全线列表
	// *
	// * @param strData
	// * 要转换的字符串
	// * @return 机构全线列表
	// */
	// public static List<OrgPermission> fromOrgPermissionString(String strData)
	// {
	// List<OrgPermission> listOrgPermission = new ArrayList<OrgPermission>();
	// if(strData==null || strData.isEmpty())return listOrgPermission;
	// String[] strPerms = strData.split(",");
	// if (strPerms == null || strPerms.length == 0)
	// return listOrgPermission;
	// for (String strCode : strPerms) {
	// OrgPermission userPerm =
	// OrgPermission.parseCode(Integer.parseInt(strCode));
	// if (userPerm != null)
	// listOrgPermission.add(userPerm);
	// }
	// return listOrgPermission;
	// }
}
