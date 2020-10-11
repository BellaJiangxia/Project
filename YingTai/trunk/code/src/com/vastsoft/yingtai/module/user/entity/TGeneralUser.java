package com.vastsoft.yingtai.module.user.entity;

import java.util.Date;

public class TGeneralUser extends TBaseUser{
	private String section;//所属部门
	private long verify_user_id;//审核用户ID
	private Date verify_time;//审核时间
	
	public long getVerify_user_id() {
		return verify_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public Date getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
	public TGeneralUser getUserInfo(TBaseUser baseUser) {
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
		this.setNote(baseUser.getNote());
		this.setGrade(baseUser.getGrade());
		
		return this;
	}
}
