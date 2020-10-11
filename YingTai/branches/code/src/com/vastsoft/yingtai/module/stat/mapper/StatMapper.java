package com.vastsoft.yingtai.module.stat.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.stat.entity.*;

public interface StatMapper {

	public int queryOrgCaseHistoryTotal(long org_id);

	public int queryCaseHistoryCountByArg(Map<String, Object> mapArg);

	public int queryRequestCountByArg(Map<String, Object> mapArg);

	public int queryOrgUserCountByArg(Map<String, Object> mapArg);

	public int queryDiagnosisCountByArg(Map<String, Object> mapArg);

	public int statPerOrgCaseHistoryCount();

	public List<FStatOrgMedicalHis> statPerOrgCaseHistory(Map<String, Object> mapArg);

	public int statDevicePartDiagnosisCount();

	public List<FStatDevicePartDiagnosis> statDevicePartDiagnosis(Map<String, Object> mapArg);

	public int statDiagnosisDoctorCount();

	public List<FStatDiagnosisDoctor> statDiagnosisDoctor(Map<String, Object> mapArg);

	public List<FStatOrgFomData> statReportOrgFom(Map<String, Object> mapArg);

	public List<FStatDeviceReport> statDeviceReport(Map<String, Object> mapArg);

	public List<FStatReportCost> statReportCost(Map<String, Object> mapArg);

	public List<FStatReportDoctor> statReportDoctor(Map<String, Object> mapArg);

	public List<FStatReportDoctor> statAuditDoctor(Map<String, Object> mapArg);

	public List<FStatReportSick> statReportSick(Map<String, Object> mapArg);

    long selectStatReportEarningCount(Map<String, Object> mapArg);

	List<Map<String,Object>> selectStatReportEarning(Map<String, Object> mapArg);

	Map<String,Object> selectStatReportEarningFooting(Map<String, Object> mapArg);

    List<Map<String,Object>> selectStatDoctorWorkAuditReport(Map<String, Object> mapArg);

	List<Map<String,Object>> selectStatDoctorWorkReportReport(Map<String, Object> mapArg);
}
