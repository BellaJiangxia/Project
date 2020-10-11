package com.vastsoft.yingtaidicom.search.orgsearch.systems.emr;

import java.util.List;

import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public class EmrSysExecutor extends ExternalSystemSearchExecutor {

	public EmrSysExecutor(SystemIdentity systemIdentity, SessionFactory factory) {
		super(systemIdentity, factory);
	}

	@Override
	public boolean canSearch(RemoteParamsType paramType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void searchPatientData(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchLastCaseNums(LastCaseNums result,
			RemoteParamsManager remoteParamsManager) throws SearchExecuteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid)
			throws SearchExecuteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(RemoteParamsManager remoteParamsManager, String patient_name, String identify_id)
			throws SearchExecuteException {
		// TODO Auto-generated method stub
		return null;
	}
}
