package com.vastsoft.yingtai.module.sys.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.DateTools;
import org.apache.ibatis.logging.LogException;
import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.sys.constants.LogOperatModule;
import com.vastsoft.yingtai.module.sys.constants.LogStatus;
import com.vastsoft.yingtai.module.sys.entity.FSysLog;
import com.vastsoft.yingtai.module.sys.entity.TOperationRecord;
import com.vastsoft.yingtai.module.sys.mapper.LogMapper;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class LogService {
	public static final LogService instance = new LogService();

	private LogService() {
	}
	/**
	 * 添加一条日志
	 * @throws BaseException
	 */
	public void addOperationRecord(TOperationRecord tor) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (tor==null) {
				throw new NullParameterException("tor");
			}
			LogMapper mapper = session.getMapper(LogMapper.class);
			mapper.addLogRecord(tor);
			session.commit();
		} catch (Exception e) {
			if (session!=null)
				session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

//	/**
//	 * 获取日志列表
//	 * 
//	 * @param userId
//	 * @param lOrgId
//	 * @param dtStart
//	 * @param dtEnd
//	 * @param spu
//	 * @return
//	 * @throws BaseException
//	 */
//	public List<TOperationRecord> selectAllLogs(Passport passport, Long lOrgId,Long lUserId, Date dtStart, Date dtEnd,
//			SplitPageUtil spu) throws BaseException {
//		SqlSession session = null;
//		try {
//			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_LOG_MGR))
//				throw new NotPermissionException();
//			session = SessionFactory.getSession();
//			LogMapper mapper=session.getMapper(LogMapper.class);
//			Map<String, Object> prms = new HashMap<String, Object>();
//			prms.put("org_id", lOrgId);
//			prms.put("operator_id", lUserId);
//			prms.put("start", DateTools.dateToString(dtStart));
//			prms.put("end", DateTools.dateToString(dtEnd));
//			int iCount=mapper.searchRecordCount(prms);
//			if (iCount<=0)return new ArrayList<TOperationRecord>();
//			spu.setTotalRow(iCount);
//			prms.put("minRow", spu.getCurrMinRow());
//			prms.put("maxRow", spu.getCurrMaxRowNum());
//			return mapper.searchRecords(prms);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			SessionFactory.closeSession(session);
//		}
//	}
	
	/**搜索系统日志
	 * @param passport
	 * @param strPeratorName
	 * @param strOrgName
	 * @param operType
	 * @param module
	 * @param status
	 * @param dtStart
	 * @param dtEnd
	 * @return
	 * @throws BaseException 
	 */
	public List<FSysLog> searchSysLogList(Passport passport,String strPeratorName,String strOrgName,
			UserType operType,LogOperatModule module,LogStatus status,Date dtStart,Date dtEnd,
			SplitPageUtil spu) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_LOG_MGR))
			throw new LogException("你没有日志管理的权限");
		SqlSession session=null;
		try {
			session=SessionFactory.getSession();
			if (spu==null)throw new LogException("日志查看内容较多，必须分页！");
			LogMapper mapper=session.getMapper(LogMapper.class);
			Map<String, Object> mapArg=new HashMap<String,Object>();
			mapArg.put("operator_name", strPeratorName);
			mapArg.put("org_name", strOrgName);
			mapArg.put("operator_type", operType==null?null:operType.getCode());
			mapArg.put("inf_name", module==null?null:module.getInf_name());
			mapArg.put("status", status==null?null:status.getCode());
			mapArg.put("start", dtStart==null?null: DateTools.dateToString(DateTools.getStartTimeByDay(dtStart)));
			mapArg.put("end", dtEnd==null?null:DateTools.dateToString(DateTools.getLastTimeByDay(dtEnd)));
			int iCount=mapper.searchSysLogCount(mapArg);
			spu.setTotalRow(iCount);
			if (iCount<=0)return null;
			mapArg.put("minRow", spu.getCurrMinRowNum());
			mapArg.put("maxRow", spu.getCurrMaxRowNum());
			return mapper.searchSysLogList(mapArg);
		} catch (Exception e) {
			throw e;
		}finally{
			SessionFactory.closeSession(session);
		}
	}
}
