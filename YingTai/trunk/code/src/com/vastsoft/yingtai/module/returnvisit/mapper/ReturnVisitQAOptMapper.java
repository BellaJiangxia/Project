package com.vastsoft.yingtai.module.returnvisit.mapper;

import java.util.List;

import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQAOpt;

public interface ReturnVisitQAOptMapper
{
	public List<TReturnVisitQAOpt> select(TReturnVisitQAOpt t);

	public List<TReturnVisitQAOpt> selectAndLock(TReturnVisitQAOpt t);

	public void insert(TReturnVisitQAOpt t);

	public void update(TReturnVisitQAOpt t);

	public void delete(TReturnVisitQAOpt t);
}
