package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.ver2server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.collection.UniqueList;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.HttpConnection;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.PatientInfoResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.RemoteInterface;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.OrgRemoteConfig;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.RemoteServerController;

public class Ver2RemoteServerController implements RemoteServerController {

	@Override
	public ShareRemoteResult searchPatientInfo(OrgRemoteConfig orConfig, Map<ShareRemoteParamsType, String> params)
			throws IOException, JSONException, BaseException {
		Map<String, String> mapParams = new HashMap<String, String>();
		for (Iterator<Entry<ShareRemoteParamsType, String>> iterator = params.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<ShareRemoteParamsType, String> type = (Entry<ShareRemoteParamsType, String>) iterator.next();
			mapParams.put(type.getKey().getParam_name(), type.getValue());
		}
		String strResponse = HttpConnection
				.requestAsStr(StringTools.endWith(orConfig.getQuery_url(), '/') + "searchServlet", mapParams);
		JSONObject root = new JSONObject(strResponse);
		int code = root.optInt("code", -1);
		if (code != 0)
			throw new RemotePatientInfoException("远程搜索错误！信息：" + root.getString("errorMsg"));
		JSONObject rt = root.getJSONObject("data").getJSONObject("rootPatientData");
		if (rt == null)
			throw new RemotePatientInfoException("与远程检索服务器数据格式不匹配！");
		ShareRemoteResult dm = new ShareRemoteResult(orConfig.getOrg_id(), orConfig.getId());
		dm.desrialize(rt);
		return dm;
	}

	@Override
	public Map<ShareRemoteParamsType, List<String>> searchSearchRemotePascCaseNum(String queryUrl,
			Map<ShareRemoteParamsType, String> params) throws RemotePatientInfoException {
		try {
			Map<String, String> mapParams = new HashMap<String, String>();
			for (Iterator<Entry<ShareRemoteParamsType, String>> iterator = params.entrySet().iterator(); iterator
					.hasNext();) {
				Entry<ShareRemoteParamsType, String> type = (Entry<ShareRemoteParamsType, String>) iterator.next();
				mapParams.put(type.getKey().getParam_name(), type.getValue());
			}
			String strResponse = HttpConnection
					.requestAsStr(StringTools.endWith(queryUrl, '/') + "searchLastCaseNumServlet", mapParams);
			if (StringTools.isEmpty(strResponse))
				throw new RemotePatientInfoException("远程服务器没有返回数据！");
			JSONObject root = new JSONObject(strResponse);
			int code = root.optInt("code", -1);
			if (code != 0)
				throw new RemotePatientInfoException("远程搜索错误！信息：" + root.getString("errorMsg"));
			JSONObject rt = root.optJSONObject("data");
			if (rt == null)
				throw new RemotePatientInfoException("与远程检索服务器数据格式不匹配！");
			Map<ShareRemoteParamsType, List<String>> result = new HashMap<ShareRemoteParamsType, List<String>>();
			List<ShareRemoteParamsType> listRpType = ShareRemoteParamsType.getAll();
			for (ShareRemoteParamsType remoteParamsType : listRpType) {
				JSONArray jsoArr = rt.optJSONArray(remoteParamsType.getParam_name());
				List<String> listNums = new ArrayList<String>();
				if (jsoArr != null && jsoArr.length() > 0){
					for (int i = 0; i < jsoArr.length(); i++) {
						String str = jsoArr.optString(i);
						if (StringTools.isEmpty(str))
							continue;
						listNums.add(str);
					}
				}
				
//				if (ShareRemoteParamsType.PASC_NUM.equals(remoteParamsType)) {
//					jsoArr = rt.optJSONArray("eps_num");
//					if (jsoArr != null && jsoArr.length() > 0){
//						for (int i = 0; i < jsoArr.length(); i++) {
//							String str = jsoArr.optString(i);
//							if (StringTools.isEmpty(str))
//								continue;
//							listNums.add(str);
//						}
//					}
//				}
//				
				result.put(remoteParamsType, listNums);
			}
			return result;
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			throw RemotePatientInfoException.exceptionOf(e);
		}
	}

	@Override
	public byte[] readThumbnail(OrgRemoteConfig orConfig, Map<ShareRemoteParamsType, String> params, String thumbnail_uid)
			throws RemotePatientInfoException {
		try {
			Map<String, String> mapParams = new HashMap<String, String>();
			mapParams.put("thumbnail_uid", thumbnail_uid);
			for (Iterator<Entry<ShareRemoteParamsType, String>> iterator = params.entrySet().iterator(); iterator
					.hasNext();) {
				Entry<ShareRemoteParamsType, String> type = (Entry<ShareRemoteParamsType, String>) iterator.next();
				mapParams.put(type.getKey().getParam_name(), type.getValue());
			}
			String strResponse = HttpConnection
					.requestAsStr(StringTools.endWith(orConfig.getQuery_url(), '/') + "readThumbnailServlet", mapParams);
			JSONObject root = new JSONObject(strResponse);
			int code = root.optInt("code", -1);
			if (code != 0)
				throw new RemotePatientInfoException("远程搜索错误！信息：" + root.optString("errorMsg"));
			JSONObject rt = root.optJSONObject("data");
			if (rt == null)
				return null;
			rt = rt.optJSONObject("thumbnailData");
			if (rt == null)
				return null;
			String hexStr = rt.optString("thumbnail", "");
			if (StringTools.isEmpty(hexStr))
				return null;
			return CommonTools.hexStringToBytes(hexStr.trim());
		} catch (Exception e) {
			e.printStackTrace();
			throw RemotePatientInfoException.exceptionOf(e);
		}
	}

	@Override
	public boolean isSupportInterface(RemoteInterface ri) {
		return ShareRemoteServerVersion.V_2.isSupportInterface(ri);
	}

	@Override
	public List<PatientInfoResult> searchParamValuesByPatientNameOrPatientIdentifyId(
			OrgRemoteConfig orgConfig,String orgCode, String patientName, String patient_identity_id) throws RemotePatientInfoException {
		try {
			Map<String, String> mapParams = new HashMap<String, String>();
			mapParams.put(ShareRemoteParamsType.PATIENT_NAME.getParam_name(), patientName == null?"":StringTools.toHexString(patientName.getBytes("UTF-8")));
			mapParams.put(ShareRemoteParamsType.IDENTITY_ID.getParam_name(), patient_identity_id);
			mapParams.put(ShareRemoteParamsType.ORG_CODE.getParam_name(), orgCode);
			String strResponse = HttpConnection
					.requestAsStr(StringTools.endWith(orgConfig.getQuery_url(), '/') + "searchPatientInfoByPatientNameServlet", mapParams);
			if (StringTools.isEmpty(strResponse))
				throw new RemotePatientInfoException("远程服务器没有返回数据！");
			JSONObject root = new JSONObject(strResponse);
			int code = root.optInt("code", -1);
			if (code != 0)
				throw new RemotePatientInfoException("远程搜索错误！信息：" + root.getString("errorMsg"));
			if (root.isNull("data")) 
				return null;
			JSONObject jsonObjectTmp = root.optJSONObject("data");
			if (jsonObjectTmp == null)
				return null;
			JSONArray rt = jsonObjectTmp.optJSONArray("listPatientInfoResult");
			if (rt == null)
				throw new RemotePatientInfoException("与远程检索服务器数据格式不匹配！");
			if (rt.length() <=0)
				return null;
			List<PatientInfoResult> result = new UniqueList<PatientInfoResult>();
			for (int i = 0; i < rt.length(); i++) {
				JSONObject jsonObjTemp = rt.optJSONObject(i);
				if (jsonObjTemp == null)
					continue;
				PatientInfoResult patientInfoResult = new PatientInfoResult();
				patientInfoResult.jsonDeserialize(jsonObjTemp, orgConfig.getOrg_id());
				result.add(patientInfoResult);
			}
			return result;
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			throw RemotePatientInfoException.exceptionOf(e);
		}
	}

}
