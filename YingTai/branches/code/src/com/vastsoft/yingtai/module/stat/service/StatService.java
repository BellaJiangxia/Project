package com.vastsoft.yingtai.module.stat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.assist.SearchReportParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReport;
import com.vastsoft.yingtai.module.stat.assist.SearchStatParam;
import com.vastsoft.yingtai.module.stat.entity.*;
import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.financel.constants.AccountRecordRandEType;
import com.vastsoft.yingtai.module.financel.constants.AccountRecordType;
import com.vastsoft.yingtai.module.financel.service.FinanceService;
import com.vastsoft.yingtai.module.stat.constants.StatFullType;
import com.vastsoft.yingtai.module.stat.constants.StatSubType;
import com.vastsoft.yingtai.module.stat.exception.StatException;
import com.vastsoft.yingtai.module.stat.mapper.StatMapper;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

/**
 * @author jben
 */
public class StatService {
    public static final StatService instance = new StatService();

    /**
     * 获取本机构的病例总数
     *
     * @throws BaseException
     */
    public int takeOrgMedicalHisTotal(Passport passport) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR)) {
                throw new StatException("你没有机构管理员权限！");
            }
            StatMapper mapper = session.getMapper(StatMapper.class);
            return mapper.queryOrgCaseHistoryTotal(passport.getOrgId());
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 通过类型对本机构病例进行统计，病例统计只支持时间统计
     *
     * @throws BaseException
     */
    public List<FStatEntity> orgMedicalHisStatByType(Passport passport, StatSubType statSubType, boolean isSys) throws BaseException {
        if (!statSubType.getFullType().equals(StatFullType.TIME_STATTYPE)) {
            throw new StatException("不支持的统计类型！");
        }
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            if (!isSys) mapArg.put("org_id", passport.getOrgId());
            List<FStatEntity> listMh = null;
            Date currDate = null;
            Date endDate = null;
            if (statSubType.equals(StatSubType.YEAR_SUBTYPE)) {
                Date[] yearsType = DateTools.getLastYear(5);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < yearsType.length; i++) {
                    currDate = yearsType[i];
                    if (i == yearsType.length - 1)
                        endDate = DateTools.getLastTimeByYear(currDate);
                    else endDate = yearsType[i + 1];
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryCaseHistoryCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else if (statSubType.equals(StatSubType.MONTH_SUBTYPE)) {
                Date[] monthsYear = DateTools.getLast12Months();
                if (monthsYear != null && monthsYear.length > 0) {
                    listMh = new ArrayList<FStatEntity>();
                    for (int i = 0; i < monthsYear.length; i++) {
                        currDate = monthsYear[i];
                        if (i == monthsYear.length - 1) {
                            endDate = DateTools.getLastDayOfMonth(currDate);
                        } else endDate = monthsYear[i + 1];
                        mapArg.put("start", DateTools.dateToString(currDate));
                        mapArg.put("end", DateTools.dateToString(endDate));
                        int iValue = mapper.queryCaseHistoryCountByArg(mapArg);
                        FStatEntity fsmh = new FStatEntity();
                        fsmh.setDate(currDate);
                        fsmh.setValue(iValue);
                        listMh.add(fsmh);
                    }
                }
            } else if (statSubType.equals(StatSubType.DAY_SUBTYPE)) {
                List<Date> days = DateTools.getLastDays(10);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < days.size(); i++) {
                    currDate = days.get(i);
                    if (i == days.size() - 1)
                        endDate = DateTools.getLastTimeByDay(currDate);
                    else endDate = days.get(i + 1);
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryCaseHistoryCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else {
                throw new StatException("不支持的统计类型！");
            }
            return listMh;
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 通过类型对本机构申请进行统计，申请统计只支持时间统计
     *
     * @throws BaseException
     */
    public List<FStatEntity> orgRequestStatByType(Passport passport, StatSubType statSubType, boolean isSys) throws BaseException {
        if (!statSubType.getFullType().equals(StatFullType.TIME_STATTYPE)) {
            throw new StatException("不支持的统计类型！");
        }
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            if (!isSys) mapArg.put("org_id", passport.getOrgId());
            List<FStatEntity> listMh = null;
            Date currDate = null;
            Date endDate = null;
            if (statSubType.equals(StatSubType.YEAR_SUBTYPE)) {
                Date[] yearsType = DateTools.getLastYear(5);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < yearsType.length; i++) {
                    currDate = yearsType[i];
                    if (i == yearsType.length - 1)
                        endDate = DateTools.getLastTimeByYear(currDate);
                    else endDate = yearsType[i + 1];
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryRequestCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else if (statSubType.equals(StatSubType.MONTH_SUBTYPE)) {
                Date[] monthsYear = DateTools.getLast12Months();
                if (monthsYear != null && monthsYear.length > 0) {
                    listMh = new ArrayList<FStatEntity>();
                    for (int i = 0; i < monthsYear.length; i++) {
                        currDate = monthsYear[i];
                        if (i == monthsYear.length - 1) {
                            endDate = DateTools.getLastDayOfMonth(currDate);
                        } else endDate = monthsYear[i + 1];
                        mapArg.put("start", DateTools.dateToString(currDate));
                        mapArg.put("end", DateTools.dateToString(endDate));
                        int iValue = mapper.queryRequestCountByArg(mapArg);
                        FStatEntity fsmh = new FStatEntity();
                        fsmh.setDate(currDate);
                        fsmh.setValue(iValue);
                        listMh.add(fsmh);
                    }
                }
            } else if (statSubType.equals(StatSubType.DAY_SUBTYPE)) {
                List<Date> days = DateTools.getLastDays(10);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < days.size(); i++) {
                    currDate = days.get(i);
                    if (i == days.size() - 1)
                        endDate = DateTools.getLastTimeByDay(currDate);
                    else endDate = days.get(i + 1);
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryRequestCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else {
                throw new StatException("不支持的统计类型！");
            }
            return listMh;
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 通过类型对本机构新增人员进行统计，新增人员统计只支持时间统计
     *
     * @throws BaseException
     */
    public List<FStatEntity> orgOrgUserStatByType(Passport passport, StatSubType statSubType, boolean isSys) throws BaseException {
        if (!statSubType.getFullType().equals(StatFullType.TIME_STATTYPE)) {
            throw new StatException("不支持的统计类型！");
        }
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            if (!isSys) mapArg.put("org_id", passport.getOrgId());
            List<FStatEntity> listMh = null;
            Date currDate = null;
            Date endDate = null;
            if (statSubType.equals(StatSubType.YEAR_SUBTYPE)) {
                Date[] yearsType = DateTools.getLastYear(5);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < yearsType.length; i++) {
                    currDate = yearsType[i];
                    if (i == yearsType.length - 1)
                        endDate = DateTools.getLastTimeByYear(currDate);
                    else endDate = yearsType[i + 1];
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryOrgUserCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else if (statSubType.equals(StatSubType.MONTH_SUBTYPE)) {
                Date[] monthsYear = DateTools.getLast12Months();
                if (monthsYear != null && monthsYear.length > 0) {
                    listMh = new ArrayList<FStatEntity>();
                    for (int i = 0; i < monthsYear.length; i++) {
                        currDate = monthsYear[i];
                        if (i == monthsYear.length - 1) {
                            endDate = DateTools.getLastDayOfMonth(currDate);
                        } else endDate = monthsYear[i + 1];
                        mapArg.put("start", DateTools.dateToString(currDate));
                        mapArg.put("end", DateTools.dateToString(endDate));
                        int iValue = mapper.queryOrgUserCountByArg(mapArg);
                        FStatEntity fsmh = new FStatEntity();
                        fsmh.setDate(currDate);
                        fsmh.setValue(iValue);
                        listMh.add(fsmh);
                    }
                }
            } else if (statSubType.equals(StatSubType.DAY_SUBTYPE)) {
                List<Date> days = DateTools.getLastDays(10);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < days.size(); i++) {
                    currDate = days.get(i);
                    if (i == days.size() - 1)
                        endDate = DateTools.getLastTimeByDay(currDate);
                    else endDate = days.get(i + 1);
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryOrgUserCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else {
                throw new StatException("不支持的统计类型！");
            }
            return listMh;
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FStatEntity> orgDiagnosisStatByType(Passport passport, StatSubType statSubType, boolean isSys) throws BaseException {
        if (!statSubType.getFullType().equals(StatFullType.TIME_STATTYPE)) {
            throw new StatException("不支持的统计类型！");
        }
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            if (!isSys) mapArg.put("org_id", passport.getOrgId());
            List<FStatEntity> listMh = null;
            Date currDate = null;
            Date endDate = null;
            if (statSubType.equals(StatSubType.YEAR_SUBTYPE)) {
                Date[] yearsType = DateTools.getLastYear(5);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < yearsType.length; i++) {
                    currDate = yearsType[i];
                    if (i == yearsType.length - 1)
                        endDate = DateTools.getLastTimeByYear(currDate);
                    else endDate = yearsType[i + 1];
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryDiagnosisCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else if (statSubType.equals(StatSubType.MONTH_SUBTYPE)) {
                Date[] monthsYear = DateTools.getLast12Months();
                if (monthsYear != null && monthsYear.length > 0) {
                    listMh = new ArrayList<FStatEntity>();
                    for (int i = 0; i < monthsYear.length; i++) {
                        currDate = monthsYear[i];
                        if (i == monthsYear.length - 1) {
                            endDate = DateTools.getLastDayOfMonth(currDate);
                        } else endDate = monthsYear[i + 1];
                        mapArg.put("start", DateTools.dateToString(currDate));
                        mapArg.put("end", DateTools.dateToString(endDate));
                        int iValue = mapper.queryDiagnosisCountByArg(mapArg);
                        FStatEntity fsmh = new FStatEntity();
                        fsmh.setDate(currDate);
                        fsmh.setValue(iValue);
                        listMh.add(fsmh);
                    }
                }
            } else if (statSubType.equals(StatSubType.DAY_SUBTYPE)) {
                List<Date> days = DateTools.getLastDays(10);
                listMh = new ArrayList<FStatEntity>();
                for (int i = 0; i < days.size(); i++) {
                    currDate = days.get(i);
                    if (i == days.size() - 1)
                        endDate = DateTools.getLastTimeByDay(currDate);
                    else endDate = days.get(i + 1);
                    mapArg.put("start", DateTools.dateToString(currDate));
                    mapArg.put("end", DateTools.dateToString(endDate));
                    int iValue = mapper.queryDiagnosisCountByArg(mapArg);
                    FStatEntity fsmh = new FStatEntity();
                    fsmh.setDate(currDate);
                    fsmh.setValue(iValue);
                    listMh.add(fsmh);
                }
            } else {
                throw new StatException("不支持的统计类型！");
            }
            return listMh;
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 查询指定机构ID的账户流水
     *
     * @throws BaseException
     */
    public List<FFinanceRecord> searchOrgAccountFlowing(Passport passport, Long lRequestOrgId, Long lDiagnosisOrgId, AccountRecordRandEType reType, Long lOrgId, AccountRecordType art,
                                                        Date dtStart, Date dtEnd, SplitPageUtil spu) throws BaseException {
        if (passport.getUserType().getCode() != UserType.SUPER_ADMIN.getCode()) {
            if (passport.getUserType().getCode() == UserType.ADMIN.getCode()) {
                if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_SATA_MGR))
                    throw new NotPermissionException();
                if (lOrgId == null) {
                    lOrgId = FinanceService.SYS_ACCOUNT_ORG_ID;
                }
            } else if (passport.getUserType().getCode() == UserType.ORG_DOCTOR.getCode() ||
                    passport.getUserType().getCode() == UserType.ORG_GENERAL.getCode()) {
                if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR)) {
                    throw new NotPermissionException();
                } else {
                    if (!passport.getOrgId().equals(lOrgId)) {
                        throw new NotPermissionException("你没有权限查看不是当前登录机构的信息！");
                    }
                }
            } else {
                throw new NotPermissionException();
            }
        } else {
            if (lOrgId == null) {
                lOrgId = FinanceService.SYS_ACCOUNT_ORG_ID;
            }
        }
        List<AccountRecordRandEType> listReType = new ArrayList<AccountRecordRandEType>();
        if (reType == null) {
            listReType.add(AccountRecordRandEType.CASH_IN);
            listReType.add(AccountRecordRandEType.CASH_OUT);
        } else {
            listReType.add(reType);
        }
        return FinanceService.instance.searchAccountRecordList(passport, lRequestOrgId, lDiagnosisOrgId, lOrgId, listReType, null, dtStart, dtEnd, spu);
    }

    /**
     * 统计各个机构的病例数，支持分页
     *
     * @param passport
     * @param spu
     * @return
     * @throws BaseException
     */
    public List<FStatOrgMedicalHis> statPerOrgMedicalHis(Passport passport, SplitPageUtil spu) throws BaseException {
        if (!passport.getUserType().equals(UserType.SUPER_ADMIN) && !passport.getUserType().equals(UserType.ADMIN)) {
            throw new NotPermissionException("你无权执行此操作！");
        }
        if (!passport.getUserType().equals(UserType.SUPER_ADMIN)) {
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_SATA_MGR)) {
                throw new NotPermissionException("你没有病例管理权限！");
            }
        }
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            if (spu != null) {
                int iCount = mapper.statPerOrgCaseHistoryCount();
                spu.setTotalRow(iCount);
                if (iCount <= 0) return null;
                mapArg.put("minRow", spu.getCurrMinRowNum());
                mapArg.put("maxRow", spu.getCurrMaxRowNum());
            }
            return mapper.statPerOrgCaseHistory(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FStatDevicePartDiagnosis> statDevicePartDiagnosis(Passport passport, SplitPageUtil spu) throws BaseException {
        if (!passport.getUserType().equals(UserType.SUPER_ADMIN) && !passport.getUserType().equals(UserType.ADMIN)) {
            throw new NotPermissionException("你无权执行此操作！");
        }
        if (!passport.getUserType().equals(UserType.SUPER_ADMIN)) {
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_SATA_MGR)) {
                throw new NotPermissionException("你没有病例管理权限！");
            }
        }
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            if (spu != null) {
                int iCount = mapper.statDevicePartDiagnosisCount();
                spu.setTotalRow(iCount);
                if (iCount <= 0) return null;
                mapArg.put("minRow", spu.getCurrMinRowNum());
                mapArg.put("maxRow", spu.getCurrMaxRowNum());
            }
            return mapper.statDevicePartDiagnosis(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FStatDiagnosisDoctor> statDiagnosisDoctor(Passport passport, SplitPageUtil spu) throws BaseException {
        if (!passport.getUserType().equals(UserType.SUPER_ADMIN) && !passport.getUserType().equals(UserType.ADMIN)) {
            throw new NotPermissionException("你无权执行此操作！");
        }
        if (!passport.getUserType().equals(UserType.SUPER_ADMIN)) {
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_SATA_MGR)) {
                throw new NotPermissionException("你没有病例管理权限！");
            }
        }
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            if (spu != null) {
                int iCount = mapper.statDiagnosisDoctorCount();
                spu.setTotalRow(iCount);
                if (iCount <= 0) return null;
                mapArg.put("minRow", spu.getCurrMinRowNum());
                mapArg.put("maxRow", spu.getCurrMaxRowNum());
            }
            return mapper.statDiagnosisDoctor(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

//	public List<FReport> statReport(Passport passport, String medicalHisNum, String requestOrgName,
//			String diagnosisOrgName, String publishOrgName, Long deviceTypeId, String requestUserName,
//			String acceptUserName, String vertifyUserName, ReportFomType fom, Date dtStart, Date dtEnd,
//			SplitPageUtil spu) throws BaseException {
//		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_SATA_MGR))
//			throw new StatException("你没有统计管理权限！");
//		ReportSearchParam rsp = new ReportSearchParam();
//		rsp.setCase_his_num(medicalHisNum);
//		rsp.setRequest_org_name(requestOrgName);
//		rsp.setDiagnosis_org_name(diagnosisOrgName);
//		rsp.setPublish_report_org_name(publishOrgName);
//		rsp.setDicom_img_device_type_id(deviceTypeId);
//		rsp.setRequest_user_name(requestUserName);
//		rsp.set
//		
//		
//		SqlSession session = null;
//		try {
//			session = SessionFactory.getSession();
//			if (spu == null)
//				throw new StatException("统计报告必须分页！");
//			ReportMapper mapper = session.getMapper(ReportMapper.class);
//			Map<String, Object> mapArg = new HashMap<String, Object>();
//			mapArg.put("request_org_name", requestOrgName);
//			mapArg.put("request_user_name", requestUserName);
//			mapArg.put("accept_user_name", acceptUserName);
//			mapArg.put("verify_user_name", vertifyUserName);
//			mapArg.put("diagnosis_org_name", diagnosisOrgName);
//			mapArg.put("publish_org_name", publishOrgName);
//			mapArg.put("medicalHisNum", medicalHisNum);
//			mapArg.put("device_type_id", deviceTypeId);
//			mapArg.put("part_type_id", partTypeId);
//			mapArg.put("start", DateTools.dateToString(dtStart));
//			mapArg.put("end", DateTools.dateToString(dtEnd));
//			mapArg.put("fom", fom == null ? null : fom.getCode());
//			mapArg.put("status", DiagnosisStatus2.DIAGNOSISED.getCode());
//			int iCount = mapper.statReportCount(mapArg);
//			spu.setTotalRow(iCount);
//			if (iCount <= 0)
//				return null;
//			mapArg.put("minRow", spu.getCurrMinRowNum());
//			mapArg.put("maxRow", spu.getCurrMaxRowNum());
//			return mapper.statReportList(mapArg);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			SessionFactory.closeSession(session);
//		}
//	}

    /**
     * 机构阴阳性统计
     *
     * @param passport
     * @param mode           1标识诊断方，2标识申请方，其他后台方
     * @param dtStart
     * @param dtEnd
     * @param requestOrgId
     * @param diagnosisOrgId
     * @return
     * @throws BaseException
     */
    public List<FStatOrgFomData> statReportOrgFom(Passport passport, int mode, Date dtStart, Date dtEnd,
                                                  Long deviceTypeId, Long partTypeId, Long requestOrgId, Long diagnosisOrgId) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("request_org_id", requestOrgId);
            mapArg.put("diagnosis_org_id", diagnosisOrgId);
            this.installMapArg(mapArg, passport, mode);
            mapArg.put("device_type_id", deviceTypeId);
            mapArg.put("part_type_id", partTypeId);
            mapArg.put("start", DateTools.dateToString(dtStart));
            mapArg.put("end", DateTools.dateToString(dtEnd));
//			mapArg.put("status", DiagnosisStatus2.DIAGNOSISED.getCode());
            return mapper.statReportOrgFom(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    private void installMapArg(Map<String, Object> mapArg, Passport passport, int mode) throws StatException {
//		mapArg.put("mode", mode);
        switch (mode) {
            case 1:
                if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
                    throw new StatException("你没有机构管理员权限，不能执行本操作！");
                mapArg.put("diagnosis_org_id", passport.getOrgId());
                break;
            case 2:
                if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
                    throw new StatException("你没有机构管理员权限，不能执行本操作！");
                mapArg.put("request_org_id", passport.getOrgId());
                break;
            default:
                if (!passport.getUserType().equals(UserType.ADMIN) && !passport.getUserType().equals(UserType.SUPER_ADMIN))
                    throw new StatException("你没有系统管理员权限，不能执行本操作！");
                break;
        }
    }

    public List<FStatDeviceReport> statDeviceReport(Passport passport, int mode, Long requestOrgId, Long diagnosisOrgId,
                                                    Date dtStart, Date dtEnd, Long deviceTypeId, Long partTypeId) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("request_org_id", requestOrgId);
            mapArg.put("diagnosis_org_id", diagnosisOrgId);
            this.installMapArg(mapArg, passport, mode);
            mapArg.put("device_type_id", deviceTypeId);
            mapArg.put("part_type_id", partTypeId);
            mapArg.put("start", DateTools.dateToString(dtStart));
            mapArg.put("end", DateTools.dateToString(dtEnd));
            mapArg.put("status", DiagnosisStatus2.DIAGNOSISED.getCode());
            return mapper.statDeviceReport(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

//	public List<FReport> statReportCost(Passport passport,int mode,Long requestOrgId,Long diagnosisOrgId,
//			Date dtStart, Date dtEnd,Long deviceTypeId,Long partTypeId) throws BaseException {
//		SqlSession session=null;
//		try {
//			session = SessionFactory.getSession();
//			StatMapper mapper=session.getMapper(StatMapper.class);
//			Map<String, Object> mapArg=new HashMap<String,Object>();
//			mapArg.put("request_org_id", requestOrgId);
//			mapArg.put("diagnosis_org_id", diagnosisOrgId);
//			this.installMapArg(mapArg, passport, mode);
//			mapArg.put("device_type_id", deviceTypeId);
////			mapArg.put("part_type_id", partTypeId);
//			mapArg.put("start", DateTools.dateToString(dtStart));
//			mapArg.put("end", DateTools.dateToString(dtEnd));
//			mapArg.put("status", DiagnosisStatus2.DIAGNOSISED.getCode());
//			return mapper.statReportCost(mapArg);
//		} catch (Exception e) {
//			throw e;
//		}finally{
//			SessionFactory.closeSession(session);
//		}
//	}

    /**
     * 统计用户所在机构诊断的报告的医生工作情况
     *
     * @param diagnosisOrgId
     * @param ssp
     * @return
     * @throws BaseException
     */
    public List<Map<String,Object>> statReportDoctorWorkOfDiagnosisByOrgId(long diagnosisOrgId,SearchStatParam ssp) throws BaseException {
        ssp.setDiagnosis_org_id(diagnosisOrgId);
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = null;
            try {
                mapArg = ssp.buildMap();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new StatException(e);
            }
//            List<Map<String,Object>> dataResult;
            if (new Integer(2).equals(ssp.getWork_type())) {
                return mapper.selectStatDoctorWorkAuditReport(mapArg);
//                dataResult = mapper.statAuditDoctor(mapArg);
            } else {
                return mapper.selectStatDoctorWorkReportReport(mapArg);
//                dataResult = mapper.statReportDoctor(mapArg);
            }
//            FStatReportDoctor.setStatTypeForList(dataResult, statType);
//            return dataResult;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

//    public Map<String,Object> statReportDoctorWorkOfDiagnosisByOrgIdFooting(long diagnosis_org_id, SearchStatParam ssp) throws StatException {
//        ssp.setDiagnosis_org_id(diagnosis_org_id);
//        SqlSession session = null;
//        try {
//            session = SessionFactory.getSession();
//            StatMapper mapper = session.getMapper(StatMapper.class);
//            Map<String, Object> mapArg = null;
//            try {
//                mapArg = ssp.buildMap();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//                throw new StatException(e);
//            }
////            List<Map<String,Object>> dataResult;
//            if (new Integer(2).equals(ssp.getWork_type())) {
//                return mapper.selectStatDoctorWorkAuditReportFooting(mapArg);
////                dataResult = mapper.statAuditDoctor(mapArg);
//            } else {
//                return mapper.selectStatDoctorWorkReportReportFooting(mapArg);
////                dataResult = mapper.statReportDoctor(mapArg);
//            }
////            FStatReportDoctor.setStatTypeForList(dataResult, statType);
////            return dataResult;
//        } finally {
//            SessionFactory.closeSession(session);
//        }
//    }

    /**
     * @param passport
     * @param mode
     * @param requestOrgId
     * @param diagnosisOrgId
     * @param dtStart
     * @param dtEnd
     * @param deviceTypeId
     * @param doctorId
     * @param statType       统计类型，1标识报告，2标识审核，其他都默认为报告
     * @return
     * @throws BaseException
     */
    public List<FStatReportDoctor> statReportDoctor(Passport passport, int mode, Long requestOrgId, Long diagnosisOrgId,
                                                    Date dtStart, Date dtEnd, Long deviceTypeId, Long doctorId, int statType) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("request_org_id", requestOrgId);
            mapArg.put("diagnosis_org_id", diagnosisOrgId);
            this.installMapArg(mapArg, passport, mode);
            mapArg.put("device_type_id", deviceTypeId);
            mapArg.put("user_id", doctorId);
            mapArg.put("start", dtStart == null ? null : DateTools.dateToString(DateTools.getStartTimeByDay(dtStart)));
            mapArg.put("end", dtEnd == null ? null : DateTools.dateToString(DateTools.getLastTimeByDay(dtEnd)));
            List<FStatReportDoctor> dataResult;
            if (statType == 2)
                dataResult = mapper.statAuditDoctor(mapArg);
            else
                dataResult = mapper.statReportDoctor(mapArg);
            FStatReportDoctor.setStatTypeForList(dataResult, statType);
            return dataResult;
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * @param passport
     * @param mode           1标识诊断方，2标识申请方，其他后台方
     * @param dtStart
     * @param dtEnd
     * @param deviceTypeId
     * @param partTypeId
     * @param requestOrgId
     * @param diagnosisOrgId
     * @param sickName
     * @return
     * @throws BaseException
     */
    public List<FStatReportSick> statReportSick(Passport passport, int mode, Date dtStart, Date dtEnd, Long deviceTypeId, Long partTypeId, Long requestOrgId,
                                                Long diagnosisOrgId, String sickName) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            if (sickName == null || sickName.trim().isEmpty())
                throw new StatException("必须指定病症！");
            StatMapper mapper = session.getMapper(StatMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("request_org_id", requestOrgId);
            mapArg.put("diagnosis_org_id", diagnosisOrgId);
            this.installMapArg(mapArg, passport, mode);
            mapArg.put("device_type_id", deviceTypeId);
            mapArg.put("part_type_id", partTypeId);
            mapArg.put("sick_name", sickName);
            mapArg.put("start", dtStart == null ? null : DateTools.dateToString(DateTools.getStartTimeByDay(dtStart)));
            mapArg.put("end", dtEnd == null ? null : DateTools.dateToString(DateTools.getLastTimeByDay(dtEnd)));
            mapArg.put("status", DiagnosisStatus2.DIAGNOSISED.getCode());
            return FStatReportSick.setSick_nameForList(mapper.statReportSick(mapArg), sickName);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public Map<String, Object> statReportFooting(long diagnosis_org_id, SearchStatParam ssp) throws StatException {
        ssp.setDiagnosis_org_id(diagnosis_org_id);
        SqlSession session = SessionFactory.getSession();
        try {
            final StatMapper mapper = session.getMapper(StatMapper.class);
            try {
                Map<String, Object> mapArg = ssp.buildMap();
                if (ssp.getSpu() != null) {
                    long count = mapper.selectStatReportEarningCount(mapArg);
                    ssp.getSpu().setTotalRow(count);
                    if (count <= 0)
                        return null;
                    mapArg.put("minRow", ssp.getSpu().getCurrMinRowNum());
                    mapArg.put("maxRow", ssp.getSpu().getCurrMaxRowNum());
                    mapArg.put("rowCount", ssp.getSpu().getPerPageCount());
                }
//                List<Map<String,Object>> result = mapper.selectStatReportEarning(mapArg);
//                if (CollectionTools.isEmpty(result))
//                    return result;
                return mapper.selectStatReportEarningFooting(mapArg);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new StatException(e);
            }
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 统计机构诊断的收入
     */
    public List<Map<String, Object>> statReportByMyOrgDiagnosis(long diagnosis_org_id, SearchStatParam ssp) throws StatException {
        ssp.setDiagnosis_org_id(diagnosis_org_id);
        SqlSession session = SessionFactory.getSession();
        try {
            final StatMapper mapper = session.getMapper(StatMapper.class);
            try {
                Map<String, Object> mapArg = ssp.buildMap();
                if (ssp.getSpu() != null) {
                    long count = mapper.selectStatReportEarningCount(mapArg);
                    ssp.getSpu().setTotalRow(count);
                    if (count <= 0)
                        return null;
                    mapArg.put("minRow", ssp.getSpu().getCurrMinRowNum());
                    mapArg.put("maxRow", ssp.getSpu().getCurrMaxRowNum());
                    mapArg.put("rowCount", ssp.getSpu().getPerPageCount());
                }
                List<Map<String, Object>> result = mapper.selectStatReportEarning(mapArg);
                if (CollectionTools.isEmpty(result))
                    return result;
//                Map<String,Object> footing = mapper.selectStatReportEarningFooting(mapArg);
//                result.add(footing);
                return result;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new StatException(e);
            }
        } finally {
            SessionFactory.closeSession(session);
        }
    }
}
