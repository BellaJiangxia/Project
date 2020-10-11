package com.vastsoft.yingtai.module.diagnosis2.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReportMsg;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosisHandle;

public interface DiagnosisMapper {

	public void insertDiagnosis(TDiagnosis diagnosis);

	public void updateDiagnosis(TDiagnosis diagnosis);

	public TDiagnosis selectDiagnosisByIdForUpdate(long id);

	public TDiagnosis selectDiagnosisById(long id);

//	public int searchDiagnosisCount(Map<String, Object> mapArg);

	public List<FDiagnosis> selectDiagnosis(Map<String, Object> mapArg);

	public void addDiagnosisHandle(TDiagnosisHandle handle);

	public TDiagnosisHandle queryHandleById(long lHandleId);

	public void deleteHandleById(long lHandleId);

	public TDiagnosisHandle queryHandleByIdForUpdate(long id);

	public void updateHandle(TDiagnosisHandle handle);

//	public List<TDiagnosisHandle2> searchHandle(Map<String, Object> mapArg);

//	public int searchHandleCount(Map<String, Object> mapArg);
	// TODO
	public List<FDiagnosisHandle> searchDiagnosisHandle(Map<String, Object> mapArg);
	
	public int searchMsgCount(Map<String, Object> mapArg);

	public List<TReportMsg> searchMsg(Map<String, Object> mapArg);

//	public int querySendMsgCount(Map<String, Object> mapArg);

	public int selectDiagnosisCount(Map<String, Object> mapArg);

	public int searchDiagnosisHandleCount(Map<String, Object> mapArg);

	public List<Long> queryUserIdByDiagnosisId(Long diagnosis_id);

//	public int searchFDiagnosisShareCount(Map<String, Object> mapArg);

//	public List<FReportShare> searchFDiagnosisShare(Map<String, Object> mapArg);

//	public void deleteDiagnosisShareById(long id);

//	public void addDiagnosisShare(TReportShare diagnosisShare);

//	public int searchFDiagnosisShareSpeechCount(Map<String, Object> mapArg);

//	public List<FReportShareSpeech> searchFDiagnosisShareSpeech(Map<String, Object> mapArg);

//	public void addDiagnosisShareSpeech(TReportShareSpeech speech);

//	public int searchFDiagnosisMsgCount(Map<String, Object> mapArg);

//	public List<FReportMsg> searchFDiagnosisMsg(Map<String, Object> mapArg);

//	public List<FDiagnosis2> searchFDiagnosisWithMsgCount(Map<String, Object> mapArg);

//	public void updateDiagnosisMsgStatus(Map<String, Object> mapArg);

//	public int queryUnreadDiagnosisMsgCount(Map<String, Object> mapArg);

//	public int statReportCount(Map<String, Object> mapArg);

//	public List<FDiagnosis2> statReportList(Map<String, Object> mapArg);

//	public void addMedicalHisAbout(TMedicalHisAbout2 tmha);
	
//	public List<TMedicalHisAbout2> selectMedicahHisAboutByDiagnosisId(long diagnosis_id);

//	public List<TModifyReportRequest2> selectModifyReportRequestByReportId(long diagnosis_id);

//	public void insertModifyReportRequest(TModifyReportRequest2 mrr);

//	public void updateModifyReportFinishByReoprtId(long diagnosis_id);

//	public int selectModifyReportRequestCount(Map<String, Object> mapArg);

//	public List<FModifyReportRequest2> selectModifyReportRequest(Map<String, Object> mapArg);

//	public TModifyReportRequest2 selectModifyReportRequestByIdForUpdate(long id);

//	public void updateModifyReportRequest(TModifyReportRequest2 mrr);
}
