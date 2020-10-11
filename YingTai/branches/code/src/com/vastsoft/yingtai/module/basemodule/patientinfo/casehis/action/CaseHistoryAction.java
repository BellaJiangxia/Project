package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.action;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.assist.SearchCaseHistoryParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.exception.CaseHistoryException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReport;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.service.ReportService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class CaseHistoryAction extends BaseYingTaiAction {
	private TCaseHistory caseHistory;
	private List<TDicomImg> listDicomImg;
	private long productId;
	private String markChar;
	private Integer gender;
	private SplitPageUtil spu;
	private String sicker_name;
	private Long case_his_id, caseId;
	private boolean newAdd = false;
	private CaseHistoryStatus caseHistoryStatus;
	private String orgName;
	private String orgCode;
	private String sickerIdentityId;
	private String aboutids;
	private Long orgId;
	private Long patient_id;
	private String pacs_case_num;
	private String his_case_num;
	private String patient_name;
	private String patient_identity_id;
	private Gender patient_gender;
	private String hospitalized_num;
	private String case_his_num;
	private ShareRemoteParamsType remoteParamsType;
	private String remoteParamsValue;
	private String eps_num;
	
	/** 查询病例详细信息 */
	public String queryCaseInfo() {
		try {
			FCaseHistory caseHistory = CaseHistoryService.instance.queryCaseHistoryById(caseId,true);
			if (caseHistory == null)
				throw new CaseHistoryException("指定的病例未找到！");
			addElementToData("caseHistory", caseHistory);
			TPatient patient = PatientService.instance.queryPatientById(caseHistory.getPatient_id());
			addElementToData("patient", patient);
			List<FDicomImg> listDicomImg = DicomImgService.instance.queryDicomImgByCaseHisId(caseHistory.getId());
			addElementToData("listDicomImg", listDicomImg);
			List<FReport> listReport = ReportService.instance.queryReportByCaseId(caseHistory.getId());
			addElementToData("listReport", listReport);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 综合搜索病例 */
	public String searchCaseHistoryGreat() {
		try {
			Passport passport = takePassport();
			List<FCaseHistory> listCaseHistory = CaseHistoryService.instance.queryCaseHistory(passport.getOrgId(),
					remoteParamsType, remoteParamsValue);
			addElementToData("listCaseHistory", listCaseHistory);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("管理员搜索病例")
	public String searchAdminMedicalHis() {
		try {
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setOrg_id(orgId);
			chsp.setPatient_id(patient_id);
			chsp.setPatient_name(patient_name);
			chsp.setPatient_identity_id(patient_identity_id);
			chsp.setPatient_gender(patient_gender);
			chsp.setCase_his_num(case_his_num);
			chsp.setStatus(caseHistoryStatus);
			chsp.setCreateStart(super.getStart());
			chsp.setCreateEnd(super.getEnd());
			chsp.setSpu(spu);
			List<FCaseHistory> listMedicalHis = CaseHistoryService.instance.searchCaseHistory(chsp);
			// (null, null, null, orgId,
			// patient_id, pacs_case_num, his_case_num, null, null,
			// caseHistoryStatus, start, end, null, spu);
			addElementToData("listMedicalHis", listMedicalHis);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// // @ActionDesc("保存病例和图像信息")
	// public String saveMedicalHis() {
	// try {
	// Passport passport = takePassport();
	// caseHistory =
	// CaseHistoryService.instance.saveCaseHistoryAndDicomImgList(passport,
	// caseHistory,
	// listDicomImg);
	// addElementToData("caseHistory", caseHistory);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

//	// @ActionDesc("通过病例号查询本机构的病例和图像列表")
//	public String querySelfOrgMedicalHis() {
//		try {
//			Passport passport = takePassport();
//			CaseHistorySearchParam chsp = new CaseHistorySearchParam();
//			chsp.setOrg_id(passport.getOrgId());
//			chsp.setCase_his_num(case_his_num);
//			caseHistory = CaseHistoryService.instance.searchCaseHistory(chsp);
//			addElementToData("listDicomImg", DicomImgService.instance.queryDicomImgByCaseHisId(caseHistory.getId()));
//		} catch (Exception e) {
//			catchException(e);
//		}
//		return SUCCESS;
//	}

	// @ActionDesc("通过病例Id查询病例和图像列表")
	public String queryCaseHistoryById() {
		try {
			FCaseHistory caseHistory = CaseHistoryService.instance.queryCaseHistoryById(caseId,true);
			if (caseHistory == null)
				throw new CaseHistoryException("没有找到指定的病例！");
			addElementToData("caseHistory", caseHistory);
			TPatient patient = PatientService.instance.queryPatientById(caseHistory.getPatient_id());
			if (patient == null)
				throw new CaseHistoryException("没有找到指定的病人信息！");
			addElementToData("patient", patient);
			addElementToData("listDicomImg", DicomImgService.instance.queryDicomImgByCaseHisId(caseHistory.getId()));
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("保存病例并提交诊断申请")
	// public String createMedicalHisAndcommitDiagnosis() {
	// try {
	// Passport passport = takePassport();
	// caseHistory =
	// CaseHistoryService.instance.saveCaseHistoryAndDicomImgList(passport,
	// caseHistory, listDicomImg);
	// TDicomImg dicomImg =
	// DicomImgService.instance.queryDicomImgByMarkChar(markChar);
	// TDiagnosis diagnosis =
	// DiagnosisService.instance.commitDiagnosis(passport, caseHistory.getId(),
	// dicomImg.getId(), orgId, productId, aboutids);
	// addElementToData("caseHistory", caseHistory);
	// addElementToData("diagnosis", diagnosis);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// @ActionDesc("搜索我的机构中除开当前病例的其他病例,搜索关联病例")
	public String searchOtherSelfOrgMedicalHis() {
		try {
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setCase_his_num(case_his_num);
			chsp.setExcludeIds(case_his_id == null ? null : new long[] { case_his_id });
			chsp.setPatient_id(patient_id);
			chsp.setPatient_name(patient_name);
			chsp.setPatient_identity_id(patient_identity_id);
			chsp.setPatient_gender(patient_gender);
			chsp.setSpu(spu);
			chsp.setOrg_id(takePassport().getOrgId());
			List<FCaseHistory> listCaseHistory = CaseHistoryService.instance.searchCaseHistory(chsp);
			addElementToData("listCaseHistory", listCaseHistory);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("搜索我所在机构的病例")
	public String searchThisOrgCaseHistory() {
		try {
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setCase_his_num(case_his_num);
			chsp.setPatient_id(patient_id);
			chsp.setPatient_name(patient_name);
			chsp.setPatient_identity_id(patient_identity_id);
			chsp.setPatient_gender(patient_gender);
			chsp.setHospitalized_num(hospitalized_num);
			chsp.setStatus(caseHistoryStatus);
			chsp.setCreateEnd(super.getEnd());
			chsp.setCreateStart(super.getStart());
			chsp.setSpu(spu);
			chsp.setOrg_id(takePassport().getOrgId());
			List<FCaseHistory> listCaseHistory = CaseHistoryService.instance.searchCaseHistory(chsp);
			addElementToData("listCaseHistory", listCaseHistory);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setCaseHistoryStatus(int status) {
		this.caseHistoryStatus = CaseHistoryStatus.parseCode(status);
	}

	public void setSickerGender(Integer iGender) {
		this.gender = filterParam(iGender);
	}

	public void setSicker_gender(Integer iGender) {
		this.gender = filterParam(iGender);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setSicker_name(String strSicker_name) {
		this.sicker_name = filterParam(strSicker_name);
	}

	public void setSickerName(String strSicker_name) {
		this.sicker_name = filterParam(strSicker_name);
	}

	public void setOrgId(long lOrgId) {
		this.orgId = lOrgId;
	}

	public void setProductId(long lProductId) {
		this.productId = lProductId;
	}

	public void setMarkChar(String strMarkChar) {
		this.markChar = filterParam(strMarkChar);
	}

	public void setStartTime(String dtStart) {
		Date dd = DateTools.strToDate(dtStart);
		super.setStart(dd == null ? null : DateTools.getStartTimeByDay(dd));
	}

	public void setEndTime(String dtEnd) {
		Date dd = DateTools.strToDate(dtEnd);
		super.setEnd(dd == null ? null : DateTools.getEndTimeByDay(dd));
	}

	public void setStart(String dtStart) {
		Date dd = DateTools.strToDate(dtStart);
		super.setStart(dd == null ? null : DateTools.getStartTimeByDay(dd));
	}

	public void setEnd(String dtEnd) {
		Date dd = DateTools.strToDate(dtEnd);
		if (dd!= null)
			super.setEnd(DateTools.getEndTimeByDay(dd));
		else
			super.setEnd(null);
	}

	public void setMedicalHisId(Long lMedicalHisId) {
		this.case_his_id = filterParam(lMedicalHisId);
	}

	public void setNewAdd(boolean newAdd) {
		this.newAdd = newAdd;
	}

	public void setOrgName(String strOrgName) {
		this.orgName = filterParam(strOrgName);
	}

	public void setOrgCode(String strOrgCode) {
		this.orgCode = filterParam(strOrgCode);
	}

	public void setOrg_name(String strOrgName) {
		this.orgName = filterParam(strOrgName);
	}

	public void setOrg_code(String strOrgCode) {
		this.orgCode = filterParam(strOrgCode);
	}

	public void setSickerIdentityId(String strSickerIdentityId) {
		this.sickerIdentityId = filterParam(strSickerIdentityId);
	}

	public void setAboutids(String strAboutids) {
		this.aboutids = filterParam(strAboutids);
	}

	public void setListDicomImg(List<TDicomImg> listDicomImg) {
		this.listDicomImg = listDicomImg;
	}

	public void setCaseId(Long caseId) {
		this.caseId = filterParam(caseId);
	}

	public void setGender(Integer iGender) {
		this.gender = filterParam(iGender);
	}

	public void setOrgId(Long orgId) {
		this.orgId = filterParam(orgId);
	}

	public void setPatient_id(Long patient_id) {
		this.patient_id = filterParam(patient_id);
	}

	public void setPacs_case_num(String pacs_case_num) {
		this.pacs_case_num = filterParam(pacs_case_num);
	}

	public void setHis_case_num(String his_case_num) {
		this.his_case_num = filterParam(his_case_num);
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = filterParam(patient_name);
	}

	public void setPatient_identity_id(String patient_identity_id) {
		this.patient_identity_id = filterParam(patient_identity_id);
	}

	public void setPatient_gender(int patient_gender) {
		this.patient_gender = Gender.parseCode(patient_gender);
	}

	public void setEps_num(String eps_num) {
		this.pacs_case_num = filterParam(eps_num);
	}

	public void setHospitalized_num(String hospitalized_num) {
		this.hospitalized_num = filterParam(hospitalized_num);
	}

	public void setCaseHistory(TCaseHistory caseHistory) {
		this.caseHistory = caseHistory;
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = filterParam(case_his_num);
	}

	public void setCase_his_id(Long case_his_id) {
		this.case_his_id = case_his_id;
	}

	public void setRemoteParamsType(int remoteParamsType) {
		this.remoteParamsType = ShareRemoteParamsType.parseCode(remoteParamsType);
	}

	public void setRemoteParamsValue(String remoteParamsValue) {
		this.remoteParamsValue = filterParam(remoteParamsValue);
	}

}
