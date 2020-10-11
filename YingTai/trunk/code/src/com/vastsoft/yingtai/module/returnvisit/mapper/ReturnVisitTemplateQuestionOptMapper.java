package com.vastsoft.yingtai.module.returnvisit.mapper;

import java.util.List;

import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplateQuestionOpt;

public interface ReturnVisitTemplateQuestionOptMapper
{
	public List<TReturnVisitTemplateQuestionOpt> select(TReturnVisitTemplateQuestionOpt t);

	public List<TReturnVisitTemplateQuestionOpt> selectAndLock(TReturnVisitTemplateQuestionOpt t);

	public void insert(TReturnVisitTemplateQuestionOpt t);

	public void update(TReturnVisitTemplateQuestionOpt t);

	public void delete(TReturnVisitTemplateQuestionOpt t);
}
