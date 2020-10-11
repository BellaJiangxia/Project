package com.vastsoft.yingtai.module.basemodule.patientinfo.report.service;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.db.VsSqlSession;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportModifyRequestParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportShareParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.*;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.*;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.exception.ReportException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.mapper.ReportMapper;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestClass;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.exception.RequestException;
import com.vastsoft.yingtai.module.diagnosis2.mapper.DiagnosisMapper;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.assist.SearchRequestTranferParam;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity.FRequestTranfer;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.services.RequestTranferService;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.service.FileService;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    public static final ReportService instance = new ReportService();

    private ReportService() {
    }

    /**
     * 搜索诊断消息
     *
     * @param passport
     * @param lSendUserId  发送人
     * @param lDiagnosisId 诊断ID
     * @param spu          分页
     * @return
     * @throws BaseException
     */
    public List<FReportMsg> searchMsg(Passport passport, Long lSendUserId, Long lSendOrgId, Long lRecvOrgId,
                                      Long lRecvUserId, Long report_id, SplitPageUtil spu) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            if (spu == null)
                throw new NullParameterException("diagnosisHandle");
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("send_user_id", lSendUserId);
            mapArg.put("send_org_id", lSendOrgId);
            mapArg.put("recv_org_id", lRecvOrgId);
            mapArg.put("recv_user_id", lRecvUserId);
            mapArg.put("report_id", report_id);
            int iCount = mapper.selectReportMsgCount(mapArg);
            spu.setTotalRow(iCount);
            if (iCount <= 0)
                return null;
            mapArg.put("minRow", spu.getCurrMinRowNum());
            mapArg.put("maxRow", spu.getCurrMaxRowNum());
            return mapper.selectReportMsg(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 通过分享的ID获取分享发言
     *
     * @throws BaseException
     */
    public List<FReportShareSpeech> searchReportShareSpeech(Passport passport, long reportShareid, SplitPageUtil spu,
                                                            ReportMapper mapper) throws BaseException {
        try {
            if (spu == null)
                spu = new SplitPageUtil(1, 10);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("share_id", reportShareid);
            int count = mapper.selectReportShareSpeechCount(mapArg);// searchFDiagnosisShareSpeechCount(mapArg);
            spu.setTotalRow(count);
            if (count <= 0)
                return null;
            mapArg.put("minRow", spu.getCurrMinRowNum());
            mapArg.put("maxRow", spu.getCurrMaxRowNum());
            return mapper.selectReportShareSpeech(mapArg);// searchFDiagnosisShareSpeech(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * 通过分享的ID获取分享发言
     *
     * @throws BaseException
     */
    public List<FReportShareSpeech> searchReportShareSpeech(Passport passport, long reportShareid, SplitPageUtil spu)
            throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            return this.searchReportShareSpeech(passport, reportShareid, spu, mapper);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 取消报告分享
     *
     * @throws Exception
     */
    public void cancelReportShare(Passport passport, long reportId) throws Exception {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            // if (!UserService.instance.checkUserPermission(passport,
            // UserPermission.ORG_MEDICAL_MGR))
            // throw new Diagnosis2Exception("你没有病例管理权限，不能执行操作！");
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            TReport report = mapper.selectReportByIdForUpdate(reportId);
            if (report == null)
                throw new ReportException("要取消的报告未找到！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("要取消分享的报告不是远诊系统的报告！");
            TDiagnosis diagnosis = session.getMapper(DiagnosisMapper.class)
                    .selectDiagnosisByIdForUpdate(report.getDiagnosis_id());
            if (diagnosis == null)
                throw new RequestException("指定的分享不存在！");
            if (!passport.getOrgId().equals(diagnosis.getDiagnosis_org_id())
                    && !passport.getOrgId().equals(diagnosis.getRequest_org_id()))
                throw new RequestException("你不能取消别人的分享！");
            List<FReportShare> listReportShare = this.queryReportShareByReportIdAndShareOrgId(reportId,
                    passport.getOrgId());
            if (listReportShare == null || listReportShare.size() <= 0)
                throw new RequestException("指定的分享不存在！");
            for (FReportShare freportShare : listReportShare) {
                mapper.deleteReportShareById(freportShare.getId());
            }
            session.commit();
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    private List<FReportShare> queryReportShareByReportIdAndShareOrgId(long reportId, long shareOrgId)
            throws Exception {
        SearchReportShareParam srsp = new SearchReportShareParam();
        srsp.setReport_id(reportId);
        srsp.setShare_org_id(shareOrgId);
        return this.searchReportShare(srsp);
    }

    /**
     * 分享诊断
     *
     * @throws BaseException
     */
    public TReportShare shareReport(Passport passport, long reportId) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            TReport report = mapper.selectReportByIdForUpdate(reportId);
            if (report == null)
                throw new ReportException("要分享的报告未找到！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("指定要分享的报告不是远诊系统的报告！");
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            TDiagnosis diagnosis = diagnosisMapper.selectDiagnosisByIdForUpdate(report.getDiagnosis_id());
            if (diagnosis == null)
                throw new ReportException("指定的诊断信息未找到！");
            boolean isRequestOrg = passport.getOrgId().equals(diagnosis.getRequest_org_id());
            boolean isDiagnosisOrg = passport.getOrgId().equals(diagnosis.getDiagnosis_org_id());
            if (!isRequestOrg && !isDiagnosisOrg)
                throw new RequestException("此申请不属于你所在机构或不是向你所在机构提交的！");
            if (isRequestOrg) {
                if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
                    throw new NotPermissionException("你没有病例管理权限！");
            }
            if (isDiagnosisOrg) {
                if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT)
                        && !UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
                    throw new NotPermissionException("你不是报告员或者审核员！");
            }
            List<TReportShare> listReportShare = mapper.queryReportShareByReportId(reportId);
            TReportShare reportShare;
            if (listReportShare != null && listReportShare.size() > 0) {
                if (listReportShare.size() == 1) {
                    reportShare = listReportShare.get(0);
                    if (reportShare.getStatus() == ReportShareStatus.IS_SHARE.getCode())
                        throw new RequestException("此申请已经被分享了，不需要重复分享！");
                }
                for (TReportShare reportShareTmp : listReportShare) {
                    mapper.deleteReportShareById(reportShareTmp.getId());
                }
            }
            reportShare = new TReportShare();
            reportShare.setReport_id(reportId);
            reportShare.setShare_org_id(passport.getOrgId());
            reportShare.setShare_time(new Date());
            reportShare.setShare_user_id(passport.getUserId());
            reportShare.setStatus(ReportShareStatus.IS_SHARE.getCode());
            mapper.insertReportShare(reportShare);// addDiagnosisShare(diagnosisShare);
            session.commit();
            return reportShare;
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    // /**
    // * 通过分享ID查询分享
    // *
    // * @throws BaseException
    // */
    // protected FReportShare selectReportShareById(Passport passport, Long
    // lDiagnosisShareId, ReportMapper mapper)
    // throws BaseException {
    // List<FReportShare> listDiagnosisShare = this.searchReportShare(passport,
    // lDiagnosisShareId, null, null, null,
    // null, null, null, null, null, null, null, null, null, mapper);
    // if (listDiagnosisShare == null || listDiagnosisShare.size() <= 0)
    // throw new ReportException("指定的分享没找到！");
    // return listDiagnosisShare.get(0);
    // }

    /**
     * 搜索分享
     *
     * @param passport
     * @param lDiagnosisShareId 诊断分享ID
     * @param lDiagnosisId      诊断ID
     * @param lMedicalHisId     病例ID
     * @param lShareOrgId       分享机构ID
     * @param strMedicalHisNum  病历号
     * @param strShareOrgName   分享机构名称
     * @param lShareUserId      分享用户ID
     * @param lCloseShareUserId 关闭用户ID
     * @param strSymptom        症状
     * @param dtStart           开始时间
     * @param dtEnd             结束时间
     * @param shareStatus       分享状态
     * @param spu               分页
     * @return
     * @throws Exception
     */
    /*
	 * Passport passport, Long reportShareId, Long report_id, Long
	 * lMedicalHisId, Long lShareOrgId, String strMedicalHisNum, String
	 * strShareOrgName, Long lShareUserId, Long lCloseShareUserId, String
	 * strSymptom, Date dtStart, Date dtEnd, ReportShareStatus shareStatus,
	 * SplitPageUtil spu, ReportMapper mapper
	 */
    public List<FReportShare> searchReportShare(SearchReportShareParam srsp) throws Exception {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            Map<String, Object> mapArg = srsp.buildMap();
            // new HashMap<String, Object>();
            // mapArg.put("id", reportShareId);
            // mapArg.put("report_id", report_id);
            // mapArg.put("org_id", lShareOrgId);
            // mapArg.put("share_user_id", lShareUserId);
            // mapArg.put("medical_his_num", strMedicalHisNum);
            // mapArg.put("close_share_user_id", lCloseShareUserId);
            // mapArg.put("share_org_name", strShareOrgName);
            // mapArg.put("symptom", strSymptom);
            // mapArg.put("medical_his_id", lMedicalHisId);
            // mapArg.put("status", shareStatus == null ? null :
            // shareStatus.getCode());
            // mapArg.put("start", DateTools.dateToString(dtStart));
            // mapArg.put("end", DateTools.dateToString(dtEnd));
            if (srsp.getSpu() != null) {
                int count = mapper.selectReportShareCount(mapArg);// mapper.searchFDiagnosisShareCount(mapArg);
                if (count <= 0)
                    return null;
                srsp.getSpu().setTotalRow(count);
                mapArg.put("minRow", srsp.getSpu().getCurrMinRowNum());
                mapArg.put("maxRow", srsp.getSpu().getCurrMaxRowNum());
            }
            return mapper.selectReportShare(mapArg);// searchFDiagnosisShare(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    // /**
    // * 搜索分享
    // *
    // * @param passport
    // * @param lDiagnosisShareId
    // * 诊断分享ID
    // * @param lDiagnosisId
    // * 诊断ID
    // * @param lMedicalHisId
    // * 病例ID
    // * @param lShareOrgId
    // * 分享机构ID
    // * @param strMedicalHisNum
    // * 病历号
    // * @param strShareOrgName
    // * 分享机构名称
    // * @param lShareUserId
    // * 分享用户ID
    // * @param lCloseShareUserId
    // * 关闭用户ID
    // * @param strSymptom
    // * 症状
    // * @param dtStart
    // * 开始时间
    // * @param dtEnd
    // * 结束时间
    // * @param shareStatus
    // * 分享状态
    // * @param spu
    // * 分页
    // * @return
    // * @throws BaseException
    // */
    // public List<FReportShare> searchReportShare(Passport passport, Long
    // reportShareId, Long reportId,
    // Long lMedicalHisId, Long lShareOrgId, String strMedicalHisNum, String
    // strShareOrgName, Long lShareUserId,
    // Long lCloseShareUserId, String strSymptom, Date dtStart, Date dtEnd,
    // ReportShareStatus shareStatus,
    // SplitPageUtil spu) throws BaseException {
    // SqlSession session = null;
    // try {
    // session = SessionFactory.getSession();
    // ReportMapper mapper = session.getMapper(ReportMapper.class);
    // return this.searchReportShare(passport, reportShareId, reportId,
    // lMedicalHisId, lShareOrgId,
    // strMedicalHisNum, strShareOrgName, lShareUserId, lCloseShareUserId,
    // strSymptom, dtStart, dtEnd,
    // shareStatus, spu, mapper);
    // } catch (Exception e) {
    // throw e;
    // } finally {
    // SessionFactory.closeSession(session);
    // }
    // }

    /**
     * 通过分享ID查询分享
     *
     * @throws Exception
     */
    public FReportShare selectReportShareById(long id) throws Exception {
        SearchReportShareParam srsp = new SearchReportShareParam();
        srsp.setId(id);
        List<FReportShare> listDiagnosisShare = this.searchReportShare(srsp);
        if (listDiagnosisShare == null || listDiagnosisShare.size() <= 0)
            throw new ReportException("指定的分享没找到！");
        return listDiagnosisShare.get(0);
    }

    public TReportShareSpeech addReportShareSpeech(Passport passport, long reportShareId, String strContent)
            throws Exception {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            if (strContent == null || strContent.trim().isEmpty())
                throw new ReportException("你必须填写发言内容！");
            if (strContent.length() >= 1000)
                throw new ReportException("你的发言内容太长！");
            ReportMapper mapper = session.getMapper(ReportMapper.class);

            FReportShare reportShare = this.selectReportShareById(reportShareId);
            if (reportShare == null || reportShare.getStatus() != ReportShareStatus.IS_SHARE.getCode())
                throw new ReportException("分享没找到！");
            TReportShareSpeech speech = new TReportShareSpeech();
            speech.setContent(strContent);
            speech.setShare_id(reportShareId);
            speech.setSpeech_time(new Date());
            speech.setSpeech_user_id(passport.getUserId());
            speech.setStatus(1);
            mapper.insertReportShareSpeech(speech);// mapper.addDiagnosisShareSpeech(speech);
            session.commit();
            return speech;
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 通过信息ID查询诊断信息
     *
     * @throws BaseException
     */
    public FReportMsg queryDiagnosisMsgById(long lDiagnosisMsgId) throws BaseException {
        List<FReportMsg> listdm = this.searchReportMsg(lDiagnosisMsgId, null, null, null, null, null, null, null);
        if (listdm == null || listdm.size() <= 0)
            return null;
        return listdm.get(0);
    }

    /**
     * 通过信息ID查询诊断信息
     *
     * @throws BaseException
     */
    public FReportMsg queryDiagnosisMsgById(long lDiagnosisMsgId, SqlSession session) throws BaseException {
        List<FReportMsg> listdm = this.searchReportMsg(lDiagnosisMsgId, null, null, null, null, null, null, null,
                session);
        if (listdm == null || listdm.size() <= 0)
            return null;
        return listdm.get(0);
    }

    /**
     * 查询未查看的诊断信息
     *
     * @throws BaseException
     */
    public List<FReportMsg> searchReportMsg(Long lDiangosisMsgId, Long reportId, Long lSendOrgId, Long lSendUserId,
                                            Long lRecvOrgId, Long lRecvUserId, ReportMsgStatus status, SplitPageUtil spu) throws BaseException {
        SqlSession session = SessionFactory.getSession();
        try {
            return this.searchReportMsg(lDiangosisMsgId, reportId, lSendOrgId, lSendUserId, lRecvOrgId, lRecvUserId,
                    status, spu, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 查询未查看的诊断信息
     *
     * @throws BaseException
     */
    public List<FReportMsg> searchReportMsg(Long lDiangosisMsgId, Long reportId, Long lSendOrgId, Long lSendUserId,
                                            Long lRecvOrgId, Long lRecvUserId, ReportMsgStatus status, SplitPageUtil spu, SqlSession session)
            throws BaseException {
        ReportMapper mapper = session.getMapper(ReportMapper.class);
        Map<String, Object> mapArg = new HashMap<String, Object>();
        mapArg.put("id", lDiangosisMsgId);
        mapArg.put("report_id", reportId);
        mapArg.put("send_org_id", lSendOrgId);
        mapArg.put("send_user_id", lSendUserId);
        mapArg.put("recv_org_id", lRecvOrgId);
        mapArg.put("recv_user_id", lRecvUserId);
        mapArg.put("status", status == null ? null : status.getCode());
        if (spu != null) {
            int iCount = mapper.selectReportMsgCount(mapArg);// mapper.searchFDiagnosisMsgCount(mapArg);
            spu.setTotalRow(iCount);
            if (iCount <= 0)
                return null;
            mapArg.put("minRow", spu.getCurrMinRowNum());
            mapArg.put("maxRow", spu.getCurrMaxRowNum());
        }
        return mapper.selectReportMsg(mapArg);// mapper.searchFDiagnosisMsg(mapArg);
    }

    /**
     * 通过诊断ID查询聊天记录
     *
     * @throws BaseException
     */
    public List<FReportMsg> readDiagnosisMsgByReportId(Passport passport, long reportId) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            List<FReportMsg> listMsg = this.searchReportMsg(null, reportId, null, null, null, null, null, null);
            if (listMsg == null || listMsg.isEmpty())
                return listMsg;
            // Map<String, Object> mapArg = new HashMap<String, Object>();
            // mapArg.put("status", ReportMsgStatus.READED.getCode());
            // mapArg.put("report_id", reportId);
            // mapArg.put("recv_user_id", passport.getUserId());
            mapper.updateReportMsgStatus(
                    new TReportMsg(reportId, passport.getUserId(), ReportMsgStatus.READED.getCode()));
            session.commit();
            return listMsg;
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 获取未读信息条数，数量代表有多少报告含有未读消息
     *
     * @param passport
     * @return
     * @throws BaseException
     */
    public int queryUnreadDiagnosisMsgCount(Passport passport, ReportMsgType msgType) throws BaseException {
        if (msgType == null)
            throw new ReportException("请指定消息类型！");
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("curr_user_id", passport.getUserId());
            mapArg.put("curr_org_id", passport.getOrgId());
            mapArg.put("type", msgType.getCode());
            mapArg.put("status", ReportMsgStatus.UNREAD.getCode());
            return mapper.queryUnreadReportMsgCount(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public TReport publishReport(Passport passport, TDiagnosis diagnosisTmp, TDiagnosisHandle diagnosisHandle,
                                 SqlSession session) throws ReportException, NotPermissionException {
        if (!CommonTools.judgeCaller(DiagnosisService2.class))
            throw new ReportException("你无权调用此方法！");
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
            throw new NotPermissionException(UserPermission.ORG_AUDIT);
        TReport newReport = new TReport();
        if (!newReport.buildFromDiagnosis(diagnosisTmp))
            throw new ReportException("诊断信息出现错误，不能正确的应用到报告！");
        newReport.setPic_conclusion(diagnosisHandle.getPic_conclusion());
        newReport.setPic_opinion(diagnosisHandle.getPic_opinion());
        newReport.setF_o_m(diagnosisHandle.getF_o_m());
        newReport.setPublish_user_id(diagnosisHandle.getCurr_user_id());
        if (diagnosisTmp.getRequest_class() == RequestClass.DIAGNOSIS.getCode()) {// 如果报告是一个诊断报告
            newReport.setDuty_report_user_id(diagnosisTmp.getAccept_user_id());
            newReport.setDuty_verify_user_id(diagnosisHandle.getCurr_user_id());
            newReport.setDuty_org_id(diagnosisTmp.getPublish_report_org_id());
        } else if (diagnosisTmp.getRequest_class() == RequestClass.CONSULT.getCode()) {// 如果报告是一个咨询报告
            newReport.setPublish_report_org_id(diagnosisTmp.getDiagnosis_org_id());
        }
        session.getMapper(ReportMapper.class).insertReport(newReport);
        return newReport;
    }

    /**
     * 查询修改申请
     *
     * @throws Exception
     */
    public List<FModifyReportRequest> searchModifyReportRequest(SearchReportModifyRequestParam rmrp) throws Exception {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            Map<String, Object> mapArg = rmrp.buildMap();
            if (rmrp.getSpu() != null) {
                int count = mapper.selectReportModfyRequertCount(mapArg);
                rmrp.getSpu().setTotalRow(count);
                if (count <= 0)
                    return null;
                mapArg.put("minRow", rmrp.getSpu().getCurrMinRowNum());
                mapArg.put("maxRow", rmrp.getSpu().getCurrMaxRowNum());
            }
            return mapper.selectReportModfyRequert(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
        // try {
        // Map<String, Object> mapArg = new HashMap<String, Object>();
        // mapArg.put("diagnosis_id", diagnosisId);
        // if (requestUserId != null || requestOrgId != null) {
        // if (requestUserId == null || requestOrgId == null)
        // throw new Diagnosis2Exception("申请人和申请机构不能一个为空一个不为空！");
        // }
        // mapArg.put("request_user_id", requestUserId);
        // mapArg.put("request_org_id", requestOrgId);
        // if (responseUserId != null || responseOrgId != null) {
        // if (responseUserId == null || responseOrgId == null)
        // throw new Diagnosis2Exception("响应人和响应机构不能一个为空一个不为空！");
        // }
        // mapArg.put("response_user_id", responseUserId);
        // mapArg.put("response_org_id", responseOrgId);
        // mapArg.put("status", status == null ? null : status.getCode());
        // if (spu != null) {
        // int count = mapper.selectModifyReportRequestCount(mapArg);
        // spu.setTotalRow(count);
        // if (count <= 0)
        // return null;
        // mapArg.put("minRow", spu.getCurrMinRowNum());
        // mapArg.put("maxRow", spu.getCurrMaxRowNum());
        // }
        // return mapper.selectModifyReportRequest(mapArg);
        // } catch (Exception e) {
        // throw e;
        // }
    }

	/*
	 * public List<FModifyReportRequest> searchModifyReportRequest(Passport
	 * passport, Long diagnosisId, Long requestUserId, Long requestOrgId, Long
	 * responseUserId, Long responseOrgId, ReportModifyRequestStatus status,
	 * SplitPageUtil spu) throws BaseException { SqlSession session = null; try
	 * { session = SessionFactory.getSession(); return
	 * this.searchModifyReportRequest(passport, diagnosisId, requestUserId,
	 * requestOrgId, responseUserId, responseOrgId, status, spu,
	 * session.getMapper(DiagnosisMapper2.class)); } catch (Exception e) { throw
	 * e; } finally { SessionFactory.closeSession(session); } }
	 */

    /**
     * 同意或拒绝修改报告申请
     */
    public TModifyReportRequest confirmModifyReportRequest(Passport passport, long mrrId, boolean beConfirm,
                                                           String answer) throws BaseException {
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
            throw new ReportException("你缺少病例管理权限！");
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            TModifyReportRequest mrr = mapper.selectModifyReportRequestByIdForUpdate(mrrId);
            if (mrr == null)
                throw new ReportException("指定的修改报告申请不存在！");
            if (passport.getUserId() != mrr.getResponse_user_id())
                throw new ReportException("你没有权利回复不是你申请的病例修改报告申请！");
            if (passport.getOrgId() != mrr.getResponse_org_id())
                throw new ReportException("你当前没有在诊断申请的机构中！");
            if (mrr.getStatus() != ReportModifyRequestStatus.INIT_STATUS.getCode())
                throw new ReportException("此申请已经回复，每个申请只能回复一次！");
            mrr.setResponse_time(new Date());
            if (beConfirm) {
                mrr.setStatus(ReportModifyRequestStatus.ACCEPTED.getCode());
            } else {
                mrr.setStatus(ReportModifyRequestStatus.REJECTED.getCode());
                if (answer == null || answer.trim().isEmpty())
                    throw new ReportException("拒绝时必须填写理由！");
            }
            mapper.updateModifyReportRequest(mrr);
            session.commit();
            return mrr;
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }

    }

    /**
     * 诊断方修改报告
     *
     * @throws Exception
     */
    public void modifyReport(Passport passport, TDiagnosisHandle diagnosisHandle, long reportId) throws Exception {
        if (diagnosisHandle == null)
            throw new ReportException("缺少诊断处理参数！");
        if (!StringTools.checkStr(diagnosisHandle.getPic_conclusion(), 1000))
            throw new ReportException("诊断意见必填，且最大长度为1000个字！");
        if (!StringTools.checkStr(diagnosisHandle.getPic_opinion(), 1000))
            throw new ReportException("影像所见必填，且最大长度为1000个字！");
        if (ReportFomType.parseCode(diagnosisHandle.getF_o_m()) == null)
            throw new ReportException("阴阳性不正确！");
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            TReport report = mapper.selectReportByIdForUpdate(reportId);
            if (report == null)
                throw new ReportException("没有找到指定的报告！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("此报告不是来自于远诊系统，不能修改！");
            if (report.getPublish_user_id() != passport.getUserId())
                throw new ReportException("你不是此报告的审核员！");
            if (report.getF_o_m() == diagnosisHandle.getF_o_m()
                    && report.getPic_conclusion().equals(diagnosisHandle.getPic_conclusion())
                    && report.getPic_opinion().equals(diagnosisHandle.getPic_opinion()))
                throw new ReportException("修改后的报告与修改前的报告内容完全一致！");
            TDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(report.getDiagnosis_id());
            if (!passport.getOrgId().equals(diagnosis.getDiagnosis_org_id()))
                throw new ReportException("此报告的诊断机构不是你当前的工作机构！");
            if (!this.checkModifyReport(passport, reportId))
                throw new ReportException("报告已被申请方查看，你必须提交修改申请才能修改");
            diagnosisHandle.setCreate_time(new Date());
            diagnosisHandle.setCurr_user_id(passport.getUserId());
            diagnosisHandle.setDiagnosis_id(report.getDiagnosis_id());
            diagnosisHandle.setOrg_id(passport.getOrgId());
            diagnosisHandle.setStatus(DiagnosisHandleStatus2.REPORT_MODIFYED.getCode());
            diagnosisHandle.setTransfer_time(new Date());
            diagnosisMapper.addDiagnosisHandle(diagnosisHandle);
            report.setF_o_m(diagnosisHandle.getF_o_m());
            report.setPic_conclusion(diagnosisHandle.getPic_conclusion());
            report.setPic_opinion(diagnosisHandle.getPic_opinion());
            mapper.updateReportContent(report);
            mapper.updateModifyReportRequestStatusByReoprtId(
                    new TModifyReportRequest(reportId, ReportModifyRequestStatus.MODIFY_FINISH.getCode()));
            session.commit();
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 申请修改报告
     *
     * @throws ReportException
     * @throws Exception
     */
    public TModifyReportRequest requestModifyReport(Passport passport, long reportId, String reason) throws ReportException {
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
            throw new ReportException("你没有审核员权限，不能指定被操作！");
        if (reason == null || reason.trim().isEmpty())
            throw new ReportException("必须指定修改原因！");
        reason = reason.trim();
        SqlSession session = SessionFactory.getSession();
        try {
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            TReport report = mapper.selectReportByIdForUpdate(reportId);
            if (report == null)
                throw new ReportException("没有找到指定的报告！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("此报告不是远诊系统的报告，不能修改！");
            if (report.getView_user_id() <= 0)
                throw new ReportException("此报告还没有被下级机构查看，可以直接修改，无需提交修改申请！");
            if (report.getPublish_user_id() != passport.getUserId())
                throw new ReportException("你不是本报告的审核员，不能修改本报告！");
            FDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(report.getDiagnosis_id());
            if (diagnosis == null)
                throw new ReportException("此报告的远诊申请未找到！");
            List<TModifyReportRequest> listModifyReportRequest = mapper.selectModifyReportRequestByReportId(reportId);
            if (listModifyReportRequest != null && listModifyReportRequest.size() > 0) {
                for (TModifyReportRequest mrr : listModifyReportRequest) {
                    if (mrr.getStatus() != ReportModifyRequestStatus.MODIFY_FINISH.getCode())
                        throw new ReportException("还有修改申请未处理完成，不可以添加新申请！");
                }
            }
            TModifyReportRequest mrr = new TModifyReportRequest();
            mrr.setCreate_time(new Date());
            mrr.setReport_id(reportId);
            mrr.setReason(reason);
            mrr.setRequest_user_id(passport.getUserId());
            mrr.setResponse_user_id(diagnosis.getRequest_user_id());
            mrr.setStatus(ReportModifyRequestStatus.INIT_STATUS.getCode());
            mrr.setRequest_org_id(passport.getOrgId());
            mrr.setResponse_org_id(diagnosis.getRequest_org_id());
            mapper.insertModifyReportRequest(mrr);
            session.commit();
            return mrr;
        } catch (Exception e) {
            session.rollback(true);
            throw new ReportException(e);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 检查当前用户是否可以修改报告
     *
     * @throws Exception
     */
    public boolean checkModifyReport(Passport passport, long reportId) throws Exception {
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
            throw new RequestException("你缺少审核员权限，不可以修改报告！");
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            FReport report = this.queryReportById(reportId);
            if (report == null)
                throw new ReportException("指定的报告未找到！");
            if (report.getDiagnosis_org_id() != passport.getOrgId())
                throw new ReportException("指定报告不是你当前所在机构诊断的，不能修改！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("此报告不是来自于远诊系统，不能修改！");
            if (report.getView_user_id() <= 0)
                return true;
            List<TModifyReportRequest> listMrr = mapper.selectModifyReportRequestByReportId(reportId);
            if (listMrr == null || listMrr.size() <= 0)
                return false;
            for (TModifyReportRequest mrr : listMrr) {
                if (mrr.getStatus() == ReportModifyRequestStatus.ACCEPTED.getCode())
                    return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FReport> searchReport(SearchReportParam rsp, boolean with_request) throws ReportException, DicomImgException, RequestException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            return this.searchReport(rsp, with_request, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * @param rsp
     * @param with_request 是否在返回的报告中包含申请的信息，如果有的话
     * @param session
     * @return
     * @throws ReportException
     * @throws DicomImgException
     * @throws RequestException
     */
    public List<FReport> searchReport(SearchReportParam rsp, boolean with_request, SqlSession session)
            throws ReportException, DicomImgException, RequestException {
        ReportMapper mapper = session.getMapper(ReportMapper.class);
        Map<String, Object> mapArg;
        try {
            mapArg = rsp.buildMap();
            System.out.println("搜索报告参数：" + mapArg);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ReportException(e);
        }
        if (rsp.getSpu() != null) {
            int count = mapper.selectReportCount(mapArg);
            rsp.getSpu().setTotalRow(count);
            if (count <= 0)
                return null;
            mapArg.put("minRow", rsp.getSpu().getCurrMinRowNum());
            mapArg.put("maxRow", rsp.getSpu().getCurrMaxRowNum());
        }
        List<FReport> result = mapper.selectReport(mapArg);
        if (CollectionTools.isEmpty(result))
            return result;
        if (rsp.isWith_dicom_series_list() || with_request) {
            for (FReport fReport : result) {
                fReport.setRequest(DiagnosisService2.instance.queryDiagnosisById(fReport.getDiagnosis_id(), false, session));
                fReport.setListDicomImgSeries(
                        DicomImgService.instance.querySeriesByDicomImgId(fReport.getDicom_img_id(), session));
            }
        }
        return result;
    }

    /**
     * 发送报告信息
     *
     * @throws BaseException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private FReportMsg sendDisgnosisMsg(long send_org_id, long send_user_id, long reportId, String strContent,
                                        SqlSession session) throws BaseException, IllegalArgumentException, IllegalAccessException {
        if (strContent == null || strContent.trim().isEmpty())
            throw new RequestException("请填写消息内容");
        strContent = strContent.trim();
        ReportMapper mapper = session.getMapper(ReportMapper.class);
        TReport report = mapper.selectReportByIdForUpdate(reportId);
        if (report == null)
            throw new RequestException("诊断报告没找到！");
        if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
            throw new ReportException("只有来自于远诊系统的报告才可以发送消息！");
        TDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(report.getDiagnosis_id());
        if (diagnosis == null)
            throw new ReportException("指定的报告的远诊申请未找到！");
        TReportMsg msg = new TReportMsg();
        msg.setContent(strContent);
        if (send_org_id == diagnosis.getDiagnosis_org_id()) {// 诊断机构
            msg.setRecv_org_id(diagnosis.getRequest_org_id());
            msg.setRecv_user_id(diagnosis.getRequest_user_id());
            msg.setType(ReportMsgType.TO_REQUESTER.getCode());
        } else if (send_org_id == diagnosis.getRequest_org_id()) {// 申请机构
            int iMsgMaxCount = SysService.instance.queryMaxReportMsgCount();
            if (iMsgMaxCount > 0) {
                int iMsgCount = this.querySendMsgCount(send_org_id, reportId);
                if (iMsgCount >= iMsgMaxCount)
                    throw new RequestException("在此次诊断中，你发送的消息已经达到上限(" + iMsgMaxCount + ")条！");
            }
            msg.setRecv_org_id(diagnosis.getDiagnosis_org_id());
            msg.setRecv_user_id(diagnosis.getVerify_user_id());
            msg.setType(ReportMsgType.TO_DIAGNOSISER.getCode());
        }
        if (msg.getRecv_user_id() == send_user_id)
            throw new RequestException("你不能给自己发送报告消息！");
        msg.setReport_id(reportId);
        msg.setSend_org_id(send_org_id);
        msg.setSend_user_id(send_user_id);
        msg.setStatus(ReportMsgStatus.UNREAD.getCode());
        msg.setSend_time(new Date());
        mapper.insertReportMsg(msg);
        {
            SearchRequestTranferParam srtp = new SearchRequestTranferParam();
            if (send_org_id == diagnosis.getDiagnosis_org_id()) {// 诊断机构
                srtp.setNew_request_id(diagnosis.getId());
                List<FRequestTranfer> listRequestTranfer = RequestTranferService.instance.searchRequestTranfer(srtp);
                if (!CollectionTools.isEmpty(listRequestTranfer)) {
                    for (FRequestTranfer fRequestTranfer : listRequestTranfer) {
                        if (fRequestTranfer.getOld_request().getStatus() == DiagnosisStatus2.DIAGNOSISED.getCode()) {
                            TReport reportTmp = this.queryReportByRequestId(fRequestTranfer.getOld_request_id(),
                                    session);
                            this.sendDisgnosisMsg(fRequestTranfer.getOld_request().getDiagnosis_org_id(),
                                    fRequestTranfer.getOld_request().getVerify_user_id(), reportTmp.getId(), strContent,
                                    session);
                            msg.setStatus(ReportMsgStatus.READED.getCode());
                            mapper.updateReportMsgStatus(msg);
                        }
                    }
                }
            } else if (send_org_id == diagnosis.getRequest_org_id()) {// 申请机构
                srtp.setOld_request_id(diagnosis.getId());
                List<FRequestTranfer> listRequestTranfer = RequestTranferService.instance.searchRequestTranfer(srtp);
                if (!CollectionTools.isEmpty(listRequestTranfer)) {
                    for (FRequestTranfer fRequestTranfer : listRequestTranfer) {
                        if (fRequestTranfer.getNew_request().getStatus() == DiagnosisStatus2.DIAGNOSISED.getCode()) {
                            TReport reportTmp = this.queryReportByRequestId(fRequestTranfer.getNew_request_id(),
                                    session);
                            this.sendDisgnosisMsg(fRequestTranfer.getNew_request().getRequest_org_id(),
                                    fRequestTranfer.getNew_request().getRequest_user_id(), reportTmp.getId(),
                                    strContent, session);
                            msg.setStatus(ReportMsgStatus.READED.getCode());
                            mapper.updateReportMsgStatus(msg);
                        }
                    }
                }
            }
        }
        return this.queryDiagnosisMsgById(msg.getId(), session);
    }

    /**
     * 发送报告信息
     *
     * @throws Exception
     */
    public FReportMsg sendDisgnosisMsg(Passport passport, long reportId, String strContent) throws Exception {
        VsSqlSession session = null;
        String sessKey = StringTools.getUUID();
        try {
            session = SessionFactory.getSession(sessKey);
            FReportMsg reportMsg = this.sendDisgnosisMsg(passport.getOrgId(), passport.getUserId(), reportId,
                    strContent, session);
            session.commit(sessKey);
            return reportMsg;
        } catch (Exception e) {
            session.rollback(true, sessKey);
            throw e;
        } finally {
            SessionFactory.closeSession(session, sessKey);
        }
    }

    /**
     * 查询出我所在单位发送了多少条诊断消息
     *
     * @throws BaseException
     */
    private int querySendMsgCount(long send_org_id, long report_id) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            return mapper.querySendMsgCount(new TReportMsg(report_id, send_org_id));
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public FReport queryReportById(long reportId) throws Exception {
        SearchReportParam rsp = new SearchReportParam(true);
        rsp.setId(reportId);
        List<FReport> listReport = this.searchReport(rsp, false);
        if (listReport == null || listReport.size() <= 0)
            return null;
        FReport report = listReport.get(0);
        List<TFile> listFile = FileService.instance
                .queryFileByIdList(StringTools.StrToLongArray(report.getCase_eaf_list(), ','));
        report.setEaf_file_list(listFile);
        List<FCaseHistory> listCaseHistory = CaseHistoryService.instance
                .queryAboutCaseHistoryByIds(report.getAbout_case_ids());
        report.setAbout_case_list(listCaseHistory);
        if (report.getSource_type() == PatientDataSourceType.YUANZHEN_SYS.getCode()) {
            List<FDiagnosisHandle> listDiagnosisHandle = DiagnosisService2.instance
                    .queryDiagnosisHandleByDiagnosisId(report.getDiagnosis_id());
            report.setListDiagnosisHandle(listDiagnosisHandle);
        }
        return report;
    }

    public TReport viewReport(Passport passport, long reportId)
            throws IllegalArgumentException, IllegalAccessException, ReportException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            TReport report = mapper.selectReportByIdForUpdate(reportId);
            if (report == null)
                throw new ReportException("指定的报告未找到！");
            if (report.getSource_type() == PatientDataSourceType.YUANZHEN_SYS.getCode()) {
                TDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(report.getDiagnosis_id(),
                        session);
                if (diagnosis == null)
                    throw new ReportException("此报告的诊断未找到！");
                if (diagnosis.getRequest_org_id() == passport.getOrgId()) {
                    report.setView_user_id(passport.getUserId());
                    mapper.updateReport(report);
                }
            }
            return report;
        } catch (Exception e) {
            session.rollback(true);
            throw new ReportException(e);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 打印报告，用于统计打印次数
     *
     * @param passport
     * @param lDiagnosisId
     * @throws BaseException
     */
    public void printDiagnosisReport(Passport passport, long reportId, int print_type) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
                throw new ReportException("你没有病例管理权限！");
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            TReport report = mapper.selectReportByIdForUpdate(reportId);
            if (report == null)
                throw new ReportException("没有找到指定的诊断报告！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("此报告不是远诊系统的报告，暂时不支持打印！");
            TDiagnosis diagnosis = session.getMapper(DiagnosisMapper.class)
                    .selectDiagnosisByIdForUpdate(report.getDiagnosis_id());
            if (!passport.getOrgId().equals(diagnosis.getRequest_org_id()))
                throw new ReportException("必须要是申请机构才可以打印报告！");
            report.setPrint_times(report.getPrint_times() + 1);
            report.setPrint_user_id(passport.getUserId());
            report.setPrint_time(new Date());
            if (print_type != 1 && print_type != 2)
                throw new ReportException("必须指定有效的打印类型！");
            switch (print_type) {
                case 1:
                    break;
                case 2:
                    if (report.getRequest_class() != RequestClass.CONSULT.getCode())
                        break;
                    if (report.getDuty_org_id() > 0 && report.getDuty_report_user_id() > 0 &&
                            report.getDuty_verify_user_id() > 0)
                        break;
                    report.setDuty_org_id(passport.getOrgId());
                    report.setDuty_report_user_id(passport.getUserId());
                    report.setDuty_verify_user_id(passport.getUserId());
                    break;
            }
            mapper.updateReport(report);
            session.commit();
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 下级机构人员查看报告，此方法只能下级机构调用，且
     *
     * @throws BaseException
     */
    public TReport viewDiagnosisReport(Passport passport, long reportId) throws BaseException {
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
            throw new RequestException("你缺少病例管理权限，不能查看！");
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            TReport report = mapper.selectReportByIdForUpdate(reportId);
            if (report == null)
                throw new ReportException("没有找到指定的诊断报告！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("此报告不是远诊系统的报告，暂时不支持打印！");
            TDiagnosis diagnosis = session.getMapper(DiagnosisMapper.class)
                    .selectDiagnosisByIdForUpdate(report.getDiagnosis_id());
            if (diagnosis == null)
                throw new RequestException("指定的报告未找到！");
            if (diagnosis.getStatus() != DiagnosisStatus2.DIAGNOSISED.getCode())
                throw new RequestException("此报告还未完成，还不能查看！");
            if (diagnosis.getRequest_org_id() != passport.getOrgId())
                throw new RequestException("指定报告不是你当前所在机构申请的，不可以查看！");
            if (report.getView_user_id() > 0)
                return report;
            report.setView_user_id(passport.getUserId());
            mapper.updateReport(report);
            session.commit();
            return report;
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 查询我申请的报告，并且返回结果携带未读报告消息条数
     *
     * @param passport
     * @param rsp
     * @return
     * @throws Exception
     */
    public List<FReport> searchThisOrgReportByMeRequest(Passport passport, SearchReportParam rsp) throws Exception {
        rsp.setRequest_org_id(passport.getOrgId());
        rsp.setRequest_user_id(passport.getUserId());
        List<FReport> listReport = this.searchReport(rsp, false);
        if (listReport == null || listReport.isEmpty())
            return listReport;
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            ReportMapper mapper = session.getMapper(ReportMapper.class);
            for (FReport report : listReport) {
                int newMsgCount = mapper.selectUnReadReportMsgByReportId(
                        new TReportMsg(report.getId(), passport.getUserId(), ReportMsgStatus.UNREAD.getCode()));
                report.setNewMsgCount(newMsgCount);
            }
            return listReport;
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 通过病例ID查询报告列表
     *
     * @throws Exception
     */
    public List<FReport> queryReportByCaseId(long caseId) throws Exception {
        SearchReportParam rsp = new SearchReportParam(true);
        rsp.setCase_his_id(caseId);
        return this.searchReport(rsp, false);
    }

    public FReport queryReportByRequestId(long request_id) throws Exception {
        SqlSession session = SessionFactory.getSession();
        try {
            return this.queryReportByRequestId(request_id, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public FReport queryReportByRequestId(long request_id, SqlSession session)
            throws ReportException, DicomImgException, RequestException {
        SearchReportParam rsp = new SearchReportParam(true);
        rsp.setRequest_id(request_id);
        List<FReport> listReport = this.searchReport(rsp, false, session);
        return CollectionTools.isEmpty(listReport) ? null : listReport.get(0);
    }
}
