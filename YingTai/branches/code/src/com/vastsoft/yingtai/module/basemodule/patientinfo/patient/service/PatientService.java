package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.assist.SearchPatientParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.constants.PatientStatus;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.FPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.exception.PatientException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.mapper.PatientMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.RemoteServerService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.orgAffix.service.OrgAffixService;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jben
 */
public class PatientService {
    public static final PatientService instance = new PatientService();

    private PatientService() {
    }

    /**
     * 从身份证号中查询出生日
     */
    public static Date takeBirthdayByIdentityId(String identityId, Date default_date) {
        if (StringTools.isEmpty(identityId))
            return default_date;
        identityId = identityId.trim();
        if (!StringTools.wasIdentityId(identityId))
            return default_date;
        if (identityId.length() == 15) {
            String birthdayStr = "19" + identityId.substring(6, 12);
            return DateTools.strToDate(birthdayStr);
        } else if (identityId.length() == 18) {
            String birthdayStr = identityId.substring(6, 14);
            return DateTools.strToDate(birthdayStr);
        }
        return default_date;
    }

    /**
     * 添加病人,并添加病人机构映射关系
     *
     * @param passport 用户身份
     * @param patient  要添加的病人对象
     * @return 返回添加完成之后的病人对象，此对象数据与其数据库的数据一致
     * @throws PatientException
     */
    public TPatient addPatientAndOrgMapping(Passport passport, TPatient patient) throws PatientException {
        patient.setSource_type(PatientDataSourceType.YUANZHEN_SYS.getCode());
        SqlSession session = SessionFactory.getSession();
        try {
            PatientMapper mapper = session.getMapper(PatientMapper.class);
            TPatientOrgMapper patientOrgMapper = null;
            TPatient oldPatient = null;
                if (!StringTools.isEmpty(patient.getIdentity_id()))
                    oldPatient = this.queryPatientByIdentityId(patient.getIdentity_id(), null, session);
            if (oldPatient == null) {
                if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
                    throw new NotPermissionException(UserPermission.ORG_MEDICAL_MGR);
                patient.setCreate_time(new Date());
                patient.setCreate_user_id(passport.getUserId());
                patient.setStatus(PatientStatus.NORMAL.getCode());
                patient.setBirthday(patient.getBirthday()==null?new Date():patient.getBirthday());
                this.checkPatientObj(patient);
                mapper.insertPatient(patient);
                oldPatient = patient;
            } else {
                patientOrgMapper = this.queryPatientOrgMapperByPatientIdAndOrgId(oldPatient.getId(),
                        passport.getOrgId());
            }
            if (patientOrgMapper == null) {
                patientOrgMapper = new TPatientOrgMapper(oldPatient.getId(), passport.getOrgId());
                patientOrgMapper.setCreate_time(new Date());
                patientOrgMapper.setGot_card_time(new Date());
                patientOrgMapper.setSource_type(PatientDataSourceType.YUANZHEN_SYS.getCode());
                mapper.insertPatientOrgMapper(patientOrgMapper);
            }
            session.commit();
            return oldPatient;
        } catch (Exception e) {
            session.rollback(true);
            throw new PatientException(e);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    // /**
    // * 从本地查询病人信息，如未找到返回null
    // *
    // * @param org_id
    // * 要查询的病例库所在机构
    // * @param type
    // * 参数类型
    // * @param value
    // * 参数值
    // * @return 查询到的病人，如未找到返回null
    // * @throws BaseException
    // */
    // private TPatient queryPatientLocal(long org_id, RemoteParamsType type,
    // String value) throws BaseException {
    //
    // }

    /**
     * 从本地或者远程查询病人信息，如未找到返回null<br>
     * 在本地未找到的情况下触发远程查找
     *
     * @param org_id 要查询的病例库所在机构
     * @param type   参数类型
     * @param value  参数值
     * @return 查询到的病人，如未找到返回null
     * @throws Exception
     */
    public TPatient queryPatient(long org_id, ShareRemoteParamsType type, String value) throws Exception {
        Map<ShareRemoteParamsType, String> params = new HashMap<ShareRemoteParamsType, String>();
        params.put(type, value);
        TOrganization org = OrgService.instance.searchOrgById(org_id);
        if (org == null)
            throw new RemotePatientInfoException("未找到指定的机构");
        params.put(ShareRemoteParamsType.ORG_CODE, org.getOrg_code() + "");
        RemoteServerService.instance.onSearchRemotePatientInfo(
                CollectionTools.buildList(OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(org_id)),
                params);
        if (ShareRemoteParamsType.DIAGNOSIS_CARD_NUM.equals(type)) {
            List<FPatientOrgMapper> listPatientOrgMapper = this.searchPatientOrgMapper(null, org_id, value, null,
                    null, null, null);
            if (listPatientOrgMapper == null || listPatientOrgMapper.size() <= 0)
                return null;
            return this.queryPatientById(listPatientOrgMapper.get(0).getPatient_id());
        } else if (ShareRemoteParamsType.IDENTITY_ID.equals(type)) {
            RemoteServerService.instance.onSearchRemotePatientInfo(
                    CollectionTools.buildList(OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(org_id)),
                    params);
            return this.queryPatientByIdentityId(value, null);
        } else if (ShareRemoteParamsType.PASC_NUM.equals(type) || ShareRemoteParamsType.EPS_NUM.equals(type) || ShareRemoteParamsType.ZHUYUAN_NUM.equals(type)) {
            List<FCaseHistory> listCaseHistory = CaseHistoryService.instance.queryCaseHistory(org_id, type, value);
            if (listCaseHistory == null || listCaseHistory.size() <= 0)
                return null;
            return this.queryPatientById(listCaseHistory.get(0).getPatient_id());
        } else {
            return null;
        }
    }

    /**
     * 通过ID查询病人信息
     *
     * @param patient_id
     * @return
     * @throws BaseException
     */
    public TPatient queryPatientById(long patient_id) {
        SearchPatientParam psp = new SearchPatientParam();
        psp.setPatientId(patient_id);
        List<TPatient> listPatient = this.searchPatient(psp);
        return (listPatient == null || listPatient.size() <= 0) ? null : listPatient.get(0);
    }

    /**
     * 检索病人
     *
     * @param patientSearchParam
     * @return
     */
    public List<TPatient> searchPatient(SearchPatientParam patientSearchParam) {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            return this.searchPatient(patientSearchParam, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 检索病人
     *
     * @param patientSearchParam
     * @param session
     * @return
     */
    public List<TPatient> searchPatient(SearchPatientParam patientSearchParam, SqlSession session) {
        PatientMapper mapper = session.getMapper(PatientMapper.class);
        Map<String, Object> mapArg = patientSearchParam.buildMap();
        if (patientSearchParam.getSpu() != null) {
            int count = mapper.selectPatientCount(mapArg);
            patientSearchParam.getSpu().setTotalRow(count);
            if (count <= 0)
                return null;
            mapArg.put("minRow", patientSearchParam.getSpu().getCurrMinRowNum());
            mapArg.put("maxRow", patientSearchParam.getSpu().getCurrMaxRowNum());
        }
        return mapper.selectPatient(mapArg);
    }

    /**
     * 查询病人机构映射
     *
     * @param patient_id   病人ID
     * @param org_id       机构ID
     * @param card_num     病人就诊卡号
     * @param patient_name 病人姓名
     * @param identity_id  病人身份证
     * @param gender       病人性别
     * @param spu          分页模型
     * @return 返回病人机构映射列表
     * @throws BaseException
     */
    private List<FPatientOrgMapper> searchPatientOrgMapper(Long patient_id, Long org_id, String card_num,
                                                           String patient_name, String identity_id, Gender gender, SplitPageUtil spu) throws BaseException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            PatientMapper mapper = session.getMapper(PatientMapper.class);
            Map<String, Object> mapArg = new HashMap<String, Object>();
            mapArg.put("patient_id", patient_id);
            mapArg.put("org_id", org_id);
            mapArg.put("card_num", card_num);
            mapArg.put("patient_name", patient_name);
            mapArg.put("identity_id", identity_id);
            mapArg.put("gender", gender == null ? null : gender.getCode());
            if (spu != null) {
                int count = mapper.selectPatientOrgMapperCount(mapArg);
                spu.setTotalRow(count);
                if (count <= 0)
                    return null;
                mapArg.put("minRow", spu.getCurrMinRowNum());
                mapArg.put("maxRow", spu.getCurrMaxRowNum());
            }
            return mapper.selectPatientOrgMapper(mapArg);
        } catch (Exception e) {
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 搜索指定机构的病人，机构模型
     *
     * @param org_id       机构ID
     * @param patient_name 病人姓名
     * @param identity_id  病人身份证
     * @param gender       病人性别
     * @param spu          分页模型
     * @return 返回病人机构映射列表
     * @throws BaseException
     */
    public List<FPatientOrgMapper> searchOrgPatient(long org_id, String patient_name, String identity_id, Gender gender,
                                                    SplitPageUtil spu) throws BaseException {
        return this.searchPatientOrgMapper(null, org_id, null, patient_name, identity_id, gender, spu);
    }

    /**
     * 通过病人ID查询机构列表
     */
    public List<FPatientOrgMapper> queryPatientOrgMapperByPatientId(long patientId) {
        return null;
    }

    /**
     * 通过病人ID和机构映射关系
     *
     * @throws BaseException
     */
    public FPatientOrgMapper queryPatientOrgMapperByPatientIdAndOrgId(long patientId, long orgId, SqlSession session)
            throws BaseException {
        List<FPatientOrgMapper> listpom = (List<FPatientOrgMapper>) this.searchPatientOrgMapper(patientId, orgId, null,
                null, null, null, null);
        return (listpom == null || listpom.size() <= 0) ? null : listpom.get(0);
    }

    /**
     * 通过病人ID和机构映射关系
     *
     * @throws BaseException
     */
    public FPatientOrgMapper queryPatientOrgMapperByPatientIdAndOrgId(long patientId, long orgId) throws BaseException {
        List<FPatientOrgMapper> listpom = (List<FPatientOrgMapper>) this.searchPatientOrgMapper(patientId, orgId, null,
                null, null, null, null);
        return (listpom == null || listpom.size() <= 0) ? null : listpom.get(0);
    }

    /**
     * 通过机构ID和诊断卡号查询病人
     *
     * @throws BaseException
     */
    public TPatient queryPatientByOrgIdAndCardNum(long org_id, String card_num) {
        SqlSession session = SessionFactory.getSession();
        PatientMapper mapper = session.getMapper(PatientMapper.class);
        return mapper.selectPatientByOrgIdAndCardNum(new TPatientOrgMapper(org_id, card_num));
    }

    /**
     * @param identity_id
     * @param org_id      可选，用于确定指定的病人是否是此机构的病人，如果不为空，将作为过滤条件
     * @return
     * @throws BaseException
     */
    public TPatient queryPatientByIdentityId(String identity_id, Long org_id, SqlSession session) {
        SearchPatientParam psp = new SearchPatientParam();
        psp.setOrg_id(org_id);
        psp.setIdentity_id(identity_id);
        List<TPatient> listPatient = this.searchPatient(psp, session);
        return (listPatient == null || listPatient.isEmpty()) ? null : listPatient.get(0);
    }

    /**
     * @param identity_id
     * @return
     * @throws BaseException
     */
    public TPatient queryPatientByIdentityId(String identity_id) throws BaseException {
        SqlSession session = SessionFactory.getSession();
        try {
            return this.queryPatientByIdentityId(identity_id, null, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * @param identity_id
     * @param org_id      可选，用于确定指定的病人是否是此机构的病人，如果不为空，将作为过滤条件
     * @return
     * @throws BaseException
     */
    public TPatient queryPatientByIdentityId(String identity_id, Long org_id) {
        SqlSession session = SessionFactory.getSession();
        try {
            return this.queryPatientByIdentityId(identity_id, org_id, session);
        } finally {
            SessionFactory.closeSession(session);
        }
        // SearchPatientParam psp = new SearchPatientParam();
        // psp.setOrg_id(org_id);
        // psp.setIdentity_id(identity_id);
        // List<TPatient> listPatient = this.searchPatient(psp);
        // return (listPatient == null || listPatient.isEmpty()) ? null :
        // listPatient.get(0);
    }

    public TPatient modifyPatient(Passport passport, TPatient patient, SqlSession session) throws PatientException {
        this.checkPatientObj(patient);
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
            throw new PatientException("你缺少病例管理权限！");
        PatientMapper mapper = session.getMapper(PatientMapper.class);
        TPatient oldPatient = mapper.selectPatientByIdForUpdate(patient.getId());
        if (oldPatient == null)
            throw new PatientException("要修改的病人数据未找到！");
        if (oldPatient.equals(patient))
            return patient;
        oldPatient.setBirthday(patient.getBirthday());
        oldPatient.setBorn_address(patient.getBorn_address());
        oldPatient.setGender(patient.getGender());
        oldPatient.setHome_address(patient.getHome_address());
        oldPatient.setMarital_status(patient.getMarital_status());
        oldPatient.setMingzu(patient.getMingzu());
        oldPatient.setContact_name(patient.getContact_name());
        oldPatient.setContact_phone_num(patient.getContact_phone_num());
        oldPatient.setContact_addr(patient.getContact_addr());
        oldPatient.setContact_rela(patient.getContact_rela());
        if (!StringTools.wasIdentityId(oldPatient.getIdentity_id()))
            oldPatient.setIdentity_id(patient.getIdentity_id());
        if (!StringTools.isPhoneNum(oldPatient.getMobile()))
            oldPatient.setPhone_num(patient.getPhone_num());
        oldPatient.setName(patient.getName());
        oldPatient.setSick_his(patient.getSick_his());
        oldPatient.setWork(patient.getWork());
        mapper.updatePatient(oldPatient);
        return oldPatient;
    }

    private void checkPatientObj(TPatient patient) throws PatientException {
        if (patient == null)
            throw new PatientException("请指定病人信息！");
        if (patient.getBorn_address() != null) {
            patient.setBorn_address(patient.getBorn_address().trim());
            if (patient.getBorn_address().length() >= 256)
                throw new PatientException("病人的出生地太长了，最长256个字！");
        }
        if (Gender.parseCode(patient.getGender()) == null)
            throw new PatientException("病人性别必须指定！");
        if (patient.getHome_address() != null) {
            patient.setHome_address(patient.getHome_address().trim());
            if (!patient.getHome_address().isEmpty()) {
                if (patient.getHome_address().length() >= 256)
                    throw new PatientException("病人的居住地太长了，最长256个字！");
            }
        }
        if (patient.getIdentity_id() != null) {
            patient.setIdentity_id(patient.getIdentity_id().trim());
            if (!patient.getIdentity_id().isEmpty()) {
                if (!StringTools.wasIdentityId(patient.getIdentity_id()))
                    throw new PatientException("病人的身份证号码不正确！");
                else {
                    patient.setBirthday(takeBirthdayByIdentityId(patient.getIdentity_id(), patient.getBirthday()));
                }
            }
        }
        if (patient.getBirthday() == null)
            throw new PatientException("病人生日/年龄必须指定至少一个！");
        if (patient.getPhone_num() != null) {
            patient.setPhone_num(patient.getPhone_num().trim());
            if (!patient.getMobile().isEmpty()) {
                if (!StringTools.isPhoneNum(patient.getMobile()))
                    throw new PatientException("病人电话不正确！");
            }
        }
        if (StringTools.isEmpty(patient.getName()))
            throw new PatientException("病人姓名必须填写！");
        patient.setName(patient.getName().trim());
        if (patient.getName().length() >= 64)
            throw new PatientException("病人姓名太长了，最长64个字！");
        if (patient.getSick_his() != null) {
            patient.setSick_his(patient.getSick_his().trim());
            if (!patient.getSick_his().isEmpty()) {
                if (patient.getSick_his().length() >= 1024)
                    throw new PatientException("病人的其他病史太长，最长1000个字！");
            }
        }
        if (PatientDataSourceType.parseCode(patient.getSource_type()) == null)
            throw new PatientException("病人来源必须指定！");
        if (patient.getWork() != null) {
            patient.setWork(patient.getWork().trim());
            if (!patient.getWork().isEmpty()) {
                if (patient.getWork().length() >= 256)
                    throw new PatientException("病人工作职业太长，最长256个字！");
            }
        }
    }

    public TPatient replaceWith(long patient_id, TPatient patient, SqlSession session) throws PatientException {
        PatientMapper mapper = session.getMapper(PatientMapper.class);
        TPatient oldPatient = mapper.selectPatientByIdForUpdate(patient_id);
        if (oldPatient == null)
            throw new PatientException("指定的病人未找到！");
        oldPatient.replaceWith(patient);
        mapper.updatePatient(oldPatient);
        return oldPatient;
    }
}
