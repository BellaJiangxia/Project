package com.vastsoft.yingtai.dataMove.service.destService;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.yingtai.dataMove.mapper.destMapper.DestCaseHistoryMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReport;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;

public class DestService {
	public static final DestService instance = new DestService();

	private DestService() {
	}

	public void insertPatient(TPatient patient, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		mapper.insertPatient(patient);
	}

	public void insertCaseHistory(TCaseHistory caseHistory, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		mapper.insertCaseHistory(caseHistory);
	}

	public void insertDicomImg(TDicomImg dicomImg, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		mapper.insertDicomImg(dicomImg);
	}

	public void insertSeries(TSeries series, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		mapper.insertSeries(series);
	}

	public void insertDiagnosis(TDiagnosis newDiagnosis, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		mapper.insertDiagnosis(newDiagnosis);
	}

	public TDicomImg selectDicomImgById(long id, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		return mapper.selectDicomImgById(id);
	}

	public void insertReport(TReport report, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		mapper.insertReport(report);
	}

	public void insertPatientOrgMapper(TPatientOrgMapper patientOrgMapper, SqlSession destSession) {
		DestCaseHistoryMapper mapper = destSession.getMapper(DestCaseHistoryMapper.class);
		mapper.insertPatientOrgMapper(patientOrgMapper);
	}
}
