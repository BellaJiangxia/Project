package com.vastsoft.yingtai.module.user.entity;

import java.util.Set;

import com.vastsoft.yingtai.module.user.constants.UserStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;

public class FOrgUserMapping extends TOrgUserMapping {
	private String user_name;
	private int user_status;
	private String user_mobile;
	private String user_email;
	private String org_name;
	private String org_permission;
	private int user_type;
	private String user_identity_id;

	public String getUser_name() {
		return user_name;
	}

	public int getUser_status() {
		return user_status;
	}

	public String getUser_statusStr() {
		UserStatus us = UserStatus.parseCode(user_status);
		return us == null ? "" : us.getName();
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public int getUser_type() {
		return user_type;
	}

	public String getUser_typeStr() {
		UserType ut = UserType.parseCode(user_type);
		return ut == null ? "" : ut.getName();
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setUser_status(int user_status) {
		this.user_status = user_status;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public String getOrg_permission() {
		return org_permission;
	}

	public void setOrg_permission(String org_permission) {
		this.org_permission = org_permission;
	}

//	public List<OrgPermission> getOrgPermissionList() {
//		return OrgPermission.parsePermissionStr(this.org_permission);
//	}

	// public String getUser_permission() {
	// return user_permission;
	// }
	// public void setUser_permission(String user_permission) {
	// this.user_permission = user_permission;
	// }
	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_identity_id() {
		return user_identity_id;
	}

	public void setUser_identity_id(String user_identity_id) {
		this.user_identity_id = user_identity_id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public Set<FUserPermission> getUserPerms() {
		return FUserPermission.getAllPermissionByUserTypeAndOrgPerm(UserType.parseCode(user_type),
				this.getPermissionList());
	}
}
