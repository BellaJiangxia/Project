package com.vastsoft.yingtai.module.returnvisit.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitBatch;

public interface ReturnVisitBatchMapper
{
	public int selectCount(Map<String,Object> prms);

	public List<TReturnVisitBatch> select(Map<String,Object> prms);

	public List<TReturnVisitBatch> selectAndLock(TReturnVisitBatch t);

	public void insert(TReturnVisitBatch t);

	public void update(TReturnVisitBatch t);

	public void delete(TReturnVisitBatch t);
}
