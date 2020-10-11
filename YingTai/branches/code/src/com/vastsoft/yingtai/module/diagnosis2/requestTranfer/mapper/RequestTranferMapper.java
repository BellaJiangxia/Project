package com.vastsoft.yingtai.module.diagnosis2.requestTranfer.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity.FRequestTranfer;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity.TRequestTranfer;

public interface RequestTranferMapper {

	int selectRequestTranferCount(Map<String, Object> mapArg);

	List<FRequestTranfer> selectRequestTranfer(Map<String, Object> mapArg);

	void insertRequestTranfer(TRequestTranfer requestTranfer);

	TRequestTranfer selectRequestTranferByIdForUpdate(long id);

	List<TRequestTranfer> selectRequestTranferByNewRequestId(long new_request_id);
	
}
