package com.vastsoft.yingtai.module.stat.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.*;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportFomType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReport;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.service.ReportService;
import com.vastsoft.yingtai.module.financel.constants.AccountRecordRandEType;
import com.vastsoft.yingtai.module.financel.service.FinanceService;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.stat.assist.SearchStatParam;
import com.vastsoft.yingtai.module.stat.constants.StatFullType;
import com.vastsoft.yingtai.module.stat.constants.StatSubType;
import com.vastsoft.yingtai.module.stat.entity.*;
import com.vastsoft.yingtai.module.stat.exception.StatException;
import com.vastsoft.yingtai.module.stat.service.StatService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.attributeUtils.arrayAttrsUtils.ArrayAttrsUtils;
import com.vastsoft.yingtai.utils.poi.excel.exception.ExcelException;
import org.apache.struts2.components.Number;

public class StatAction extends BaseYingTaiAction {
    private SplitPageUtil spu;
    private Integer iType;
    private String medicalHisNum, requestOrgName;
    private String vertifyUserName, acceptUserName;
    private String requestUserName, publishOrgName;
    private String diagnosisOrgName, sickName;
    private Long partTypeId, deviceTypeId, doctorId;
    private int mode = 0;
    private Long request_org_id;
    private Long diagnosis_org_id;
    private String sick_key_words;
    private ReportFomType fom;
    private OrgProductChargeType charge_type;

    /**
     * 申请机构统计病人信息
     */
    public String statPatientInfoForRequestOrg() {
        try {
            Passport passport = takePassport();
            SearchReportParam rsp = new SearchReportParam(false);
            rsp.setRequest_org_id(passport.getOrgId());
            rsp.setDiagnosis_org_id(diagnosis_org_id);
            rsp.setSick_key_words(sick_key_words);
            rsp.setStart(super.getStart());
            rsp.setEnd(super.getEnd());
            rsp.setSpu(spu);
            List<FReport> listReport = ReportService.instance.searchReport(rsp, false);
            addElementToData("listReport", listReport);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**
     * 统计病人信息
     */
    public String statPatientInfoForDiagnosisOrg() {
        try {
            Passport passport = takePassport();
            SearchReportParam rsp = new SearchReportParam(false);
            rsp.setDiagnosis_org_id(passport.getOrgId());
            rsp.setRequest_org_id(request_org_id);
            rsp.setSick_key_words(sick_key_words);
            rsp.setStart(super.getStart());
            rsp.setEnd(super.getEnd());
            rsp.setSpu(spu);
            List<FReport> listReport = ReportService.instance.searchReport(rsp, false);
            addElementToData("listReport", listReport);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("病症统计")
    public String statReportSick() {
        try {
            List<FStatReportSick> listReport = StatService.instance.statReportSick(takePassport(), mode, super.getStart(), super.getEnd(),
                    deviceTypeId, partTypeId, request_org_id, diagnosis_org_id, sickName);
            addElementToData("listReport", listReport);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    /**统计当前机构医生工作情况*/
    public String statDoctorWorkOfMyOrgDiagnosis(){
        try{
            Passport passport = super.takePassport();
            if (passport.getOrgId() == null || passport.getOrgId() <=0)
                throw new StatException("你没有进入一个机构！");
            SearchStatParam ssp = new SearchStatParam();
            ssp.setRequest_org_id(this.request_org_id);
            ssp.setDiagnosis_org_id(passport.getOrgId());
            ssp.setDicom_img_device_type_id(this.deviceTypeId);
            ssp.setDoctor_id(doctorId);
            ssp.setStart(super.getStart());
            ssp.setEnd(super.getEnd());
            ssp.setWork_type(this.iType);
            List<Map<String,Object>> listReport = StatService.instance.statReportDoctorWorkOfDiagnosisByOrgId(passport.getOrgId(),ssp);
            super.addElementToData("listReport",listReport);
//            super.addElementToData("footing",StatService.instance.statReportDoctorWorkOfDiagnosisByOrgIdFooting(passport.getOrgId(),ssp));
        }catch (Exception e){
            super.catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("医生工作统计")
    public String statReportDoctor() {
        try {

            List<FStatReportDoctor> listReport = StatService.instance.statReportDoctor(takePassport(), mode,
                    request_org_id, diagnosis_org_id, super.getStart(), super.getEnd(), deviceTypeId, doctorId, iType);
            addElementToData("listReport", listReport);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("当前机构诊断的报告花费统计")
    public String statReportByMyOrgDiagnosis() {
        try {
            Passport passport = takePassport();
            SearchStatParam ssp = new SearchStatParam();
            ssp.setRequest_org_id(this.request_org_id);
            ssp.setDicom_img_device_type_id(deviceTypeId);
            ssp.setSource_type(PatientDataSourceType.YUANZHEN_SYS);
            ssp.setCharge_type(this.charge_type);
            ssp.setStart(super.getStart());
            ssp.setEnd(super.getEnd());
            ssp.setSpu(spu);
            List<Map<String,Object>> listReport = StatService.instance.statReportByMyOrgDiagnosis(passport.getOrgId(), ssp);
            addElementToData("listReport", listReport);
            addElementToData("footing", StatService.instance.statReportFooting(passport.getOrgId(),ssp));
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("设备报告统计")
    public String statDeviceReport() {
        try {
            List<FStatDeviceReport> listReport = StatService.instance.statDeviceReport(takePassport(), mode,
                    request_org_id, diagnosis_org_id, super.getStart(), super.getEnd(), deviceTypeId, partTypeId);
            addElementToData("listReport", listReport);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("机构阴阳性统计")
    public String statReportOrgFom() {
        try {
            List<FStatOrgFomData> listReport = StatService.instance.statReportOrgFom(takePassport(), mode, super.getStart(), super.getEnd(),
                    deviceTypeId, partTypeId, request_org_id, diagnosis_org_id);
            addElementToData("listReport", listReport);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("统计报告")
    public String statReport() {
        try {
            SearchReportParam rsp = new SearchReportParam(false);
            rsp.setCase_his_num(medicalHisNum);
            rsp.setRequest_org_name(requestOrgName);
            rsp.setDiagnosis_org_name(diagnosisOrgName);
            rsp.setPublish_report_org_name(publishOrgName);
            rsp.setDicom_img_device_type_id(deviceTypeId);
            rsp.setRequest_user_name(requestUserName);
            rsp.setPublish_report_user_name(vertifyUserName);
            rsp.setF_o_m(fom);
            rsp.setStart(super.getStart());
            rsp.setEnd(super.getEnd());
            rsp.setSpu(spu);
            List<FReport> listReport = ReportService.instance.searchReport(rsp, false);
            addElementToData("listReport", listReport);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("医生诊断统计")
    public String statDiagnosisDoctor() {
        try {
            List<FStatDiagnosisDoctor> listSdd = StatService.instance.statDiagnosisDoctor(takePassport(), spu);
            addElementToData("listSdd", listSdd);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("统计设备和部位的数量情况")
    public String statDevicePartDiagnosis() {
        try {
            List<FStatDevicePartDiagnosis> listSdpd = StatService.instance.statDevicePartDiagnosis(takePassport(), spu);
            addElementToData("listSdpd", listSdpd);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("统计每个机构的病例个数")
    public String statPerOrgMedicalHis() {
        try {
            List<FStatOrgMedicalHis> listSomh = StatService.instance.statPerOrgMedicalHis(takePassport(), spu);
            addElementToData("listSomh", listSomh);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("搜索系统账户流水")
    public String searchSysAccountFlowing() {
        try {
            Passport passport = takePassport();
            List<FFinanceRecord> listFinanceRecord = StatService.instance.searchOrgAccountFlowing(passport,
                    request_org_id, diagnosis_org_id, iType == null ? null : AccountRecordRandEType.parseCode(iType),
                    FinanceService.SYS_ACCOUNT_ORG_ID, null, super.getStart(), super.getEnd(), spu);
            addElementToData("listFinanceRecord", listFinanceRecord);
            addElementToData("spu", spu);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("根据类型对本机构进行统计")
    public String orgStatByType() {
        try {
            Passport passport = takePassport();
            StatSubType subType = iType == null ? StatSubType.takeDefaultSubType() : StatSubType.parseCode(iType);
            List<FStatEntity> listStatmh = StatService.instance.orgMedicalHisStatByType(passport, subType, false);
            List<FStatEntity> listRequest = StatService.instance.orgRequestStatByType(passport, subType, false);
            List<FStatEntity> listOrgUser = StatService.instance.orgOrgUserStatByType(passport, subType, false);
            List<FStatEntity> listDiagnosis = StatService.instance.orgDiagnosisStatByType(passport, subType, false);
            addStatEntityList("listStatmh", listStatmh, subType);
            addStatEntityList("listRequest", listRequest, subType);
            addStatEntityList("listOrgUser", listOrgUser, subType);
            addStatEntityList("listDiagnosis", listDiagnosis, subType);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    // @ActionDesc("根据类型对系统进行统计")
    public String sysStatByType() {
        try {
            Passport passport = takePassport();
            StatSubType subType = iType == null ? StatSubType.takeDefaultSubType() : StatSubType.parseCode(iType);
            List<FStatEntity> listStatmh = StatService.instance.orgMedicalHisStatByType(passport, subType, true);
            List<FStatEntity> listRequest = StatService.instance.orgRequestStatByType(passport, subType, true);
            List<FStatEntity> listOrgUser = StatService.instance.orgOrgUserStatByType(passport, subType, true);
            List<FStatEntity> listDiagnosis = StatService.instance.orgDiagnosisStatByType(passport, subType, true);
            addStatEntityList("listStatmh", listStatmh, subType);
            addStatEntityList("listRequest", listRequest, subType);
            addStatEntityList("listOrgUser", listOrgUser, subType);
            addStatEntityList("listDiagnosis", listDiagnosis, subType);
        } catch (Exception e) {
            catchException(e);
        }
        return SUCCESS;
    }

    private void addStatEntityList(String keyName, List<FStatEntity> listStat, StatSubType subType)
            throws StatException {
        if (subType.getFullType().equals(StatFullType.TIME_STATTYPE)) {
            String[] attrs = null;
            if (subType.equals(StatSubType.YEAR_SUBTYPE)) {
                attrs = new String[]{"year>label", "value"};
            } else if (subType.equals(StatSubType.MONTH_SUBTYPE)) {
                attrs = new String[]{"month>label", "value"};
            } else if (subType.equals(StatSubType.DAY_SUBTYPE)) {
                attrs = new String[]{"day>label", "value"};
            } else {
                throw new StatException("无效的统计类型");
            }
            this.addElementToData(keyName, ArrayAttrsUtils.SerializeSingleArrayToList(listStat, attrs));
        }
    }

    public void setType(Integer iType) {
        this.iType = filterParam(iType);
    }

    public void setSpu(SplitPageUtil spu) {
        this.spu = spu;
    }

    public void setStart(String start) {
        Date dd = DateTools.strToDate(start);
        if (dd != null)
            super.setStart(DateTools.getStartTimeByDay(dd));
        else
            super.setStart(null);
    }

    public void setEnd(String end) {
        Date dd = DateTools.strToDate(end);
        if (dd != null)
            super.setEnd(DateTools.getEndTimeByDay(dd));
        else
            super.setEnd(null);
    }

    public void setMedicalHisNum(String medicalHisNum) {
        this.medicalHisNum = filterParam(medicalHisNum);
    }

    public void setRequestOrgName(String requestOrgName) {
        this.requestOrgName = filterParam(requestOrgName);
    }

    public void setVertifyUserName(String vertifyUserName) {
        this.vertifyUserName = filterParam(vertifyUserName);
    }

    public void setAcceptUserName(String acceptUserName) {
        this.acceptUserName = filterParam(acceptUserName);
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = filterParam(requestUserName);
    }

    public void setPartTypeId(Long partTypeId) {
        this.partTypeId = filterParam(partTypeId);
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = filterParam(deviceTypeId);
    }

    public void setPublishOrgName(String publishOrgName) {
        this.publishOrgName = filterParam(publishOrgName);
    }

    public void setDiagnosisOrgName(String diagnosisOrgName) {
        this.diagnosisOrgName = filterParam(diagnosisOrgName);
    }

    public void setFom(int fom) {
        this.fom = ReportFomType.parseCode(fom);
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = filterParam(doctorId);
    }

    public void setSickName(String sickName) {
        this.sickName = filterParam(sickName);
    }

    public void setRequestOrgId(Long requestOrgId) {
        this.request_org_id = filterParam(requestOrgId);
    }

    public void setDiagnosisOrgId(Long diagnosisOrgId) {
        this.diagnosis_org_id = filterParam(diagnosisOrgId);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setRequest_org_id(Long request_org_id) {
        this.request_org_id = filterParam(request_org_id);
    }

    public void setDiagnosis_org_id(Long diagnosis_org_id) {
        this.diagnosis_org_id = filterParam(diagnosis_org_id);
    }

    public void setSick_key_words(String sick_key_words) {
        this.sick_key_words = filterParam(sick_key_words);
    }

    public void setCharge_type(int charge_type) {
        this.charge_type = OrgProductChargeType.parseCode(charge_type);
    }
}
