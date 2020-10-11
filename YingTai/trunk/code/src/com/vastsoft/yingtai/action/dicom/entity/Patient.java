package com.vastsoft.yingtai.action.dicom.entity;
/**SELECT *
  FROM [DICOMDB].[dbo].[series1] where study_uid_id in (SELECT study_uid_id
  FROM [DICOMDB].[dbo].[study1] where ptn_id_id in (
  select ptn_id_id from patient1 where ptn_id='28323'));
  
  SELECT *
  FROM [DICOMDB].[dbo].[study1] where ptn_id_id in (
  select ptn_id_id from patient1 where ptn_id='28323')*/
public class Patient {
	private String ptn_id;//病人ID
	private String ptn_name;//病人姓名
	private long ptn_id_id;
	private String sex;//病人性别
	private long birth_date;//病人生日
	public String getPtn_id() {
		return ptn_id;
	}
	public String getPtn_name() {
		return ptn_name;
	}
	public long getPtn_id_id() {
		return ptn_id_id;
	}
	public String getSex() {
		return sex;
	}
	public long getBirth_date() {
		return birth_date;
	}
	public void setPtn_id(String ptn_id) {
		this.ptn_id = ptn_id;
	}
	public void setPtn_name(String ptn_name) {
		this.ptn_name = ptn_name;
	}
	public void setPtn_id_id(long ptn_id_id) {
		this.ptn_id_id = ptn_id_id;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setBirth_date(long birth_date) {
		this.birth_date = birth_date;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (birth_date ^ (birth_date >>> 32));
		result = prime * result + ((ptn_id == null) ? 0 : ptn_id.hashCode());
		result = prime * result + (int) (ptn_id_id ^ (ptn_id_id >>> 32));
		result = prime * result + ((ptn_name == null) ? 0 : ptn_name.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
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
		Patient other = (Patient) obj;
		if (birth_date != other.birth_date)
			return false;
		if (ptn_id == null) {
			if (other.ptn_id != null)
				return false;
		} else if (!ptn_id.equals(other.ptn_id))
			return false;
		if (ptn_id_id != other.ptn_id_id)
			return false;
		if (ptn_name == null) {
			if (other.ptn_name != null)
				return false;
		} else if (!ptn_name.equals(other.ptn_name))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Patient [" + (ptn_id != null ? "ptn_id=" + ptn_id + ", " : "")
				+ (ptn_name != null ? "ptn_name=" + ptn_name + ", " : "") + "ptn_id_id=" + ptn_id_id + ", "
				+ (sex != null ? "sex=" + sex + ", " : "") + "birth_date=" + birth_date + "]";
	}
	
	
}
