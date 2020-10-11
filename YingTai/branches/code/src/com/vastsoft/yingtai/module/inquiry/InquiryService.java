package com.vastsoft.yingtai.module.inquiry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.SystemException;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.inquiry.entity.TInquiry;
import com.vastsoft.yingtai.module.inquiry.entity.TInquiryReply;
import com.vastsoft.yingtai.module.inquiry.exceptions.InquiryException;
import com.vastsoft.yingtai.module.inquiry.mapper.InquiryMapper;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.weixin.entity.InquiryCount;
import com.vastsoft.yingtai.module.weixin.service.WeixinInterfaceService;
import com.vastsoft.yingtai.module.weixin.service.exceptions.ReleationException;

public class InquiryService
{
	public final static InquiryService instance = new InquiryService();

	//测试使用问诊次数
	private final int maxCount =3;

	private InquiryService()
	{
	}

	public long onUserInquiry(long lPatientId, long lOrgId, String strContent,Long lInquiryId,List<Long> listCaseId,List<String> listImg,InquiryCount lRestCount) throws Exception {
		if(strContent==null || strContent.isEmpty())
			throw new InquiryException("询问内容不能为空");

		TOrganization org= null;
		try {
			org = OrgService.instance.searchOrgById(lOrgId);
		} catch (OrgOperateException e) {
			throw new InquiryException(e.getMessage());
		}

		if(org==null)
			throw new InquiryException("未知的机构");

		if(!OrgStatus.VALID.equals(org.getOrgStatus()))
			throw new InquiryException("无效的机构");

		//TODO 是否检查病人医院关系

		//检查病人病历关系
		if(listCaseId!=null&&!listCaseId.isEmpty()){
			try {
				List<FCaseHistory> chls=CaseHistoryService.instance.queryCaseHistoryByIds(StringTools.listToString(listCaseId,','));
				if(chls!=null&&!chls.isEmpty()){
					for(FCaseHistory fc:chls){
						if(fc.getPatient_id()!=lPatientId) throw new InquiryException("患者与病历不匹配");
					}
				}
			} catch (BaseException e) {
				throw new InquiryException(e.getMessage());
			}
		}

		SqlSession session=null;
		int count=1;
		try {
			session = SessionFactory.getSession();
			InquiryMapper mapper = session.getMapper(InquiryMapper.class);

			if (lInquiryId == null) {
				TInquiry tInquiry = new TInquiry();
				tInquiry.setOrg_id(lOrgId);
				tInquiry.setPatient_id(lPatientId);
				tInquiry.setCase_id(listCaseId == null || listCaseId.isEmpty() ? 0 : listCaseId.get(0));
				tInquiry.setInquiry_content(strContent);
				tInquiry.setInquiry_time(new Date());
				JSONArray arr=new JSONArray();
				JSONObject jo=new JSONObject();
				jo.put("idx",count);
				jo.put("images",listImg!=null&&!listImg.isEmpty()?new JSONArray(listImg):null);
				arr.put(jo);
				tInquiry.setInquiry_image(arr.toString());

				mapper.insertInquiry(tInquiry);
				session.commit();
				lInquiryId=tInquiry.getInquiry_id();
			} else {
				TInquiry tInquiry = mapper.selectInquiryById(lInquiryId);

				if (tInquiry == null)
					throw new InquiryException("未知的问诊");

				if (tInquiry.getPatient_id() != lPatientId)
					throw new InquiryException("问诊患者不一致");

				if (tInquiry.getOrg_id() != lOrgId)
					throw new InquiryException("问诊机构不一致");

				if(tInquiry.getReplies()!=null&&!tInquiry.getReplies().isEmpty()){
					List<TInquiryReply> replyList=tInquiry.getReplies();

					for(TInquiryReply r:replyList){
						if(ReplyType.REPLY_Q.equals(r.getReplyType())) count++;
					}
				}

				count++;

				if(this.maxCount-count<0) throw new InquiryException("已超过最大询问次数");

				TInquiryReply tr = new TInquiryReply();
				tr.setOrg_id(lOrgId);
				tr.setInquiry_id(lInquiryId);
				tr.setReplier_id(lPatientId);
				tr.setReply_content(strContent);
				tr.setReply_time(new Date());
				tr.setReply_type(ReplyType.REPLY_Q.getCode());

				mapper.insertReply(tr);

				tInquiry.setLast_read_time(null);
				if(listImg!=null&&!listImg.isEmpty()){
					List<Map<String, Object>> imgs=tInquiry.getImages();

					if(imgs==null) imgs=new ArrayList<>();

					Map<String,Object> map=new HashMap<>();
					map.put("idx",count);
					map.put("images",listImg);
					imgs.add(map);
					tInquiry.setImageJsonString(imgs);
				}
				mapper.modifyInquiry(tInquiry);

				session.commit();

			}

			if(lRestCount!=null) lRestCount.setCount(maxCount-count);

			return lInquiryId;
		}catch (InquiryException e){
			if(session!=null) session.rollback();
			throw e;
		} catch (Exception e) {
			System.out.println("ERROR:\t"+e.getMessage());
			if(session!=null) session.rollback();
			throw new SystemException();
		}finally {
			SessionFactory.closeSession(session);
		}
	}

	public void sendInquiryReply(UserService.Passport passport,long lInquirySessionId, String strContent) throws NullParameterException, InquiryException, SystemException, ReleationException {
		if(passport==null)
			throw new NullParameterException("非法调用");

		if(strContent==null || strContent.isEmpty())
			throw new InquiryException("回复内容不能为空!");

		SqlSession session=null;

		try {
			session=SessionFactory.getSession();

			TInquiry tInquiry=session.getMapper(InquiryMapper.class).selectInquiryByIdAndLock(lInquirySessionId);

			if (tInquiry == null)
        throw new InquiryException("未知的问诊");

			if(tInquiry.getOrg_id()!=passport.getOrgId())
        throw new InquiryException("无法回复其他医院的患者问诊");

			TInquiryReply tr=new TInquiryReply();
			tr.setInquiry_id(lInquirySessionId);
			tr.setOrg_id(passport.getOrgId());
			tr.setReplier_id(passport.getUserId());
			tr.setReply_content(strContent);
			tr.setReply_time(new Date());
			tr.setReply_type(ReplyType.REPLY_A.getCode());

			session.getMapper(InquiryMapper.class).insertReply(tr);

			WeixinInterfaceService.instance.replyInquiry(tInquiry.getPatient_id(),lInquirySessionId,strContent);

			tInquiry.setLast_read_time(new Date());
			session.getMapper(InquiryMapper.class).readInquiry(tInquiry);

			session.commit(true);

		} catch (InquiryException | ReleationException e) {
			if(session!=null) session.rollback(true);
			throw e;
		}catch(Exception e){
			if(session!=null) session.rollback(true);
			throw new SystemException();
		}finally {
			SessionFactory.closeSession(session);
		}
	}

	public TInquiry getInquiryById(UserService.Passport passport,long lInquiryId) throws NullParameterException, SystemException {
		if(passport==null)
			throw new NullParameterException("非法调用");

		SqlSession session=null;

		try {
			session=SessionFactory.getSession();
			return session.getMapper(InquiryMapper.class).selectInquiryById(lInquiryId);
		} catch (Exception e) {
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TInquiry> getInquiryList(UserService.Passport passport,String strKeyword,boolean isNew,SplitPageUtil spu) throws NullParameterException, SystemException {
		if(passport==null)
			throw new NullParameterException("非法调用");

		SqlSession session=null;

		try {
			session= SessionFactory.getSession();
			InquiryMapper mapper=session.getMapper(InquiryMapper.class);
			Map<String,Object> prms=new HashMap<>();

			prms.put("org_id",passport.getOrgId());
			prms.put("keyword",strKeyword==null || strKeyword.isEmpty()?null:strKeyword);
			prms.put("is_new",isNew?1:null);

			if(spu!=null){
        int count=mapper.selectInquiryCount(prms);
        spu.setTotalRow(count);

        if(count==0) return null;

        prms.put("begin_idx",spu.getCurrMinRowNum());
        prms.put("end_idx",spu.getCurrMaxRowNum());
      }

			return mapper.selectTInquiryList(prms);
		} catch (Exception e) {
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TInquiry> getReplyList(UserService.Passport passport,String strKeyword,Long lReplyUserId,SplitPageUtil spu) throws NullParameterException, SystemException {
		if (passport == null)
			throw new NullParameterException("非法调用");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			InquiryMapper mapper = session.getMapper(InquiryMapper.class);
			Map<String, Object> prms = new HashMap<>();

			prms.put("org_id", passport.getOrgId());
			prms.put("keyword", strKeyword == null || strKeyword.isEmpty() ? null : strKeyword);
			prms.put("reply_user", lReplyUserId);

			if (spu != null) {
				int count = mapper.selectReplyCount(prms);
				spu.setTotalRow(count);

				if (count == 0) return null;

				prms.put("begin_idx", spu.getCurrMinRowNum());
				prms.put("end_idx", spu.getCurrMaxRowNum());
			}

			return mapper.selectReplyList(prms);
		} catch (Exception e) {
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public int getNewInquiryCount(UserService.Passport passport){
		if(passport==null) return 0;

		SqlSession session=null;

		try {
			session= SessionFactory.getSession();
			Map<String,Object> prms=new HashMap<>();
			prms.put("org_id",passport.getOrgId());
			prms.put("is_new",1);

			return session.getMapper(InquiryMapper.class).selectInquiryCount(prms);
		} catch (Exception e) {
			return 0;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

}


