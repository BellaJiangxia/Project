package com.vastsoft.yingtai.module.returnvisit.mapper;

import java.util.List;

import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQA;

public interface ReturnVisitQAMapper
{
	public List<TReturnVisitQA> select(TReturnVisitQA t);

	public List<TReturnVisitQA> selectAndLock(TReturnVisitQA t);

	public void insert(TReturnVisitQA t);

	public void update(TReturnVisitQA t);

	public void delete(TReturnVisitQA t);
}
