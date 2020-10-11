package com.vastsoft.yingtai.module.financel.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.financel.entity.TFinanceAccount;
import com.vastsoft.yingtai.module.financel.entity.TFinanceFreeze;
import com.vastsoft.yingtai.module.financel.entity.TFinanceRecord;
import com.vastsoft.yingtai.module.financel.entity.TFinanceRequest;
import com.vastsoft.yingtai.module.stat.entity.FFinanceRecord;

public interface FinanceMapper {

	public TFinanceAccount queryAccountByOrgId(long lOrgId);

	public TFinanceAccount queryAccountById(long lAccountId);

	public TFinanceAccount queryAccountByIdForUpdate(long lAccountId);

	public TFinanceAccount queryAccountByOrgIdForUpdate(long lOrgId);

	public void addAccount(TFinanceAccount account);

	public void modifyAccount(TFinanceAccount account);

	public List<TFinanceAccount> searchAccountList(Map<String, Object> mapArg);

	public int searchAccountCount(Map<String, Object> mapArg);

	public TFinanceFreeze queryFreezeById(long lFreezeId);

	public void modifyFreezeStatus(TFinanceFreeze freeze);

	public void addFreeze(TFinanceFreeze freeze);

	public TFinanceFreeze queryFreezeByDiagnosisIdForUpdate(long lDiagnosisId);

	public TFinanceFreeze queryFreezeByDiagnosisId(long lDiagnosisId);

	public TFinanceRequest queryFinacalRequestByIdForUpdate(long lFinacalRequestId);

	public void modifyFinacalRequestStatus(TFinanceRequest request);

	public void removeFinanceRequestById(long lRequestId);

	public void addFinanceRequest(TFinanceRequest request);

	public List<TFinanceRequest> searchRequestList(Map<String, Object> mapArg);

	public int searchRequestCount(Map<String, Object> mapArg);

	public void addAccountRecord(TFinanceRecord record);

	public List<FFinanceRecord> searchAccountRecordList(Map<String, Object> mapArg);

	public int searchAccountRecordCount(Map<String, Object> mapArg);

}
