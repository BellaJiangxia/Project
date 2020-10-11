package com.vastsoft.yingtai.module.returnvisit.mapper;

import java.util.List;

import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplateQuestion;

public interface ReturnVisitTemplateQuestionMapper
{
	public List<TReturnVisitTemplateQuestion> select(TReturnVisitTemplateQuestion t);

	public List<TReturnVisitTemplateQuestion> selectAndLock(TReturnVisitTemplateQuestion t);

	public void insert(TReturnVisitTemplateQuestion t);

	public void update(TReturnVisitTemplateQuestion t);

	public void delete(TReturnVisitTemplateQuestion t);
}
