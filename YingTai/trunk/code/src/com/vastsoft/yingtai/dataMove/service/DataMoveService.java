package com.vastsoft.yingtai.dataMove.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.ReflectTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.dataMove.db.DataMoveSessionManager;
import com.vastsoft.yingtai.dataMove.entity.TDiagnosis_V1;
import com.vastsoft.yingtai.dataMove.entity.TMedicalHis;
import com.vastsoft.yingtai.dataMove.entity.TMedicalHisImg;
import com.vastsoft.yingtai.dataMove.service.destService.DestService;
import com.vastsoft.yingtai.dataMove.service.srcService.SrcService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryState;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.constants.PatientStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReport;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;

public class DataMoveService {
	public static final DataMoveService instance = new DataMoveService();

	public void dataMove() throws Exception {
		this.moveMedicalHis();
	}

	private void moveMedicalHis() throws Exception {
		System.out.println("开始处理“medicalHis”...");
		SqlSession srcSession = DataMoveSessionManager.getSrcSession();
		SqlSession destSession = DataMoveSessionManager.getDestSession();
		try {
			SplitPageUtil spu = new SplitPageUtil(1, 100);
			do {
				List<TMedicalHis> listMedicalHis = SrcService.instance.queryMedicalHis(spu, srcSession);
				if (CollectionTools.isEmpty(listMedicalHis))
					break;
				for (TMedicalHis medicalHis : listMedicalHis) {
					TPatient patient = this.handPatient(medicalHis, srcSession, destSession);
					this.handleCaseHistory(medicalHis, patient, srcSession, destSession);
					this.handleMedicalHisImg(patient, medicalHis, srcSession, destSession);
					this.handleDiagnosisV1(patient, medicalHis, srcSession, destSession);
				}
			} while (spu.nextPage());
			destSession.commit();
		} catch (Exception e) {
			destSession.rollback(true);
			throw e;
		} finally {
			DataMoveSessionManager.closeSession(srcSession);
			DataMoveSessionManager.closeSession(destSession);
			System.out.println("结束处理“medicalHis”...");
		}
	}

	private void handleMedicalHisImg(TPatient patient, TMedicalHis medicalHis, SqlSession srcSession,
			SqlSession destSession) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, BaseException {
		List<TMedicalHisImg> listMedicalHisImg = SrcService.instance
				.queryMedicalHisImgByMedicalHisId(medicalHis.getId(), srcSession);
		if (CollectionTools.isEmpty(listMedicalHisImg))
			return;
		for (TMedicalHisImg tMedicalHisImg : listMedicalHisImg) {
			TDicomImg dicomImg = new TDicomImg();
			Map<String, String> mapPropertyMapping = new HashMap<String, String>();
			mapPropertyMapping.put("medical_his_id", "case_id");
			ReflectTools.copyObject(tMedicalHisImg, dicomImg, mapPropertyMapping, true);
			dicomImg.setEps_num(medicalHis.getMedical_his_num());
			dicomImg.setBrand("");
			dicomImg.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
			dicomImg.setVersion("");
			DestService.instance.insertDicomImg(dicomImg, destSession);
			TSeries series = new TSeries();
			mapPropertyMapping = new HashMap<String, String>();
			ReflectTools.copyObject(tMedicalHisImg, series, mapPropertyMapping, true);
			series.setBrand("");
			series.setCreate_time(new Date());
			series.setDicom_img_id(dicomImg.getId());
			series.setExpose_times(0);
			series.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
			series.setThumbnail_data(null);
			series.setVersion("");
			DestService.instance.insertSeries(series, destSession);
		}
	}

	private void handleDiagnosisV1(TPatient patient, TMedicalHis medicalHis, SqlSession srcSession,
			SqlSession destSession) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, BaseException {
		List<TDiagnosis_V1> listOldDiagnosis = SrcService.instance.queryDiagnosisByMedicalHisId(medicalHis.getId(),
				srcSession);
		if (CollectionTools.isEmpty(listOldDiagnosis))
			return;
		for (TDiagnosis_V1 diagnosis_V1 : listOldDiagnosis) {
			TDiagnosis newDiagnosis = new TDiagnosis();
			Map<String, String> mapPropertyMapping = new HashMap<String, String>();
			mapPropertyMapping.put("medical_his_id", "case_his_id");
			mapPropertyMapping.put("request_time", "create_time");
			mapPropertyMapping.put("publish_org_id", "publish_report_org_id");
			mapPropertyMapping.put("medical_his_img_id", "dicom_img_id");
			ReflectTools.copyObject(diagnosis_V1, newDiagnosis, mapPropertyMapping, true);
			TDicomImg dicomImg = DestService.instance.selectDicomImgById(diagnosis_V1.getMedical_his_img_id(),
					destSession);
			newDiagnosis.setAllow_reporter_publish_report(0);
			newDiagnosis.setCase_eaf_list(medicalHis.getEaf_list());
			newDiagnosis.setCase_enter_time(medicalHis.getCreate_time());
			newDiagnosis.setCase_his_num(medicalHis.getMedical_his_num());
			newDiagnosis.setCase_hospitalized_num("");
			newDiagnosis.setCase_leave_time(medicalHis.getCreate_time());
			newDiagnosis.setCase_reception_doctor("");
			newDiagnosis.setCase_reception_section(medicalHis.getRequest_section());
			newDiagnosis.setCase_source_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
			newDiagnosis.setCase_symptom(medicalHis.getSymptom());
			newDiagnosis.setCase_type(CaseHistoryType.OTHER.getCode());
			newDiagnosis.setDicom_img_check_pro(dicomImg.getCheck_pro());
			newDiagnosis.setDicom_img_check_time(dicomImg.getCheck_time());
			newDiagnosis.setDicom_img_checklist_num(dicomImg.getChecklist_num());
			newDiagnosis.setDicom_img_device_type_id(dicomImg.getDevice_type_id());
			newDiagnosis.setDicom_img_mark_char(dicomImg.getMark_char());
			newDiagnosis.setPatient_id(patient.getId());
			newDiagnosis.setPatient_birthday(patient.getBirthday());
			newDiagnosis.setPatient_born_address(patient.getBorn_address());
			newDiagnosis.setPatient_gender(patient.getGender());
			newDiagnosis.setPatient_home_address(patient.getHome_address());
			newDiagnosis.setPatient_identity_id(patient.getIdentity_id());
			newDiagnosis.setPatient_name(patient.getName());
			newDiagnosis.setPatient_sick_his(patient.getSick_his());
			newDiagnosis.setPatient_source_type(patient.getSource_type());
			newDiagnosis.setPatient_work(patient.getWork());
			DestService.instance.insertDiagnosis(newDiagnosis, destSession);
			if (diagnosis_V1.getStatus() == DiagnosisStatus2.DIAGNOSISED.getCode()) {
				TReport report = new TReport();
				mapPropertyMapping = new HashMap<String, String>();
				mapPropertyMapping.put("verify_user_id", "publish_user_id");
				ReflectTools.copyObject(newDiagnosis, report, mapPropertyMapping, true);
				report.setDiagnosis_id(newDiagnosis.getId());
				report.setType(ReportType.DICOM_IMG_REPORT.getCode());
				report.setPic_opinion(diagnosis_V1.getPic_opinion());
				report.setPic_conclusion(diagnosis_V1.getPic_conclusion());
				report.setF_o_m(diagnosis_V1.getF_o_m());
				report.setPrint_user_id(0);
				report.setPrint_times(0);
				report.setPrint_time(null);
				report.setView_user_id(0);
				report.setSource_type(PatientDataSourceType.YUANZHEN_SYS.getCode());
				DestService.instance.insertReport(report, destSession);
			}
		}
	}

	private TCaseHistory handleCaseHistory(TMedicalHis medicalHis, TPatient patient, SqlSession srcSession,
			SqlSession destSession) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, BaseException {
		TCaseHistory caseHistory = new TCaseHistory();
		Map<String, String> mapPropertyMapping = new HashMap<String, String>();
		mapPropertyMapping.put("medical_his_num", "case_his_num");
		mapPropertyMapping.put("creator_id", "create_user_id");
		ReflectTools.copyObject(medicalHis, caseHistory, mapPropertyMapping, true);
		caseHistory.setBrand("");
		caseHistory.setCheck_body("");
		caseHistory.setDoctor_advice("");
		caseHistory.setEnter_time(caseHistory.getCreate_time());
		caseHistory.setLeave_time(caseHistory.getCreate_time());
		caseHistory.setGuahao_type("");
		caseHistory.setHospitalized_num("");
		caseHistory.setPatient_id(patient.getId());
		caseHistory.setPatient_say("");
		caseHistory.setReception_doctor("");
		caseHistory.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
		caseHistory.setState(CaseHistoryState.NORMAL.getCode());
		caseHistory.setType(CaseHistoryType.OTHER.getCode());
		caseHistory.setVersion("");
		DestService.instance.insertCaseHistory(caseHistory, destSession);
		return caseHistory;
	}

	private TPatient handPatient(TMedicalHis medicalHis, SqlSession srcSession, SqlSession destSession)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, BaseException {
		TPatient patient = new TPatient();
		Map<String, String> mapPropertyMapping = new HashMap<String, String>();
		mapPropertyMapping.put("sicker_name", "name");
		mapPropertyMapping.put("live_site", "home_address");
		mapPropertyMapping.put("born_site", "born_address");
		mapPropertyMapping.put("creator_id", "create_user_id");
		ReflectTools.copyObject(medicalHis, patient, mapPropertyMapping, true);
		patient.setBrand("");
		patient.setContact_addr("");
		patient.setContact_name("");
		patient.setContact_phone_num("");
		patient.setContact_rela("");
		patient.setMarital_status("未知");
		patient.setMingzu("汉族");
		patient.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
		patient.setVersion("");
		patient.setStatus(PatientStatus.NORMAL.getCode());
		DestService.instance.insertPatient(patient, destSession);
		TPatientOrgMapper patientOrgMapper = new TPatientOrgMapper();
		patientOrgMapper.setBrand("");
		patientOrgMapper.setCard_num("");
		patientOrgMapper.setCreate_time(new Date());
		patientOrgMapper.setGot_card_time(new Date());
		patientOrgMapper.setOrg_id(medicalHis.getOrg_id());
		patientOrgMapper.setPatient_id(patient.getId());
		patientOrgMapper.setSource_type(PatientDataSourceType.REMOTE_PASC_SYS.getCode());
		patientOrgMapper.setVersion("");
		patientOrgMapper.setLast_jz_time(new Date());
		DestService.instance.insertPatientOrgMapper(patientOrgMapper, destSession);
		return patient;
	}

}
