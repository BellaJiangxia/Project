package com.vastsoft.yingtai.module.user.entity;

import java.util.Set;

import com.vastsoft.yingtai.core.PermissionsSerializer;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.module.user.constants.UserType;

public class TAdminUser extends TBaseUser{
	private String permission;//权限

	public void setPermission(String strPermission) {
		this.permission = strPermission;
	}

	public String getPermission() {
		return permission;
	}

	public Set<UserPermission> getPermissionList() {
		return PermissionsSerializer.fromUserPermissionString(this.permission, UserType.ADMIN);
	}

	public void setPermissionList(Set<UserPermission> listUserPermission) {
		if (listUserPermission==null)return;
		this.permission=PermissionsSerializer.toUserPermissionString(listUserPermission);
	}
	
	public TAdminUser getUserInfo(TBaseUser baseUser) {
		this.setId(baseUser.getId());
		this.setBirthday(baseUser.getBirthday());
		this.setCreate_time(baseUser.getCreate_time());
		this.setEmail(baseUser.getEmail());
		this.setGender(baseUser.getGender());
		this.setIdentity_id(baseUser.getIdentity_id());
		this.setLast_dev_login_time(baseUser.getLast_dev_login_time());
		this.setLast_login_time(baseUser.getLast_login_time());
		this.setMobile(baseUser.getMobile());
		this.setPhoto_file_id(baseUser.getPhoto_file_id());
		this.setPwd(baseUser.getPwd());
		this.setStatus(baseUser.getStatus());
		this.setType(baseUser.getType());
		this.setUser_name(baseUser.getUser_name());
		
		return this;
	}
}
