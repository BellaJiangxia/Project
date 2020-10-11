package com.vastsoft.yingtai.module.returnvisit.entity;

import java.util.List;

public class TReturnVisitQA
{
	private long question_id;
	private long visit_id;
	private int question_type;
	private String question_name;
	private int min_score;
	private int max_score;
	private String answer_text;
	private int answer_score;

	private List<TReturnVisitQAOpt> listOpt;

	public long getVisit_id()
	{
		return visit_id;
	}

	public void setVisit_id(long visit_id)
	{
		this.visit_id = visit_id;
	}

	public long getQuestion_id()
	{
		return question_id;
	}

	public void setQuestion_id(long question_id)
	{
		this.question_id = question_id;
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

	public String getAnswer_text()
	{
		return answer_text;
	}

	public void setAnswer_text(String answer_text)
	{
		this.answer_text = answer_text;
	}

	public int getAnswer_score()
	{
		return answer_score;
	}

	public void setAnswer_score(int answer_score)
	{
		this.answer_score = answer_score;
	}

	public List<TReturnVisitQAOpt> getListOpt()
	{
		return listOpt;
	}

	public void setListOpt(List<TReturnVisitQAOpt> listOpt)
	{
		this.listOpt = listOpt;
	}

}
