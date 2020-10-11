package com.vastsoft.yingtai.module.weixin.service;

import java.util.List;

public class QuestionAnswer
{
	private long question_id;
	private int question_type;
	private List<Integer> answer_options_id;
	private String answer_text;
	private int answer_score;

	public long getQuestion_id()
	{
		return question_id;
	}

	public void setQuestion_id(long question_id)
	{
		this.question_id = question_id;
	}

	public List<Integer> getAnswer_options_id()
	{
		return answer_options_id;
	}

	public void setAnswer_options_id(List<Integer> answer_options_id)
	{
		this.answer_options_id = answer_options_id;
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

	public int getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(int question_type) {
		this.question_type = question_type;
	}
}
