package com.vastsoft.yingtai.module.basemodule.patientinfo.report.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FModifyReportRequest;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReport;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReportMsg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReportShare;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReportShareSpeech;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TModifyReportRequest;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReport;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReportMsg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReportShare;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReportShareSpeech;

public interface ReportMapper {

	public void insertReport(TReport report);

	public int selectReportCount(Map<String, Object> mapArg);

	public List<FReport> selectReport(Map<String, Object> mapArg);

	public TReport selectReportByIdForUpdate(long id);

	public void updateReport(TReport report);

	public int selectReportModfyRequertCount(Map<String, Object> mapArg);

	public List<FModifyReportRequest> selectReportModfyRequert(Map<String, Object> mapArg);

	public void insertReportMsg(TReportMsg msg);

	public int querySendMsgCount(TReportMsg tReportMsg);

	public List<TModifyReportRequest> selectModifyReportRequestByReportId(long report_id);

	public void insertModifyReportRequest(TModifyReportRequest mrr);

//	public int statReportCount(Map<String, Object> mapArg);

//	public List<FReport> statReportList(Map<String, Object> mapArg);

	public void updateModifyReportRequestStatusByReoprtId(TModifyReportRequest tModifyReportRequest);

	public TModifyReportRequest selectModifyReportRequestByIdForUpdate(long id);

	public void updateModifyReportRequest(TModifyReportRequest mrr);

	public int queryUnreadReportMsgCount(Map<String, Object> mapArg);

	public void updateReportMsgStatus(TReportMsg tReportMsg);

	public int selectReportMsgCount(Map<String, Object> mapArg);

	public List<FReportMsg> selectReportMsg(Map<String, Object> mapArg);

	public void insertReportShareSpeech(TReportShareSpeech speech);

	public int selectReportShareCount(Map<String, Object> mapArg);

	public List<FReportShare> selectReportShare(Map<String, Object> mapArg);

	public void deleteReportShareById(long id);

	public void insertReportShare(TReportShare reportShare);

	public int selectReportShareSpeechCount(Map<String, Object> mapArg);

	public List<FReportShareSpeech> selectReportShareSpeech(Map<String, Object> mapArg);

	public int selectUnReadReportMsgByReportId(TReportMsg tReportMsg);

	public void updateReportContent(TReport report);

	public List<TReportShare> queryReportShareByReportId(long report_id);

//	public TReport selectReportByCaseIdAndDicomImgIdAndSourceTypeForUpdate(TReport report);

	public List<TReport> queryReportByDicomImgMarkCharAndSourceTypeAndType(TReport tReport);
}
