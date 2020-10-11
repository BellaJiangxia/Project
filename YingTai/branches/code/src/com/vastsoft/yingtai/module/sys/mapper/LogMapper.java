package com.vastsoft.yingtai.module.sys.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.sys.entity.FSysLog;
import com.vastsoft.yingtai.module.sys.entity.TOperationRecord;

public interface LogMapper {

	public void addLogRecord(TOperationRecord tor);

	public List<FSysLog> searchSysLogList(Map<String, Object> mapArg);

	public int searchSysLogCount(Map<String, Object> mapArg);
}
