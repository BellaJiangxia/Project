package com.vastsoft.yingtai.module.user.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.constants.UserChangeStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;

public class TUserChange {
	private long id;
	private int user_type;
	private int user_gender;
	private String user_name;
	private String identity_id;
	private Date birthday;
	private int photo_file_id;
	private Date startwork_time;
	private int work_years;
	private String grade;
	private String hospital;
	private String section;
	private String diploma_id;
	private String scan_file_ids;
	private long verify_user_id;
	private Date verify_time;
	private String note;
	private Date change_time;
	private int change_status;
	private long user_id;
	private long sign_file_id;
	private String unit_phone;
	private String job_note;
	private long identify_file_id;
	private long qualification_id;
	private long device_opreator_id;
	
	private String mobile;
	private String c_user_name;
	private int c_type;
	private String verify_user_name;
	private int c_gender;
	private String c_identity_id;
	private Date c_birthday;
	private Date c_startwork_time;
	private int c_work_years;
	private String c_grade;
	private String c_hospital;
	private String c_section;
	private String c_diploma_id;
	private String c_scan_file_ids;
	private long c_sign_file_id;
	private String c_unit_phone;
	private String c_job_note;
	private long c_identify_file_id;
	private long c_qualification_id;
	private long c_device_opreator_id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public int getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(int user_gender) {
		this.user_gender = user_gender;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

	public int getPhoto_file_id() {
		return photo_file_id;
	}

	public void setPhoto_file_id(int photo_file_id) {
		this.photo_file_id = photo_file_id;
	}

	public Date getStartwork_time() {
		return startwork_time;
	}

	public void setStartwork_time(Date startwork_time) {
		this.startwork_time = startwork_time;
	}

	public int getWork_years() {
		return work_years;
	}

	public void setWork_years(int work_years) {
		this.work_years = work_years;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getDiploma_id() {
		return diploma_id;
	}

	public void setDiploma_id(String diploma_id) {
		this.diploma_id = diploma_id;
	}

	public String getScan_file_ids() {
		return scan_file_ids;
	}

	public void setScan_file_ids(String scan_file_ids) {
		this.scan_file_ids = scan_file_ids;
	}

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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getChange_time() {
		return change_time;
	}

	public void setChange_time(Date change_time) {
		this.change_time = change_time;
	}

	public int getChange_status() {
		return change_status;
	}

	public void setChange_status(int change_status) {
		this.change_status = change_status;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	public long getSign_file_id() {
		return sign_file_id;
	}

	public void setSign_file_id(long sign_file_id) {
		this.sign_file_id = sign_file_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getC_user_name() {
		return c_user_name;
	}

	public void setC_user_name(String c_user_name) {
		this.c_user_name = c_user_name;
	}

	public int getC_type() {
		return c_type;
	}

	public void setC_type(int c_type) {
		this.c_type = c_type;
	}

	public String getVerify_user_name() {
		return verify_user_name;
	}

	public void setVerify_user_name(String verify_user_name) {
		this.verify_user_name = verify_user_name;
	}

	public int getC_gender() {
		return c_gender;
	}

	public void setC_gender(int c_gender) {
		this.c_gender = c_gender;
	}

	public String getC_identity_id() {
		return c_identity_id;
	}

	public void setC_identity_id(String c_identity_id) {
		this.c_identity_id = c_identity_id;
	}

	public Date getC_birthday() {
		return c_birthday;
	}

	public void setC_birthday(Date c_birthday) {
		this.c_birthday = c_birthday;
	}

	public Date getC_startwork_time() {
		return c_startwork_time;
	}

	public void setC_startwork_time(Date c_startwork_time) {
		this.c_startwork_time = c_startwork_time;
	}

	public int getC_work_years() {
		return c_work_years;
	}

	public void setC_work_years(int c_work_years) {
		this.c_work_years = c_work_years;
	}

	public String getC_grade() {
		return c_grade;
	}

	public void setC_grade(String c_grade) {
		this.c_grade = c_grade;
	}

	public String getC_hospital() {
		return c_hospital;
	}

	public void setC_hospital(String c_hospital) {
		this.c_hospital = c_hospital;
	}

	public String getC_section() {
		return c_section;
	}

	public void setC_section(String c_section) {
		this.c_section = c_section;
	}

	public String getC_diploma_id() {
		return c_diploma_id;
	}

	public void setC_diploma_id(String c_diploma_id) {
		this.c_diploma_id = c_diploma_id;
	}

	public String getC_scan_file_ids() {
		return c_scan_file_ids;
	}

	public void setC_scan_file_ids(String c_scan_file_ids) {
		this.c_scan_file_ids = c_scan_file_ids;
	}

	public long getC_sign_file_id() {
		return c_sign_file_id;
	}

	public void setC_sign_file_id(long c_sign_file_id) {
		this.c_sign_file_id = c_sign_file_id;
	}

	public UserChangeStatus getUserChangeStatus(){
		return UserChangeStatus.parseCode(this.change_status);
	}
	
	public Gender getCUserGender(){
		return Gender.parseCode(this.user_gender);
	}
	
	public UserType getCUserType(){
		return UserType.parseCode(this.c_type);
	}
	
	public Gender getGender(){
		return Gender.parseCode(this.c_gender);
	}
	
	public UserType getType(){
		return UserType.parseCode(this.user_type);
	}

	public String getUnit_phone() {
		return unit_phone;
	}

	public void setUnit_phone(String unit_phone) {
		this.unit_phone = unit_phone;
	}

	public String getJob_note() {
		return job_note;
	}

	public void setJob_note(String job_note) {
		this.job_note = job_note;
	}

	public String getC_unit_phone() {
		return c_unit_phone;
	}

	public void setC_unit_phone(String c_unit_phone) {
		this.c_unit_phone = c_unit_phone;
	}

	public String getC_job_note() {
		return c_job_note;
	}

	public void setC_job_note(String c_job_note) {
		this.c_job_note = c_job_note;
	}

	public long getIdentify_file_id() {
		return identify_file_id;
	}

	public void setIdentify_file_id(long identify_file_id) {
		this.identify_file_id = identify_file_id;
	}

	public long getC_identify_file_id() {
		return c_identify_file_id;
	}

	public void setC_identify_file_id(long c_identify_file_id) {
		this.c_identify_file_id = c_identify_file_id;
	}

	public long getQualification_id() {
		return qualification_id;
	}

	public void setQualification_id(long qualification_id) {
		this.qualification_id = qualification_id;
	}

	public long getC_qualification_id() {
		return c_qualification_id;
	}

	public void setC_qualification_id(long c_qualification_id) {
		this.c_qualification_id = c_qualification_id;
	}

	public long getDevice_opreator_id() {
		return device_opreator_id;
	}

	public void setDevice_opreator_id(long device_opreator_id) {
		this.device_opreator_id = device_opreator_id;
	}

	public long getC_device_opreator_id() {
		return c_device_opreator_id;
	}

	public void setC_device_opreator_id(long c_device_opreator_id) {
		this.c_device_opreator_id = c_device_opreator_id;
	}

}
