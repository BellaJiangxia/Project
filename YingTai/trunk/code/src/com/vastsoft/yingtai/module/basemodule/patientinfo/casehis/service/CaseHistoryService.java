package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.assist.SearchCaseHistoryParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.exception.CaseHistoryException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.mapper.CaseHistoryMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.RemoteServerService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.orgAffix.entity.TOrgAffix;
import com.vastsoft.yingtai.module.org.orgAffix.exception.OrgAffixException;
import com.vastsoft.yingtai.module.org.orgAffix.service.OrgAffixService;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.service.FileService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class CaseHistoryService {
	public static final CaseHistoryService instance = new CaseHistoryService();

	private CaseHistoryService() {
	}

	/**
	 * 从本地查询病例库
	 * 
	 * @param org_id
	 * @param type
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private List<FCaseHistory> queryCaseHistoryLocal(long org_id, ShareRemoteParamsType type, String value)
			throws CaseHistoryException {
		if (ShareRemoteParamsType.DIAGNOSIS_CARD_NUM.equals(type)) {
			TPatient patient = PatientService.instance.queryPatientByOrgIdAndCardNum(org_id, value);
			if (patient == null)
				return null;
			return this.queryCaseHistoryByPatientId(patient.getId(), org_id, null);
		} else if (ShareRemoteParamsType.IDENTITY_ID.equals(type)) {
			TPatient patient = PatientService.instance.queryPatientByIdentityId(value, org_id);
			if (patient == null)
				return null;
			return this.queryCaseHistoryByPatientId(patient.getId(), org_id, null);
		} else if (ShareRemoteParamsType.PASC_NUM.equals(type) || ShareRemoteParamsType.EPS_NUM.equals(type)) {
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setOrg_id(org_id);
			chsp.setCase_his_num(value);
			chsp.setType(CaseHistoryType.OTHER);
			chsp.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS);
			return this.searchCaseHistory(chsp);
		} else if (ShareRemoteParamsType.ZHUYUAN_NUM.equals(type)) {
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setOrg_id(org_id);
			chsp.setHospitalized_num(value);
			return this.searchCaseHistory(chsp);
		} else if (ShareRemoteParamsType.BINGAN_NUM.equals(type)) {
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setOrg_id(org_id);
			chsp.setCase_his_num(value);
			chsp.setType(CaseHistoryType.HOSPITALIZED);
			return this.searchCaseHistory(chsp);
		} else if (ShareRemoteParamsType.PATIENT_NAME.equals(type)) {
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setOrg_id(org_id);
			chsp.setPatient_name(value);
			return this.searchCaseHistory(chsp);
		} else {
			return null;
		}
	}

	/**
	 * 从本地或者远程检索病例
	 * 
	 * @param org_id
	 * @param type
	 * @param value
	 * @return
	 * @throws OrgOperateException 
	 * @throws OrgAffixException 
	 * @throws Exception
	 */
	public List<FCaseHistory> queryCaseHistory(long org_id, ShareRemoteParamsType type, String value) throws CaseHistoryException, OrgOperateException, OrgAffixException {
		if (org_id <= 0)
			throw new CaseHistoryException("必须指定有效的机构ID！");
		if (type == null || StringTools.isEmpty(value))
			throw new CaseHistoryException("参数类型和参数值必须指定！");
		Map<ShareRemoteParamsType, String> params = new HashMap<ShareRemoteParamsType, String>();
		params.put(type, value);
		params.put(ShareRemoteParamsType.ORG_CODE,
				String.valueOf(OrgService.instance.searchOrgById(org_id).getOrg_code()));
		TOrgAffix orgAffix = OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(org_id);
		if (orgAffix.getOrg_id() <= 0)
			orgAffix.setOrg_id(org_id);
		RemoteServerService.instance.onSearchRemotePatientInfo(CollectionTools.buildList(orgAffix), params);
		return this.queryCaseHistoryLocal(org_id, type, value);
	}

	/**
	 * 通过病人ID从本地查询病例
	 * 
	 * @param patientId
	 * @param spu
	 * @return
	 * @throws CaseHistoryException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends TCaseHistory> List<T> queryCaseHistoryByPatientId(long patientId, Long orgId, SplitPageUtil spu) throws CaseHistoryException {
		SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
		chsp.setOrg_id(orgId);
		chsp.setPatient_id(patientId);
		return (List<T>) this.searchCaseHistory(chsp);
	}
	
	public TCaseHistory queryCaseHistoryByPatientIdAndPacsNumPlaint(Passport passport,long patient_id,String pacs_num,String plaint) throws CaseHistoryException, OrgOperateException, OrgAffixException {
		if (passport == null)
			throw new CaseHistoryException("必须指定passport!");
		TPatient patient = PatientService.instance.queryPatientById(patient_id);
		List<FCaseHistory> listCaseHistory = this.queryCaseHistory(passport.getOrgId(), ShareRemoteParamsType.PASC_NUM, pacs_num);
		if (CollectionTools.isEmpty(listCaseHistory))
			return null;
		TCaseHistory result = listCaseHistory.get(0);
		SqlSession session = SessionFactory.getSession();
		try {
			CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
			TPatient newPatient = PatientService.instance.queryPatientById(result.getPatient_id());
			if (StringTools.isEmpty(result.getSymptom()) && !StringTools.isEmpty(plaint)) {
				result.setSymptom(plaint.trim());
				if (patient != null && patient_id != result.getPatient_id()) {
					
					if (newPatient != null)
						PatientService.instance.replaceWith(patient_id,newPatient,session);
					result.setPatient_id(patient.getId());
				}
				mapper.updateCaseHistory(result);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback(true);
			throw new CaseHistoryException(e);
		}finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 通过内部ID获取病例，查询数据库，如无则返回null
	 * 
	 * @param caseId
	 * @return
	 * @throws BaseException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public FCaseHistory queryCaseHistoryById(long caseId, SqlSession session)
			throws BaseException, IllegalArgumentException, IllegalAccessException {
		SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
		chsp.setId(caseId);
		List<FCaseHistory> listCaseHistory = this.searchCaseHistory(chsp, session);
		if (listCaseHistory == null || listCaseHistory.isEmpty())
			return null;
		FCaseHistory result = listCaseHistory.get(0);
		List<TFile> listFile = FileService.instance
				.queryFileByIdList(StringTools.StrToLongArray(result.getEaf_list(), ','));
		result.setEafFiles(listFile);
		return result;
	}

	/**
	 * 通过内部ID获取病例，查询数据库，如无则返回null
	 * 
	 * @param caseId
	 * @param with_eaf_file_list 返回数据是否携带电子申请单文件列表
	 * @return
	 * @throws Exception
	 */
	public FCaseHistory queryCaseHistoryById(long caseId,boolean with_eaf_file_list) throws Exception {
		SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
		chsp.setId(caseId);
		List<FCaseHistory> listCaseHistory = this.searchCaseHistory(chsp);
		if (listCaseHistory == null || listCaseHistory.isEmpty())
			return null;
		FCaseHistory result = listCaseHistory.get(0);
		if (!with_eaf_file_list)
			return result;
		List<TFile> listFile = FileService.instance
				.queryFileByIdList(StringTools.StrToLongArray(result.getEaf_list(), ','));
		result.setEafFiles(listFile);
		return result;
	}

	public List<FCaseHistory> searchCaseHistory(SearchCaseHistoryParam shsp, SqlSession session) throws CaseHistoryException {
		CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
		Map<String, Object> mapArg;
		try {
			mapArg = shsp.buildMap();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new CaseHistoryException(e);
		}
		if (shsp.getSpu() != null) {
			int count = mapper.selectCaseHistoryCount(mapArg);
			shsp.getSpu().setTotalRow(count);
			if (count <= 0)
				return null;
			mapArg.put("minRow", shsp.getSpu().getCurrMinRowNum());
			mapArg.put("maxRow", shsp.getSpu().getCurrMaxRowNum());
		}
		List<FCaseHistory> result = mapper.selectCaseHistory(mapArg);
		if (CollectionTools.isEmpty(result))
			return result;
		if (shsp.isNeedSortByEnnerTime()) {
			Collections.sort(result, new Comparator<FCaseHistory>() {
				@Override
				public int compare(FCaseHistory o1, FCaseHistory o2) {
					return -(o1.getEnter_time().compareTo(o2.getEnter_time()));
				}
			});
		}
		return result;
	}

	/**
	 * 主体病例检索服务，任何条件都可选，当为null是不作为过滤条件
	 * 
	 * @param params
	 * @throws CaseHistoryException 
	 * @throws Exception
	 */
	public List<FCaseHistory> searchCaseHistory(SearchCaseHistoryParam shsp) throws CaseHistoryException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return this.searchCaseHistory(shsp, session);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// -------------------------------------------------------------------------------------------------------------

	/**
	 * 根据ID查询病例及电子申请单
	 * 
	 * @param passport
	 * @param caseId
	 * @return
	 * @throws Exception
	 */
	public FCaseHistory queryCaseHistoryWithEafById(Passport passport, long caseId) throws Exception {
		FCaseHistory caseHistory = this.queryCaseHistoryById(caseId,true);
		if (caseHistory == null)
			return null;
		return (FCaseHistory) (passport.getOrgId().equals(caseHistory.getOrg_id()) ? caseHistory
				: caseHistory.formatPropertys());
	}

	/**
	 * 验证病例是否在机构下
	 * 
	 * @param orgId
	 *            必填
	 * @param lMedicalHisId
	 *            必填
	 * @param lImgId
	 *            选填
	 * @return 只有当全部验证通过才返回true
	 * @throws Exception
	 */
	public boolean checkCaseHistoryOfOrg(Passport passport, long caseHistoryId) throws Exception {
		if (passport == null)
			throw new NullParameterException("passport");
		TCaseHistory caseHistory = this.queryCaseHistoryById(caseHistoryId,false);
		if (caseHistory == null)
			throw new CaseHistoryException("指定ID的病例未找到！");
		return caseHistory.getOrg_id() == passport.getOrgId();
	}

	/**
	 * 验证病例是否在机构下，如果需要，验证图像是否属于病例
	 * 
	 * @param orgId
	 *            必填
	 * @param lMedicalHisId
	 *            必填
	 * @param lImgId
	 *            选填
	 * @return 只有当全部验证通过才返回true
	 * @throws Exception
	 */
	public boolean checkCaseHistoryAndDicomImgOfOrg(Passport passport, long caseHistoryId, long dicomImgId)
			throws Exception {
		if (passport == null)
			throw new NullParameterException("passport");
		if (!this.checkCaseHistoryOfOrg(passport, caseHistoryId))
			return false;
		TDicomImg dicomImg = DicomImgService.instance.queryDicomImgById(dicomImgId);
		if (dicomImg == null)
			throw new CaseHistoryException("指定的影像未找到！");
		return dicomImg.getCase_id() == caseHistoryId;
	}

	/**
	 * 通过病例ID查询图像
	 * 
	 * @param passport
	 * @param caseId
	 * @return
	 * @throws Exception
	 */
	public List<FDicomImg> queryDicomImgListByCaseId(Passport passport, long caseId) throws Exception {
		return DicomImgService.instance.queryDicomImgByCaseHisId(caseId);
	}

	/**
	 * 撤销病例申请，注意本方法只能DiagnosisService类调用<br>
	 * 否则将抛出异常
	 * 
	 * @param passport
	 * @param lMedicalHisId
	 * @param session
	 * @throws BaseException
	 */
	public void cancelRequest(Passport passport, long caseHisId, SqlSession session) throws BaseException {
		try {
			if (!CommonTools.judgeCaller(DiagnosisService2.class))
				throw new CaseHistoryException("你不能调用此方法！");
			CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
			TCaseHistory medicalHis = mapper.selecCaseHistoryByIdForUpdate(caseHisId);
			if (medicalHis == null)
				throw new CaseHistoryException("指定的病例未找到！");
			if (medicalHis.getStatus() != CaseHistoryStatus.COMMITED.getCode())
				return;
			medicalHis.setStatus(CaseHistoryStatus.NORMAL.getCode());
			mapper.updateCaseHistory(medicalHis);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据图像markchar 查询图像
	 *
	 * @param passport
	 * @param markChar
	 * @return
	 * @throws Exception
	 */
	public FDicomImg queryDicomImgByMarkChar(Passport passport, String markChar) throws Exception {
		if (passport == null)
			throw new NullParameterException("passport");
		return DicomImgService.instance.queryDicomImgByMarkChar(markChar);
	}

	/**
	 * 通过缩略图UID获取图像，如果没有返回 null
	 * 
	 * @param passport
	 * @param thumbnail_uid
	 * @return
	 * @throws Exception
	 */
	public FDicomImg queryDicomImgByThumbnailUid(String thumbnail_uid) throws Exception {
		return DicomImgService.instance.queryDicomImgByThumbnailUid(thumbnail_uid);
	}

	/**
	 * 查询某诊断的历史病例
	 * 
	 * @param passport
	 * @param containIds
	 * @return
	 * @throws Exception
	 */
	public List<FCaseHistory> queryAboutCaseHistoryByIds(long[] containIds) throws Exception {
		if (containIds == null || containIds.length <= 0)
			return null;
		SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
		chsp.setContainIds(containIds);
		return this.searchCaseHistory(chsp);
	}

	/**
	 * 查询某诊断的历史病例
	 * 
	 * @param passport
	 * @param containIds
	 * @return
	 * @throws Exception
	 */
	public List<FCaseHistory> queryAboutCaseHistoryByIds(String containIds) throws Exception {
		return this.queryAboutCaseHistoryByIds(StringTools.StrToLongArray(containIds, ','));
	}

	// /**
	// * 通过esp_num查询本机构的病例
	// *
	// * @param passport
	// * @param pasc_case_num
	// * @return
	// * @throws BaseException
	// */
	// public TCaseHistory queryThisOrgCaseHistoryByPascCaseNum(Passport
	// passport, String pacs_case_num)
	// throws BaseException {
	// CaseHistorySearchParam chsp = new CaseHistorySearchParam();
	// chsp.setCase_his_num(pacs_case_num);
	// chsp
	// chsp.setOrg_id(passport.getOrgId());
	// List<FCaseHistory> listCaseHistory = this.searchCaseHistory(chsp);
	// return (listCaseHistory == null || listCaseHistory.size() <= 0) ? null :
	// listCaseHistory.get(0);
	// }

	public TCaseHistory modifyCaseHistory(Passport passport, TCaseHistory caseHistory, SqlSession session)
			throws CaseHistoryException {
		this.checkCaseHistoryObj(caseHistory);
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
			throw new CaseHistoryException("你缺少病例管理权限，不能执行本操作！");
		CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
		TCaseHistory oldCaseHistory = mapper.selecCaseHistoryByIdForUpdate(caseHistory.getId());
		if (oldCaseHistory == null)
			throw new CaseHistoryException("要修改的病例未找到！");
		if (oldCaseHistory.equals(caseHistory))
			return oldCaseHistory;
		oldCaseHistory.setEaf_list(caseHistory.getEaf_list());
		oldCaseHistory.setEnter_time(caseHistory.getEnter_time());
		oldCaseHistory.setHospitalized_num(caseHistory.getHospitalized_num());
		oldCaseHistory.setLeave_time(caseHistory.getLeave_time());
		oldCaseHistory.setModify_time(new Date());
		oldCaseHistory.setModify_user_id(passport.getUserId());
		oldCaseHistory.setNote(caseHistory.getNote());
		oldCaseHistory.setReception_doctor(caseHistory.getReception_doctor());
		oldCaseHistory.setReception_section(caseHistory.getReception_section());
		oldCaseHistory.setSymptom(caseHistory.getSymptom());
		oldCaseHistory.setGuahao_type(caseHistory.getGuahao_type());
		oldCaseHistory.setEnter_time(caseHistory.getEnter_time());
		oldCaseHistory.setLeave_time(caseHistory.getLeave_time());
		oldCaseHistory.setCheck_body(caseHistory.getCheck_body());
		oldCaseHistory.setDoctor_advice(caseHistory.getDoctor_advice());
		oldCaseHistory.setDiagnosis(caseHistory.getDiagnoses());
		mapper.updateCaseHistory(oldCaseHistory);
		return oldCaseHistory;
	}

	private void checkCaseHistoryObj(TCaseHistory caseHistory) throws CaseHistoryException {
		if (caseHistory == null)
			throw new CaseHistoryException("错误的病例！");
		if (caseHistory.getCase_his_num() != null) {
			caseHistory.setCase_his_num(caseHistory.getCase_his_num().trim());
			if (caseHistory.getCase_his_num().length() >= 256)
				throw new CaseHistoryException("病例的HIS病历号太长，最长256个字！");
		}
		if (caseHistory.getNote() != null) {
			caseHistory.setNote(caseHistory.getNote().trim());
			if (!caseHistory.getNote().isEmpty()) {
				if (caseHistory.getNote().length() >= 2000)
					throw new CaseHistoryException("病例备注太长，最长2000个字！");
			}
		}
		if (caseHistory.getOrg_id() <= 0)
			throw new CaseHistoryException("病例所属机构必须指定！");
		if (caseHistory.getPatient_id() <= 0)
			throw new CaseHistoryException("病例所属病人ID必须指定！");
		if (caseHistory.getReception_doctor() != null) {
			caseHistory.setReception_doctor(caseHistory.getReception_doctor().trim());
			if (!caseHistory.getReception_doctor().isEmpty()) {
				if (caseHistory.getReception_doctor().length() >= 64)
					throw new CaseHistoryException("病例的接诊医生太长，最长64个字！");
			}
		}
		if (PatientDataSourceType.parseCode(caseHistory.getSource_type()) == null)
			throw new CaseHistoryException("病例的来源类型必须指定！");
		if (CaseHistoryType.parseCode(caseHistory.getType()) == null)
			throw new CaseHistoryException("病例的类型必须指定！");
		if (caseHistory.getCase_his_num() == null || caseHistory.getCase_his_num().trim().isEmpty())
			throw new CaseHistoryException("病历号不能为空！");
		caseHistory.setCase_his_num(caseHistory.getCase_his_num().trim());
//		if (!caseHistory.getCase_his_num().matches("[a-zA-Z0-9-_]{1,100}?"))
		if (StringTools.isEmpty(caseHistory.getCase_his_num()))
			throw new CaseHistoryException("病历号不符合规范,病历号最大长度为100个字，不可为空！");
		caseHistory.setHospitalized_num(
				caseHistory.getHospitalized_num() == null ? "" : caseHistory.getHospitalized_num().trim());
		if (caseHistory.getHospitalized_num().length() >= 256) {
			throw new CaseHistoryException("病人住院号最大长度不可操作256个字！");
		}
		caseHistory.setReception_section(
				caseHistory.getReception_section() == null ? "" : caseHistory.getReception_section().trim());
		if (caseHistory.getReception_section().length() >= 128)
			throw new CaseHistoryException("病例的接诊科室太长，最长128个字！");
		if (caseHistory.getSymptom() != null)
			caseHistory.setSymptom(caseHistory.getSymptom().trim());

	}

	public TCaseHistory commitRequest(Passport passport, long caseHistoryId, SqlSession session)
			throws CaseHistoryException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
			throw new CaseHistoryException("你缺少病例管理权限，不能执行本操作！");
		CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
		TCaseHistory oldCaseHistory = mapper.selecCaseHistoryByIdForUpdate(caseHistoryId);
		if (oldCaseHistory == null)
			throw new CaseHistoryException("要修改的病例未找到！");
		if (oldCaseHistory.getOrg_id() != passport.getOrgId())
			throw new CaseHistoryException("要提交申请的病例不属于您当前所在机构的病例！");
		oldCaseHistory.setStatus(CaseHistoryStatus.COMMITED.getCode());
		mapper.updateCaseHistory(oldCaseHistory);
		return oldCaseHistory;
	}

	public List<FCaseHistory> queryCaseHistoryByIds(String case_about_ids) throws Exception {
		if (case_about_ids == null || case_about_ids.trim().isEmpty())
			return null;
		SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
		chsp.setContainIds(StringTools.StrToLongArray(case_about_ids, ','));
		return this.searchCaseHistory(chsp);
	}
}
