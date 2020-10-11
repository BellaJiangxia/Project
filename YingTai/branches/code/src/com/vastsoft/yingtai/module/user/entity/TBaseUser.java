package com.vastsoft.yingtai.module.user.entity;

import java.util.Date;

import com.vastsoft.yingtai.core.EntityBaseInf;
import com.vastsoft.yingtai.module.user.constants.UserType;

public class TBaseUser implements EntityBaseInf{
	private long id;
	private String mobile;//手机
	private String email;//邮箱
	private String pwd;//密码/
	private int type;//类型
	private int status;//状态
	private Date last_login_time;//最后一次登录时间
	private Date last_dev_login_time;//最后一次移动端登录时间
	private String user_name;//用户名称
	private Date create_time;//创建时间
	private String identity_id;//身份证号
	private Date birthday;//出生日期
	private int gender;//性别
	private int photo_file_id;//头像ID
	private String note;
	private String grade;
	
	private String v_grade;
	private String v_job_note;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
	}

	public Date getLast_dev_login_time() {
		return last_dev_login_time;
	}

	public void setLast_dev_login_time(Date last_dev_login_time) {
		this.last_dev_login_time = last_dev_login_time;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getIdentity_id() {
		return identity_id;
	}

	public void setIdentity_id(String identity_id) {
		this.identity_id = identity_id;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getPhoto_file_id() {
		return photo_file_id;
	}

	public void setPhoto_file_id(int photo_file_id) {
		this.photo_file_id = photo_file_id;
	}
	
	public UserType getUserType(){
		return UserType.parseCode(this.type);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getV_grade() {
		return v_grade;
	}

	public void setV_grade(String v_grade) {
		this.v_grade = v_grade;
	}

	public String getV_job_note() {
		return v_job_note;
	}

	public void setV_job_note(String v_job_note) {
		this.v_job_note = v_job_note;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + gender;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((identity_id == null) ? 0 : identity_id.hashCode());
		result = prime * result + ((last_dev_login_time == null) ? 0 : last_dev_login_time.hashCode());
		result = prime * result + ((last_login_time == null) ? 0 : last_login_time.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + photo_file_id;
		result = prime * result + ((pwd == null) ? 0 : pwd.hashCode());
		result = prime * result + status;
		result = prime * result + type;
		result = prime * result + ((user_name == null) ? 0 : user_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TBaseUser other = (TBaseUser) obj;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (gender != other.gender)
			return false;
		if (id != other.id)
			return false;
		if (identity_id == null) {
			if (other.identity_id != null)
				return false;
		} else if (!identity_id.equals(other.identity_id))
			return false;
		if (last_dev_login_time == null) {
			if (other.last_dev_login_time != null)
				return false;
		} else if (!last_dev_login_time.equals(other.last_dev_login_time))
			return false;
		if (last_login_time == null) {
			if (other.last_login_time != null)
				return false;
		} else if (!last_login_time.equals(other.last_login_time))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (photo_file_id != other.photo_file_id)
			return false;
		if (pwd == null) {
			if (other.pwd != null)
				return false;
		} else if (!pwd.equals(other.pwd))
			return false;
		if (status != other.status)
			return false;
		if (type != other.type)
			return false;
		if (user_name == null) {
			if (other.user_name != null)
				return false;
		} else if (!user_name.equals(other.user_name))
			return false;
		return true;
	}

	@Override
	public void copyTo(EntityBaseInf user) {
		if (user==null)return;
		if (!(user instanceof TBaseUser))return;
		TBaseUser bu=(TBaseUser)user;
		bu.setBirthday(this.birthday);
		bu.setCreate_time(this.create_time);
		bu.setEmail(this.email);
		bu.setGender(this.gender);
		bu.setId(this.id);
		bu.setIdentity_id(this.identity_id);
		bu.setLast_dev_login_time(this.last_dev_login_time);
		bu.setLast_login_time(this.last_login_time);
		bu.setMobile(this.mobile);
		bu.setNote(this.note);
		bu.setPhoto_file_id(this.photo_file_id);
		bu.setPwd(this.pwd);
		bu.setStatus(this.status);
		bu.setType(this.type);
		bu.setUser_name(this.user_name);
	}
	
}
