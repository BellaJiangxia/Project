package com.vastsoft.yingtai.module.org.realtion.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationPatientInfoShareType;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationStatus;
import com.vastsoft.yingtai.module.org.realtion.constants.PublishReportType;
import com.vastsoft.yingtai.module.org.realtion.entity.FOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelationConfig;
import com.vastsoft.yingtai.module.org.realtion.exception.OrgRelationException;
import com.vastsoft.yingtai.module.org.realtion.mapper.OrgRelationMapper;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class OrgRelationService {
	public static final OrgRelationService instance = new OrgRelationService();

	/**
	 * 查询我的当前机构的好友机构
	 * 
	 * @throws Exception
	 */
	public List<FOrgRelation> searchOrgRelation(SearchOrgRelationConfigParam orcsp) throws Exception {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			Map<String, Object> mapArg = orcsp.buildMap();
			if (orcsp.getSpu() != null) {
				int count = mapper.selectOrgRelationCount(mapArg);
				orcsp.getSpu().setTotalRow(count);
				if (count <= 0)
					return null;
				mapArg.put("minRow", orcsp.getSpu().getCurrMinRowNum());
				mapArg.put("maxRow", orcsp.getSpu().getCurrMaxRowNum());
			}
			List<TOrgRelation> listOrgRelation = mapper.selectOrgRelation(mapArg);
			if (CollectionTools.isEmpty(listOrgRelation))
				return null;
			List<FOrgRelation> result = new ArrayList<FOrgRelation>();
			for (TOrgRelation orgRelation : listOrgRelation) {
				if (orgRelation == null)
					continue;
				FOrgRelation forn = new FOrgRelation();
				forn.setOrg_relation(orgRelation);
				long relation_org_id = 0;
				if (orgRelation.getOrg_id() == orcsp.getSelf_org_id()) {
					relation_org_id = orgRelation.getFriend_org_id();
				} else if (orgRelation.getFriend_org_id() == orcsp.getSelf_org_id()) {
					relation_org_id = orgRelation.getOrg_id();
				}
				{
					TOrgRelationConfig orgRelationConfig = this.selectOrgRelationConfig(orgRelation.getId(),
							orcsp.getSelf_org_id());
					if (orgRelationConfig == null) {
						orgRelationConfig = new TOrgRelationConfig(orgRelation.getId(), orcsp.getSelf_org_id());
						orgRelationConfig.setShare_patient_info(OrgRelationPatientInfoShareType.NONE.getCode());
						if (orgRelation.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
							orgRelationConfig.setPublish_report_org_id(-1);
						} else if (orgRelation.getPublish_report_type() == PublishReportType.Diagnosiser.getCode()) {
							orgRelationConfig.setPublish_report_org_id(relation_org_id);
						} else if (orgRelation.getPublish_report_type() == PublishReportType.Requestor.getCode()) {
							orgRelationConfig.setPublish_report_org_id(orcsp.getSelf_org_id());
						} else {

						}
					}
					forn.setOrg_relation_config(orgRelationConfig);
				}
				{
					TOrgRelationConfig relationOrgRelationConfig = this.selectOrgRelationConfig(orgRelation.getId(),
							relation_org_id);
					if (relationOrgRelationConfig == null) {
						relationOrgRelationConfig = new TOrgRelationConfig(orgRelation.getId(), relation_org_id);
						relationOrgRelationConfig.setShare_patient_info(OrgRelationPatientInfoShareType.NONE.getCode());
						if (orgRelation.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
							relationOrgRelationConfig.setPublish_report_org_id(-1);
						} else if (orgRelation.getPublish_report_type() == PublishReportType.Diagnosiser.getCode()) {
							relationOrgRelationConfig.setPublish_report_org_id(orcsp.getSelf_org_id());
						} else if (orgRelation.getPublish_report_type() == PublishReportType.Requestor.getCode()) {
							relationOrgRelationConfig.setPublish_report_org_id(relation_org_id);
						} else {
						}
					}
					forn.setRelation_org_relation_config(relationOrgRelationConfig);
				}
				TOrganization relation_org = OrgService.instance.searchOrgById(relation_org_id);
				forn.setRelation_org_code(relation_org.getOrg_code());
				forn.setRelation_org_description(relation_org.getDescription());
				forn.setRelation_org_id(relation_org_id);
				forn.setRelation_org_name(relation_org.getOrg_name());
				forn.setRelation_org_permission(relation_org.getPermission());
				forn.setRelation_org_type(relation_org.getType());
				TDicValue dv = SysService.instance.queryDicValueById(relation_org.getType());
				forn.setRelation_org_type_name(dv == null ? "" : dv.getValue());
				result.add(forn);
			}
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询请求我的好友数量
	 * 
	 * @param passport
	 * @return
	 * @throws NullParameterException
	 * @throws DbException
	 * @throws OrgRelationException
	 */
	public int selectMyRequestFriendsCount(Passport passport) throws NullParameterException, OrgRelationException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", passport.getOrgId());
			prms.put("status", OrgStatus.APPROVING.getCode());
			return session.getMapper(OrgRelationMapper.class).selectWaitApproveFriendsCount(prms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private TOrgRelationConfig selectOrgRelationConfig(long orgRelationId, long orgId) throws OrgRelationException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			return mapper.selectRelationConfigByOrc(new TOrgRelationConfig(orgRelationId, orgId));
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 通过申请机构和诊断机构查询发布报告机构
	 * 
	 * @param requestOrgId
	 * @param diagnosisOrgId
	 * @return
	 * @throws OrgRelationException
	 * @throws OrgOperateException
	 */
	public TOrganization queryPublishOrgIdByRequestOrgIdAndDiagnosisOrgId(long requestOrgId, long diagnosisOrgId)
			throws OrgRelationException, OrgOperateException {
		TOrgRelation tor = this.selectOrgRelationByOrgIdAndAnotherOrgId(requestOrgId, diagnosisOrgId);
		if (tor == null)
			throw new OrgRelationException("指定的两个机构之间没有好友关系！");
		if (tor.getPublish_report_type() == PublishReportType.Diagnosiser.getCode()) {
			return OrgService.instance.queryOrgById(diagnosisOrgId);
		} else if (tor.getPublish_report_type() == PublishReportType.Requestor.getCode()) {
			return OrgService.instance.queryOrgById(requestOrgId);
		} else if (tor.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
			TOrgRelationConfig config = this.selectOrgRelationConfig(tor.getId(), requestOrgId);
			if (config == null)
				throw new OrgRelationException("没有指定报告发布身份机构！");
			return OrgService.instance.queryOrgById(config.getPublish_report_org_id());
		} else {
			throw new OrgRelationException("无效的报告发布身份机构！");
		}
	}

	/**
	 * 检查两个机构是否是好友
	 * 
	 * @throws OrgRelationException
	 * 
	 * @throws BaseException
	 */
	public boolean checkFirendOrg(long orgId, long anotherOrgId) throws OrgRelationException {
		TOrgRelation tor = this.selectOrgRelationByOrgIdAndAnotherOrgId(orgId, anotherOrgId);
		if (tor == null)
			return false;
		if (tor.getStatus() != OrgRelationStatus.VALID.getCode())
			return false;
		return true;
	}

	private TOrgRelation selectOrgRelationByOrgIdAndAnotherOrgId(long orgId, long anotherOrgId)
			throws OrgRelationException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			return mapper.selectOrgRelationByCoupleOrgId(new TOrgRelation(orgId, anotherOrgId));
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// public OrgRelationList searchRelationOrg(OrgRelationSearchParam orsp)
	// throws OrgRelationException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
	// Map<String, Object> mapArg = orsp.buildMap();
	// if (orsp.getSpu() != null) {
	// int count = mapper.selectRelationOrgMapperCount(mapArg);
	// orsp.getSpu().setTotalRow(count);
	// if (count <= 0)
	// return null;
	// mapArg.put("minRow", orsp.getSpu().getCurrMinRowNum());
	// mapArg.put("maxRow", orsp.getSpu().getCurrMaxRowNum());
	// }
	// List<FOrgRelation> result = mapper.selectRelationOrgMapper(mapArg);
	// return new OrgRelationList(orsp.getSelf_org_id(), result);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new OrgRelationException(e);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	/**
	 * 修改关系配置
	 * 
	 * @param passport
	 * @param trc
	 * @throws OrgRelationException
	 * @throws NotPermissionException
	 */
	public void modifyRelationConfig(Passport passport, TOrgRelationConfig trc)
			throws OrgRelationException, NotPermissionException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();
		TOrgRelation relation = this.selectRelationById(trc.getRelation_id());
		if (relation == null)
			throw new NotPermissionException("未知的好友");
		if (OrgRelationStatus.VALID.getCode() != relation.getStatus())
			throw new NotPermissionException("无效的关系");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			TOrgRelationConfig config = session.getMapper(OrgRelationMapper.class)
					.selectRelationConfigByIdAndLock(trc.getId());
			if (config == null)
				throw new OrgOperateException("未知的关系配置");
			if (config.getOrg_id() != passport.getOrgId() || relation.getId() != trc.getRelation_id())
				throw new NotPermissionException();
			session.getMapper(OrgRelationMapper.class).updateRelationConfig(trc);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 新建关系配置
	 * 
	 * @param passport
	 * @param config
	 * @throws OrgRelationException
	 * @throws NotPermissionException
	 */
	public void addRelationConfig(Passport passport, TOrgRelationConfig config)
			throws OrgRelationException, NotPermissionException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();
		TOrgRelation tor = this.selectRelationById(config.getRelation_id());
		if (tor == null)
			throw new OrgRelationException("未知的好友");
		if (OrgRelationStatus.VALID.getCode() != tor.getStatus())
			throw new OrgRelationException("无效的关系");
		if (passport.getOrgId() != tor.getOrg_id() && passport.getOrgId() != tor.getFriend_org_id())
			throw new NotPermissionException();
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			TOrgRelationConfig trc = new TOrgRelationConfig();
			trc.setRelation_id(tor.getId());
			trc.setOrg_id(passport.getOrgId());
			trc.setPublish_report_org_id(config.getPublish_report_org_id());
			session.getMapper(OrgRelationMapper.class).insertRelationConfig(trc);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public TOrgRelation selectRelationById(long lRelationId) throws OrgRelationException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return session.getMapper(OrgRelationMapper.class).selectRelationById(lRelationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// /**
	// * 通过机构ID查询好友机构列表
	// *
	// * @throws NullParameterException
	// * @throws OrgRelationException
	// */
	// public List<TOrgRelation> queryMyFriendList(Passport passport, String
	// strOrgName, OrgRelationStatus status,
	// SplitPageUtil spu) throws NullParameterException, OrgRelationException {
	// if (passport == null)
	// throw new NullParameterException("passport");
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// Map<String, Object> prms = new HashMap<String, Object>();
	// prms.put("org_id", passport.getOrgId());
	// prms.put("org_name", strOrgName == null || strOrgName.isEmpty() ? null :
	// strOrgName);
	// prms.put("relation_status", status == null ? null : status.getCode());
	// prms.put("visible", OrgVisible.EPS.getCode());
	// if (spu != null) {
	// prms.put("begin_idx", spu.getCurrMinRowNum());
	// prms.put("end_idx", spu.getCurrMaxRowNum());
	// }
	// return
	// session.getMapper(OrgRelationMapper.class).selectFriendsByOrg(prms);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new OrgRelationException(e);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	/**
	 * 获取好友总数
	 * 
	 * @param passport
	 * @param isOwner
	 * @param status
	 * @return
	 * @throws NullParameterException
	 * @throws OrgRelationException
	 */
	public int queryMyFriendCount(Passport passport, String strOrgName, OrgRelationStatus status)
			throws OrgRelationException, NullParameterException {
		if (passport == null)
			throw new NullParameterException("passport");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", passport.getOrgId());
			prms.put("org_name", strOrgName == null || strOrgName.isEmpty() ? null : strOrgName);
			prms.put("status", status == null ? null : status.getCode());
			return session.getMapper(OrgRelationMapper.class).selectFriendsCountByOrg(prms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 撤销机构好友关系
	 * 
	 * @throws OrgRelationException
	 * @throws NotPermissionException
	 */
	public void cancelOrgFriend(Passport passport, long lOrgRelationId)
			throws OrgRelationException, NotPermissionException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			TOrgRelation friend = mapper.selectRelationByIdAndLock(lOrgRelationId);
			if (friend == null)
				throw new OrgOperateException("未知的好友");
			mapper.deleteFriend(lOrgRelationId);
			mapper.deleteRelationConfigByRelation(lOrgRelationId);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 确认机构好友
	 * 
	 * @param passport
	 * @param lRelationId
	 * @param isPass
	 * @throws OrgOperateException
	 * @throws NotPermissionException
	 * @throws OrgRelationException
	 */
	public void confrimFriend(Passport passport, long relation_id, boolean isPass, Long publish_report_org_id,
			OrgRelationPatientInfoShareType orpist) throws NotPermissionException, OrgRelationException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();
		orpist = orpist == null ? OrgRelationPatientInfoShareType.NONE : orpist;
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			TOrgRelation friendRelation = mapper.selectRelationByIdAndLock(relation_id);
			if (friendRelation == null)
				throw new OrgRelationException("要审核的合作关系不存在！");
			if (OrgRelationStatus.APPROVING.getCode() != friendRelation.getStatus())
				throw new OrgRelationException("非验证状态不能操作！");
			if (friendRelation.getFriend_org_id() != passport.getOrgId())
				throw new OrgRelationException("此合作申请不是提交给你当前所在机构的，你不能进行此操作！");
			friendRelation.setStatus((isPass ? OrgRelationStatus.VALID : OrgRelationStatus.REJECTED).getCode());
			mapper.updateOrgRelation(friendRelation);
			if (isPass) {
				if (friendRelation.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
					if (publish_report_org_id == null || publish_report_org_id <= 0)
						throw new OrgRelationException("必须指定诊断申请发布报告的机构！");
					if (!ArrayTools.exist(new Long[] { friendRelation.getFriend_org_id(), friendRelation.getOrg_id() },
							publish_report_org_id))
						throw new OrgRelationException("您的机构的诊断申请发布报告的机构只能是您当前所在机构与好友机构中的其中一个！");
				} else {
					publish_report_org_id = (long) 0;
				}
				TOrgRelationConfig orc = this.selectOrgRelationConfig(friendRelation.getId(), passport.getOrgId());
				if (orc == null) {
					orc = new TOrgRelationConfig();
					orc.setOrg_id(passport.getOrgId());
					orc.setPublish_report_org_id(publish_report_org_id);
					orc.setRelation_id(friendRelation.getId());
					orc.setShare_patient_info(orpist.getCode());
					mapper.insertRelationConfig(orc);
				} else {
					orc.setOrg_id(passport.getOrgId());
					orc.setPublish_report_org_id(publish_report_org_id);
					orc.setRelation_id(friendRelation.getId());
					orc.setShare_patient_info(orpist.getCode());
					mapper.updateRelationConfig(orc);
				}
			}
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 添加机构好友关系
	 * 
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws OrgOperateException
	 * 
	 * @throws BaseException
	 */
	public void createOrgFriend(Passport passport, long lFriendOrgId, OrgRelationPatientInfoShareType orpist,
			PublishReportType art, Long publish_report_org_id)
			throws OrgRelationException, NotPermissionException, OrgOperateException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();
		if (passport.getOrgId() == lFriendOrgId)
			throw new OrgRelationException("不能自己与自己达成合作关系！");
		TOrganization org = OrgService.instance.searchOrgById(lFriendOrgId);
		if (org == null)
			throw new OrgRelationException("指定的合作机构不存在！");
		if (!OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())))
			throw new OrgRelationException("指定的合作机构无效！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			TOrgRelation tor = this.selectOrgRelationByRelationOrgId(passport, lFriendOrgId);
			if (tor != null) {
				if (OrgRelationStatus.REJECTED.getCode() != tor.getStatus())
					throw new OrgRelationException("合作关系已经建立，无需重复建立！");
				tor.setPublish_report_type(art.getCode());
				tor.setCreate_time(new Date());
				tor.setStatus(OrgRelationStatus.APPROVING.getCode());
				mapper.updateOrgRelation(tor);
			} else {
				tor = new TOrgRelation();
				tor.setPublish_report_type(art.getCode());
				tor.setCreate_time(new Date());
				tor.setFriend_org_id(lFriendOrgId);
				tor.setOrg_id(passport.getOrgId());
				tor.setStatus(OrgRelationStatus.APPROVING.getCode());
				mapper.addFriend(tor);
			}
			if (tor.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
				if (publish_report_org_id == null || publish_report_org_id <= 0)
					throw new OrgRelationException("请指定您的机构的诊断申请发布报告的机构！");
				if (!publish_report_org_id.equals(passport.getOrgId()) && !publish_report_org_id.equals(lFriendOrgId))
					throw new OrgRelationException("您的机构的诊断申请发布报告的机构只能是您当前所在机构与好友机构中的其中一个！");
			} else {
				publish_report_org_id = (long) 0;
			}
			TOrgRelationConfig orc = this.selectOrgRelationConfig(tor.getId(), passport.getOrgId());
			if (orc == null) {
				orc = new TOrgRelationConfig();
				orc.setOrg_id(passport.getOrgId());
				orc.setPublish_report_org_id(publish_report_org_id);
				orc.setRelation_id(tor.getId());
				orc.setShare_patient_info(orpist.getCode());
				mapper.insertRelationConfig(orc);
			} else {
				orc.setOrg_id(passport.getOrgId());
				orc.setPublish_report_org_id(publish_report_org_id);
				orc.setRelation_id(tor.getId());
				orc.setShare_patient_info(orpist.getCode());
				mapper.updateRelationConfig(orc);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询好友机构关系
	 * 
	 * @throws NullParameterException
	 * @throws OrgRelationException
	 */
	public TOrgRelation selectOrgRelationByRelationOrgId(Passport passport, long lFriendOrgId)
			throws NullParameterException, OrgRelationException {
		if (passport == null)
			throw new NullParameterException("passport");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", passport.getOrgId());
			prms.put("friend_org_id", lFriendOrgId);
			// prms.put("status", OrgStatus.VALID.getCode());
			return session.getMapper(OrgRelationMapper.class).selectMyFriendByOrg(prms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 当双方都能发布报告时查询各自配置的发布机构
	 * 
	 * @param passport
	 * @param lFriendOrgId
	 * @return
	 * @throws NullParameterException
	 * @throws OrgOperateException
	 * @throws DbException
	 * @throws OrgRelationException
	 */
	public TOrgRelationConfig selectReportOrg(Passport passport, long lFriendOrgId)
			throws NullParameterException, OrgOperateException, DbException, OrgRelationException {
		if (passport == null)
			throw new NullParameterException("passport");
		TOrgRelation relation = this.selectOrgRelationByRelationOrgId(passport, lFriendOrgId);
		if (relation == null)
			throw new OrgOperateException("双方暂未建立好友关系");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("relation_id", relation.getId());
			prms.put("org_id", passport.getOrgId());
			return session.getMapper(OrgRelationMapper.class).selectRelationConfigByOrg(prms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<FOrgRelation> searchOrgRelationOfSelfOrg(Passport passport, String relation_org_name,
			OrgRelationStatus status, SplitPageUtil spu) throws Exception {
		SearchOrgRelationConfigParam orsp = new SearchOrgRelationConfigParam(passport.getOrgId());
		orsp.setStatus(status);
		orsp.setSpu(spu);
		orsp.setRelation_org_name(relation_org_name);
		return this.searchOrgRelation(orsp);
	}

	public void openOrClosePiShare(Passport passport, long orgId, OrgRelationPatientInfoShareType share_type)
			throws NotPermissionException, OrgRelationException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException(UserPermission.ORG_MGR);
		if (orgId == passport.getOrgId())
			throw new OrgRelationException("合作机构不能是自己的机构！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			TOrgRelation orgRelation = mapper
					.selectOrgRelationByCoupleOrgId(new TOrgRelation(passport.getOrgId(), orgId));
			if (orgRelation == null)
				throw new OrgRelationException("合作关系不存在！");
			orgRelation = mapper.selectRelationByIdAndLock(orgRelation.getId());
			if (orgRelation.getStatus() != OrgRelationStatus.VALID.getCode())
				throw new OrgRelationException("此合作机构关系现在还没有完全建立！");
			TOrgRelationConfig orgRelationConfig = mapper.selectOrgRelationConfigByRelationIdAndOrgIdForUpdate(
					new TOrgRelationConfig(orgRelation.getId(), passport.getOrgId()));
			if (orgRelationConfig == null) {
				if (orgRelation.getPublish_report_type() == PublishReportType.CanChoose.getCode())
					throw new OrgRelationException("在发布报告身份可选的情况下未找到合作机构关系配置！");
				orgRelationConfig = new TOrgRelationConfig(orgRelation.getId(), passport.getOrgId());
				orgRelationConfig.setPublish_report_org_id(-1);
				orgRelationConfig.setShare_patient_info((share_type == null?OrgRelationPatientInfoShareType.NONE:share_type).getCode());
				mapper.insertRelationConfig(orgRelationConfig);
			} else {
				orgRelationConfig.setShare_patient_info((share_type == null?OrgRelationPatientInfoShareType.NONE:share_type).getCode());
				mapper.updateRelationConfig(orgRelationConfig);
			}
			session.commit(true);
		} catch (Exception e) {
			session.rollback(true);
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void modifyFriend(Passport passport, long relation_id, long publish_report_org_id,
			OrgRelationPatientInfoShareType sharePatientInfo) throws NotPermissionException, OrgRelationException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException(UserPermission.ORG_MGR);
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgRelationMapper mapper = session.getMapper(OrgRelationMapper.class);
			TOrgRelation orgRelation = mapper.selectRelationByIdAndLock(relation_id);
			if (orgRelation == null)
				throw new OrgRelationException("合作机构关系未找到！");
			if (orgRelation.getStatus() != OrgRelationStatus.VALID.getCode())
				throw new OrgRelationException("此合作机构关系还未完全建立，不能进行此操作！");
			if (!ArrayTools.exist(new long[] { orgRelation.getFriend_org_id(), orgRelation.getOrg_id() },
					passport.getOrgId()))
				throw new OrgRelationException("该合作机构关系与您当前所在的机构无关，不能进行此操作！");
			TOrgRelationConfig orgRelationConfig = mapper.selectOrgRelationConfigByRelationIdAndOrgIdForUpdate(
					new TOrgRelationConfig(relation_id, passport.getOrgId()));
			if (orgRelationConfig == null) {
				orgRelationConfig = new TOrgRelationConfig(relation_id, passport.getOrgId());
				orgRelationConfig.setShare_patient_info(sharePatientInfo != null ? sharePatientInfo.getCode()
						: OrgRelationPatientInfoShareType.NONE.getCode());
				if (orgRelation.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
					if (!ArrayTools.exist(new long[] { orgRelation.getFriend_org_id(), orgRelation.getOrg_id() },
							publish_report_org_id))
						publish_report_org_id = passport.getOrgId();
					orgRelationConfig.setPublish_report_org_id(publish_report_org_id);
				} else {
					orgRelationConfig.setPublish_report_org_id(-1);
				}
				mapper.insertRelationConfig(orgRelationConfig);
			} else {
				orgRelationConfig.setShare_patient_info(sharePatientInfo != null ? sharePatientInfo.getCode()
						: OrgRelationPatientInfoShareType.NONE.getCode());
				if (orgRelation.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
					if (!ArrayTools.exist(new long[] { orgRelation.getFriend_org_id(), orgRelation.getOrg_id() },
							publish_report_org_id))
						publish_report_org_id = passport.getOrgId();
					orgRelationConfig.setPublish_report_org_id(publish_report_org_id);
				} else {
					orgRelationConfig.setPublish_report_org_id(-1);
				}
				mapper.updateRelationConfig(orgRelationConfig);
			}
			session.commit(true);
		} catch (Exception e) {
			session.rollback(true);
			e.printStackTrace();
			throw new OrgRelationException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public OrgRelationPatientInfoShareType queryOrgRelationPatientInfoShareType(Passport passport,
			long relation_org_id) throws OrgRelationException {
		TOrgRelation orgRelation = this.selectOrgRelationByOrgIdAndAnotherOrgId(passport.getOrgId(), relation_org_id);
		if (orgRelation == null)
			throw new OrgRelationException("您当前所在机构与对方机构不是合作机构关系！");
		TOrgRelationConfig orgRelationConfig = this.selectOrgRelationConfig(orgRelation.getId(), relation_org_id);
		if (orgRelationConfig == null)
			return OrgRelationPatientInfoShareType.NONE;
		return OrgRelationPatientInfoShareType.parseCode(orgRelationConfig.getShare_patient_info());
	}
}
