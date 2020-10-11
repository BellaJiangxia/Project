package com.vastsoft.yingtai.module.returnvisit.entity;

import java.util.Date;
import java.util.List;

public class TReturnVisitTemplate
{
	private long template_id;
	private long org_id;
	private String template_name;
	private Date create_time;
	private long create_user_id;
	private String create_user_name;
	private int state;

	private List<TReturnVisitTemplateQuestion> listQ;

	public List<TReturnVisitTemplateQuestion> getListQ()
	{
		return listQ;
	}

	public void setListQ(List<TReturnVisitTemplateQuestion> listQ)
	{
		this.listQ = listQ;
	}

	public long getTemplate_id()
	{
		return template_id;
	}

	public void setTemplate_id(long template_id)
	{
		this.template_id = template_id;
	}

	public long getOrg_id()
	{
		return org_id;
	}

	public void setOrg_id(long org_id)
	{
		this.org_id = org_id;
	}

	public String getTemplate_name()
	{
		return template_name;
	}

	public void setTemplate_name(String template_name)
	{
		this.template_name = template_name;
	}

	public Date getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(Date create_time)
	{
		this.create_time = create_time;
	}

	public long getCreate_user_id()
	{
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id)
	{
		this.create_user_id = create_user_id;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public String getCreate_user_name()
	{
		return create_user_name;
	}

	public void setCreate_user_name(String create_user_name)
	{
		this.create_user_name = create_user_name;
	}

}
