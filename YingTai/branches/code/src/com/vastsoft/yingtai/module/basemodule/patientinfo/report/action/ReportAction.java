package com.vastsoft.yingtai.module.basemodule.patientinfo.report.action;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.TimeLimit;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportModifyRequestParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportShareParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportModifyRequestStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportMsgStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportShareStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.*;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.exception.ReportException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.service.ReportService;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestClass;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.org.configs.service.OrgConfigService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.entity.TDoctorUser;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;
import com.vastsoft.yingtai.utils.attributeUtils.AttributeUtils;

import java.util.Date;
import java.util.List;

public class ReportAction extends BaseYingTaiAction {
    private long mrrId;
    private Long reportId, reportShareId, requestOrgId, deviceTypeId, publish_user_id, diagnosis_org_id;
    private String reason, case_his_num, answer, content, case_symptom, shareOrgName;
    private TDiagnosisHandle diagnosisHandle;
    private SplitPageUtil spu;
    private ReportModifyRequestStatus modifyReportRequestStatus;
    private TimeLimit timelimit;
    private ReportShareStatus reportShareStatus;
    private String patient_name;
    private Gender patient_gender;
    private int print_type;
    private String sick_key_words;

    public static void main(String[] args) {
        System.out.println("CT".hashCode());
    }

    /**
     * 查找当前机构申请产生的报告
     */
    public String searchThisOrgReportByOrgRequest() {
        try {
            Passport passport = takePassport();
            SearchReportParam rsp = new SearchReportParam(false);
            rsp.setCase_his_num(case_his_num);
            rsp.setPatient_name(patient_name);
            rsp.setPatient_gender(patient_gender);
            rsp.setDicom_img_device_type_id(deviceTypeId);
            rsp.setDiagnosis_org_id(diagnosis_org_id);
            rsp.setTimeLimit(timelimit);
            rsp.setRequest_org_id(passport.getOrgId());
            rsp.setSick_key_words(sick_key_words);
            rsp.setSpu(spu);
            List<FReport> listReport = ReportService.instance.searchReport(rsp, false);
            String[] attrs = {"id", "diagnosis_id", "case_his_num", "patient_name", "patient_genderStr",
                    "device_type_name", "diagnosis_org_name", "request_user_name", "create_time", "print_user_name",
                    "pic_opinions", "pic_conclusions"};
            addElementToData("listReport", AttributeUtils.SerializeList(listReport, attrs));
            addElementToData("spu", rsp.getSpu());
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 查询我申请的报告
     */
    public String searchThisOrgReportByMeRequest() {
        try {
            Passport passport = takePassport();
            SearchReportParam rsp = new SearchReportParam(false);
            rsp.setCase_his_num(case_his_num);
            rsp.setPatient_name(patient_name);
            rsp.setPatient_gender(patient_gender);
            rsp.setDicom_img_device_type_id(deviceTypeId);
            rsp.setDiagnosis_org_id(diagnosis_org_id);
            rsp.setTimeLimit(timelimit);
            rsp.setRequest_org_id(passport.getOrgId());
            rsp.setRequest_user_id(passport.getUserId());
            rsp.setSick_key_words(sick_key_words);
            rsp.setSpu(spu);
            List<FReport> listReport = ReportService.instance.searchThisOrgReportByMeRequest(passport, rsp);
            if (!CollectionTools.isEmpty(listReport)) {
                SplitPageUtil sspu = new SplitPageUtil(1, 2);
                for (FReport report : listReport) {
                    ReportService.instance.searchReportMsg(null, report.getId(), null, null, passport.getOrgId(),
                            passport.getUserId(), ReportMsgStatus.UNREAD, sspu);
                    report.setReport_msg_count((int) sspu.getTotalRow());
                }
            }
            String[] attrs = {"id", "diagnosis_id", "case_his_num", "patient_name", "patient_genderStr",
                    "device_type_name", "diagnosis_org_name", "request_user_name", "create_time", "print_user_name",
                    "pic_opinions", "pic_conclusions", "request_urgent_level_name"};
            addElementToData("listReport", AttributeUtils.SerializeList(listReport, attrs));
            addElementToData("spu", rsp.getSpu());
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc("分享报告")
    public String shareReport() {
        try {
            Passport passport = takePassport();
            TReportShare reportShare = ReportService.instance.shareReport(passport, reportId);
            addElementToData("reportShare", reportShare);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("获取所有报告分享")
    public String queryReportShareList() {
        try {
            SearchReportShareParam srsp = new SearchReportShareParam();
            srsp.setShare_org_name(shareOrgName);
            srsp.setCase_symptom(case_symptom);
            srsp.setStartAndEnd(super.getStart(), super.getEnd());
            srsp.setStatus(ReportShareStatus.IS_SHARE);
            srsp.setSpu(spu);
            List<FReportShare> listReportShare = ReportService.instance.searchReportShare(srsp);
            addElementToData("listReportShare", listReportShare);
            addElementToData("spu", srsp.getSpu());
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 获取本机构的报告分享
     */
    public String queryReportShareByThisOrg() {
        try {
            SearchReportShareParam srsp = new SearchReportShareParam();
            srsp.setShare_org_id(takePassport().getOrgId());
            srsp.setCase_his_num(case_his_num);
            srsp.setCase_symptom(case_symptom);
            srsp.setStartAndEnd(super.getStart(), super.getEnd());
            srsp.setStatus(reportShareStatus);
            srsp.setSpu(spu);
            List<FReportShare> listReportShare = ReportService.instance.searchReportShare(srsp);
            addElementToData("listReportShare", listReportShare);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc(value = "取消报告分享", grade = 4)
    public String cancelReportShare() {
        try {
            ReportService.instance.cancelReportShare(takePassport(), reportId);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("获取申请分享详细信息")
    public String queryReportShareDetail() {
        try {
            Passport passport = takePassport();
            FReportShare reportShare = ReportService.instance.selectReportShareById(reportShareId);
            FReport report = ReportService.instance.queryReportById(reportShare.getReport_id());
            // TCaseHistory medicalHis =
            // CaseHistoryService.instance.queryCaseHistoryById(report.getCase_his_id());
            if (spu == null)
                spu = new SplitPageUtil(1, 10);
            List<FReportShareSpeech> listSpeech = ReportService.instance.searchReportShareSpeech(passport,
                    reportShareId, spu);
            addElementToData("reportShare", reportShare);
            addElementToData("report", report);
            // addElementToData("medicalHis", medicalHis);
            addElementToData("listSpeech", listSpeech);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * "通过申请ID查询分享发言"
     */
    public String queryReportShareSpeechByShareId() {
        try {
            Passport passport = takePassport();
            if (spu == null)
                spu = new SplitPageUtil(1, 10);
            List<FReportShareSpeech> listSpeech = ReportService.instance.searchReportShareSpeech(passport,
                    reportShareId, spu);
            addElementToData("listSpeech", listSpeech);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc(value = "打印报告", grade = 5)
    public String printReport() {
        try {
            ReportService.instance.printDiagnosisReport(takePassport(), reportId, print_type);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc("添加分享发言")
    public String addReportShareSpeech() {
        try {
            TReportShareSpeech speech = ReportService.instance.addReportShareSpeech(takePassport(), reportShareId,
                    content);
            addElementToData("speech", speech);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 通过诊断ID查询聊天记录
     */
    public String queryReportMsgByReportId() {
        try {
            Passport passport = takePassport();
            List<FReportMsg> listReportMsg = ReportService.instance.readDiagnosisMsgByReportId(passport, reportId);
            addElementToData("listReportMsg", listReportMsg);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc("发送诊断信息")
    public String sendReportMsg() {
        try {
            Passport passport = takePassport();
            FReportMsg reportMsg = ReportService.instance.sendDisgnosisMsg(passport, reportId, content);
            addElementToData("reportMsg", reportMsg);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 通过诊断ID查询报告，以及历史诊断
     */
    public String queryReportDetailById() {
        try {
            FReport report = ReportService.instance.queryReportById(reportId);
            if (report == null)
                throw new ReportException("指定的报告未找到！");
            addElementToData("report", report);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 通过诊断ID预览报告，以及历史诊断
     */
    public String previewYuanzhenReportDetailById() {
        try {
            FReport report = ReportService.instance.queryReportById(reportId);
            if (report == null)
                throw new ReportException("指定的报告未找到！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("指定的报告不是来自于远诊系统！");
            super.addElementToData("report", report);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 通过诊断ID查询报告，以及历史诊断
     */
    public String queryYuanzhenReportDetailById() {
        try {
            FReport report = ReportService.instance.queryReportById(reportId);
            if (report == null)
                throw new ReportException("指定的报告未找到！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("指定的报告不是来自于远诊系统！");
            addElementToData("report", report);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 通过诊断ID查询报告，以及历史诊断
     */
    public String queryYuanzhenReportDetailByIdPrePrint() {
        try {
            Passport passport = takePassport();
            FReport report = ReportService.instance.queryReportById(reportId);
            if (report == null)
                throw new ReportException("指定的报告未找到！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("指定的报告不是来自于远诊系统！");
            if (print_type != 1 && print_type != 2)
                throw new ReportException("必须指定有效的打印类型！");
            switch (print_type) {
                case 1:
                    break;
                case 2:
                    if (report.getRequest_class() != RequestClass.CONSULT.getCode())
                        break;
                    if (report.getDuty_org_id() > 0 && report.getDuty_report_user_id() > 0
                            && report.getDuty_verify_user_id() > 0)
                        break;
                    report.setDuty_org_id(passport.getOrgId());
                    report.setDuty_org_name(passport.getOrgName());
                    report.setDuty_report_user_id(passport.getUserId());
                    report.setDuty_report_user_name(passport.getUserName());
                    report.setDuty_verify_user_id(passport.getUserId());
                    report.setDuty_verify_user_name(passport.getUserName());
                    if (UserType.ORG_DOCTOR.equals(passport.getUserType())) {
                        TDoctorUser me = (TDoctorUser) UserService.instance.queryOnlineUserById(passport);
                        report.setDuty_verify_user_sign_file_id(me.getSign_file_id());
                    }
                    break;
            }
            report.setDevice_type_name(OrgConfigService.instance.takeDeviceNameReportMapperByOrgIdAndDeviceTypeName(passport.getOrgId(), report.getDevice_type_name()));
            addElementToData("report", report);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc("下级机构查看报告")
    public String viewReport() {
        try {
            TReport report = ReportService.instance.viewReport(takePassport(), reportId);
            addElementToData("report", report);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 搜索我的诊断报告
     */
    public String searchMyReport() {
        try {
            Passport passport = takePassport();
            SearchReportParam dsp = new SearchReportParam(false);
            dsp.setDiagnosis_org_id(passport.getOrgId());
            dsp.setPublish_user_id(passport.getUserId());
            dsp.setCase_his_num(case_his_num);
            dsp.setRequest_org_id(requestOrgId);
            dsp.setDicom_img_device_type_id(deviceTypeId);
            dsp.setTimeLimit(timelimit);
            dsp.setSpu(spu);
            List<FReport> listReport = ReportService.instance.searchReport(dsp, false);
            if (!CollectionTools.isEmpty(listReport)) {
                SplitPageUtil sspu = new SplitPageUtil(1, 2);
                for (FReport report : listReport) {
                    ReportService.instance.searchReportMsg(null, report.getId(), null, null, passport.getOrgId(),
                            passport.getUserId(), ReportMsgStatus.UNREAD, sspu);
                    report.setReport_msg_count((int) sspu.getTotalRow());
                }
            }
            addElementToData("listReport", listReport);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("搜索机构诊断报告")
    public String searchOrgReport() {
        try {
            Passport passport = takePassport();
            SearchReportParam dsp = new SearchReportParam(false);
            dsp.setDiagnosis_org_id(passport.getOrgId());
            dsp.setCase_his_num(case_his_num);
            dsp.setRequest_org_id(requestOrgId);
            dsp.setDicom_img_device_type_id(deviceTypeId);
            dsp.setPublish_user_id(publish_user_id);
            dsp.setTimeLimit(timelimit);
            dsp.setSpu(spu);
            dsp.setSick_key_words(sick_key_words);
            List<FReport> listReport = ReportService.instance.searchReport(dsp, false);
            addElementToData("listReport", listReport);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    public String queryModifyReportCount() {
        try {
            Passport passport = takePassport();
            try {
                spu.setTotalRow(0);
                SearchReportModifyRequestParam rmrp = new SearchReportModifyRequestParam();
                rmrp.setRequest_org_id(passport.getOrgId());
                rmrp.setRequest_user_id(passport.getUserId());
                rmrp.setStatus(modifyReportRequestStatus);
                rmrp.setSpu(spu);
                ReportService.instance.searchModifyReportRequest(rmrp);
                addElementToData("modifyRequestCount", spu.getTotalRow());
            } catch (Exception e) {
                addElementToData("modifyRequestCount", 0);
            }
            try {
                spu.setTotalRow(0);
                SearchReportModifyRequestParam rmrp = new SearchReportModifyRequestParam();
                rmrp.setResponse_org_id(passport.getOrgId());
                rmrp.setResponse_user_id(passport.getUserId());
                rmrp.setStatus(ReportModifyRequestStatus.INIT_STATUS);
                rmrp.setSpu(spu);
                ReportService.instance.searchModifyReportRequest(rmrp);
                addElementToData("modifyConfirmCount", spu.getTotalRow());
            } catch (Exception e) {
                addElementToData("modifyConfirmCount", 0);
            }
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc("拒绝诊断方修改报告")
    public String rejestModifyRequest() {
        try {
            TModifyReportRequest mrr = ReportService.instance.confirmModifyReportRequest(takePassport(), mrrId, false,
                    answer);
            addElementToData("mrr", mrr);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc("同意诊断方修改报告")
    public String confirmModifyRequest() {
        try {
            TModifyReportRequest mrr = ReportService.instance.confirmModifyReportRequest(takePassport(), mrrId, true,
                    null);
            addElementToData("mrr", mrr);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 查询提交给我的修改报告申请
     */
    public String searchModifyReportRequestToMe() {
        try {
            Passport passport = takePassport();
            SearchReportModifyRequestParam rmrp = new SearchReportModifyRequestParam();
            rmrp.setResponse_org_id(passport.getOrgId());
            rmrp.setResponse_user_id(passport.getUserId());
            rmrp.setStatus(modifyReportRequestStatus);
            rmrp.setSpu(spu);
            List<FModifyReportRequest> listModifyReportRequest = ReportService.instance.searchModifyReportRequest(rmrp);
            addElementToData("listModifyReportRequest", listModifyReportRequest);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 查询我提交的修改报告申请
     */
    public String searchModifyReportRequestFromMe() {
        try {
            Passport passport = takePassport();
            SearchReportModifyRequestParam rmrp = new SearchReportModifyRequestParam();
            rmrp.setRequest_org_id(passport.getOrgId());
            rmrp.setRequest_user_id(passport.getUserId());
            rmrp.setStatus(modifyReportRequestStatus);
            rmrp.setSpu(spu);
            List<FModifyReportRequest> listModifyReportRequest = ReportService.instance.searchModifyReportRequest(rmrp);
            addElementToData("listModifyReportRequest", listModifyReportRequest);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 请求进入修改报告 ,准备数据
     */
    public String requestModifyReportById() {
        try {
            Passport passport = takePassport();
            boolean canDirectModifyReport = ReportService.instance.checkModifyReport(passport, reportId);
            if (!canDirectModifyReport)
                throw new ReportException("你不能直接修改报告！");
            FReport report = ReportService.instance.queryReportById(reportId);
            if (report == null)
                throw new ReportException("要修改的报告未找到！");
            if (report.getSource_type() != PatientDataSourceType.YUANZHEN_SYS.getCode())
                throw new ReportException("必须是远诊报告才可以修改！");
            addElementToData("report", report);
            // FDiagnosis fDiagnosis =
            // DiagnosisService2.instance.queryDiagnosisById(handle.getDiagnosis_id());
            // addElementToData("fDiagnosis", fDiagnosis);
            List<FDiagnosisHandle> listDiagnosisHandle = DiagnosisService2.instance
                    .queryDiagnosisHandleByDiagnosisId(report.getDiagnosis_id());
            addElementToData("listHandle", listDiagnosisHandle);
            List<FCaseHistory> listMedicalHis = CaseHistoryService.instance
                    .queryCaseHistoryByIds(report.getAbout_case_ids());
            addElementToData("listMedicalHis", listMedicalHis);
            TDicomImg dicomImg = DicomImgService.instance.queryDicomImgById(report.getDicom_img_id());
            addElementToData("dicomImg", dicomImg);
            TDiagnosisHandle handle = new TDiagnosisHandle();
            handle.setCan_tranfer(0);
            handle.setCreate_time(new Date());
            handle.setCurr_user_id(passport.getUserId());
            handle.setDiagnosis_id(report.getDiagnosis_id());
            handle.setF_o_m(report.getF_o_m());
            handle.setNext_user_id(0);
            handle.setOrg_id(passport.getOrgId());
            handle.setPic_conclusion(report.getPic_conclusion());
            handle.setPic_opinion(report.getPic_opinion());
            handle.setStatus(DiagnosisHandleStatus2.WRITING.getCode());
            addElementToData("handle", handle);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 检查是否可以直接修改报告
     */
    public String checkAcceptModifyReport() {
        try {
            boolean canModify = ReportService.instance.checkModifyReport(takePassport(), reportId);
            addElementToData("canModify", canModify);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc(value = "发起报告修改申请", grade = 5)
    public String requestModifyReport() {
        try {
            TModifyReportRequest mdr = ReportService.instance.requestModifyReport(takePassport(), reportId, reason);
            addElementToData("modifyReportRequest", mdr);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    @ActionDesc(value = "修改报告", grade = 5)
    public String modifyReport() {
        try {
            ReportService.instance.modifyReport(takePassport(), diagnosisHandle, reportId);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    public void setReportId(Long reportId) {
        this.reportId = filterParam(reportId);
    }

    public void setReason(String reason) {
        this.reason = filterParam(reason);
    }

    public void setDiagnosisHandle(TDiagnosisHandle diagnosisHandle) {
        this.diagnosisHandle = diagnosisHandle;
    }

    public void setSpu(SplitPageUtil spu) {
        this.spu = spu;
    }

    public void setMrrId(long mrrId) {
        this.mrrId = filterParam(mrrId);
    }

    public void setAnswer(String answer) {
        this.answer = filterParam(answer);
    }

    public void setModifyReportRequestStatus(int modifyReportRequestStatus) {
        this.modifyReportRequestStatus = ReportModifyRequestStatus.parseCode(modifyReportRequestStatus);
    }

    public void setRequestOrgId(Long requestOrgId) {
        this.requestOrgId = filterParam(requestOrgId);
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = filterParam(deviceTypeId);
    }

    public void setTimelimit(int timelimit) {
        this.timelimit = TimeLimit.parseCode(timelimit);
    }

    public void setCase_his_num(String case_his_num) {
        this.case_his_num = filterParam(case_his_num);
    }

    public void setPublish_user_id(Long publish_user_id) {
        this.publish_user_id = filterParam(publish_user_id);
    }

    public void setContent(String content) {
        this.content = filterParam(content);
    }

    public void setReportShareId(Long reportShareId) {
        this.reportShareId = filterParam(reportShareId);
    }

    public void setSymptom(String symptom) {
        this.case_symptom = filterParam(symptom);
    }

    public void setReportShareStatus(int reportShareStatus) {
        this.reportShareStatus = ReportShareStatus.parseCode(reportShareStatus);
    }

    public void setShareOrgName(String shareOrgName) {
        this.shareOrgName = filterParam(shareOrgName);
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = filterParam(patient_name);
    }

    public void setPatient_gender(int patient_gender) {
        this.patient_gender = Gender.parseCode(patient_gender);
    }

    public void setDiagnosis_org_id(Long diagnosis_org_id) {
        this.diagnosis_org_id = filterParam(diagnosis_org_id);
    }

    public void setCase_symptom(String case_symptom) {
        this.case_symptom = filterParam(case_symptom);
    }

    public void setPrint_type(int print_type) {
        this.print_type = print_type;
    }

    public void setSick_key_words(String sick_key_words) {
        this.sick_key_words = filterParam(sick_key_words);
    }
}
