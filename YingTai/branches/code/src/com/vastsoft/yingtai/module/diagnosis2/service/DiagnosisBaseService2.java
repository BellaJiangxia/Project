package com.vastsoft.yingtai.module.diagnosis2.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.exception.ReportException;
import com.vastsoft.yingtai.module.diagnosis2.assist.DiagnosisSearchParams2;
import com.vastsoft.yingtai.module.diagnosis2.assist.HandleSearchParam;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.exception.RequestException;
import com.vastsoft.yingtai.module.diagnosis2.mapper.DiagnosisMapper;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.services.RequestTranferService;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

abstract class DiagnosisBaseService2 {

	// /**
	// * 通过分享的ID获取分享发言
	// *
	// * @throws BaseException
	// */
	// public List<FReportShareSpeech> searchDiagnosisShareSpeech(Passport
	// passport, long lDiagnosisShareid,
	// SplitPageUtil spu, SqlSession session) throws BaseException {
	// try {
	// if (spu == null)
	// spu = new SplitPageUtil(1, 10);
	// DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
	// Map<String, Object> mapArg = new HashMap<String, Object>();
	// mapArg.put("share_id", lDiagnosisShareid);
	// int count = mapper.searchFDiagnosisShareSpeechCount(mapArg);
	// spu.setTotalRow(count);
	// if (count <= 0)
	// return null;
	// mapArg.put("minRow", spu.getCurrMinRowNum());
	// mapArg.put("maxRow", spu.getCurrMaxRowNum());
	// return mapper.searchFDiagnosisShareSpeech(mapArg);
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// }
	// }

	/**
	 * 通过ID查询一个申请的详细信息，默认包含移交的诊断
	 * 
	 * @param diagnosisId
	 * @param with_img_series 是否包含影像序列列表
	 * @param session
	 * @return
	 * @throws DicomImgException
	 * @throws RequestException
	 */
	public FDiagnosis queryDiagnosisById(long diagnosisId,boolean with_img_series, SqlSession session)
			throws DicomImgException, RequestException {
		FDiagnosis result = this.queryDiagnosisById(diagnosisId, with_img_series, true, session);
		return result;
	}

	/**
	 * 通过ID查询一个申请的详细信息
	 * 
	 * @param diagnosisId
	 * @param request_tranfer_flag
	 *            //是否查询移交的申请
	 * @param session
	 * @return
	 * @throws RequestException 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws OrgOperateException
	 * @throws ReportException
	 */
	public FDiagnosis queryDiagnosisById(long diagnosisId, boolean with_img_series,boolean request_tranfer_flag, SqlSession session) throws RequestException {
		DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
		dsp.setId(diagnosisId);
		dsp.setRequest_tranfer_flag(request_tranfer_flag);
		List<FDiagnosis> listFDiagnosis = this.searchDiagnosis(dsp, session);
		if (CollectionTools.isEmpty(listFDiagnosis))
			return null;
		FDiagnosis result = listFDiagnosis.get(0);
		String request_tranfer_org_name_path = RequestTranferService.instance.takeTranferOrgNamePath(result);
		result.setRequest_tranfer_org_name_path(request_tranfer_org_name_path);
		return result;
	}

	// /**
	// * 通过分享ID查询分享
	// *
	// * @throws BaseException
	// */
	// protected FReportShare selectDiagnosisShareById(Passport passport, Long
	// lDiagnosisShareId,
	// DiagnosisMapper mapper) throws BaseException {
	// List<FReportShare> listDiagnosisShare =
	// this.searchDiagnosisShare(passport, lDiagnosisShareId, null, null,
	// null, null, null, null, null, null, null, null, null, null, mapper);
	// if (listDiagnosisShare == null || listDiagnosisShare.size() <= 0)
	// throw new DiagnosisException("指定的分享没找到！");
	// return listDiagnosisShare.get(0);
	// }

	// /**
	// * 搜索分享
	// *
	// * @param passport
	// * @param lDiagnosisShareId
	// * 诊断分享ID
	// * @param lDiagnosisId
	// * 诊断ID
	// * @param lMedicalHisId
	// * 病例ID
	// * @param lShareOrgId
	// * 分享机构ID
	// * @param strMedicalHisNum
	// * 病历号
	// * @param strShareOrgName
	// * 分享机构名称
	// * @param lShareUserId
	// * 分享用户ID
	// * @param lCloseShareUserId
	// * 关闭用户ID
	// * @param strSymptom
	// * 症状
	// * @param dtStart
	// * 开始时间
	// * @param dtEnd
	// * 结束时间
	// * @param shareStatus
	// * 分享状态
	// * @param spu
	// * 分页
	// * @return
	// * @throws BaseException
	// */
	// public List<FReportShare> searchDiagnosisShare(Passport passport, Long
	// lDiagnosisShareId, Long lDiagnosisId,
	// Long lMedicalHisId, Long lShareOrgId, String strMedicalHisNum, String
	// strShareOrgName, Long lShareUserId,
	// Long lCloseShareUserId, String strSymptom, Date dtStart, Date dtEnd,
	// DiagnosisShareStatus2 shareStatus,
	// SplitPageUtil spu, DiagnosisMapper mapper) throws BaseException {
	// try {
	// Map<String, Object> mapArg = new HashMap<String, Object>();
	// mapArg.put("id", lDiagnosisShareId);
	// mapArg.put("diagnosis_id", lDiagnosisId);
	// mapArg.put("org_id", lShareOrgId);
	// mapArg.put("share_user_id", lShareUserId);
	// mapArg.put("medical_his_num", strMedicalHisNum);
	// mapArg.put("close_share_user_id", lCloseShareUserId);
	// mapArg.put("share_org_name", strShareOrgName);
	// mapArg.put("symptom", strSymptom);
	// mapArg.put("medical_his_id", lMedicalHisId);
	// mapArg.put("status", shareStatus == null ? null : shareStatus.getCode());
	// mapArg.put("start", DateTools.dateToString(dtStart));
	// mapArg.put("end", DateTools.dateToString(dtEnd));
	// if (spu != null) {
	// int count = mapper.searchFDiagnosisShareCount(mapArg);
	// if (count <= 0)
	// return null;
	// spu.setTotalRow(count);
	// mapArg.put("minRow", spu.getCurrMinRowNum());
	// mapArg.put("maxRow", spu.getCurrMaxRowNum());
	// }
	// return mapper.searchFDiagnosisShare(mapArg);
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// }
	// }

	// /**
	// * 查询处理
	// *
	// * @param passport
	// * @param notSelf
	// * 是否 不包含自己的处理
	// * @param lRequestOrgId
	// * @param lPartTypeId
	// * @param lDeviceTypeId
	// * @param gender
	// * @param strSickerName
	// * @param strMedicalHisNum
	// * @param lDiagnosisId
	// * @param lCurrUserId
	// * @param lNextUserId
	// * @param lOrgId
	// * 诊断机构
	// * @param dtStart
	// * @param dtEnd
	// * @param status
	// * @param strOrderBy
	// * @param spu
	// * @return
	// * @throws BaseException
	// */
	// public List<FDiagnosisHandle2> searchTranferToAuditHandle(Passport
	// passport, Long lRequestOrgId, Long lPartTypeId,
	// Long lDeviceTypeId, Gender gender, String strMedicalHisNum, Long
	// lCurrUserId, SplitPageUtil spu)
	// throws BaseException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// DiagnosisMapper2 mapper = session.getMapper(DiagnosisMapper2.class);
	// Map<String, Object> mapArg = new HashMap<String, Object>();
	// mapArg.put("request_org_id", lRequestOrgId);
	// mapArg.put("part_type_id", lPartTypeId);
	// mapArg.put("device_type_id", lDeviceTypeId);
	// mapArg.put("sicker_gender", gender == null ? null : gender.getCode());
	// mapArg.put("medical_his_num", strMedicalHisNum);
	// mapArg.put("curr_user_id", lCurrUserId);
	// mapArg.put("next_user_id", 0L);
	// mapArg.put("org_id", passport.getOrgId());
	// // mapArg.put("start", DateTools.dateToString(dtStart));
	// // mapArg.put("end", DateTools.dateToString(dtEnd));
	// mapArg.put("status", DiagnosisHandleStatus2.TRANFERED.getCode());
	// mapArg.put("order_by", "TRANSFER_TIME");
	// if (true)
	// mapArg.put("self_id", passport.getUserId());
	// if (spu != null) {
	// int iCount = mapper.searchFDiagnosisHandleCount(mapArg);
	// if (iCount <= 0)
	// return null;
	// spu.setTotalRow(iCount);
	// mapArg.put("minRow", spu.getCurrMinRowNum());
	// mapArg.put("maxRow", spu.getCurrMaxRowNum());
	// }
	// return mapper.searchFDiagnosisHandle(mapArg);
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	/**
	 * 查询指定用户书写中的处理,指定在哪个机构中
	 * 
	 * @throws BaseException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public FDiagnosisHandle queryUnFinishHandle(long userId, long orgId) throws IllegalArgumentException, IllegalAccessException {
		HandleSearchParam hsp = new HandleSearchParam();
		hsp.setCurr_user_id(userId);
		hsp.setOrg_id(orgId);
		hsp.setStatus(DiagnosisHandleStatus2.WRITING);
		List<FDiagnosisHandle> listDiagnosis = this.searchDiagnosisHandle(hsp);
		return listDiagnosis == null || listDiagnosis.size() <= 0 ? null : listDiagnosis.get(0);
	}

	/**
	 * 查询自己在当前机构中正在书写中的处理
	 * 
	 * @throws BaseException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public FDiagnosisHandle queryUnFinishHandle(Passport passport) throws IllegalArgumentException, IllegalAccessException {
		return this.queryUnFinishHandle(passport.getUserId(), passport.getOrgId());
	}

	public List<FDiagnosisHandle> searchDiagnosisHandle(HandleSearchParam hsp) throws IllegalArgumentException, IllegalAccessException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			Map<String, Object> mapArg = hsp.buildMap();
			if (hsp.getSpu() != null) {
				int iCount = mapper.searchDiagnosisHandleCount(mapArg);
				hsp.getSpu().setTotalRow(iCount);
				if (iCount <= 0)
					return null;
				mapArg.put("minRow", hsp.getSpu().getCurrMinRowNum());
				mapArg.put("maxRow", hsp.getSpu().getCurrMaxRowNum());
			}
			return mapper.searchDiagnosisHandle(mapArg);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 搜索诊断申请，申请和诊断的机构管理员或后台管理员
	 * 
	 * @param searchObj
	 * @return
	 * @throws RequestException 
	 * @throws BaseException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	protected List<FDiagnosis> searchDiagnosis(DiagnosisSearchParams2 dsp, SqlSession session) throws RequestException {
		DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
		Map<String, Object> mapArg;
		try {
			mapArg = dsp.buildMap();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RequestException(e);
		}
		if (dsp.getSpu() != null) {
			int iCount = mapper.selectDiagnosisCount(mapArg);
			dsp.getSpu().setTotalRow(iCount);
			if (iCount <= 0)
				return null;
			mapArg.put("minRow", dsp.getSpu().getCurrMinRowNum());
			mapArg.put("maxRow", dsp.getSpu().getCurrMaxRowNum());
		}
		return mapper.selectDiagnosis(mapArg);
	}

	/**
	 * 搜索诊断申请，申请和诊断的机构管理员或后台管理员
	 * 
	 * @param searchObj
	 * @return
	 * @throws Exception
	 */
	public List<FDiagnosis> searchDiagnosis(DiagnosisSearchParams2 searchObj) throws Exception {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return this.searchDiagnosis(searchObj, session);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}
}
