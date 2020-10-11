package com.vastsoft.yingtai.module.returnvisit.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisit;

public interface ReturnVisitMapper
{
	public int selectCount(Map<String,Object> t);

	public List<TReturnVisit> select(Map<String,Object> t);

	public List<TReturnVisit> selectAndLock(TReturnVisit t);

	public void insert(TReturnVisit t);

	public void update(TReturnVisit t);

	public void delete(TReturnVisit t);

	public TReturnVisit selectVisitById(long id);
}
