package com.vastsoft.yingtai.action;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportMsgType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.service.ReportService;
import com.vastsoft.yingtai.module.diagnosis2.assist.HandleSearchParam;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.inquiry.InquiryService;
import com.vastsoft.yingtai.module.org.realtion.service.OrgRelationService;
import com.vastsoft.yingtai.module.reservation.service.ReservationService;
import com.vastsoft.yingtai.module.user.constants.OrgUserMapperStatus;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class TimerAction extends BaseYingTaiAction {

    /**
     * 查询带诊断和待处理的流转,新诊断消息的数量
     */
    public String timerQuery() {
        try {
            Passport passport = takePassport();
            SplitPageUtil spu = new SplitPageUtil(1, 2);
            try {
                DiagnosisService2.instance.queryTranferToUserDiagnosisHandleByUserIdAndOrgIdAndStatus(
                        passport.getUserId(), passport.getOrgId(), DiagnosisHandleStatus2.TRANFERED, spu);
                addElementToData("unAcceptHandleCount", spu.getTotalRow());
            } catch (Exception e) {
                addElementToData("unAcceptHandleCount", 0);
            }
            try {
                long waitDiagnosisCount = DiagnosisService2.instance.queryWaitDiagnosisCount(passport);
                addElementToData("waitDiagnosisHandleCount", waitDiagnosisCount);
            } catch (Exception e) {
                addElementToData("waitDiagnosisHandleCount", 0);
            }
            try {
                int unreadDiagnosisMsgCount = ReportService.instance.queryUnreadDiagnosisMsgCount(passport,
                        ReportMsgType.TO_DIAGNOSISER);
                addElementToData("unreadDiagnosisMsgCount", unreadDiagnosisMsgCount);
            } catch (Exception e) {
                addElementToData("unreadDiagnosisMsgCount", 0);
            }
            try {
                int unreadRequestMsgCount = ReportService.instance.queryUnreadDiagnosisMsgCount(passport,
                        ReportMsgType.TO_REQUESTER);
                addElementToData("unreadRequestMsgCount", unreadRequestMsgCount);
            } catch (Exception e) {
                addElementToData("unreadRequestMsgCount", 0);
            }
            try {
                TDiagnosisHandle handle = DiagnosisService2.instance.queryUnFinishHandle(passport);
                addElementToData("unfinish", handle != null);
            } catch (Exception e) {
                addElementToData("unfinish", false);
            }
            try {
                spu.setTotalRow(0);
                HandleSearchParam hsp = new HandleSearchParam();
                hsp.setOrg_id(passport.getOrgId());
                hsp.setStatus(DiagnosisHandleStatus2.TRANFER_AUDIT);
                hsp.setSpu(spu);
                DiagnosisService2.instance.searchDiagnosisHandle(hsp);
                addElementToData("unAuditCount", spu.getTotalRow());
            } catch (Exception e) {
                addElementToData("unAuditCount", 0);
            }
            try {
                this.addElementToData("joining_user_count", UserService.instance.queryUserListCountByOrg(passport, null, null, null,
                        OrgUserMapperStatus.APPROVING));
            } catch (Exception e) {
                addElementToData("joining_user_count", 0);
            }
            try {
                this.addElementToData("joining_org_count", OrgRelationService.instance.selectMyRequestFriendsCount(passport));
            } catch (Exception e) {
                addElementToData("joining_org_count", 0);
            }
            try {
                this.addElementToData("newInquiryCount", InquiryService.instance.getNewInquiryCount(passport));
            } catch (Exception e) {
                this.addElementToData("newInquiryCount", 0);
            }
            try {
                this.addElementToData("reserveCount", ReservationService.instance.getReservationCount(passport));
            } catch (Exception e) {
                this.addElementToData("reserveCount", 0);
            }
            try {
                spu = new SplitPageUtil(1, 2);
                SearchReportParam rsp = new SearchReportParam(false);
                rsp.setRequest_org_id(passport.getOrgId());//100052
                rsp.setRequest_user_id(passport.getUserId());//500518
                rsp.setSpu(spu);
                rsp.setViewed(-1);
                ReportService.instance.searchThisOrgReportByMeRequest(passport, rsp);
                this.addElementToData("unviewed_report_count", spu.getTotalRow());
            } catch (Exception e) {
                this.addElementToData("unviewed_report_count", 0);
            }
        } catch (Exception e) {
            super.catchException(e);
        }
        return SUCCESS;
    }
}
