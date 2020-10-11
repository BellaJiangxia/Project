package com.vastsoft.yingtai.module.msg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.DateTools;
import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.service.sms.MsgSenderException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.msg.constants.MsgStatus;
import com.vastsoft.yingtai.module.msg.constants.MsgType;
import com.vastsoft.yingtai.module.msg.entity.FMessage;
import com.vastsoft.yingtai.module.msg.entity.TMessage;
import com.vastsoft.yingtai.module.msg.exception.MessageOperatException;
import com.vastsoft.yingtai.module.msg.mapper.MsgMapper;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class MsgService {
	public final static MsgService instance = new MsgService();
	private MsgService(){}
	/**通过UID获取短信
	 * @throws BaseException */
	synchronized List<TMessage> queryMessageByUid(Passport passport,String strUid) throws BaseException{
		SqlSession session=null;
		try {
			session=SessionFactory.getSession();
			MsgMapper mapper = session.getMapper(MsgMapper.class);
			return mapper.queryMessageByUid(strUid);
		} catch (Exception e) {
			throw e;
		}finally {
			SessionFactory.closeSession(session);
		}
	}
	/**查询未发送且应该发送的短信
	 * @throws BaseException */
	synchronized List<String> queryNeedSendMessageUidList(Passport passport) throws BaseException{
		SqlSession session=null;
		try {
			session=SessionFactory.getSession();
			MsgMapper mapper = session.getMapper(MsgMapper.class);
			return mapper.queryNeedSendMessageUidList(DateTools.dateToString(new Date()));
		} catch (Exception e) {
			throw e;
		}finally {
			SessionFactory.closeSession(session);
		}
	}
	
//	/**修改短信状态
//	 * @throws BaseException */
//	private void modifyMessageStatus(String strUid,MsgStatus msgStatus) throws BaseException{
//		
//	}
	/**取消短消息发送
	 * @param passport
	 * @param lDiagnosisId
	 * @param msgType
	 * @param session
	 * @throws BaseException
	 */
	public synchronized void cancelSendMessage(Passport passport,long lDiagnosisId,MsgType msgType,SqlSession session) throws BaseException{
		try {
			MsgMapper mapper = session.getMapper(MsgMapper.class);
			Map<String, Object> mapArg=new HashMap<String,Object>();
			mapArg.put("status",MsgStatus.CANCELED.getCode());
			mapArg.put("diagnosis_id", lDiagnosisId);
			mapArg.put("msg_type", msgType.getCode());
			mapper.modifyMessageStatusByDiagnosisIdAndType(mapArg);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**完成短消息发送
	 * @param passport
	 * @param strUid
	 * @throws BaseException
	 */
	synchronized void finishSendMessage(Passport passport,String strUid) throws BaseException{
		if (strUid==null||strUid.trim().isEmpty())return;
		strUid=strUid.trim();
		SqlSession session=null;
		try {
			session=SessionFactory.getSession();
			MsgMapper mapper = session.getMapper(MsgMapper.class);
			Map<String, Object> mapArg=new HashMap<String,Object>();
			mapArg.put("status",MsgStatus.SUCCESS.getCode());
			mapArg.put("send_time", DateTools.dateToString(new Date()));
			mapArg.put("uid", strUid);
			mapper.modifyMessageStatusAndSendTimeByUid(mapArg);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		}finally {
			SessionFactory.closeSession(session);
		}
	}
	/**添加待发消息
	 * @param passport
	 * @param diagnosis
	 * @param msgType
	 * @throws BaseException 
	 */
	public synchronized void addWaitSendPlan(Passport passport,TDiagnosis diagnosis,Long lTranferUserId,MsgType msgType,SqlSession session) throws BaseException{
		try {
			List<TBaseUser> listBaseUser=null;
			List<TMessage> listMessage=new ArrayList<TMessage>();
			if (msgType.equals(MsgType.DIAGNOSIS_REMIND)) {
				TOrganization org=OrgService.instance.searchOrgById(diagnosis.getDiagnosis_org_id());
				if (org==null)
					throw new MsgSenderException("目标机构未找到！");
				listBaseUser=UserService.instance.selectDoctorUserByOrg(passport, org.getId());
				if (listBaseUser==null||listBaseUser.size()<=0)return;
				for (TBaseUser tBaseUser : listBaseUser) {
					listMessage.add(this.makeMessage(tBaseUser,org.getOrg_name(), diagnosis, msgType));
				}
			}else if(msgType.equals(MsgType.DIAGNOSISED_REMIND)){
				TBaseUser user=UserService.instance.queryBaseUserById(passport,diagnosis.getRequest_user_id());
				TOrganization org=OrgService.instance.searchOrgById(diagnosis.getRequest_org_id());
				listMessage.add(this.makeMessage(user,org.getOrg_name(),diagnosis, msgType));
			}else if (msgType.equals(MsgType.NEW_TRANFAR_REMIND)) {
				if (lTranferUserId==null||lTranferUserId<=0)
					throw new MessageOperatException("请指定有效的用户ID！");
				TBaseUser user=UserService.instance.queryBaseUserById(passport,lTranferUserId);
				TOrganization org=OrgService.instance.searchOrgById(diagnosis.getDiagnosis_org_id());
				listMessage.add(this.makeMessage(user,org.getOrg_name(), diagnosis, msgType));
			}else {
				throw new MessageOperatException("暂不支持其他类型的消息！");
			}
			addWaitSendMessage(listMessage,session);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private TMessage makeMessage(TBaseUser user,String strOrgName, TDiagnosis diagnosis,MsgType msgType) throws BaseException{
		TMessage message=new TMessage();
		message.setDiagnosis_id(diagnosis.getId());
		message.setMsg_type(msgType.getCode());
		message.setRecv_mobile(user.getMobile());
		message.setRecv_user_id(user.getId());
		int minutes=SysService.instance.takeRemindTimeMinutes();
		message.setPlan_time(new Date(System.currentTimeMillis()+(minutes*60*1000)));
		StringBuilder sbb=new StringBuilder();
		sbb.append("尊敬的").append(user.getUser_name()).append('，');
		if (msgType.equals(MsgType.DIAGNOSIS_REMIND)) {
			sbb.append("您所在的").append(strOrgName);
			sbb.append("有新的影像未及时诊断，请及时处理。");
		}else if(msgType.equals(MsgType.DIAGNOSISED_REMIND)){
			sbb.append("您所在的").append(strOrgName);
			sbb.append("有新提交诊断的影像已经诊断完成，请及时查看和打印报告。");
		}else if (msgType.equals(MsgType.NEW_TRANFAR_REMIND)) {
			sbb.append("您所在的").append(strOrgName);
			sbb.append("有新移交给您的诊断，请及时处理。");
		}else {
			throw new MessageOperatException("暂不支持其他类型的消息！");
		}
		sbb.append("【影泰科技】");
		message.setContent(sbb.toString());
		return message;
	}
	
	private String addWaitSendMessage(List<TMessage> listMessage, SqlSession session) throws BaseException {
		if (listMessage==null||listMessage.size()<=0)return null;
		try {
			MsgMapper mapper = session.getMapper(MsgMapper.class);
			String uid=CommonTools.getUUID();
			for (TMessage tMessage : listMessage) {
				tMessage.setStatus(MsgStatus.WAITING.getCode());
				tMessage.setUid(uid);
				mapper.addMessage(tMessage);
			}
			return uid;
		} catch (Exception e) {
			throw e;
		}
	}

	/**查询短消息
	 * @param msg_status */
	public List<FMessage> searchMsg(Passport passport, MsgStatus msg_status, Date dtStart, Date dtEnd,SplitPageUtil spu) throws BaseException {
		if (spu==null)throw new MsgSenderException("缺少分页参数，此查询必须分页！");
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_MESSAGE_MGR))
			throw new NotPermissionException();
		SqlSession session = null;
		try {
			session=SessionFactory.getSession();
			MsgMapper mapper=session.getMapper(MsgMapper.class);
			Map<String, Object> mapArg=new HashMap<String,Object>();
			if (dtStart !=null && dtEnd !=null){
				mapArg.put("start",DateTools.dateToString(dtStart));
				mapArg.put("end",DateTools.dateToString(dtEnd));
			}
			mapArg.put("status", msg_status==null?null:msg_status.getCode());
			int iCount=mapper.searchMsgCount(mapArg);
			spu.setTotalRow(iCount);
			if (iCount<=0)return new ArrayList<FMessage>();
			mapArg.put("minRow", spu.getCurrMinRowNum());
			mapArg.put("maxRow", spu.getCurrMaxRowNum());
			return mapper.searchMsg(mapArg);
		} catch (Exception e) {
			throw e;
		}finally{
			SessionFactory.closeSession(session);
		}
	}
}
