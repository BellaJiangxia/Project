package com.vastsoft.yingtai.module.weixin.entity;

import java.util.Date;
import java.util.List;

public class TPatientExternalRelation
{
	private long id;// 关系唯一ID
	private int seq_id;// 关系序号
	private long patient_id;// 病人ID
	private String patient_name;// 病人名称
	private int patient_gender;// 性别
	private String patient_mobile;// 电话号码
	private int external_sys_type;// 外部系统类型 目前只有微信
	private String external_sys_user_id;// 外部用户ID
	private Date create_time; // 创建时间
	private int status; // 状态 待绑定/已绑定/取消关注
	private int state;// 作废/有效

	private List<TPatientOrgMapping> orgList;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getSeq_id()
	{
		return seq_id;
	}

	public void setSeq_id(int seq_id)
	{
		this.seq_id = seq_id;
	}

	public long getPatient_id()
	{
		return patient_id;
	}

	public void setPatient_id(long patient_id)
	{
		this.patient_id = patient_id;
	}

	public String getPatient_name()
	{
		return patient_name;
	}

	public void setPatient_name(String patient_name)
	{
		this.patient_name = patient_name;
	}

	public int getPatient_gender()
	{
		return patient_gender;
	}

	public void setPatient_gender(int patient_gender)
	{
		this.patient_gender = patient_gender;
	}

	public String getPatient_mobile()
	{
		return patient_mobile;
	}

	public void setPatient_mobile(String patient_mobile)
	{
		this.patient_mobile = patient_mobile;
	}

	public int getExternal_sys_type()
	{
		return external_sys_type;
	}

	public void setExternal_sys_type(int external_sys_type)
	{
		this.external_sys_type = external_sys_type;
	}

	public String getExternal_sys_user_id()
	{
		return external_sys_user_id;
	}

	public void setExternal_sys_user_id(String external_sys_user_id)
	{
		this.external_sys_user_id = external_sys_user_id;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public Date getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(Date create_time)
	{
		this.create_time = create_time;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public List<TPatientOrgMapping> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TPatientOrgMapping> orgList) {
		this.orgList = orgList;
	}
}
