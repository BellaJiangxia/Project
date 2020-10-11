package com.vastsoft.yingtai.module.returnvisit.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplate;

public interface ReturnVisitTemplateMapper
{
	public List<TReturnVisitTemplate> select(Map<String,Object> t);

	public List<TReturnVisitTemplate> selectAndLock(TReturnVisitTemplate t);

	public void insert(TReturnVisitTemplate t);

	public void update(TReturnVisitTemplate t);

	public void delete(TReturnVisitTemplate t);

	public int selectCount(Map<String,Object> t);
}
