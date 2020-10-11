package com.vastsoft.yingtai.module.diagnosis2.requestTranfer.services;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.NumberTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.db.VsSqlSession;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.exception.ReportException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.service.ReportService;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.exception.RequestException;
import com.vastsoft.yingtai.module.diagnosis2.mapper.DiagnosisMapper;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.assist.SearchRequestTranferParam;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity.FRequestTranfer;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity.TRequestTranfer;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.excaption.RequestTranferException;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.mapper.RequestTranferMapper;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.financel.service.FinanceService;
import com.vastsoft.yingtai.module.msg.constants.MsgType;
import com.vastsoft.yingtai.module.msg.service.MsgService;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.product.entity.TOrgProduct;
import com.vastsoft.yingtai.module.org.product.service.OrgProductService;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationStatus;
import com.vastsoft.yingtai.module.org.realtion.constants.PublishReportType;
import com.vastsoft.yingtai.module.org.realtion.entity.FOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.service.OrgRelationService;
import com.vastsoft.yingtai.module.org.realtion.service.SearchOrgRelationConfigParam;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

public class RequestTranferService {
    public static final RequestTranferService instance = new RequestTranferService();

    public TDiagnosis acceptAndTranferRequest(Passport passport, long diagnosis_id, long diagnosis_org_id,
                                              long diagnosis_product_id) throws Exception {
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT))
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
                throw new NotPermissionException("您缺少报告员权限或者审核员权限！");
        if (diagnosis_id <= 0)
            throw new RequestTranferException("必须指定有效的申请ID！");
        if (diagnosis_org_id <= 0)
            throw new RequestTranferException("必须指定有效的被移交的机构的ID！");
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            TDiagnosis oldDiagnosis = diagnosisMapper.selectDiagnosisByIdForUpdate(diagnosis_id);
            if (oldDiagnosis == null)
                throw new RequestTranferException("指定的诊断申请未找到！");
            if (oldDiagnosis.getDiagnosis_org_id() != passport.getOrgId())
                throw new RequestTranferException("指定的诊断申请不是提交给你当前登录的机构的申请！");
            if (oldDiagnosis.getStatus() != DiagnosisStatus2.NOTDIAGNOSIS.getCode())
                throw new RequestTranferException("指定的诊断不是处在等待诊断的状态！");
            // 验证服务是否在机构下，且有效
            if (!OrgProductService.instance.checkProductByOrg(passport, diagnosis_product_id, diagnosis_org_id))
                throw new RequestException("机构服务验证失败！");
            TOrgProduct product = OrgProductService.instance.searchProductById(diagnosis_product_id);
            if (product == null)
                throw new RequestException("指定的诊断服务未找到！");
            FOrgRelation orgRelation = FOrgRelation.existInListByRelationOrgId(
                    this.queryFriendOrgCanTranferByDiagnosisId(passport, diagnosis_id, session), diagnosis_org_id);
            if (orgRelation == null)
                throw new RequestTranferException("您还不能将申请转交给此机构！");
            {
                // 检查是否存在链式转交的情况
                SearchRequestTranferParam srtp = new SearchRequestTranferParam();
                srtp.setNew_request_id(diagnosis_id);
                List<FRequestTranfer> listOldRequestTranfer = this.searchRequestTranfer(srtp, session);
                if (!CollectionTools.isEmpty(listOldRequestTranfer))
                    throw new RequestTranferException("指定的申请是其他机构转交给你的申请，您不可以再次转交！");
            }
            oldDiagnosis.setStatus(DiagnosisStatus2.ACCEPT_TRANFER.getCode());
            oldDiagnosis.setAccept_user_id(passport.getUserId());
            oldDiagnosis.setHandle_time(new Date());
            diagnosisMapper.updateDiagnosis(oldDiagnosis);
            TDiagnosis newDiagnosis = (TDiagnosis) oldDiagnosis.clone();
            newDiagnosis.setRequest_org_id(passport.getOrgId());
            newDiagnosis.setRequest_user_id(passport.getUserId());
            newDiagnosis.setAccept_user_id(0);
            newDiagnosis.setCreate_time(new Date());
            newDiagnosis.setCurr_handle_id(0);
            newDiagnosis.setDiagnosis_org_id(diagnosis_org_id);
            newDiagnosis.setDiagnosis_product_id(diagnosis_product_id);
            newDiagnosis.setHandle_time(null);
            newDiagnosis.setNote(null);
            if (newDiagnosis.getAllow_reporter_publish_report() == 1) {
                TOrganization publishReportOrg = OrgRelationService.instance
                        .queryPublishOrgIdByRequestOrgIdAndDiagnosisOrgId(passport.getOrgId(), diagnosis_org_id);
                if (publishReportOrg == null)
                    throw new RequestTranferException("未找到发布报告的机构！");
                newDiagnosis.setPublish_report_org_id(publishReportOrg.getId());
            } else {
                // 以老申请的报告发布者为准
            }
            newDiagnosis.setRequest_org_id(passport.getOrgId());
            newDiagnosis.setRequest_user_id(passport.getUserId());
            newDiagnosis.setStatus(DiagnosisStatus2.NOTDIAGNOSIS.getCode());
            newDiagnosis.setVerify_user_id(0);
            diagnosisMapper.insertDiagnosis(newDiagnosis);

            RequestTranferMapper mapper = session.getMapper(RequestTranferMapper.class);
            TRequestTranfer requestTranfer = new TRequestTranfer();
            requestTranfer.setCreate_org_id(passport.getOrgId());
            requestTranfer.setCreate_time(new Date());
            requestTranfer.setCreate_user_id(passport.getUserId());
            requestTranfer.setOld_request_id(diagnosis_id);
            requestTranfer.setNew_request_id(newDiagnosis.getId());
            mapper.insertRequestTranfer(requestTranfer);
            MsgService.instance.addWaitSendPlan(passport, newDiagnosis, null, MsgType.DIAGNOSIS_REMIND, session);
            // 冻结资金
            double total_price = OrgProductService.instance.calculatePriceByDicomImgIdAndOrgProductId(
                    oldDiagnosis.getDicom_img_id(), oldDiagnosis.getCharge_amount(), diagnosis_product_id, session);
            if (total_price <= 0)
                throw new RequestException("计算得到的费用为0，提交失败！");
            FinanceService.instance.getFinanceTrade().freezePrice(passport, newDiagnosis, product.getPrice(), session);
            session.commit();
            return null;
        } catch (Exception e) {
            session.rollback(true);
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FOrgRelation> queryFriendOrgCanTranferByDiagnosisId(Passport passport, long diagnosisId,
                                                                    SqlSession session) throws Exception {
        DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
        TDiagnosis oldDiagnosis = diagnosisMapper.selectDiagnosisByIdForUpdate(diagnosisId);
        if (oldDiagnosis == null)
            throw new RequestTranferException("指定的申请未找到！");
        SearchRequestTranferParam srtp = new SearchRequestTranferParam();
        srtp.setNew_request_id(diagnosisId);
        List<FRequestTranfer> listTranfer = this.searchRequestTranfer(srtp, session);
        @SuppressWarnings("unchecked")
        List<Long> exclude_relation_org_ids = SetUniqueList.decorate(new ArrayList<Long>());
        if (!CollectionTools.isEmpty(listTranfer)) {
            for (FRequestTranfer requestTranfer : listTranfer) {
                if (requestTranfer.getNew_request().getStatus() != DiagnosisStatus2.CANCELED.getCode())
                    continue;
                if (requestTranfer.getNew_request().getStatus() != DiagnosisStatus2.BACKED.getCode())
                    continue;
                exclude_relation_org_ids.add(requestTranfer.getOld_request().getDiagnosis_org_id());
            }
        }
        exclude_relation_org_ids.add(oldDiagnosis.getRequest_org_id());
        SearchOrgRelationConfigParam sorcp = new SearchOrgRelationConfigParam(passport.getOrgId());
        sorcp.setExclude_relation_org_ids(NumberTools.ListToLongArray(exclude_relation_org_ids));
        sorcp.setStatus(OrgRelationStatus.VALID);
        List<FOrgRelation> result = OrgRelationService.instance.searchOrgRelation(sorcp);
        if (CollectionTools.isEmpty(result))
            return result;
        for (Iterator<FOrgRelation> iterator = result.iterator(); iterator.hasNext(); ) {
            FOrgRelation fOrgRelation = (FOrgRelation) iterator.next();
            TOrganization organization = OrgService.instance.queryOrgById(fOrgRelation.getRelation_org_id());
            if (organization == null) {
                iterator.remove();
                continue;
            }
//			if (!organization.getPermissionList().contains(OrgPermission.DIAGNOSISER)) {
//				iterator.remove();
//				continue;
//			}
            if (oldDiagnosis.getAllow_reporter_publish_report() == 1)
                continue;
            if (fOrgRelation.getOrg_relation().getPublish_report_type() == PublishReportType.Diagnosiser.getCode()) {
                iterator.remove();
                continue;
            } else if (fOrgRelation.getOrg_relation().getPublish_report_type() == PublishReportType.Requestor
                    .getCode()) {
                continue;
            } else if (fOrgRelation.getOrg_relation().getPublish_report_type() == PublishReportType.CanChoose
                    .getCode()) {
                if (fOrgRelation.getOrg_relation_config() == null) {
                    iterator.remove();
                    continue;
                }
                if (fOrgRelation.getOrg_relation_config().getPublish_report_org_id() != passport.getOrgId()) {
                    iterator.remove();
                    continue;
                }
            }
        }
        return result;
    }

    public List<FOrgRelation> queryFriendOrgCanTranferByDiagnosisId(Passport passport, long diagnosisId)
            throws Exception {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            return this.queryFriendOrgCanTranferByDiagnosisId(passport, diagnosisId, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FRequestTranfer> searchRequestTranfer(SearchRequestTranferParam srtp) throws OrgOperateException, ReportException, DicomImgException, RequestException, RequestTranferException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            return this.searchRequestTranfer(srtp, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FRequestTranfer> searchRequestTranfer(SearchRequestTranferParam srtp, SqlSession session)
            throws OrgOperateException, ReportException, DicomImgException, RequestException, RequestTranferException {
        RequestTranferMapper mapper = session.getMapper(RequestTranferMapper.class);
        Map<String, Object> mapArg;
        try {
            mapArg = srtp.buildMap();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RequestTranferException(e);
        }
        if (srtp.getSpu() != null) {
            int count = mapper.selectRequestTranferCount(mapArg);
            srtp.getSpu().setTotalRow(count);
            if (count <= 0)
                return null;
            mapArg.put("minRow", srtp.getSpu().getCurrMinRowNum());
            mapArg.put("maxRow", srtp.getSpu().getCurrMaxRowNum());
        }
        List<FRequestTranfer> result = mapper.selectRequestTranfer(mapArg);
        if (CollectionTools.isEmpty(result))
            return result;
        for (FRequestTranfer fRequestTranfer : result) {
            fRequestTranfer.setNew_request(
                    DiagnosisService2.instance.queryDiagnosisById(fRequestTranfer.getNew_request_id(), false, session));
            fRequestTranfer.setOld_request(
                    DiagnosisService2.instance.queryDiagnosisById(fRequestTranfer.getOld_request_id(), false, session));
            if (fRequestTranfer.getNew_request() != null
                    && fRequestTranfer.getNew_request().getStatus() == DiagnosisStatus2.DIAGNOSISED.getCode())
                fRequestTranfer.setNew_request_report(
                        ReportService.instance.queryReportByRequestId(fRequestTranfer.getNew_request_id(), session));
        }
        return result;
    }

    public boolean checkRepeatTranferRequest(Passport passport, long diagnosis_id, long diagnosis_org_id) throws OrgOperateException, ReportException, DicomImgException, RequestException, RequestTranferException {
        SearchRequestTranferParam srtp = new SearchRequestTranferParam();
        srtp.setCreate_org_id(passport.getOrgId());
        srtp.setOld_request_id(diagnosis_id);
        List<FRequestTranfer> listRequestTranfer = this.searchRequestTranfer(srtp);
        if (CollectionTools.isEmpty(listRequestTranfer))
            return false;
        for (FRequestTranfer fRequestTranfer : listRequestTranfer) {
            if (fRequestTranfer.getNew_request().getDiagnosis_org_id() == diagnosis_org_id)
                return true;
        }
        return false;
    }

    public void cancelTranferRequestById(Passport passport, long request_tranfer_id) throws Exception {
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT))
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
                throw new NotPermissionException("您缺少报告员权限或者审核员权限！");
        VsSqlSession session = null;
        String sessionKey = StringTools.getUUID();
        try {
            session = SessionFactory.getSession(sessionKey);
            RequestTranferMapper mapper = session.getMapper(RequestTranferMapper.class);
            TRequestTranfer requestTranfer = mapper.selectRequestTranferByIdForUpdate(request_tranfer_id);
            if (requestTranfer == null)
                throw new RequestTranferException("指定的移交申请未找到！");
            if (requestTranfer.getCreate_org_id() != passport.getOrgId())
                throw new RequestTranferException("指定的移交申请不是您当前所在的机构发起的，您不能撤回！");

            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            TDiagnosis diagnosis = diagnosisMapper.selectDiagnosisByIdForUpdate(requestTranfer.getNew_request_id());
            if (diagnosis == null)
                throw new RequestException("诊断申请不存在！");
            if (diagnosis.getStatus() != DiagnosisStatus2.NOTDIAGNOSIS.getCode())
                throw new RequestTranferException("要撤回的申请不是等待诊断状态，您不能撤回！");
            diagnosis.setStatus(DiagnosisStatus2.CANCELED.getCode());
            diagnosisMapper.updateDiagnosis(diagnosis);
            FinanceService.instance.getFinanceTrade().unFreezePrice(passport, diagnosis, session);
            session.commit(sessionKey);
        } catch (Exception e) {
            session.rollback(true, sessionKey);
            throw e;
        } finally {
            SessionFactory.closeSession(session, sessionKey);
        }
    }

    public void againTranferRequestByRequestTranferId(Passport passport, long request_id, long diagnosis_org_id,
                                                      long diagnosis_product_id) throws Exception {
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT))
            if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
                throw new NotPermissionException("您缺少报告员权限或者审核员权限！");
        VsSqlSession session = null;
        String sessionKey = StringTools.getUUID();
        try {
            session = SessionFactory.getSession(sessionKey);
            RequestTranferMapper mapper = session.getMapper(RequestTranferMapper.class);
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            {
                // 检查是否重复提交，且存在未完成的申请
                SearchRequestTranferParam srtp = new SearchRequestTranferParam();
                srtp.setOld_request_id(request_id);
                srtp.setCreate_org_id(passport.getOrgId());
                List<FRequestTranfer> listOldRequestTranfer = this.searchRequestTranfer(srtp, session);
                if (!CollectionTools.isEmpty(listOldRequestTranfer)) {
                    int[] exclude_newRequest_status = new int[]{DiagnosisStatus2.ACCEPT_TRANFER.getCode(),
                            DiagnosisStatus2.DIAGNOSISED.getCode(), DiagnosisStatus2.DIAGNOSISING.getCode(),
                            DiagnosisStatus2.NOTDIAGNOSIS.getCode()};
                    for (FRequestTranfer fRequestTranfer : listOldRequestTranfer) {
                        if (ArrayTools.exist(exclude_newRequest_status, fRequestTranfer.getNew_request().getStatus()))
                            throw new RequestTranferException("此申请的以往移交的申请中存在未完结或者已经诊断完成的申请，不能再次转交！");
                    }
                }
            }
            TDiagnosis oldDiagnosis = diagnosisMapper.selectDiagnosisByIdForUpdate(request_id);
            if (oldDiagnosis == null)
                throw new RequestTranferException("指定的诊断申请未找到！");
            if (oldDiagnosis.getDiagnosis_org_id() != passport.getOrgId())
                throw new RequestTranferException("指定的诊断申请不是提交给你当前登录的机构的申请！");
            if (oldDiagnosis.getStatus() != DiagnosisStatus2.ACCEPT_TRANFER.getCode())
                throw new RequestTranferException("指定的诊断不是处在已接受且已转交的状态！");
            // 验证服务是否在机构下，且有效
            if (!OrgProductService.instance.checkProductByOrg(passport, diagnosis_product_id, diagnosis_org_id))
                throw new RequestException("机构服务验证失败！");
            TOrgProduct product = OrgProductService.instance.searchProductById(diagnosis_product_id);
            if (product == null)
                throw new RequestException("指定的诊断服务未找到！");
            FOrgRelation orgRelation = FOrgRelation.existInListByRelationOrgId(
                    this.queryFriendOrgCanTranferByDiagnosisId(passport, request_id, session), diagnosis_org_id);
            if (orgRelation == null)
                throw new RequestTranferException("您还不能将申请转交给此机构！");
            oldDiagnosis.setStatus(DiagnosisStatus2.ACCEPT_TRANFER.getCode());
            oldDiagnosis.setAccept_user_id(passport.getUserId());
            oldDiagnosis.setHandle_time(new Date());
            diagnosisMapper.updateDiagnosis(oldDiagnosis);
            TDiagnosis newDiagnosis = (TDiagnosis) oldDiagnosis.clone();
            newDiagnosis.setRequest_org_id(passport.getOrgId());
            newDiagnosis.setRequest_user_id(passport.getUserId());
            newDiagnosis.setAccept_user_id(0);
            newDiagnosis.setCreate_time(new Date());
            newDiagnosis.setCurr_handle_id(0);
            newDiagnosis.setDiagnosis_org_id(diagnosis_org_id);
            newDiagnosis.setDiagnosis_product_id(diagnosis_product_id);
            newDiagnosis.setHandle_time(null);
            newDiagnosis.setNote(null);
            if (newDiagnosis.getAllow_reporter_publish_report() == 1) {
                TOrganization publishReportOrg = OrgRelationService.instance
                        .queryPublishOrgIdByRequestOrgIdAndDiagnosisOrgId(passport.getOrgId(), diagnosis_org_id);
                if (publishReportOrg == null)
                    throw new RequestTranferException("未找到发布报告的机构！");
                newDiagnosis.setPublish_report_org_id(publishReportOrg.getId());
            } else {
                // 以老申请的报告发布者为准
            }
            newDiagnosis.setRequest_org_id(passport.getOrgId());
            newDiagnosis.setRequest_user_id(passport.getUserId());
            newDiagnosis.setStatus(DiagnosisStatus2.NOTDIAGNOSIS.getCode());
            newDiagnosis.setVerify_user_id(0);
            newDiagnosis.setRequest_class(oldDiagnosis.getRequest_class());
            diagnosisMapper.insertDiagnosis(newDiagnosis);

            TRequestTranfer newRequestTranfer = new TRequestTranfer();
            newRequestTranfer.setCreate_org_id(passport.getOrgId());
            newRequestTranfer.setCreate_time(new Date());
            newRequestTranfer.setCreate_user_id(passport.getUserId());
            newRequestTranfer.setOld_request_id(request_id);
            newRequestTranfer.setNew_request_id(newDiagnosis.getId());
            mapper.insertRequestTranfer(newRequestTranfer);
            MsgService.instance.addWaitSendPlan(passport, newDiagnosis, null, MsgType.DIAGNOSIS_REMIND, session);
            // 冻结资金
            FinanceService.instance.getFinanceTrade().freezePrice(passport, newDiagnosis, product.getPrice(), session);
            session.commit(sessionKey);
        } catch (Exception e) {
            session.rollback(true, sessionKey);
            throw e;
        } finally {
            SessionFactory.closeSession(session, sessionKey);
        }
    }

    /**
     * 获取机构移交路径
     *
     * @throws ReportException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws OrgOperateException
     */
    private String takeTranferOrgNamePath(TDiagnosis request, SqlSession session) {
        TOrganization org = OrgService.instance.queryOrgById(request.getRequest_org_id(), session);
        if (org == null)
            return "";
        String result = org.getOrg_name();
        List<TRequestTranfer> listRequestTranfer = session.getMapper(RequestTranferMapper.class)
                .selectRequestTranferByNewRequestId(request.getId());
        if (CollectionTools.isEmpty(listRequestTranfer))
            return result;
        TDiagnosis oldDiagnosis = session.getMapper(DiagnosisMapper.class)
                .selectDiagnosisById(listRequestTranfer.get(0).getOld_request_id());
        String rtmp = this.takeTranferOrgNamePath(oldDiagnosis, session);
        return StringTools.isEmpty(rtmp) ? result : (rtmp + " --> " + result);
    }

    /**
     * 获取机构移交路径
     *
     * @throws ReportException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws OrgOperateException
     */
    public String takeTranferOrgNamePath(TDiagnosis request) {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            TOrganization org = OrgService.instance.queryOrgById(request.getDiagnosis_org_id(), session);
            if (org == null)
                return "";
            String rTmp = this.takeTranferOrgNamePath(request, session);
            return StringTools.isEmpty(rTmp) ? org.getOrg_name() : (rTmp + " --> " + org.getOrg_name());
        } finally {
            SessionFactory.closeSession(session);
        }
    }
}
