package com.vastsoft.yingtai.action.dicom.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.action.dicom.entity.FPatient;
import com.vastsoft.yingtai.action.dicom.exception.DicomQueryException;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.org.orgAffix.entity.TOrgAffix;
import com.vastsoft.yingtai.module.org.orgAffix.service.OrgAffixService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class QueryDicomAction extends BaseYingTaiAction {
	private Logger logger = Logger.getLogger(QueryDicomAction.class);
	private String strPatientName = "";

	public void setPatient_name(String strPatientName) {
		this.strPatientName = strPatientName;
	}

	private String strMedicalHisNum = "";
	private long lOrgId;

	private FPatient queryPatient(String strURL, TOrgAffix orgAffix, List<String> listAeTitle, String strPatientName,
			String strMedicalNum) throws BaseException {

			// InputStream in= HttpRequest.sendGetOfStream(strURL,
			// "ae_list="+StringTools.listToString(listAeTitle,
			// ',')+"&patient_name="+
			// strPatientName+"&medicalHisNum="+strMedicalNum);
			// if (in==null)
			// throw new DicomQueryException("没找到图像数据！");
			// ObjectInputStream ois=new ObjectInputStream(in);
			// FPatient fpatient= (FPatient) ois.readObject();
			// if
			// (fpatient.getMedical_his_num()==null||fpatient.getMedical_his_num().equals("nf"))
			// {
			// throw new DicomQueryException("没找到图像数据！");
			// }
			// return fpatient;
//			if (orgAffix == null)
//				return null;
//			String strResponse = null;
//			Map<String, String> mapParam = new HashMap<String, String>();
//			mapParam.put("ae_list", StringTools.listToString(listAeTitle, ','));
//			mapParam.put("patient_name", strPatientName);
//			mapParam.put("medicalHisNum", strMedicalNum);
//			// Response hr = null;
//			// try {
//			// Requester request = new Requester();
//			// request.setDefaultContentEncoding("utf-8");
//			// hr = request.sendGet(strURL, mapParam);
//			// } catch (Exception e) {
//			// throw new DicomQueryException("连接到DICOM影像检索服务器失败，请通知系统管理员！");
//			// }
//			// strResponse=hr.getContent();
//			strResponse = HttpRequest.sendGet(strURL, "ae_list=" + StringTools.listToString(listAeTitle, ',')
//					+ "&patient_name=" + strPatientName + "&medicalHisNum=" + strMedicalNum);
//			 logger.debug("Yingtai:strResponse:"+strResponse);
//			FPatient patient = new FPatient();
//			patient.setDicom_list(new ArrayList<FDicom>());
//			// logger.debug(strResponse);
//			JSONObject jo = new JSONObject(strResponse);
//			int iCode = JSONFilter.getInt(jo, "code");
//			if (iCode != 0) {
//				String strName = JSONFilter.getString(jo, "name");
//				if (strName != null)
//					throw new DicomQueryException(strName);
//				else
//					throw new DicomQueryException("没找到图像数据！");
//			}
//			JSONObject joData = JSONFilter.getJSONObject(jo, "data");
//			if (joData == null)
//				throw new DicomQueryException("没找到图像数据！");
//			JSONObject joPatient = JSONFilter.getJSONObject(joData, "patient");
//			if (joPatient == null)
//				throw new DicomQueryException("没找到图像数据！");
//			patient.setMedical_his_num(JSONFilter.getString(joPatient, "medical_his_num"));
//			patient.setPatient_name(JSONFilter.getString(joPatient, "patient_name"));
//			patient.setPerson_identity(JSONFilter.getString(joPatient, "person_identity"));
//			patient.setBirthdayStr(JSONFilter.getString(joPatient, "birthdayStr"));
//			patient.setSex(JSONFilter.getString(joPatient, "sex"));
//			JSONArray jarr = JSONFilter.getJSONArray(joPatient, "dicom_list");
//			if (jarr == null || jarr.length() <= 0)
//				throw new DicomQueryException("没找到图像数据！");
//			for (int i = 0; i < jarr.length(); i++) {
//				JSONObject joDicom = JSONFilter.getJSONObject(jarr, i);
//				if (joDicom == null)
//					continue;
//				FDicom dicom = new FDicom();
//				dicom.setAe_title(JSONFilter.getString(joDicom, "ae_title"));
//				dicom.setChecklist_num(JSONFilter.getString(joDicom, "checklist_num"));
//				dicom.setDev_type(JSONFilter.getString(joDicom, "dev_type"));
//				TDicValue dicValue = SysService.instance.queryAndAddDicValueByTypeAndValue(null, dicom.getDev_type(),
//						null, DictionaryType.DEVICE_TYPE);
//				dicom.setDevice_type_id(dicValue.getId());
//				dicom.setPart_type(JSONFilter.getString(joDicom, "part_type"));
//				// dicValue =
//				// SysService.instance.queryAndAddDicValueByTypeAndValue(null,dicom.getPart_type(),dicValue.getId(),
//				// DictionaryType.BODY_PART_TYPE);
//				// dicom.setPart_type_id(dicValue.getId());
//				// List<TDicValue>
//				// listCheckPro=SysService.instance.queryDicValueListByParent(null,
//				// dicValue.getId(), DictionaryType.MEDICAL_HIS_IMG_CHECK_PRO);
//				dicom.buildListCheckProFromListDic(new ArrayList<TDicValue>());
//				dicom.setMark_char(JSONFilter.getString(joDicom, "mark_char"));
//				TDicomImg tmpImgss = null;
//				try {
//					tmpImgss = DicomImgService.instance.queryDicomImgByMarkChar(dicom.getMark_char());
//				} catch (Exception e) {
//				}
//				if (tmpImgss == null)
//					dicom.setNewQuery(true);
//				dicom.setThumbnail_uid(JSONFilter.getString(joDicom, "thumbnail_uid"));
//				dicom.setStudy_date(JSONFilter.getString(joDicom, "study_date"));
//				dicom.setAffix_id(orgAffix.getId());
//				patient.addDicom(dicom);
//			}
			return null;
	}

	public String execute() {
		try {
			Passport passport = takePassport();
			TOrgAffix orgAffix = OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(passport.getOrgId());// 从系统获取机构的AE列表和请求地址
			if (orgAffix == null)
				throw new DicomQueryException("没有找到机构DICOM服务器的相关配置，请联系系统管理员！");
			if ((this.strPatientName == null || this.strPatientName.trim().isEmpty())
					&& (this.strMedicalHisNum == null || this.strMedicalHisNum.trim().isEmpty())) {
				throw new DicomQueryException("病历号和病人身份证号必须指定一个！");
			}
//			List<FPatient> listPatient = new ArrayList<FPatient>();
			String dicomServerUrl = orgAffix.getQuery_url() + "queryServlet";
//			FPatient patient = null;
			List<String> listAeList = StringTools.splitStrAsList(orgAffix.getAe_code(), ',', '，');
			logger.debug("AE号：" + listAeList);
			if (listAeList == null || listAeList.size() <= 0)
				throw new DicomQueryException("没有找到图像资料！");
			FPatient patient = this.queryPatient(dicomServerUrl, orgAffix, listAeList, this.strPatientName,
					this.strMedicalHisNum);
//			if (patient != null)
//				listPatient.add(patient);
//			FPatient fpatient = FPatient.fillProperty(listPatient);
			if (patient == null)
				throw new DicomQueryException("没有找到图像资料！");
			addElementToData("patient", patient);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setOrgId(long lOrgId) {
		this.lOrgId = lOrgId;
	}

	public void setMedicalHisNum(String strMedicalHisNum) {
		this.strMedicalHisNum = strMedicalHisNum;
	}

//	public String queryPatientNum() {
//		try {
//			Passport passport = takePassport();
//			List<TOrgAffix> listOrgAffix = OrgService.instance.queryOrgAffixByOrgId(passport.getOrgId());// 从系统获取机构的AE列表和请求地址
//			if (listOrgAffix == null || listOrgAffix.size() <= 0) {
//				listOrgAffix = OrgService.instance.queryDefaultOrgAffix();// 从系统获取机构的AE列表和请求地址
//				if (listOrgAffix == null || listOrgAffix.isEmpty())
//					throw new DicomQueryException("没有找到机构DICOM服务器的相关配置，请联系系统管理员！");
//			}
//			List<FPatient> fps = new ArrayList<FPatient>();
//			for (TOrgAffix orgAffix : listOrgAffix) {
//				String dicomServerUrl = orgAffix.getQuery_url() + "queryPatientNumServlet";
//				List<String> listAeList = StringTools.splitStrAsList(orgAffix.getAe_code(), ',', '，');
//				logger.debug("AE号：" + listAeList);
//				if (listAeList == null || listAeList.size() <= 0)
//					throw new DicomQueryException("没有找到图像资料！");
//				String strResponse = null;
//				strResponse = HttpRequest.sendGet(dicomServerUrl,
//						"ae_list=" + StringTools.listToString(listAeList, ','));
//				JSONObject jo = new JSONObject(strResponse);
//				int iCode = JSONFilter.getInt(jo, "code");
//				if (iCode != 0)
//					throw new DicomQueryException("没有找到图像资料！");
//				JSONObject joData = JSONFilter.getJSONObject(jo, "data");
//				JSONArray arr = JSONFilter.getJSONArray(joData, "fps");
//				for (int i = 0, len = arr.length(); i < len; i++) {
//					FPatient patient = new FPatient();
//					JSONObject oo = arr.getJSONObject(i);
//					patient.setMedical_his_num(oo.optString("medical_his_num", ""));
//					fps.add(patient);
//				}
//			}
//			addElementToData("fps", fps);
//		} catch (Exception e) {
//			catchException(e);
//		}
//		return SUCCESS;
//	}

}
