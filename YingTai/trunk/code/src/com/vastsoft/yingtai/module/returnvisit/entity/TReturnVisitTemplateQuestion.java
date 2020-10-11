package com.vastsoft.yingtai.module.returnvisit.entity;

import java.util.List;

public class TReturnVisitTemplateQuestion
{
	private long question_id;
	private long template_id;
	private int question_idx;
	private int question_type;
	private String question_name;
	private int min_score;
	private int max_score;

	private List<TReturnVisitTemplateQuestionOpt> listOpt;

	public List<TReturnVisitTemplateQuestionOpt> getListOpt()
	{
		return listOpt;
	}

	public void setListOpt(List<TReturnVisitTemplateQuestionOpt> listOpt)
	{
		this.listOpt = listOpt;
	}

	public long getQuestion_id()
	{
		return question_id;
	}

	public void setQuestion_id(long question_id)
	{
		this.question_id = question_id;
	}

	public long getTemplate_id()
	{
		return template_id;
	}

	public void setTemplate_id(long template_id)
	{
		this.template_id = template_id;
	}

	public int getQuestion_idx()
	{
		return question_idx;
	}

	public void setQuestion_idx(int question_idx)
	{
		this.question_idx = question_idx;
	}

	public int getQuestion_type()
	{
		return question_type;
	}

	public void setQuestion_type(int question_type)
	{
		this.question_type = question_type;
	}

	public String getQuestion_name()
	{
		return question_name;
	}

	public void setQuestion_name(String question_name)
	{
		this.question_name = question_name;
	}

	public int getMin_score()
	{
		return min_score;
	}

	public void setMin_score(int min_score)
	{
		this.min_score = min_score;
	}

	public int getMax_score()
	{
		return max_score;
	}

	public void setMax_score(int max_score)
	{
		this.max_score = max_score;
	}

}
