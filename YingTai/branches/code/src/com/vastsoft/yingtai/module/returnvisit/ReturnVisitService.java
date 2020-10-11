package com.vastsoft.yingtai.module.returnvisit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.SystemException;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisit;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitBatch;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQA;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQAOpt;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplate;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplateQuestion;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplateQuestionOpt;
import com.vastsoft.yingtai.module.returnvisit.exceptions.ReturnVisitException;
import com.vastsoft.yingtai.module.returnvisit.mapper.ReturnVisitBatchMapper;
import com.vastsoft.yingtai.module.returnvisit.mapper.ReturnVisitMapper;
import com.vastsoft.yingtai.module.returnvisit.mapper.ReturnVisitQAMapper;
import com.vastsoft.yingtai.module.returnvisit.mapper.ReturnVisitQAOptMapper;
import com.vastsoft.yingtai.module.returnvisit.mapper.ReturnVisitTemplateMapper;
import com.vastsoft.yingtai.module.returnvisit.mapper.ReturnVisitTemplateQuestionMapper;
import com.vastsoft.yingtai.module.returnvisit.mapper.ReturnVisitTemplateQuestionOptMapper;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.module.weixin.service.WeixinInterfaceService;

public class ReturnVisitService
{
	public final static ReturnVisitService instance = new ReturnVisitService();

	private ReturnVisitService()
	{
	}

	public long queryTemplateCount(Passport passport, String strName, Long lUserId)
	{
		return 0;
	}

	public List<TReturnVisitTemplate> queryTemplate(Passport passport, String strName, Long lUserId, SplitPageUtil spu) throws SystemException, NullParameterException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();
			ReturnVisitTemplateMapper mapper=session.getMapper(ReturnVisitTemplateMapper.class);

			Map<String,Object> prms=new HashMap<>();
			prms.put("template_name",strName==null || strName.isEmpty()?null:strName);
			prms.put("org_id",passport.getOrgId());
			prms.put("create_user_id",lUserId == null ? 0 : lUserId);
			prms.put("state",TemplateState.VALID.getCode());

			if(spu!=null){
				int count=mapper.selectCount(prms);
				spu.setTotalRow(count);
				if(count==0) return null;

				prms.put("begin_idx",spu.getCurrMinRowNum());
				prms.put("end_idx",spu.getCurrMaxRowNum());
			}

			List<TReturnVisitTemplate> listTpl = mapper.select(prms);

			return listTpl;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 创建回访模板，传入的QuestionList请预先填写好Idx，如果需要此方法按照列表的顺序填写问题序号以及选项序号，
	 * bReOrder请填写true
	 * 
	 * @param passport
	 * @param strName
	 * @param listQ
	 * @return
	 * @throws SystemException
	 * @throws NullParameterException
	 */
	public long createTemplate(Passport passport, String strName, List<TReturnVisitTemplateQuestion> listQ, boolean bReOrder) throws SystemException, NullParameterException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TReturnVisitTemplate tTpl = new TReturnVisitTemplate();
			tTpl.setTemplate_name(strName);
			tTpl.setCreate_time(new Date());
			tTpl.setOrg_id(passport.getOrgId());
			tTpl.setCreate_user_id(passport.getUserId());
			tTpl.setState(TemplateState.VALID.getCode());

			session.getMapper(ReturnVisitTemplateMapper.class).insert(tTpl);

			int idxQ = 1;

			for (TReturnVisitTemplateQuestion tQ : listQ)
			{
				tQ.setTemplate_id(tTpl.getTemplate_id());

				// 填写问题序号
				if (bReOrder)
				{
					tQ.setQuestion_idx(idxQ);
					idxQ++;
				}

				session.getMapper(ReturnVisitTemplateQuestionMapper.class).insert(tQ);

				int idxOpt = 1;
				if (tQ.getQuestion_type() == QuestionType.SINGLE_CHECK.getCode()
						|| tQ.getQuestion_type() == QuestionType.MULTI_CHECK.getCode())
				{
					for (TReturnVisitTemplateQuestionOpt tOpt : tQ.getListOpt())
					{
						// 填写选项序号
						if (bReOrder)
						{
							tOpt.setOption_idx(idxOpt);
							idxOpt++;
						}

						tOpt.setQuestion_id(tQ.getQuestion_id());
						session.getMapper(ReturnVisitTemplateQuestionOptMapper.class).insert(tOpt);
					}

				}
			}

			session.commit();

			return tTpl.getTemplate_id();
		}
		catch (Exception e)
		{
			session.rollback(true);

			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据ID查询模板详细信息，包括问题和问题选项
	 * 
	 * @param lTemplateId
	 * @return
	 * @throws SystemException
	 * @throws ReturnVisitException
	 * @throws NullParameterException
	 */
	public TReturnVisitTemplate queryTemplateById(Passport passport, long lTemplateId) throws SystemException, ReturnVisitException, NullParameterException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			Map<String,Object> prms=new HashMap<>();
			prms.put("org_id",passport.getOrgId());
			prms.put("template_id",lTemplateId);

			List<TReturnVisitTemplate> listTpl = session.getMapper(ReturnVisitTemplateMapper.class).select(prms);

			if (listTpl.size() == 0)
				throw new ReturnVisitException("未找到此回访模板");

			if (listTpl.size() > 1)
				throw new ReturnVisitException("系统错误：找到多个相同ID的回访模板");

			TReturnVisitTemplate tTpl = listTpl.get(0);

			TReturnVisitTemplateQuestion qQ = new TReturnVisitTemplateQuestion();
			qQ.setTemplate_id(tTpl.getTemplate_id());

			List<TReturnVisitTemplateQuestion> listQ = session.getMapper(ReturnVisitTemplateQuestionMapper.class).select(qQ);

			tTpl.setListQ(listQ);

			for (TReturnVisitTemplateQuestion tQ : listQ)
			{
				// 如果是单选或多选类型，则需要再查询一次选项
				if (tQ.getQuestion_type() == QuestionType.SINGLE_CHECK.getCode() || tQ.getQuestion_type() == QuestionType.MULTI_CHECK.getCode())
				{
					TReturnVisitTemplateQuestionOpt qOpt = new TReturnVisitTemplateQuestionOpt();

					qOpt.setQuestion_id(tQ.getQuestion_id());

					List<TReturnVisitTemplateQuestionOpt> listOpt = session.getMapper(ReturnVisitTemplateQuestionOptMapper.class).select(qOpt);
					tQ.setListOpt(listOpt);
				}
			}

			return tTpl;
		}
		catch (ReturnVisitException e)
		{
			e.printStackTrace();

			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}

	}

	/**
	 * 根据ID删除模板，系统不会真删除
	 * 
	 * @param lTemplateId
	 * @throws SystemException
	 * @throws ReturnVisitException
	 * @throws NullParameterException
	 */
	public void deleteTemplateById(Passport passport, long lTemplateId) throws SystemException, ReturnVisitException, NullParameterException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			Map<String,Object> prms=new HashMap<>();
			prms.put("template_id",lTemplateId);
			prms.put("org_id",passport.getOrgId());
			prms.put("state",TemplateState.VALID.getCode());

			List<TReturnVisitTemplate> listTpl = session.getMapper(ReturnVisitTemplateMapper.class).select(prms);
			if (listTpl.size() == 0)
				throw new ReturnVisitException("未找到此回访模板");

			TReturnVisitTemplate tTpl = new TReturnVisitTemplate();
			tTpl.setTemplate_id(lTemplateId);
			tTpl.setState(TemplateState.INVALID.getCode());

			session.getMapper(ReturnVisitTemplateMapper.class).update(tTpl);

			session.commit();
		}
		catch (ReturnVisitException e)
		{
			session.rollback(true);

			throw e;
		}
		catch (Exception e)
		{
			session.rollback(true);

			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 修改模板内容
	 * 
	 * @param passport
	 * @param tpl
	 * @throws SystemException
	 * @throws ReturnVisitException
	 * @throws NullParameterException
	 */
	public void modifyTemplate(Passport passport, TReturnVisitTemplate tpl, boolean bReOrder) throws SystemException, ReturnVisitException, NullParameterException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			Map<String,Object> prms=new HashMap<>();
			prms.put("template_id",tpl.getTemplate_id());
			prms.put("org_id",passport.getOrgId());
			prms.put("state",TemplateState.VALID.getCode());

			List<TReturnVisitTemplate> listTpl = session.getMapper(ReturnVisitTemplateMapper.class).select(prms);
			if (listTpl.size() == 0)
				throw new ReturnVisitException("未找到此回访模板");

			TReturnVisitTemplateQuestion qQ = new TReturnVisitTemplateQuestion();
			qQ.setTemplate_id(tpl.getTemplate_id());
			List<TReturnVisitTemplateQuestion> listQ = session.getMapper(ReturnVisitTemplateQuestionMapper.class).select(qQ);

			for (TReturnVisitTemplateQuestion tQ : listQ)
			{
				// 删除每一个问题的选项
				TReturnVisitTemplateQuestionOpt dOpt = new TReturnVisitTemplateQuestionOpt();
				dOpt.setQuestion_id(tQ.getQuestion_id());
				session.getMapper(ReturnVisitTemplateQuestionOptMapper.class).delete(dOpt);
			}

			// 删除所有问题
			TReturnVisitTemplateQuestion dQ = new TReturnVisitTemplateQuestion();
			dQ.setTemplate_id(tpl.getTemplate_id());
			session.getMapper(ReturnVisitTemplateQuestionMapper.class).delete(dQ);

			TReturnVisitTemplate tTpl = tpl;

			// 更新模板信息
			session.getMapper(ReturnVisitTemplateMapper.class).update(tTpl);

			int idxQ = 1;

			for (TReturnVisitTemplateQuestion tQ : tpl.getListQ())
			{
				tQ.setTemplate_id(tTpl.getTemplate_id());

				// 填写问题序号
				if (bReOrder)
				{
					tQ.setQuestion_idx(idxQ);
					idxQ++;
				}

				session.getMapper(ReturnVisitTemplateQuestionMapper.class).insert(tQ);

				int idxOpt = 1;
				if (tQ.getListOpt() != null && tQ.getQuestion_type() == QuestionType.SINGLE_CHECK.getCode() || tQ.getQuestion_type() == QuestionType.MULTI_CHECK.getCode())
				{
					for (TReturnVisitTemplateQuestionOpt tOpt : tQ.getListOpt())
					{
						// 填写选项序号
						if (bReOrder)
						{
							tOpt.setOption_idx(idxOpt);
							idxOpt++;
						}

						tOpt.setQuestion_id(tQ.getQuestion_id());
						session.getMapper(ReturnVisitTemplateQuestionOptMapper.class).insert(tOpt);
					}

				}
			}

			session.commit();
		}
		catch (ReturnVisitException e)
		{
			session.rollback(true);

			throw e;
		}
		catch (Exception e)
		{
			session.rollback(true);

			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 创建一个回访批次
	 * 
	 * @param passport
	 * @param tpl
	 * @param listPatientId
	 * @return
	 * @throws SystemException
	 * @throws NullParameterException
	 */
	public long createReturnVisitBatch(Passport passport, String strVisitName, List<TReturnVisitTemplateQuestion> listQ, List<Long> listPatientId,long lBatchId) throws SystemException, NullParameterException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			if(lBatchId!=0){
				TReturnVisitBatch trvb=new TReturnVisitBatch();
				trvb.setBatch_id(lBatchId);
				session.getMapper(ReturnVisitBatchMapper.class).delete(trvb);
				TReturnVisit trv=new TReturnVisit();
				trv.setBatch_id(lBatchId);
				Map<String,Object> prms=new HashMap<>();
				prms.put("batch_id",lBatchId);
				List<TReturnVisit> visitList=session.getMapper(ReturnVisitMapper.class).select(prms);
				session.getMapper(ReturnVisitMapper.class).delete(trv);
				for(TReturnVisit v:visitList){
					TReturnVisitQA tqa=new TReturnVisitQA();
					tqa.setVisit_id(v.getVisit_id());
					List<TReturnVisitQA> qaList=session.getMapper(ReturnVisitQAMapper.class).select(tqa);
					session.getMapper(ReturnVisitQAMapper.class).delete(tqa);
					for(TReturnVisitQA qa:qaList){
						TReturnVisitQAOpt tqaOpt=new TReturnVisitQAOpt();
						tqaOpt.setQuestion_id(qa.getQuestion_id());
						session.getMapper(ReturnVisitQAOptMapper.class).delete(tqaOpt);
					}
				}
			}

			TReturnVisitBatch b = new TReturnVisitBatch();
			b.setBatch_name(strVisitName + "_" + passport.getUserName() + "_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			b.setOrg_id(passport.getOrgId());
			b.setCreate_time(new Date());
			b.setCreate_user_id(passport.getUserId());
			b.setStatus(BatchStatus.WAIT_SEND.getCode());

			session.getMapper(ReturnVisitBatchMapper.class).insert(b);

			for (Long lPatientId : listPatientId)
			{
				TReturnVisit tRv = new TReturnVisit();
				tRv.setBatch_id(b.getBatch_id());
				tRv.setVisit_name(strVisitName);
				tRv.setOrg_id(passport.getOrgId());
				tRv.setCreate_time(new Date());
				tRv.setCreate_user_id(passport.getUserId());
				tRv.setDoctor_name(passport.getUserName());// 临时
				tRv.setPatient_id(lPatientId);

				session.getMapper(ReturnVisitMapper.class).insert(tRv);

				for (TReturnVisitTemplateQuestion q : listQ)
				{
					TReturnVisitQA qa = new TReturnVisitQA();
					qa.setVisit_id(tRv.getVisit_id());
					qa.setQuestion_type(q.getQuestion_type());
					qa.setQuestion_name(q.getQuestion_name());
					qa.setMin_score(q.getMin_score());
					qa.setMax_score(q.getMax_score());

					session.getMapper(ReturnVisitQAMapper.class).insert(qa);

					if (q.getQuestion_type() == QuestionType.SINGLE_CHECK.getCode() || q.getQuestion_type() == QuestionType.MULTI_CHECK.getCode())
					{
						for(int i=0,len=q.getListOpt().size();i<len;i++){
							TReturnVisitTemplateQuestionOpt opt=q.getListOpt().get(i);

							TReturnVisitQAOpt qao = new TReturnVisitQAOpt();
							qao.setQuestion_id(qa.getQuestion_id());
							qao.setOption_idx(i+1);
							qao.setOption_name(opt.getOption_name());

							session.getMapper(ReturnVisitQAOptMapper.class).insert(qao);
						}
					}
				}

			}

			session.commit();

			return b.getBatch_id();
		}
		catch (Exception e)
		{
			session.rollback(true);

			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	public List<TReturnVisitBatch> queryReturnVisitBatch(Passport passport, String strName, Long lUserId, BatchStatus status, SplitPageUtil spu) throws NullParameterException, SystemException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();
			ReturnVisitBatchMapper mapper=session.getMapper(ReturnVisitBatchMapper.class);

			Map<String,Object> prms=new HashMap<>();
			prms.put("batch_name",strName==null || strName.isEmpty()?null:strName);
			prms.put("org_id",passport.getOrgId());
			prms.put("create_user_id",lUserId == null ? 0 : lUserId);
			prms.put("status",status == null ? 0 : status.getCode());

			if(spu!=null){
				int count=mapper.selectCount(prms);
				spu.setTotalRow(count);
				if(count==0) return null;

				prms.put("begin_idx",spu.getCurrMinRowNum());
				prms.put("end_idx",spu.getCurrMaxRowNum());
			}

			List<TReturnVisitBatch> listB = mapper.select(prms);

			return listB;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	public List<TReturnVisit> queryReturnVisit(Passport passport, long lBatchId, String strVisitName, String strPatientName, Long lUserId, Boolean bSended, Boolean bReplied, SplitPageUtil spu) throws NullParameterException, SystemException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();
			ReturnVisitMapper mapper=session.getMapper(ReturnVisitMapper.class);

			Map<String,Object> prms=new HashMap<>();
			prms.put("visit_name",strVisitName==null || strVisitName.isEmpty()?null:strVisitName);
			prms.put("patient_name",strPatientName==null || strPatientName.isEmpty()?null:strPatientName);
			prms.put("batch_id",lBatchId);
			prms.put("org_id",passport.getOrgId());
			prms.put("create_user_id",lUserId == null ? 0 : lUserId);
//			prms.put("",bSended == null ? null : new Date(0));
//			prms.put("",bReplied == null ? null : new Date(0));

			if(spu!=null){
				int count=mapper.selectCount(prms);
				spu.setTotalRow(count);
				if(count==0) return null;

				prms.put("begin_idx",spu.getCurrMinRowNum());
				prms.put("end_idx",spu.getCurrMaxRowNum());
			}

			List<TReturnVisit> listRV = mapper.select(prms);

			return listRV;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	public TReturnVisit queryReturnVisitById(Passport passport, long lVisitid) throws SystemException, NullParameterException, ReturnVisitException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			Map<String,Object> prms=new HashMap<>();
			prms.put("visit_id",lVisitid==0?null:lVisitid);

			List<TReturnVisit> listRV = session.getMapper(ReturnVisitMapper.class).select(prms);

			if (listRV.size() == 0)
				throw new ReturnVisitException("此回访问卷不存在");

			if (listRV.size() > 1)
				throw new ReturnVisitException("错误：找到多个相同ID的回访问卷");

			TReturnVisit rv = listRV.get(0);

			TReturnVisitQA qQA = new TReturnVisitQA();
			qQA.setVisit_id(lVisitid);

			List<TReturnVisitQA> listQa = session.getMapper(ReturnVisitQAMapper.class).select(qQA);

			rv.setListQA(listQa);

			for (TReturnVisitQA qa : listQa)
			{
				TReturnVisitQAOpt qOpt = new TReturnVisitQAOpt();

				qOpt.setQuestion_id(qa.getQuestion_id());

				List<TReturnVisitQAOpt> listOpt = session.getMapper(ReturnVisitQAOptMapper.class).select(qOpt);

				qa.setListOpt(listOpt);
			}

			return rv;
		}
		catch (ReturnVisitException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 发送回访到客户
	 * 
	 * @param passport
	 * @param lBatchId
	 * @throws SystemException
	 * @throws NullParameterException
	 * @throws ReturnVisitException
	 */
	public void sendReturnVisitByBatch(Passport passport, long lBatchId) throws SystemException, NullParameterException, ReturnVisitException
	{
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			Map<String,Object> prms=new HashMap<>();
			prms.put("batch_id",lBatchId);

			TReturnVisitBatch b = null;

			List<TReturnVisitBatch> listB = session.getMapper(ReturnVisitBatchMapper.class).select(prms);
			if (listB.size() == 1)
				b = listB.get(0);

			if (b == null)
				throw new ReturnVisitException("此回访批次不存在");

			if (b.getStatus() == BatchStatus.SENDED.getCode())
				throw new ReturnVisitException("此回访批次已经发送");

			// 实际发送
			prms.clear();
			prms.put("batch_id",b.getBatch_id()==0?null:b.getBatch_id());
			List<TReturnVisit> listRV = session.getMapper(ReturnVisitMapper.class).select(prms);

			int iNotSended = 0;

			for (TReturnVisit rv : listRV)
			{
				TReturnVisit rvFull =this.queryReturnVisitById(passport, rv.getVisit_id());
//				TReturnVisit rvFull=this.selectVisitInfoById(passport,rv.getVisit_id());

				if (rvFull.getSend_time() == null)
				{
					try
					{
						WeixinInterfaceService.instance.sendReturnVisit(rvFull.getPatient_id(), rvFull);

						rvFull.setSend_time(new Date());

						session.getMapper(ReturnVisitMapper.class).update(rvFull);
					}
					catch (Exception e)
					{
						iNotSended++;
					}
				}
			}

			if (iNotSended > 0)
				b.setStatus(BatchStatus.HAS_NOT_SENDED.getCode());
			else
				b.setStatus(BatchStatus.SENDED.getCode());

			session.getMapper(ReturnVisitBatchMapper.class).update(b);

			session.commit();

		}
		catch (ReturnVisitException e)
		{
			session.rollback(true);

			throw e;
		}
		catch (Exception e)
		{
			session.rollback(true);

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 当有客户进行回访反馈
	 * 
	 * @param lPatientId
	 * @param trv
	 * @throws SystemException
	 * @throws ReturnVisitException
	 */
	public void onReturnVisitReply(long lVisitId, TReturnVisit rv) throws SystemException, ReturnVisitException
	{
		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			Map<String,Object> prms=new HashMap<>();
			prms.put("visit_id",lVisitId==0?null:lVisitId);

			TReturnVisit trv = null;

			List<TReturnVisit> listRv = session.getMapper(ReturnVisitMapper.class).select(prms);
			if (listRv.size() == 1)
				trv = listRv.get(0);

			if (trv == null)
				throw new ReturnVisitException("此回访不存在");

			if (trv.getReply_time() != null)
				throw new ReturnVisitException("此回访已经反馈过");

			trv.setReply_time(new Date());
			session.getMapper(ReturnVisitMapper.class).update(trv);

			Map<Long, TReturnVisitQA> mapQaReply = new HashMap<Long, TReturnVisitQA>();
			for (TReturnVisitQA qa : rv.getListQA())
				mapQaReply.put(qa.getQuestion_id(), qa);

			// 参考原回访问卷进行验证并更新表，验证失败将回滚。
			TReturnVisitQA qQa = new TReturnVisitQA();
			qQa.setVisit_id(lVisitId);
			List<TReturnVisitQA> listOriQA = session.getMapper(ReturnVisitQAMapper.class).select(qQa);

			for (TReturnVisitQA qaOri : listOriQA)
			{
				TReturnVisitQA qaReply = mapQaReply.get(qaOri.getQuestion_id());
				if (qaReply == null)
					throw new ReturnVisitException("反馈的问题清单与原始回访问题清单不符");

				if (qaReply.getQuestion_type() != qaOri.getQuestion_type()) //|| !qaReply.getQuestion_name().equals(qaOri.getQuestion_name())
					throw new ReturnVisitException("反馈的问题清单与原始回访问题清单不符");

				if (qaReply.getQuestion_type() == QuestionType.SINGLE_CHECK.getCode() && qaReply.getListOpt().size() > 1)
					throw new ReturnVisitException("反馈的问题中单选题出现多选");

				if (qaOri.getQuestion_type() == QuestionType.SINGLE_CHECK.getCode() || qaOri.getQuestion_type() == QuestionType.MULTI_CHECK.getCode())
				{
					TReturnVisitQAOpt qOpt = new TReturnVisitQAOpt();
					qOpt.setQuestion_id(qaOri.getQuestion_id());
					List<TReturnVisitQAOpt> listOptOri = session.getMapper(ReturnVisitQAOptMapper.class).select(qOpt);

					for (TReturnVisitQAOpt optReply : qaReply.getListOpt())
					{
						TReturnVisitQAOpt optOri = listOptOri.get(optReply.getOption_idx()-1);

						optOri.setChecked(1);

						session.getMapper(ReturnVisitQAOptMapper.class).update(optOri);
					}
				}
				else if (qaOri.getQuestion_type() == QuestionType.ANSWER_SCORE.getCode())
				{
					qaOri.setAnswer_score(qaReply.getAnswer_score());

					session.getMapper(ReturnVisitQAMapper.class).update(qaOri);
				}
				else if (qaOri.getQuestion_type() == QuestionType.ANSWER_TEXT.getCode())
				{
					qaOri.setAnswer_text(qaReply.getAnswer_text());

					session.getMapper(ReturnVisitQAMapper.class).update(qaOri);
				}
			}

			session.commit();

		}
		catch (ReturnVisitException e)
		{
			session.rollback(true);

			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			session.rollback(true);

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	public TReturnVisit selectVisitInfoById(Passport passport,long lVisitId) throws NullParameterException, SystemException {
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			return session.getMapper(ReturnVisitMapper.class).selectVisitById(lVisitId);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}

	public void sendReturnVisitByVisit(Passport passport,long lVisitId) throws NullParameterException, ReturnVisitException, SystemException {
		if (passport == null)
			throw new NullParameterException("非法的调用");

		SqlSession session = null;

		try
		{
			session = SessionFactory.getSession();

			TReturnVisit visit=session.getMapper(ReturnVisitMapper.class).selectVisitById(lVisitId);

			if(visit==null)
				throw new ReturnVisitException("未知的问卷！");

			if(visit.getSend_time()!=null)
				throw new ReturnVisitException("该问卷已经发送！");

			WeixinInterfaceService.instance.sendReturnVisit(visit.getPatient_id(), visit);

			visit.setSend_time(new Date());

			session.getMapper(ReturnVisitMapper.class).update(visit);

			session.commit();
		}
		catch (ReturnVisitException e)
		{
			session.rollback(true);

			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			session.rollback(true);

			throw new SystemException();
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
	}
}
