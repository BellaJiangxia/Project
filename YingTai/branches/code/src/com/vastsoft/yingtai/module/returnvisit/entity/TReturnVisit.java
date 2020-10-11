package com.vastsoft.yingtai.module.returnvisit.entity;

import java.util.Date;
import java.util.List;

public class TReturnVisit
{
	private long visit_id;
	private long batch_id;
	private long patient_id;
	private String patient_name;
	private long org_id;
	private String org_name;
	private long template_id;
	private String visit_name;
	private long case_id;
	private String case_info;
	private long create_user_id;
	private String create_user_name;
	private String doctor_name;
	private Date create_time;
	private Date send_time;
	private Date reply_time;

	private List<TReturnVisitQA> listQA;

	public long getVisit_id()
	{
		return visit_id;
	}

	public void setVisit_id(long visit_id)
	{
		this.visit_id = visit_id;
	}

	public long getBatch_id()
	{
		return batch_id;
	}

	public void setBatch_id(long batch_id)
	{
		this.batch_id = batch_id;
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

	public long getOrg_id()
	{
		return org_id;
	}

	public void setOrg_id(long org_id)
	{
		this.org_id = org_id;
	}

	public long getTemplate_id()
	{
		return template_id;
	}

	public void setTemplate_id(long template_id)
	{
		this.template_id = template_id;
	}

	public String getVisit_name()
	{
		return visit_name;
	}

	public void setVisit_name(String visit_name)
	{
		this.visit_name = visit_name;
	}

	public long getCreate_user_id()
	{
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id)
	{
		this.create_user_id = create_user_id;
	}

	public String getCreate_user_name()
	{
		return create_user_name;
	}

	public void setCreate_user_name(String create_user_name)
	{
		this.create_user_name = create_user_name;
	}

	public Date getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(Date create_time)
	{
		this.create_time = create_time;
	}

	public Date getSend_time()
	{
		return send_time;
	}

	public void setSend_time(Date send_time)
	{
		this.send_time = send_time;
	}

	public Date getReply_time()
	{
		return reply_time;
	}

	public void setReply_time(Date reply_time)
	{
		this.reply_time = reply_time;
	}

	public List<TReturnVisitQA> getListQA()
	{
		return listQA;
	}

	public void setListQA(List<TReturnVisitQA> listQA)
	{
		this.listQA = listQA;
	}

	public String getOrg_name()
	{
		return org_name;
	}

	public void setOrg_name(String org_name)
	{
		this.org_name = org_name;
	}

	public String getCase_info()
	{
		return case_info;
	}

	public void setCase_info(String case_info)
	{
		this.case_info = case_info;
	}

	public long getCase_id()
	{
		return case_id;
	}

	public void setCase_id(long case_id)
	{
		this.case_id = case_id;
	}

	public String getDoctor_name()
	{
		return doctor_name;
	}

	public void setDoctor_name(String doctor_name)
	{
		this.doctor_name = doctor_name;
	}

}
