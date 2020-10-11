package com.vastsoft.yingtai.module.user.entity;

import java.util.Date;

import com.vastsoft.yingtai.core.EntityBaseInf;

public class TDoctorUser extends TBaseUser implements EntityBaseInf{
	private Date startwork_time;//开始参见工作时间
	private int work_years;//工作年限
	private String hospital;//所属医院
	private String section;//所属科室
//	private String diploma_id;//执业医师资格证号
	private String verity_user_id;//审核用户ID
	private Date verity_time;//审核时间
	private String scan_file_ids;//职称证
	private long sign_file_id;//电子签名
	private long identify_file_id;//身份证
	
	private String unit_phone;
	private String job_note;
	private long qualification_id;//执业医师证
	private long device_opreator_id;//设备上岗证

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
//
//	public String getDiploma_id() {
//		return diploma_id;
//	}
//
//	public void setDiploma_id(String diploma_id) {
//		this.diploma_id = diploma_id;
//	}

	public String getVerity_user_id() {
		return verity_user_id;
	}

	public void setVerity_user_id(String verity_user_id) {
		this.verity_user_id = verity_user_id;
	}

	public Date getVerity_time() {
		return verity_time;
	}

	public void setVerity_time(Date verity_time) {
		this.verity_time = verity_time;
	}

	public String getScan_file_ids() {
		return scan_file_ids;
	}

	public void setScan_file_ids(String scan_file_ids) {
		this.scan_file_ids = scan_file_ids;
	}
	
	public long getSign_file_id() {
		return sign_file_id;
	}

	public void setSign_file_id(long sign_file_id) {
		this.sign_file_id = sign_file_id;
	}

	public TDoctorUser getUserInfo(TBaseUser baseUser) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((hospital == null) ? 0 : hospital.hashCode());
		result = prime * result + (int) (identify_file_id ^ (identify_file_id >>> 32));
		result = prime * result + ((job_note == null) ? 0 : job_note.hashCode());
		result = prime * result + ((scan_file_ids == null) ? 0 : scan_file_ids.hashCode());
		result = prime * result + ((section == null) ? 0 : section.hashCode());
		result = prime * result + (int) (sign_file_id ^ (sign_file_id >>> 32));
		result = prime * result + ((startwork_time == null) ? 0 : startwork_time.hashCode());
		result = prime * result + ((unit_phone == null) ? 0 : unit_phone.hashCode());
		result = prime * result + ((verity_time == null) ? 0 : verity_time.hashCode());
		result = prime * result + ((verity_user_id == null) ? 0 : verity_user_id.hashCode());
		result = prime * result + work_years;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDoctorUser other = (TDoctorUser) obj;
		if (hospital == null) {
			if (other.hospital != null)
				return false;
		} else if (!hospital.equals(other.hospital))
			return false;
		if (identify_file_id != other.identify_file_id)
			return false;
		if (job_note == null) {
			if (other.job_note != null)
				return false;
		} else if (!job_note.equals(other.job_note))
			return false;
		if (scan_file_ids == null) {
			if (other.scan_file_ids != null)
				return false;
		} else if (!scan_file_ids.equals(other.scan_file_ids))
			return false;
		if (section == null) {
			if (other.section != null)
				return false;
		} else if (!section.equals(other.section))
			return false;
		if (sign_file_id != other.sign_file_id)
			return false;
		if (startwork_time == null) {
			if (other.startwork_time != null)
				return false;
		} else if (!startwork_time.equals(other.startwork_time))
			return false;
		if (unit_phone == null) {
			if (other.unit_phone != null)
				return false;
		} else if (!unit_phone.equals(other.unit_phone))
			return false;
		if (verity_time == null) {
			if (other.verity_time != null)
				return false;
		} else if (!verity_time.equals(other.verity_time))
			return false;
		if (verity_user_id == null) {
			if (other.verity_user_id != null)
				return false;
		} else if (!verity_user_id.equals(other.verity_user_id))
			return false;
		if (work_years != other.work_years)
			return false;
		return true;
	}

	public void copyTo(EntityBaseInf user){
		if (user==null)return;
		super.copyTo(user);
		if (!(user instanceof TDoctorUser))return;
		TDoctorUser bu=(TDoctorUser)user;
		bu.setStartwork_time(this.startwork_time);
		bu.setWork_years(this.work_years);
		bu.setHospital(this.hospital);
		bu.setSection(this.section);
		bu.setVerity_user_id(this.verity_user_id);
		bu.setScan_file_ids(this.scan_file_ids);
		bu.setSign_file_id(this.sign_file_id);
		bu.setUnit_phone(this.unit_phone);
		bu.setJob_note(this.job_note);
		bu.setIdentify_file_id(this.identify_file_id);
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

	public long getIdentify_file_id() {
		return identify_file_id;
	}

	public void setIdentify_file_id(long identify_file_id) {
		this.identify_file_id = identify_file_id;
	}

	public long getQualification_id() {
		return qualification_id;
	}

	public void setQualification_id(long qualification_id) {
		this.qualification_id = qualification_id;
	}

	public long getDevice_opreator_id() {
		return device_opreator_id;
	}

	public void setDevice_opreator_id(long device_opreator_id) {
		this.device_opreator_id = device_opreator_id;
	}
}
