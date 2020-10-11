package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.SplitStringBuilder;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class FCaseHistory extends TCaseHistory {
	private String org_name;
	private String org_code;
	private String create_user_name;
	private String modify_user_name;
	private String patient_name;
	private int patient_gender;
	private String patient_born_address;
	private String patient_home_address;
	private String patient_work;
	private String patient_sick_his;
	private String patient_identity_id;
	private Date patient_birthday;

	private List<TFile> eafFiles;

	public void setEafFiles(List<TFile> eafFiles) {
		this.eafFiles = eafFiles;
		if (CollectionTools.isEmpty(eafFiles))
			return;
		SplitStringBuilder<Long> ssb = new SplitStringBuilder<Long>();
		for (TFile tFile : eafFiles) {
			ssb.add(tFile.getId());
		}
		super.setEaf_list(ssb.toString());
	}

	public List<TFile> getEafFiles() {
		return this.eafFiles;
	}

	public FCaseHistory() {
		super();
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getCreate_user_name() {
		return create_user_name;
	}

	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}

	public String getModify_user_name() {
		return modify_user_name;
	}

	public void setModify_user_name(String modify_user_name) {
		this.modify_user_name = modify_user_name;
	}

	public Date getPatient_birthday() {
		return patient_birthday;
	}

	public int getPatientAge() {
		return DateTools.getAgeByBirthday(patient_birthday);
	}

	public void setPatient_birthday(Date patient_birthday) {
		this.patient_birthday = patient_birthday;
	}

	public String getPatient_identity_id() {
		return StringTools.formatIdentityId(patient_identity_id);
	}

	public void setPatient_identity_id(String patient_identity_id) {
		this.patient_identity_id = patient_identity_id;
	}

	public String getPatient_name() {
		return patient_name;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}

	public int getPatient_gender() {
		return patient_gender;
	}

	public String getPatient_genderStr() {
		Gender g = Gender.parseCode(patient_gender);
		return g == null ? "" : g.getName();
	}

	public void setPatient_gender(int patient_gender) {
		this.patient_gender = patient_gender;
	}

	public String getPatient_born_address() {
		return patient_born_address;
	}

	public void setPatient_born_address(String patient_born_address) {
		this.patient_born_address = patient_born_address;
	}

	public String getPatient_home_address() {
		return patient_home_address;
	}

	public void setPatient_home_address(String patient_home_address) {
		this.patient_home_address = patient_home_address;
	}

	public String getPatient_work() {
		return patient_work;
	}

	public void setPatient_work(String patient_work) {
		this.patient_work = patient_work;
	}

	public String getPatient_sick_his() {
		return patient_sick_his;
	}

	public void setPatient_sick_his(String patient_sick_his) {
		this.patient_sick_his = patient_sick_his;
	}

}
