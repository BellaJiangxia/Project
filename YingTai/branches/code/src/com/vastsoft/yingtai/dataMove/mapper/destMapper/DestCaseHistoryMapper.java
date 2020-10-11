package com.vastsoft.yingtai.dataMove.mapper.destMapper;

import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReport;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;

public interface DestCaseHistoryMapper {

	void insertCaseHistory(TCaseHistory caseHistory);

	void insertDicomImg(TDicomImg dicomImg);

	void insertDiagnosis(TDiagnosis newDiagnosis);

	void insertReport(TReport report);

	public void insertPatient(TPatient patient);

	void insertSeries(TSeries series);

	TDicomImg selectDicomImgById(long id);

	void insertPatientOrgMapper(TPatientOrgMapper patientOrgMapper);
}
