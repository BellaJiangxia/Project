package com.vastsoft.yingtai.module.weixin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.SystemException;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.assist.SearchCaseHistoryParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.FPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.inquiry.InquiryService;
import com.vastsoft.yingtai.module.inquiry.exceptions.InquiryException;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.returnvisit.ReturnVisitService;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisit;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQA;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQAOpt;
import com.vastsoft.yingtai.module.returnvisit.exceptions.ReturnVisitException;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.weixin.entity.InquiryCount;
import com.vastsoft.yingtai.module.weixin.entity.TPatientExternalRelation;
import com.vastsoft.yingtai.module.weixin.entity.TPatientOrgMapping;
import com.vastsoft.yingtai.module.weixin.mapper.PatientExternalRelationMapper;
import com.vastsoft.yingtai.module.weixin.service.exceptions.ReleationException;
import com.vastsoft.yingtai.module.weixin.service.http.YiHuanClient;

/**
 * @author Radar
 *
 */
public class WeixinInterfaceService
{
	public final static WeixinInterfaceService instance = new WeixinInterfaceService();

	private WeixinInterfaceService()
	{
	}

	private TPatientExternalRelation selectPatientRelation(SqlSession session, String strExternalSysUserId) throws SystemException
	{
		try
		{
			Map<String,Object> prms=new HashMap<>();
			prms.put("external_sys_type",ExternalSysType.Weixin.getCode());
			prms.put("external_sys_user_id", strExternalSysUserId);
			prms.put("status", RelationStatus.Binded.getCode());
			prms.put("state", RelationState.VALID.getCode());

			List<TPatientExternalRelation> listT = session.getMapper(PatientExternalRelationMapper.class).select(prms);

			if (listT.size() == 0)
				return null;
			else
				return listT.get(0);
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
	}

	private TPatientExternalRelation selectPatientRelation(SqlSession session, long lPatientId) throws SystemException
	{
		try
		{
			Map<String,Object> prms=new HashMap<>();
			prms.put("external_sys_type",ExternalSysType.Weixin.getCode());
			prms.put("patient_id", lPatientId);
			prms.put("status", RelationStatus.Binded.getCode());
			prms.put("state", RelationState.VALID.getCode());

			List<TPatientExternalRelation> listT = session.getMapper(PatientExternalRelationMapper.class).select(prms);

			if (listT.size() == 0)
				return null;
			else
				return listT.get(0);
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
	}

	public void onUserSubcribe(String strExternalSysUserId) throws ReleationException, SystemException {
		if(strExternalSysUserId==null || strExternalSysUserId.isEmpty())
			throw new ReleationException("参数为空");

		SqlSession session=null;

		try {
			session = SessionFactory.getSession();
			PatientExternalRelationMapper mapper = session.getMapper(PatientExternalRelationMapper.class);

			Map<String,Object> prms=new HashMap<>();
			prms.put("user_id",strExternalSysUserId);
			prms.put("state",RelationState.VALID.getCode());
			List<TPatientExternalRelation> list = mapper.selectByOutSideUser(prms);
			if (list != null && !list.isEmpty()) {
				TPatientExternalRelation oldPR = null;
				for (TPatientExternalRelation pr : list) {
					if (pr.getExternal_sys_user_id().equals(strExternalSysUserId) && RelationStatus.UnSubscribed.getCode() == pr.getStatus())
						oldPR = pr;
					else if(!pr.getExternal_sys_user_id().equals(strExternalSysUserId) && RelationStatus.Binded.getCode()==pr.getStatus())
						return;
				}

				if (oldPR != null) {
					oldPR.setStatus(RelationStatus.Binded.getCode());
					mapper.updateStatus(oldPR);

					session.commit();
				}
			}

			System.out.println("["+strExternalSysUserId+"关注]");
		} catch (Exception e) {
			if(session!=null) session.rollback();
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void onUserUnsubscribe(String strExternalSysUserId) throws ReleationException, SystemException {
		if(strExternalSysUserId==null || strExternalSysUserId.isEmpty())
			throw new ReleationException("参数为空");

		SqlSession session=null;

		try {
			session=SessionFactory.getSession();
			PatientExternalRelationMapper mapper=session.getMapper(PatientExternalRelationMapper.class);

			Map<String,Object> prms=new HashMap<>();
			prms.put("external_sys_type", ExternalSysType.Weixin.getCode());
			prms.put("external_sys_user_id", strExternalSysUserId);
			prms.put("state", RelationState.VALID.getCode());

			List<TPatientExternalRelation> list=mapper.select(prms);

			if(list!=null && !list.isEmpty()){
        TPatientExternalRelation relation=list.get(0);
        relation.setStatus(RelationStatus.UnSubscribed.getCode());
        mapper.updateStatus(relation);

        session.commit();
      }

			System.out.println("["+strExternalSysUserId+"取消关注]");

		} catch (Exception e) {
			if(session!=null) session.rollback();
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 
	 * 根据真实姓名，身份证号码，请求绑定用户微信账户
	 * 
	 * @param lOrgId
	 * @param strExternalSysUserId
	 * @param strUserRealName
	 * @param strIdentityId
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public LastCaseImportantInfo requestBindPatient(long lOrgId, String strExternalSysUserId, String strUserRealName, String strIdentityId) throws SystemException, ReleationException
	{
		if(strExternalSysUserId==null || strExternalSysUserId.isEmpty())
			throw new ReleationException("参数错误");

		if(strIdentityId==null || strIdentityId.isEmpty())
			throw new ReleationException("无效的身份证号码");

		if(lOrgId<=0)
			throw new ReleationException("未指定绑定医院");

		SqlSession session = null;

		try
		{
			TPatient tPatient = PatientService.instance.queryPatient(lOrgId, ShareRemoteParamsType.IDENTITY_ID, strIdentityId);

			if (tPatient == null)
				throw new ReleationException("未找到该病人信息");

			if(!tPatient.getName().equals(strUserRealName)) throw new ReleationException("用户信息不匹配");

			SearchCaseHistoryParam schp = new SearchCaseHistoryParam();
			schp.setPatient_id(tPatient.getId());
			schp.setOrg_id(lOrgId);
			schp.setNeedSortByEnnerTime(true);
			List<FCaseHistory> listCase = CaseHistoryService.instance.searchCaseHistory(schp);

			if (listCase==null || listCase.isEmpty())
				throw new ReleationException("未找到任何病历信息");

			FPatientOrgMapper fOrgMapper = PatientService.instance.queryPatientOrgMapperByPatientIdAndOrgId(tPatient.getId(), lOrgId);

			if(fOrgMapper==null)
				throw new ReleationException("无法找到用户在该医院的就诊记录");

			session = SessionFactory.getSession();
			Map<String,Object> prms=new HashMap<>();
			prms.put("user_id",strExternalSysUserId);
			prms.put("patient_id",tPatient.getId());
			prms.put("state",RelationState.VALID.getCode());
			List<TPatientExternalRelation> relationList=session.getMapper(PatientExternalRelationMapper.class).selectByOutSideUser(prms);

			boolean isNewOrg=false;
			List<Long> listOrgId=new ArrayList<>();

			if (relationList != null && !relationList.isEmpty()) {
				TPatientExternalRelation r = relationList.get(0);

				if (RelationStatus.Binded.getCode()==r.getStatus() && tPatient.getId() == r.getPatient_id() && !r.getExternal_sys_user_id().equals(strExternalSysUserId))
					throw new ReleationException("用户已经绑定微信");

				List<TPatientOrgMapping> orgs = r.getOrgList();
				for (TPatientOrgMapping om : orgs) {
					listOrgId.add(om.getOrg_id());
					if (RelationStatus.Binded.getCode()==r.getStatus() && lOrgId == om.getOrg_id()) throw new ReleationException("用户已经绑定过的医院");
				}

				isNewOrg = true;
			}

			TCaseHistory tCase = listCase.get(0);
			CaseHistoryType type = CaseHistoryType.parseCode(tCase.getType());
			Date dtCase = tCase.getEnter_time();
			String strSymptom = tCase.getSymptom();
			String strDiagnoses = tCase.getDiagnoses();

			LastCaseImportantInfo lcii = new LastCaseImportantInfo(lOrgId, fOrgMapper.getOrg_name(), fOrgMapper.getOrg_logo_file_id(), fOrgMapper.getCard_num(), Gender.parseCode(tPatient.getGender()), tPatient.getBirthday(), type, dtCase, strSymptom,
					strDiagnoses);

			TPatientExternalRelation t=null;
			if(isNewOrg){
				t=relationList.get(0);
			}else{
				t = new TPatientExternalRelation();

				t.setSeq_id(1);
				t.setPatient_id(tPatient.getId());
				t.setExternal_sys_type(ExternalSysType.Weixin.getCode());
				t.setExternal_sys_user_id(strExternalSysUserId);
				t.setStatus(RelationStatus.Subscribed.getCode());
				t.setState(RelationState.VALID.getCode());
				t.setCreate_time(new Date());

				session.getMapper(PatientExternalRelationMapper.class).insert(t);
			}

			if(!listOrgId.contains(lOrgId) || t.getPatient_id()!=tPatient.getId()) {
				TPatientOrgMapping pom = new TPatientOrgMapping();
				pom.setOrg_id(lOrgId);
				pom.setPatient_id(tPatient.getId());
				pom.setRelation_id(t.getId());

				session.getMapper(PatientExternalRelationMapper.class).intserOrgMapping(pom);
			}

			session.commit();

			return lcii;
		}
		catch (ReleationException e)
		{
			if(session!=null) session.rollback();
			throw e;
		}
		catch (Exception e)
		{
			if(session!=null) session.rollback();
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 绑定病人
	 * 
	 * @param strExternalSysUserId
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public void BindPatient(String strExternalSysUserId) throws SystemException, ReleationException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();
			Map<String,Object> prms=new HashMap<>();

			prms.put("external_sys_type", ExternalSysType.Weixin.getCode());
			prms.put("external_sys_user_id", strExternalSysUserId);
			prms.put("status",RelationStatus.Subscribed.getCode());
			prms.put("state", RelationState.VALID.getCode());

			List<TPatientExternalRelation> listT = session.getMapper(PatientExternalRelationMapper.class).select(prms);

			if (listT.size() == 0)
				throw new ReleationException("该用户未申请绑定");

			TPatientExternalRelation t = listT.get(0);
			long id=t.getId();

			// 作为历史无效记录
			t.setState(RelationState.INVALID.getCode());
			session.getMapper(PatientExternalRelationMapper.class).updateState(t);

			// 新增有效记录
			t.setSeq_id(t.getSeq_id() + 1);
			t.setStatus(RelationStatus.Binded.getCode());
			t.setState(RelationState.VALID.getCode());

			session.getMapper(PatientExternalRelationMapper.class).insert(t);

			prms.clear();
			prms.put("old_id",id);
			prms.put("new_id",t.getId());

			session.getMapper(PatientExternalRelationMapper.class).updateMappingState(prms);

			session.commit();
		}
		catch (ReleationException e)
		{
			if(session!=null) session.rollback();
			throw e;
		}
		catch (Exception e)
		{
			if(session!=null) session.rollback();
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询病人自身信息
	 * 
	 * @param strExternalSysUserId
	 * @return
	 * @throws ReleationException
	 * @throws SystemException
	 */
	public TPatient queryPatientInfo(String strExternalSysUserId) throws ReleationException, SystemException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, strExternalSysUserId);

			if (r == null)
				throw new ReleationException("该用户未绑定真实身份");

			long lPatientId = r.getPatient_id();

			TPatient tPatient = PatientService.instance.queryPatientById(lPatientId);

			// TODO 过滤掉不返回的信息

			return tPatient;
		}
		catch (ReleationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 检索病人在指定机构（或任意机构）的病历信息
	 * 
	 * @param strExternalSysUserId
	 * @param lOrgId
	 * @return
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public List<TCaseHistory> queryCaseHistory(String strExternalSysUserId, Long lOrgId, SplitPageUtil spu) throws SystemException, ReleationException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, strExternalSysUserId);

			if (r == null)
				throw new ReleationException("该用户未绑定真实身份");

			long lPatientId = r.getPatient_id();

			List<TCaseHistory> listCase = CaseHistoryService.instance.queryCaseHistoryByPatientId(lPatientId, lOrgId, spu);

			// TODO 过滤掉不返回的信息
			for (TCaseHistory t : listCase)
			{

			}

			return listCase;
		}
		catch (ReleationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取病人一次病历的详细信息
	 * 
	 * @param strExternalSysUserId
	 * @param lCaseId
	 * @return
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public TCaseHistory queryCaseInfo(String strExternalSysUserId, long lCaseId) throws SystemException, ReleationException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, strExternalSysUserId);

			if (r == null)
				throw new ReleationException("该用户未绑定真实身份");

			TCaseHistory tCase = CaseHistoryService.instance.queryCaseHistoryById(lCaseId,true);

			if (tCase == null)
				throw new ReleationException("病历不存在");

			if (tCase.getPatient_id() != r.getPatient_id())
				throw new ReleationException("病历不存在");

			// TODO 过滤掉不返回的信息

			return tCase;
		}
		catch (ReleationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 检索有效的微信用户
	 * 
	 * @param lOrgId
	 * @param strPatientName
	 * @param gender
	 * @param strMobile
	 * @return
	 * @throws SystemException
	 */
	public List<TPatientExternalRelation> searchPatientsRelation(long lOrgId, String strPatientName, Gender gender, String strMobile) throws SystemException
	{

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();
			Map<String,Object> prms=new HashMap<>();

			prms.put("external_sys_type", ExternalSysType.Weixin.getCode());
			prms.put("patient_name", strPatientName);
			prms.put("patient_gender", gender == null ? 0 : gender.getCode());
			prms.put("patient_mobile", strMobile);
			prms.put("status",RelationStatus.Binded.getCode());
			prms.put("state", RelationState.VALID.getCode());
			prms.put("org_id", lOrgId);

			List<TPatientExternalRelation> listT = session.getMapper(PatientExternalRelationMapper.class).select(prms);

			return listT;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}

	}

	/**
	 * 获取病人就诊过的机构
	 * 
	 * @param strExternalSysUserId
	 * @return
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public List<FPatientOrgMapper> queryOrgOfPatient(String strExternalSysUserId) throws SystemException, ReleationException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, strExternalSysUserId);

			if (r == null)
				throw new ReleationException("该用户未绑定真实身份");

			long lPatientId = r.getPatient_id();

			List<FPatientOrgMapper> listOrgMapper = PatientService.instance.queryPatientOrgMapperByPatientId(lPatientId);

			if (listOrgMapper == null)
				throw new ReleationException("没有找到就诊过的医疗机构");

			if (listOrgMapper.size() == 0)
				throw new ReleationException("没有找到就诊过的医疗机构");

			// TODO 过滤掉不返回的信息

			return listOrgMapper;
		}
		catch (ReleationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 供Action直接调用，用于微信端发起问诊
	 * 
	 * @param strExternalSysUserId
	 * @param lCaseId
	 * @param listCaseIds
	 * @param strInquiryContent
	 * @return
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public long inquiryInCase(String strExternalSysUserId, long lOrgId, String strInquiryContent,Long lInquiryId,List<Long> listCaseId,List<String> listImg,InquiryCount lRestCount) throws SystemException, ReleationException, InquiryException {
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, strExternalSysUserId);

			if (r == null)
				throw new ReleationException("该用户未绑定真实身份");

			long lPatientId = r.getPatient_id();

			Long lSessionId = InquiryService.instance.onUserInquiry(lPatientId, lOrgId, strInquiryContent,lInquiryId,listCaseId,listImg,lRestCount);

			return lSessionId;
		}
		catch (ReleationException e)
		{
			throw e;
		}
		catch (InquiryException e){
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 供主系统调用，用于回复病人的问诊信息
	 * 
	 * @param lInquirySessionId
	 * @param strReplyContent
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public void replyInquiry(long lPatientId, long lInquirySessionId, String strReplyContent) throws SystemException, ReleationException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, lPatientId);

			if (r == null)
				throw new ReleationException("该用户未绑定微信");

			String strExternalSysUserId = r.getExternal_sys_user_id();

			YiHuanClient.instance.sendInquiryReply(strExternalSysUserId, lInquirySessionId, strReplyContent);
		}
		catch (ReleationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}

	}

	/**
	 * @param lOrgId
	 * @param lPatientId
	 * @throws SystemException
	 * @throws ReleationException
	 */
	public void sendReturnVisit(long lPatientId, TReturnVisit tRV) throws SystemException, ReleationException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, lPatientId);

			if (r == null)
				throw new ReleationException("该用户未绑定微信");

			String strExternalSysUserId = r.getExternal_sys_user_id();

			YiHuanClient.instance.sendReturnVisit(strExternalSysUserId, tRV);
		}
		catch (ReleationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 
	 * 来自微信端的回访回复
	 */
	public void onReturnVisitReply(String strExternalSysUserId, long lReturnVisitId, List<QuestionAnswer> listQA) throws ReleationException, SystemException, ReturnVisitException {
		if(strExternalSysUserId==null || strExternalSysUserId.isEmpty() || listQA==null || listQA.isEmpty())
			throw new ReleationException("参数错误");

		SqlSession session = null;
		try
		{
			session = SessionFactory.getSession();

			TPatientExternalRelation r = this.selectPatientRelation(session, strExternalSysUserId);

			if (r == null)
				throw new ReleationException("该用户未绑定真实身份");

			TReturnVisit visit=new TReturnVisit();
			List<TReturnVisitQA> qas=new ArrayList<>();

			for(QuestionAnswer answer:listQA){
				TReturnVisitQA qa=new TReturnVisitQA();

				qa.setVisit_id(lReturnVisitId);
				qa.setQuestion_id(answer.getQuestion_id());
				qa.setQuestion_type(answer.getQuestion_type());
				qa.setAnswer_score(answer.getAnswer_score());
				qa.setAnswer_text(answer.getAnswer_text());

				if(answer.getAnswer_options_id()!=null&&!answer.getAnswer_options_id().isEmpty()){
					List<TReturnVisitQAOpt> opts=new ArrayList<>();
					for(int idx:answer.getAnswer_options_id()){
						TReturnVisitQAOpt opt=new TReturnVisitQAOpt();
						opt.setQuestion_id(answer.getQuestion_id());
						opt.setOption_idx(idx);

						opts.add(opt);
					}

					qa.setListOpt(opts);
				}

				qas.add(qa);
			}

			visit.setListQA(qas);

			ReturnVisitService.instance.onReturnVisitReply(lReturnVisitId,visit);
		}
		catch (ReleationException | ReturnVisitException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	public List<TOrganization> queryAllOrg4wx(String strExternalSysUserId, String strOrgName, Long lOrgCode) throws OrgOperateException, SystemException {
		try {
			return OrgService.instance.selectOrgList4WX(strExternalSysUserId, strOrgName, lOrgCode, ShareRemoteServerVersion.V_2);
		} catch (OrgOperateException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		}
	}

	public List<TPatientOrgMapping> queryOrgList4Binder(String strExternalSysUserId) throws SystemException, ReleationException {
		if(strExternalSysUserId==null || strExternalSysUserId.isEmpty())
			throw new ReleationException("参数错误");

		SqlSession session=null;

		try {
			session=SessionFactory.getSession();

			Map<String,Object> prms=new HashMap<>();
			prms.put("user_id",strExternalSysUserId);
			prms.put("state",RelationState.VALID.getCode());
			prms.put("sys_type",ExternalSysType.Weixin.getCode());
			return session.getMapper(PatientExternalRelationMapper.class).selectOrgMappingList(prms);
		} catch (Exception e) {
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}

	}

}
