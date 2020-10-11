package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;

public interface CaseHistoryMapper {
	public List<FCaseHistory> selectCaseHistory(Map<String, Object> mapArg);

	public int selectCaseHistoryCount(Map<String, Object> mapArg);

	public void updateCaseHistory(TCaseHistory caseHistory);

	public TCaseHistory selecCaseHistoryByIdForUpdate(long id);

	public void insertCaseHistory(TCaseHistory caseHistory);

	public TCaseHistory selectCaseHistoryByOrgIdAndCaseHisNumForUpdate(TCaseHistory tCaseHistory);
}
