package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.assist;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class SearchPatientParam extends AbstractSearchParam {
	private Long patientId;
	private Long org_id; 
	private Long[] containIds;
	private Long[] excludeIds;
	private String patient_name;
	private String identity_id;
	private String home_address;
	private Gender gender;
	private String born_address;
	private String work;
	private String sick_his;
	private Date create_start;
	private Date create_end;
	@Override
	public Map<String, Object> buildMap() {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("id", patientId);
		result.put("org_id", org_id);
		result.put("containIds", StringTools.arrayToString(containIds, ','));
		result.put("excludeIds", StringTools.arrayToString(excludeIds,','));
		result.put("name", patient_name);
		result.put("identity_id", identity_id);
		result.put("home_address", home_address);
		result.put("gender", gender==null?null:gender.getCode());
		result.put("born_address", born_address);
		result.put("work", work);
		result.put("sick_his", sick_his);
		result.put("create_start", create_start);
		result.put("create_end", create_end);
		return result;
	}
	public Long getPatientId() {
		return patientId;
	}
	public Long getOrg_id() {
		return org_id;
	}
	public Long[] getContainIds() {
		return containIds;
	}
	public Long[] getExcludeIds() {
		return excludeIds;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public String getHome_address() {
		return home_address;
	}
	public Gender getGender() {
		return gender;
	}
	public String getBorn_address() {
		return born_address;
	}
	public String getWork() {
		return work;
	}
	public String getSick_his() {
		return sick_his;
	}
	public Date getCreate_start() {
		return create_start;
	}
	public Date getCreate_end() {
		return create_end;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}
	public void setContainIds(Long[] containIds) {
		this.containIds = containIds;
	}
	public void setExcludeIds(Long[] excludeIds) {
		this.excludeIds = excludeIds;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public void setHome_address(String home_address) {
		this.home_address = home_address;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public void setBorn_address(String born_address) {
		this.born_address = born_address;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public void setSick_his(String sick_his) {
		this.sick_his = sick_his;
	}
	public void setCreate_start(Date create_start) {
		this.create_start = create_start;
	}
	public void setCreate_end(Date create_end) {
		this.create_end = create_end;
	}
	public String getIdentity_id() {
		return identity_id;
	}
	public void setIdentity_id(String identity_id) {
		this.identity_id = identity_id;
	}

}
