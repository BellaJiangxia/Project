package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

public class ShareRemoteNumEntry {
	private String num;
	private String patient_name;
	public ShareRemoteNumEntry(String num, String patient_name) {
		super();
		this.num = num;
		this.patient_name = patient_name;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		result = prime * result + ((patient_name == null) ? 0 : patient_name.hashCode());
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
		ShareRemoteNumEntry other = (ShareRemoteNumEntry) obj;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		if (patient_name == null) {
			if (other.patient_name != null)
				return false;
		} else if (!patient_name.equals(other.patient_name))
			return false;
		return true;
	}
}
