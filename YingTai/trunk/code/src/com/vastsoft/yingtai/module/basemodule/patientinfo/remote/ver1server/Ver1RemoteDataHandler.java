package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.ver1server;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.db.VsSqlSession;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryState;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.mapper.CaseHistoryMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.constants.DicomImgStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.mapper.DicomMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.constants.PatientStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.mapper.PatientMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.AbstractShareEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteEntityAgent;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.SharePatientDataType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity.ShareCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.AbstractRemoteDataHandler;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class Ver1RemoteDataHandler extends AbstractRemoteDataHandler {

	@Override
	protected AbstractShareEntity tryTakeOldEntity(ShareRemoteEntityAgent rea, VsSqlSession session)
			throws RemotePatientInfoException {
		SharePatientDataType pdType = rea.getPdType();
		if (SharePatientDataType.PATIENT_DA.equals(pdType)) {
			TPatient patient = (TPatient) rea.getEntity();
			PatientMapper mapper = session.getMapper(PatientMapper.class);
			if (!StringTools.isEmpty(patient.getIdentity_id())) {
				patient.setIdentity_id(patient.getIdentity_id().trim());
				TPatient oldPatient = mapper.selectPatientByIdentityIdForUpdate(patient.getIdentity_id());
				if (oldPatient != null)
					return oldPatient;
			}
			// if (!StringTools.isEmpty(patient.getPhone_num())) {
			// patient.setIdentity_id(patient.getIdentity_id().trim());
			// TPatient oldPatient =
			// mapper.selectPatientByMobileForUpdate(patient.getMobile());
			// if (oldPatient != null)
			// return oldPatient;
			// }
			List<ShareRemoteEntityAgent> listRea = this.getListSubReaByUid(rea.getUid(), SharePatientDataType.CASE_HIS);
			for (ShareRemoteEntityAgent remoteEntityAgent : listRea) {
				TCaseHistory oldCaseHistory = (TCaseHistory) this.tryTakeOldEntity(remoteEntityAgent, session);
				if (oldCaseHistory == null)
					continue;
				TPatient oldPatient = mapper.selectPatientByIdForUpdate(oldCaseHistory.getPatient_id());
				if (oldPatient != null)
					return oldPatient;
			}
		} else if (SharePatientDataType.CASE_HIS.equals(pdType)) {
			TCaseHistory caseHistory = (TCaseHistory) rea.getEntity();
			CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
			ShareCaseHistory oldCaseHistory = mapper.selectCaseHistoryByOrgIdAndCaseHisNumForUpdate(
					new TCaseHistory(getCurrOrgId(), caseHistory.getCase_his_num()));
			if (oldCaseHistory != null)
				return oldCaseHistory;
		} else if (SharePatientDataType.DICOM_IMG.equals(pdType)) {
			TCaseHistory oldCaseHistory = (TCaseHistory) this.tryTakeOldEntity(this.getReaByUid(rea.getParentUid()), session);
			if (oldCaseHistory == null) 
				return null;
			TDicomImg dicomImg = (TDicomImg) rea.getEntity();
			if (StringTools.isEmpty(dicomImg.getMark_char()))
				throw new RemotePatientInfoException("影像的唯一标识必须指定！");
			dicomImg.setAffix_id(super.getAffix_id());
			dicomImg.setMark_char(dicomImg.getMark_char().trim());
			DicomMapper mapper = session.getMapper(DicomMapper.class);
			TDicomImg oldDicomImg = mapper.selectDicomImgByMarkCharForUpdate(dicomImg.getMark_char());
			if (oldDicomImg != null)
				return oldDicomImg;
		} else if (SharePatientDataType.DICOM_IMG_SERIES.equals(pdType)) {
			TDicomImg oldDicomImg = (TDicomImg) this.tryTakeOldEntity(this.getReaByUid(rea.getParentUid()), session);
			if (oldDicomImg == null) 
				return null;
			TSeries series = (TSeries) rea.getEntity();
			if (StringTools.isEmpty(series.getMark_char()))
				throw new RemotePatientInfoException("序列的唯一标识必须指定！");
			DicomMapper mapper = session.getMapper(DicomMapper.class);
			return mapper.selectSeriesByMarkCharAndImgIdForUpdate(new TSeries(oldDicomImg.getId(), series.getMark_char()));
		} else if (SharePatientDataType.REPORT.equals(pdType)) {
//			ReportMapper mapper = session.getMapper(ReportMapper.class);
//			TReport report = (TReport) rea.getEntity();
//			if (report.getSourceType() == null)
//				throw new RemotePatientInfoException("远程病人数据必须指定来源类型！");
//			ReportType rType = ReportType.parseCode(report.getType());
//			if (ReportType.DICOM_IMG_REPORT.equals(rType)) {
//				if (StringTools.isEmpty(report.getDicom_img_mark_char()))
//					throw new RemotePatientInfoException("远程查询的影像报告的影像唯一标识不可以为空！");
//				report.setDicom_img_mark_char(report.getDicom_img_mark_char().trim());
//				List<TReport> listReport = mapper.queryReportByDicomImgMarkCharAndSourceTypeAndType(
//						new TReport(report.getDicom_img_mark_char(), report.getSource_type(), rType.getCode()));
//				if (!CommonTools.isEmpty(listReport))
//					return listReport.get(0);
//			} else if (ReportType.INSPECT_REPORT.equals(rType)) {
//				if (StringTools.isEmpty(report.getSys_his_id()))
//					throw new RemotePatientInfoException("远程查询的检验报告的检验唯一标识不可以为空！");
//				report.setSys_his_id(report.getSys_his_id().trim());
//				List<TReport> listReport = mapper.queryReportBySysHisIdAndSourceTypeAndType(report);
//				if (!CommonTools.isEmpty(listReport))
//					return listReport.get(0);
//			} else if (ReportType.OTHER_REPORT.equals(rType)) {
//				return null;
//			}
		} else if (SharePatientDataType.PATIENT_ORG_MAPPER_DA.equals(pdType)) {
			AbstractShareEntity parentEntity = this.tryTakeOldEntity(super.getReaByUid(rea.getParentUid()), session);
			if (parentEntity == null)
				return null;
			TPatient patient = (TPatient) parentEntity;
			return session.getMapper(PatientMapper.class).selectPatientOrgMapperByOrgIdAndPatientIdForUpdate(
					new TPatientOrgMapper(patient.getId(), getCurrOrgId()));
		} else {
		}
		return null;
	}

	@Override
	protected void saveReaForAdd(ShareRemoteEntityAgent rea, AbstractShareEntity parentEntity, VsSqlSession session)
			throws RemotePatientInfoException {
		SharePatientDataType pdType = rea.getPdType();
		if (SharePatientDataType.PATIENT_DA.equals(pdType)) {
			TPatient patient = (TPatient) rea.getEntity();
			PatientMapper mapper = session.getMapper(PatientMapper.class);
			if (patient.getCreate_time() == null)
				patient.setCreate_time(new Date());
			if (patient.getCreate_user_id() <= 0)
				patient.setCreate_user_id(0);
			if (Gender.parseCode(patient.getGender()) == null)
				patient.setGender(Gender.MALE.getCode());
			if (patient.getName() == null)
				patient.setName("");
			if (PatientDataSourceType.parseCode(patient.getSource_type()) == null)
				throw new RemotePatientInfoException("必须指定病人数据来源类型！");
			patient.setStatus(PatientStatus.NORMAL.getCode());
			mapper.insertPatient(patient);
		} else if (SharePatientDataType.CASE_HIS.equals(pdType)) {
			TCaseHistory caseHistory = (TCaseHistory) rea.getEntity();
			CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
			if (StringTools.isEmpty(caseHistory.getCase_his_num()))
				throw new RemotePatientInfoException("保存病例的病例号必须指定！");
			if (CaseHistoryType.parseCode(caseHistory.getType()) == null)
				throw new RemotePatientInfoException("保存病例的病例类型必须指定！");
			caseHistory.setCase_his_num(caseHistory.getCase_his_num().trim());
			caseHistory.setPatient_id(((TPatient) parentEntity).getId());
			caseHistory.setOrg_id(this.getCurrOrgId());
			caseHistory.setCreate_time(new Date());
			caseHistory.setCreate_user_id(0);
			caseHistory.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
			caseHistory.setState(CaseHistoryState.NORMAL.getCode());
			caseHistory.setStatus(CaseHistoryStatus.NORMAL.getCode());
			mapper.insertCaseHistory(caseHistory);
		} else if (SharePatientDataType.DICOM_IMG.equals(pdType)) {
			TDicomImg dicomImg = (TDicomImg) rea.getEntity();
			DicomMapper mapper = session.getMapper(DicomMapper.class);
			if (StringTools.isEmpty(dicomImg.getMark_char()))
				throw new RemotePatientInfoException("影像的唯一标识必须指定！");
			if (dicomImg.getAffix_id() <= 0)
				throw new RemotePatientInfoException("影像的查询附件ID必须指定！");
			dicomImg.setMark_char(dicomImg.getMark_char().trim());
			dicomImg.setCase_id(((TCaseHistory) parentEntity).getId());
			if (dicomImg.getCheck_time() == null)
				dicomImg.setCheck_time(new Date());
			dicomImg.setCreate_time(new Date());
			dicomImg.setStatus(DicomImgStatus.NORMAL.getCode());
			mapper.insertDicomImg(dicomImg);
		}  else if (SharePatientDataType.DICOM_IMG_SERIES.equals(pdType)) {
			TSeries series = (TSeries) rea.getEntity();
			DicomMapper mapper = session.getMapper(DicomMapper.class);
			if (StringTools.isEmpty(series.getMark_char()))
				throw new RemotePatientInfoException("影像序列的唯一标识必须指定！");
			series.setMark_char(series.getMark_char().trim());
			series.setDicom_img_id(((TDicomImg) parentEntity).getId());
			series.setCreate_time(new Date());
			mapper.insertSeries(series);
		} else if (SharePatientDataType.PATIENT_ORG_MAPPER_DA.equals(pdType)) {
			TPatientOrgMapper patientOrgMapper = (TPatientOrgMapper) rea.getEntity();
			TPatient patient = (TPatient) parentEntity;
			patientOrgMapper.setCreate_time(new Date());
			patientOrgMapper.setOrg_id(this.getCurrOrgId());
			patientOrgMapper.setPatient_id(patient.getId());
			session.getMapper(PatientMapper.class).insertPatientOrgMapper(patientOrgMapper);
		} else {
			throw new RemotePatientInfoException("不能识别的数据类型！");
		}
	}

	@Override
	protected void mergeEntity(AbstractShareEntity oldEntity, ShareRemoteEntityAgent rea, VsSqlSession session)
			throws RemotePatientInfoException {
		SharePatientDataType pdType = rea.getPdType();
		if (SharePatientDataType.PATIENT_DA.equals(pdType)) {
			TPatient patient = (TPatient) rea.getEntity();
			TPatient oldPatient = (TPatient) oldEntity;
			PatientMapper mapper = session.getMapper(PatientMapper.class);
			oldPatient.merge(patient);
			mapper.updatePatient(oldPatient);
			patient.setId(oldPatient.getId());
		} else if (SharePatientDataType.CASE_HIS.equals(pdType)) {
			TCaseHistory caseHistory = (TCaseHistory) rea.getEntity();
			CaseHistoryMapper mapper = session.getMapper(CaseHistoryMapper.class);
			TCaseHistory oldCaseHistory = (TCaseHistory) oldEntity;
			oldCaseHistory.merge(caseHistory);
			mapper.updateCaseHistory(oldCaseHistory);
			caseHistory.setId(oldCaseHistory.getId());
		} else if (SharePatientDataType.DICOM_IMG.equals(pdType)) {
			TDicomImg dicomImg = (TDicomImg) rea.getEntity();
			DicomMapper mapper = session.getMapper(DicomMapper.class);
			dicomImg.setMark_char(dicomImg.getMark_char().trim());
			TDicomImg oldDicomImg = (TDicomImg) oldEntity;
			oldDicomImg.merge(dicomImg);
			mapper.updateDicomImg(oldDicomImg);
			dicomImg.setId(oldDicomImg.getId());
		} else if (SharePatientDataType.DICOM_IMG_SERIES.equals(pdType)) {
			TSeries series = (TSeries) rea.getEntity();
			oldEntity.merge(series);
			DicomMapper mapper = session.getMapper(DicomMapper.class);
			mapper.updateSeries((TSeries) oldEntity);
		} else if (SharePatientDataType.PATIENT_ORG_MAPPER_DA.equals(pdType)) {
			PatientMapper mapper = session.getMapper(PatientMapper.class);
			TPatientOrgMapper patientOrgMapper = (TPatientOrgMapper) rea.getEntity();
			TPatientOrgMapper oldPatientOrgMapper = (TPatientOrgMapper) oldEntity;
			oldPatientOrgMapper = new TPatientOrgMapper();
			oldPatientOrgMapper.setCard_num(patientOrgMapper.getCard_num());
			oldPatientOrgMapper.setGot_card_time(patientOrgMapper.getGot_card_time());
			mapper.updatePatientOrgMapper(oldPatientOrgMapper);
		} else {
			throw new RemotePatientInfoException("不能识别的数据类型！");
		}
	}
}
