package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.ver1server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.DateTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtai.action.dicom.exception.DicomQueryException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.constants.DicomImgStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.HttpConnection;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.PatientInfoResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareExternalSystemType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteEntityAgent;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.RemoteInterface;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.OrgRemoteConfig;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.RemoteServerController;
import com.vastsoft.yingtai.module.org.configs.exception.NotFoundDeviceTypeMapperException;
import com.vastsoft.yingtai.module.org.configs.service.OrgConfigService;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class Ver1RemoteServerController implements RemoteServerController {
	public Ver1RemoteServerController() {

	}

	@SuppressWarnings("deprecation")
	@Override
	public ShareRemoteResult searchPatientInfo(OrgRemoteConfig orConfig, Map<ShareRemoteParamsType, String> params)
			throws IOException, JSONException, BaseException {
		ShareRemoteResult dm = null;
		dm = new ShareRemoteResult(orConfig.getOrg_id(), orConfig.getId());
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("ae_list", params.get(ShareRemoteParamsType.ORG_AE_NUM));
		mapParam.put("patient_name", "");
		mapParam.put("medicalHisNum", params.get(ShareRemoteParamsType.PASC_NUM));
		String strResponse = HttpConnection.requestAsStr(orConfig.getQuery_url() + "queryServlet", mapParam);
		strResponse = new String(CommonTools.hexStringToBytes(strResponse), "UTF-8");
		LoggerUtils.logger.info("版本1查询病人数据返回：" + String.valueOf(strResponse));
		LoggerUtils.logger.info("服务器地址：" + String.valueOf(orConfig.getQuery_url()));
		TPatient patient = new TPatient();
		patient.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
		ShareRemoteEntityAgent patientRea = dm.addData(null, patient);
		TPatientOrgMapper patientOrgMapper = new TPatientOrgMapper();
		patientOrgMapper.setCard_num("");
		patientOrgMapper.setCreate_time(new Date());
		patientOrgMapper.setGot_card_time(new Date());
		dm.addData(patientRea.getUid(), patientOrgMapper);
		TCaseHistory caseHistory = new TCaseHistory();
		caseHistory.setType(CaseHistoryType.OTHER.getCode());
		caseHistory.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
		ShareRemoteEntityAgent caseHistoryRea = dm.addData(patientRea.getUid(), caseHistory);
		JSONObject jo = new JSONObject(strResponse.toString());
		int iCode = jo.optInt("code", -1);
		if (iCode != 0) {
			String strName = jo.optString("name");
			if (strName != null)
				throw new RemotePatientInfoException(strName);
			else
				throw new RemotePatientInfoException("没找到图像数据！");
		}
		JSONObject joData = jo.optJSONObject("data");
		if (joData == null)
			throw new RemotePatientInfoException("没找到图像数据！");
		JSONObject joPatient = joData.optJSONObject("patient");
		if (joPatient == null)
			throw new RemotePatientInfoException("没找到图像数据！");
		caseHistory.setCase_his_num(joPatient.optString("medical_his_num"));
		caseHistory.setType(CaseHistoryType.OTHER.getCode());
		caseHistory.setEnter_time(new Date());
		caseHistory.setLeave_time(new Date());
		patient.setName(joPatient.optString("patient_name"));
		patient.setIdentity_id(joPatient.optString("person_identity"));
		patient.setBirthday(DateTools.strToDate(joPatient.optString("birthdayStr")));
		patient.setGender(Gender.parseString(joPatient.optString("sex")).getCode());
		patient.setBrand("eps");
		JSONArray jarr = joPatient.optJSONArray("dicom_list");
		if (jarr == null || jarr.length() <= 0)
			throw new RemotePatientInfoException("没找到图像数据！");
		for (int i = 0; i < jarr.length(); i++) {
			JSONObject joDicom = jarr.optJSONObject(i);
			if (joDicom == null)
				continue;
			TDicomImg dicom = new TDicomImg();
			dicom.setAe_title(joDicom.optString("ae_title"));
			dicom.setChecklist_num(joDicom.optString("checklist_num"));
			dicom.setCheck_time(DateTools.strToDate(joDicom.optString("study_date")));
			dicom.setEps_num(caseHistory.getCase_his_num());
			dicom.setMark_char(joDicom.optString("mark_char"));
			dicom.setAffix_id(orConfig.getId());
			dicom.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
			dicom.setStatus(DicomImgStatus.NORMAL.getCode());
			dicom.setSystemType(ShareExternalSystemType.PACS);
			dicom.setBrand("eps");
			TDicValue dicValue = OrgConfigService.instance
					.takeDeviceTypeByOrgIdAndDeviceTypeName(orConfig.getOrg_id(), joDicom.optString("dev_type"));
			if (dicValue == null)
				throw new NotFoundDeviceTypeMapperException(orConfig.getOrg_id(), joDicom.optString("dev_type"));
			dicom.setDevice_type_id(dicValue.getId());
			ShareRemoteEntityAgent dicomImgRea = dm.addData(caseHistoryRea.getUid(), dicom);
			TSeries series = new TSeries();
			series.setBrand("eps");
			series.setExpose_times(0);
			series.setMark_char(dicom.getMark_char());
			dicValue = SysService.instance.queryAndAddPartType(joDicom.optString("part_type"), dicValue.getId());
			series.setPart_type_id(dicValue.getId());
			series.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
			series.setSystemType(ShareExternalSystemType.PACS);
			series.setThumbnail_data(null);
			series.setThumbnail_uid(joDicom.optString("thumbnail_uid"));
			dm.addData(dicomImgRea.getUid(), series);
		}
		return dm;
	}

	@Override
	public Map<ShareRemoteParamsType, List<String>> searchSearchRemotePascCaseNum(String queryUrl,
			Map<ShareRemoteParamsType, String> params) throws RemotePatientInfoException {
		try {
			Map<ShareRemoteParamsType, List<String>> result = new HashMap<ShareRemoteParamsType, List<String>>();
			String dicomServerUrl = queryUrl + "queryPatientNumServlet";
			@SuppressWarnings("deprecation")
			List<String> listAeList = StringTools.splitStrAsList(params.get(ShareRemoteParamsType.ORG_AE_NUM), ',', '，');
			if (listAeList == null || listAeList.size() <= 0)
				throw new DicomQueryException("没有找到图像资料！");
			Map<String, String> mapParams = new HashMap<String, String>();
			mapParams.put("ae_list", StringTools.listToString(listAeList, ','));
			String strResponse = HttpConnection.requestAsStr(dicomServerUrl, mapParams);
			strResponse = new String(CommonTools.hexStringToBytes(strResponse), "UTF-8");
			LoggerUtils.logger.info("查询最近病例返回：" + String.valueOf(strResponse));
			LoggerUtils.logger.info("服务器地址：" + String.valueOf(queryUrl));
			JSONObject jo = new JSONObject(strResponse);
			int iCode = jo.optInt("code", -1);
			if (iCode != 0)
				throw new DicomQueryException("没有找到图像资料！");
			JSONObject joData = jo.optJSONObject("data");
			JSONArray arr = joData.optJSONArray("fps");
			List<String> tyttmp = new ArrayList<String>();
			for (int i = 0, len = arr.length(); i < len; i++) {
				JSONObject oo = arr.getJSONObject(i);
				tyttmp.add(oo.optString("medical_his_num", ""));
			}
			Collections.sort(tyttmp, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return 0 - o1.compareToIgnoreCase(o2);
				}
			});
			result.put(ShareRemoteParamsType.PASC_NUM, tyttmp);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw RemotePatientInfoException.exceptionOf(e);
		}
	}

	@Override
	public byte[] readThumbnail(OrgRemoteConfig orConfig, Map<ShareRemoteParamsType, String> params, String thumbnail_uid)
			throws RemotePatientInfoException {
		try {
			String dicomServerUrl = orConfig.getQuery_url() + "thumbnail.action";
			Map<String, String> mapParams = new HashMap<String, String>();
			mapParams.put("thumbnail_uid", thumbnail_uid);
			InputStream is = HttpConnection.request(dicomServerUrl, mapParams);
			if (is == null)
				return null;
			try {
				byte[] result = null;
				int length = 0;
				byte[] buf = new byte[10 * 1024];
				while ((length = is.read(buf)) > 0) {
					if (result != null && (result.length > (10 * 1024 * 1024)))
						throw new RemotePatientInfoException("缩略图太大，已经超过10MB了");
					if (result == null)
						result = Arrays.copyOf(buf, length);
					else
						result = ArrayTools.concat(result, Arrays.copyOf(buf, length));
				}
				return result;
			} finally {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw RemotePatientInfoException.exceptionOf(e);
		}
	}

	@Override
	public boolean isSupportInterface(RemoteInterface ri) {
		return ShareRemoteServerVersion.V_1.isSupportInterface(ri);
	}

	@Override
	public List<PatientInfoResult> searchParamValuesByPatientNameOrPatientIdentifyId(OrgRemoteConfig orgConfig,
			String orgCode, String patientName, String patient_identity_id) throws RemotePatientInfoException {
		// TODO Auto-generated method stub
		return null;
	}

}
