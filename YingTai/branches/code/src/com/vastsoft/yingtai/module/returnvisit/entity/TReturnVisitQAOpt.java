package com.vastsoft.yingtai.module.returnvisit.entity;

public class TReturnVisitQAOpt
{
	private long question_id;
	private int option_idx;
	private String option_name;
	private int checked;

	public long getQuestion_id()
	{
		return question_id;
	}

	public void setQuestion_id(long question_id)
	{
		this.question_id = question_id;
	}

	public int getOption_idx()
	{
		return option_idx;
	}

	public void setOption_idx(int option_idx)
	{
		this.option_idx = option_idx;
	}

	public String getOption_name()
	{
		return option_name;
	}

	public void setOption_name(String option_name)
	{
		this.option_name = option_name;
	}

	public int getChecked()
	{
		return checked;
	}

	public void setChecked(int checked)
	{
		this.checked = checked;
	}

}
