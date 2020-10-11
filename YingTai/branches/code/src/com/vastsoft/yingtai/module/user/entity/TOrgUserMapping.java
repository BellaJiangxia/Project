package com.vastsoft.yingtai.module.user.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vastsoft.yingtai.core.PermissionsSerializer;
import com.vastsoft.yingtai.core.UserPermission;

public class TOrgUserMapping {
	private long id;
	private long org_id;
	private long user_id;
	private String permission;
	private int status;
	private Date create_time;
	private String note;
	
	//供显示
	private String v_user_name;
	private String v_mobile;
	
	public TOrgUserMapping() {
		super();
	}

	public TOrgUserMapping(long lUserId, long lOrgId) {
		super();
		this.user_id=lUserId;
		this.org_id=lOrgId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String strPermission) {
		this.permission = strPermission;
	}
	public List<UserPermission> getUserPermissionList(){
		return UserPermission.parsePermissionStr(this.permission);
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getV_user_name() {
		return v_user_name;
	}

	public void setV_user_name(String v_user_name) {
		this.v_user_name = v_user_name;
	}

	public String getV_mobile() {
		return v_mobile;
	}

	public void setV_mobile(String v_mobile) {
		this.v_mobile = v_mobile;
	}
	
	public Set<UserPermission> getPermissionList() {
		return PermissionsSerializer.fromUserPermissionString(this.permission);
	}
}
