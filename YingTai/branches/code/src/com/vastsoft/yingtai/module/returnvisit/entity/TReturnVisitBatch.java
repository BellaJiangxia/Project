package com.vastsoft.yingtai.module.returnvisit.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.returnvisit.BatchStatus;

public class TReturnVisitBatch
{
	private long batch_id;
	private long org_id;
	private String org_name;
	private String batch_name;
	private long create_user_id;
	private String create_user_name;
	private Date create_time;
	private int status;
	private String status_name;

	public long getBatch_id()
	{
		return batch_id;
	}

	public void setBatch_id(long batch_id)
	{
		this.batch_id = batch_id;
	}

	public long getOrg_id()
	{
		return org_id;
	}

	public void setOrg_id(long org_id)
	{
		this.org_id = org_id;
	}

	public String getOrg_name()
	{
		return org_name;
	}

	public void setOrg_name(String org_name)
	{
		this.org_name = org_name;
	}

	public String getBatch_name()
	{
		return batch_name;
	}

	public void setBatch_name(String batch_name)
	{
		this.batch_name = batch_name;
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

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;

		BatchStatus bs = BatchStatus.parseCode(status);

		if (bs != null)
			this.status_name = BatchStatus.parseCode(this.status).getName();
	}

	public String getStatus_name()
	{
		return status_name;
	}

	public void setStatus_name(String status_name)
	{
		this.status_name = status_name;
	}

}
