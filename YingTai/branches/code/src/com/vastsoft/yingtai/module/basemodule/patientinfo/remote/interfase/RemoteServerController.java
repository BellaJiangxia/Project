package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.PatientInfoResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.RemoteInterface;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;

public interface RemoteServerController {

	public ShareRemoteResult searchPatientInfo(OrgRemoteConfig orConfig, Map<ShareRemoteParamsType, String> params) throws IOException, JSONException, BaseException;

	public Map<ShareRemoteParamsType, List<String>> searchSearchRemotePascCaseNum(String queryUrl, Map<ShareRemoteParamsType, String> params)
			throws RemotePatientInfoException;

	public byte[] readThumbnail(OrgRemoteConfig orConfig, Map<ShareRemoteParamsType, String> params,String thumbnail_uid) throws RemotePatientInfoException;
	
	/**返回是否支持此远程接口*/
	public boolean isSupportInterface(RemoteInterface ri);

	public List<PatientInfoResult> searchParamValuesByPatientNameOrPatientIdentifyId(
			OrgRemoteConfig orgConfig,String orgCode, String patientName, String patient_identity_id) throws RemotePatientInfoException;
}
