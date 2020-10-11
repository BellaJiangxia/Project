package com.vastsoft.yingtai.module.org.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.vastsoft.util.RulesValidator;
import com.vastsoft.util.StringUtil;
import com.vastsoft.util.common.SplitStringBuilder;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.SystemException;
import com.vastsoft.util.vcg.VcgService;
import com.vastsoft.yingtai.core.PermissionsSerializer;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.financel.constants.AccountStatus;
import com.vastsoft.yingtai.module.financel.entity.TFinanceAccount;
import com.vastsoft.yingtai.module.financel.mapper.FinanceMapper;
import com.vastsoft.yingtai.module.online.OnlineManager;
import com.vastsoft.yingtai.module.org.constants.OrgCertificationLevel;
import com.vastsoft.yingtai.module.org.constants.OrgChangeStatus;
import com.vastsoft.yingtai.module.org.constants.OrgProperty;
import com.vastsoft.yingtai.module.org.constants.OrgPublicStatus;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.constants.OrgVisible;
//import com.vastsoft.yingtai.module.org.configs.entity.TOrgConfigs;
import com.vastsoft.yingtai.module.org.entity.TOrgChange;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.mapper.OrgMapper;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.constants.OrgUserMapperStatus;
import com.vastsoft.yingtai.module.user.constants.UserStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;
import com.vastsoft.yingtai.module.user.entity.TDoctorUser;
import com.vastsoft.yingtai.module.user.entity.TGeneralUser;
import com.vastsoft.yingtai.module.user.entity.TOrgUserMapping;
import com.vastsoft.yingtai.module.user.exception.UserOperateException;
import com.vastsoft.yingtai.module.user.mapper.UserMapper;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.sms.SmsServer;

public class OrgService {
	public static final OrgService instance = new OrgService();
	private static final Logger logger = Logger.getLogger(OrgService.class);

	private OrgService() {
	}

	/**
	 * 删除机构
	 * 
	 * @throws BaseException
	 */
	public void removeOrg(Passport passport, long lOrgId) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new OrgOperateException("你没有机构管理员权限！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			TOrganization org = mapper.selectOrgByIdAndLock(lOrgId);
			if (org == null)
				throw new OrgOperateException("该机构不存在！");
			if (org.getStatus() == OrgStatus.DELETED.getCode()) {
				session.commit();
				return;
			}
			// 删除机构中所有的用户
			mapper.deleteOrgUserMapperByOrgId(lOrgId);
			// 删除机构的所有好友机构配置
			mapper.deleteRelationConfigByOrgId(lOrgId);
			// 删除机构的所有好友机构
			mapper.deleteRelationByOrgId(lOrgId);
			org.setStatus(OrgStatus.DELETED.getCode());
			this.modifyOrg(org, mapper);
			TOrganization org2 = OnlineManager.instance.getOnlineOrgById(lOrgId);
			if (org2 != null)
				org2.setStatus(org.getStatus());
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取我创建的机构
	 * 
	 * @throws BaseException
	 */
	private List<TOrganization> queryOrgOfMeCreate(long lUserId, SqlSession session) throws BaseException {
		try {
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			return mapper.queryOrgOfUserCreate(lUserId);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 检查机构对象完整性
	 * 
	 * @throws OrgOperateException
	 */
	private void checkOrgObject(TOrganization org) throws OrgOperateException {
		if (org == null)
			throw new OrgOperateException("机构对象必须指定！");
		if (org.getOrg_name() == null || org.getOrg_name().trim().isEmpty())
			throw new OrgOperateException("机构名称必须填写！");
//		if (org.getType() <= 0)
//			throw new OrgOperateException("机构类型必选！");
		if (org.getLevels() <= 0)
			throw new OrgOperateException("机构级别必选！");
		org.setOrg_name(org.getOrg_name().trim());
		if (!StringTools.checkStr(org.getOrg_name(), 26))
			throw new OrgOperateException("机构名称最多25个字！");
//		if (org.getDescription() == null || org.getDescription().trim().isEmpty())
//			throw new OrgOperateException("机构描述必须填写！");
//		org.setDescription(org.getDescription().trim());
//		if (!StringTools.checkStr(org.getDescription(), 1001))
		if(org.getDescription()!=null&&org.getDescription().length()>=1000)
			throw new OrgOperateException("机构描述最多1000个字！");
//		if (org.getScan_file_ids() == null || org.getScan_file_ids().trim().isEmpty())
//			throw new OrgOperateException("机构资质扫描件必须提供！");
//		if (org.getLogo_file_id() <= 0)
//			throw new OrgOperateException("必须上传机构LOGO！");
		if(org.getOrg_property()<=0)
			throw new OrgOperateException("机构类型不能为空");
	}

	/**
	 * 创建机构 typeID?
	 * 
	 * @throws NullParameterException
	 * @throws DbException
	 * @throws OrgOperateException
	 */
	private static final String createOrgLock = "创建机构线程锁";

	public TOrganization createOrg(Passport passport, String strName, Long lTypeId, List<Long> lScanFileIds,
			Long lLogoFileId, Long lLevelId, String strDesc, String strNote, OrgPublicStatus pulicStatus,OrgProperty property)
			throws BaseException {
		if (passport == null)
			throw new OrgOperateException("内部错误！");

		SqlSession session = null;
		try {
			synchronized (createOrgLock) {
				session = SessionFactory.getSession();
				OrgMapper mapper = session.getMapper(OrgMapper.class);
				TBaseUser user = UserService.instance.queryUserById(passport, null);
				if (user == null)
					throw new OrgOperateException("未知的用户");
				if (!UserStatus.NORMAL.equals(UserStatus.parseCode(user.getStatus())))
					throw new OrgOperateException("您当前状态不是有效的状态，不能执行此操作！");
//				List<TOrganization> listOrg = this.queryOrgOfMeCreate(passport.getUserId(), session);
				// if (listOrg != null && listOrg.size() >= 3)
				// throw new OrgOperateException("每个用户最多只能创建3个机构，你已经创建了三个了！");
				List<TOrganization> listOrg = mapper.selectOrgByName(strName);
				if (listOrg != null && listOrg.size() > 0)
					throw new OrgOperateException("机构重名，请重新命名！");
				TOrganization org = new TOrganization();
				org.setCreate_time(new Date());
				org.setCreator_id(passport.getUserId());
				org.setDescription(strDesc);
				org.setLevels(lLevelId);
				org.setNote(strNote);
				org.setOrg_name(strName);
				org.setStatus(OrgStatus.VALID.getCode());
				org.setType(lTypeId);
				org.setLogo_file_id(lLogoFileId);
				org.setCertification_level(OrgCertificationLevel.NORMAL.getCode());
				org.setScan_file_ids(SplitStringBuilder.splitToString(lScanFileIds));
				org.setOrg_code(SysService.instance.takeNextOrgCode());
				org.setIs_public(pulicStatus == null ? OrgPublicStatus.NO.getCode() : pulicStatus.getCode());
				org.setOrg_property(property.getCode());

				this.checkOrgObject(org);
				mapper.insertOrg(org);
				// 创建映射关系
				TOrgUserMapping orgUserMapping = new TOrgUserMapping();
				orgUserMapping.setCreate_time(new Date());
				orgUserMapping.setOrg_id(org.getId());
				orgUserMapping.setStatus(OrgUserMapperStatus.VALID.getCode());
				orgUserMapping.setUser_id(org.getCreator_id());
				Set<UserPermission> lup = new HashSet<>();
				lup.add(UserPermission.ORG_MGR);
				lup.add(UserPermission.ORG_MEDICAL_MGR);
				if (user.getType()==UserType.ORG_DOCTOR.getCode()) {
					lup.add(UserPermission.ORG_REPORT);
					lup.add(UserPermission.ORG_AUDIT);
				}
				orgUserMapping.setPermission(PermissionsSerializer.toUserPermissionString(lup));
				session.getMapper(UserMapper.class).insertOrg2User(orgUserMapping);
				
				session.commit();
				return org;
			}
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 机构审核
	 * 
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws OrgOperateException
	 * @throws DbException
	 */
	public void approvedOrg(Passport passport, long lOrgId, boolean isPass, String strNote)
			throws NullParameterException, NotPermissionException, OrgOperateException, DbException {
		if (passport == null)
			throw new NullParameterException("passport");

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			TOrganization org = session.getMapper(OrgMapper.class).selectOrgByIdAndLock(lOrgId);

			if (org == null)
				throw new OrgOperateException("未能找到的机构");
			if (org.getCertification_level()!=OrgCertificationLevel.NORMAL.getCode())
				throw new OrgOperateException("机构已经认证，无需再次认证！");
//			if (!OrgStatus.APPROVING.equals(OrgStatus.parseCode(org.getStatus())))
//				throw new OrgOperateException("已审核的机构，无需重新审核");
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", lOrgId);
			prms.put("status", isPass ? OrgStatus.VALID.getCode() : OrgStatus.REJECTED.getCode());
			prms.put("varify_time", new Timestamp(new Date().getTime()));
			prms.put("verify_user_id", passport.getUserId());
			if (isPass) {
//				prms.put("permission",
//						ops == null || ops.isEmpty() ? null : PermissionsSerializer.toOrgPermissionString(ops));
			} else {
				prms.put("note", strNote == null || strNote.isEmpty() ? null : strNote);
			}

			session.getMapper(OrgMapper.class).approveOrg(prms);

			if (isPass) {
//				Set<UserPermission> ups = new HashSet<UserPermission>();
//				if (ops == null || ops.isEmpty())
//					throw new OrgOperateException("未知的机构权限");

//				if (ops.size() > 1) {
//					ups.add(UserPermission.ORG_AUDIT);
//					ups.add(UserPermission.ORG_MEDICAL_MGR);
//					ups.add(UserPermission.ORG_MGR);
//					ups.add(UserPermission.ORG_REPORT);
//				} else if (ops.contains(OrgPermission.REQUESTOR)) {
//					ups.add(UserPermission.ORG_MGR);
//					ups.add(UserPermission.ORG_MEDICAL_MGR);
//				} else if (ops.contains(OrgPermission.DIAGNOSISER)) {
//					ups.add(UserPermission.ORG_AUDIT);
//					ups.add(UserPermission.ORG_REPORT);
//					ups.add(UserPermission.ORG_MGR);
//				}

//				// 创建映射关系
//				TOrgUserMapping mapper = new TOrgUserMapping();
//				mapper.setCreate_time(new Date());
//				mapper.setOrg_id(lOrgId);
//				mapper.setStatus(OrgUserMapperStatus.VALID.getCode());
//				mapper.setUser_id(org.getCreator_id());
//				mapper.setPermission(PermissionsSerializer.toUserPermissionString(ups));
//				session.getMapper(UserMapper.class).insertOrg2User(mapper);

				// 创建账户
				TFinanceAccount acc = new TFinanceAccount();
				acc.setCreate_Time(new Date());
				acc.setOrg_id(org.getId());
				acc.setAmount(0.00);
				acc.setStatus(AccountStatus.NORMAL.getCode());
				session.getMapper(FinanceMapper.class).addAccount(acc);
			}
			TBaseUser baseUser = UserService.instance.queryBaseUserById(passport, org.getCreator_id());
			if (baseUser == null)
				throw new OrgOperateException("机构创建者不存在！");
			SmsServer.instance.sendSMS(baseUser.getMobile(), "尊敬的" + baseUser.getUser_name() + "，您创建的机构"
					+ org.getOrg_name() + (isPass ? "已经审核通过，请登录平台进行后续操作！" : "未被审核通过，您可以按要求修改资料后重新提交申请！") + "【影泰科技】");
			session.commit();
		} catch (OrgOperateException e) {
			session.rollback(true);
			throw e;
		} catch (Exception e) {
			session.rollback(true);
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 修改机构
	 * 
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws OrgOperateException
	 * @throws DbException
	 */
	public void modifyOrg(Passport passport, TOrganization org,boolean reqCertification)
			throws NullParameterException, NotPermissionException, OrgOperateException, DbException {
		if (passport == null || org == null)
			throw new NullParameterException("passport&org");

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			List<TOrganization> list = session.getMapper(OrgMapper.class).selectOrgByName(org.getOrg_name());
			if (list != null && list.size() > 1)
				throw new OrgOperateException("该机构已经注册！");

			if (!UserService.instance.checkUserBelongOrg(passport, org.getId()))
				throw new OrgOperateException("无权修改，用户不属于该机构");

			int count = this.selectOrgChangeCount(passport, org.getId(), null, null, OrgChangeStatus.APPROVING);

			if (count > 0)
				throw new OrgOperateException("正在审核认证中，不能重复提交");

			TOrganization to = session.getMapper(OrgMapper.class).selectOrgByIdAndLock(org.getId());

			if (to == null)
				throw new OrgOperateException("未知的机构");

			if (!OrgStatus.VALID.equals(OrgStatus.parseCode(to.getStatus())))
				throw new OrgOperateException("无效的机构");

			this.checkOrgObject(org);

			boolean flushCache=false;

			if(OrgCertificationLevel.CERTIFICATIONED.equals(to.getCertificationLevel())){
				if(to.aoceEquals(org)){
					session.getMapper(OrgMapper.class).modifyOrg(org);
					flushCache=true;
				}else{
					this.addOrgChange(passport,org,session);
				}
			}else{
				if(reqCertification){
					this.addOrgChange(passport,org,session);
				}else{
					session.getMapper(OrgMapper.class).modifyOrg(org);
					flushCache=true;
				}
			}

			session.commit();

			if(flushCache){
				OnlineManager.instance.updateOnlineOrg(org);
			}

			// 覆盖原有机构
			// TOrganization
			// no=session.getMapper(OrgMapper.class).selectOrgById(to.getId());
			// OnlineManager.instance.updateOnlineOrg(no);
		} catch (BaseException e) {
			session.rollback(true);
			throw e;
		} catch (Exception e) {
			session.rollback(true);
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private void addOrgChange(Passport passport,TOrganization org,SqlSession session){
		TOrgChange toc = new TOrgChange();
		toc.setChange_status(OrgChangeStatus.APPROVING.getCode());
		toc.setChange_time(new Date());
//		toc.setDescription(org.getDescription());
		toc.setLevels(org.getLevels());
		toc.setLogo_file_id(org.getLogo_file_id());
//		toc.setNote(org.getNote());
		toc.setOrg_id(org.getId());
		toc.setOrg_name(org.getOrg_name());
		toc.setScan_file_ids(org.getScan_file_ids());
		toc.setType(org.getType());
		toc.setChange_user_id(passport.getUserId());
		toc.setOrg_property(org.getOrg_property());
		session.getMapper(OrgMapper.class).insertOrgChange(toc);

		org.setCertification_level(OrgCertificationLevel.NORMAL.getCode());
		org.setVerify_user_id(0);
		org.setVarify_time(null);
		org.setNote(null);
		session.getMapper(OrgMapper.class).updateCertification(org);
	}

	public int searchOrgCount(String strOrgName, Long lOrgCode, Long lTypeId, Long lLevelId, OrgStatus status,
			Date dtStartDate, Date dtEndDate,OrgVisible visible) throws DbException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_name", strOrgName == null || strOrgName.isEmpty() ? null : strOrgName);
			prms.put("org_code", lOrgCode);
			prms.put("type", lTypeId);
			prms.put("levels", lLevelId);
			prms.put("status", status == null ? null : status.getCode());
			prms.put("start_date", dtStartDate == null ? null : new Timestamp(dtStartDate.getTime()));
			prms.put("end_date", dtEndDate == null ? null : new Timestamp(dtEndDate.getTime()));
			prms.put("visible", visible == null ? null : visible.getCode());

			return session.getMapper(OrgMapper.class).selectAllOrgCount(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询所有（搜索）
	 * 
	 * @throws DbException
	 */
	public List<TOrganization> searchOrgList(String strOrgName, Long lOrgCode, Long lTypeId, Long lLevelId,
			OrgStatus status, Date dtStartDate, OrgVisible visible, Date dtEndDate, SplitPageUtil spu)
			throws DbException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("begin_idx", spu.getCurrMinRowNum());
			prms.put("end_idx", spu.getCurrMaxRowNum());
			prms.put("org_name", strOrgName == null || strOrgName.isEmpty() ? null : strOrgName);
			prms.put("org_code", lOrgCode);
			prms.put("type", lTypeId);
			prms.put("levels", lLevelId);
			prms.put("status", status == null ? null : status.getCode());
			prms.put("start_date", dtStartDate == null ? null : new Timestamp(dtStartDate.getTime()));
			prms.put("end_date", dtEndDate == null ? null : new Timestamp(dtEndDate.getTime()));
			prms.put("visible", visible == null ? null : visible.getCode());
			int count = mapper.selectAllOrgCount(prms);
			spu.setTotalRow(count);
			return mapper.selectAllOrgs(prms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据用户查询
	 * 
	 * @throws DbException
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 */
	public List<TOrganization> searchOrgListByUser(Passport passport, Long lUserId)
			throws DbException, NullParameterException, NotPermissionException {
		if (passport == null)
			throw new NullParameterException("passport");

		SqlSession session = null;

		// JR备注
		// 既然设置了一个ADMIN_ORG，那么它的作用是什么？
		// 如果传入是一个后台用户的Passport，那么返回的OrgList到底该不该包含ADMIN_ORG？
		// 程序的一致性是否应该考虑得更深一点？
		// 如果返回的OrgList包含ADMIN_ORG，那它将其它Service方法的开发带来哪些影响？
		// 或者说，期望其它Service方法如何来处理后台用户的Passport中包含的这个特殊的ADMIN_ORG？是不是需要想到，并得出一个结论？

		try {
			boolean isAdmin = UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR);

			// JR修改，20150929，如果Passport不具有用户管理权限且lUserId不为空，除非lUserId等于Passport的UserId，否则直接抛异常。
			if (isAdmin == false && lUserId != null && lUserId != passport.getUserId())
				throw new NotPermissionException("无权查看该用户所属机构");

			// JR修改，20150929，之前没有判断lUserId是否为空，导致异常
			long uid = isAdmin ? (lUserId == null ? passport.getUserId() : lUserId) : passport.getUserId();

			session = SessionFactory.getSession();
			return session.getMapper(OrgMapper.class).selectOrgsByUserId(uid);
		} catch (NotPermissionException e) {
			throw e;
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询我的机构
	 * 
	 * @param passport
	 * @param status
	 * @return
	 * @throws NullParameterException
	 * @throws DbException
	 */
	public List<TOrganization> selectMyOrgList(Passport passport) throws NullParameterException, DbException {
		if (passport == null)
			throw new NullParameterException("passport");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			return session.getMapper(OrgMapper.class).selectMyOrgList(passport.getUserId());
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据ID查询
	 * 
	 * @throws BaseException
	 */
	public TOrganization searchOrgById(long lOrgId) throws OrgOperateException {
		// 先查缓存
		TOrganization org = OnlineManager.instance.getOnlineOrgById(lOrgId);
		if (org != null)
			return org;
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			org = session.getMapper(OrgMapper.class).selectOrgById(lOrgId);
			// 放入缓存
			if (org != null)
				OnlineManager.instance.addOnlineOrg(org);
			return org;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrgOperateException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// /**
	// * 新增服务
	// *
	// * @throws BaseException
	// */
	// private static final String createProductLock = "添加机构服务锁";
	//
	// public TOrgProduct createProduct(Passport passport, String strName,
	// double dfPrice, long lDeviceTypeId,
	// long lPartTypeId, String strDesc) throws BaseException {
	// if (passport == null || strName == null || strName.isEmpty())
	// throw new NullParameterException("passport&strName");
	//
	// if (!this.checkOrgPermission(passport, OrgPermission.DIAGNOSISER))
	// throw new NotPermissionException("机构没有诊断申请的权限！");
	//
	// if (!UserService.instance.checkUserPermission(passport,
	// UserPermission.ORG_MGR))
	// throw new NotPermissionException("你没有机构管理的权限！");
	// synchronized (createProductLock) {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// Map<String, Object> mapArg = new HashMap<String, Object>();
	// mapArg.put("org_id", passport.getOrgId());
	// mapArg.put("name", strName.trim());
	// List<TOrgProduct> listOrgProduct = mapper.searchOrgProduct(mapArg);
	// if (listOrgProduct != null && listOrgProduct.size() > 0)
	// throw new OrgOperateException("此服务重名，请重新命名！");
	// TOrgProduct tos = new TOrgProduct();
	// tos.setCreate_time(new Date());
	// tos.setCreator_id(passport.getUserId());
	// tos.setDescription(strDesc);
	// if (lDeviceTypeId <= 0)
	// throw new OrgOperateException("设备类型必填！");
	// tos.setDevice_type_id(lDeviceTypeId);
	// if (lPartTypeId <= 0) {
	// tos.setPart_type_id(0);
	// } else {
	// tos.setPart_type_id(lPartTypeId);
	// }
	// tos.setOrg_id(passport.getOrgId());
	// tos.setPrice(dfPrice);
	// tos.setName(strName);
	// tos.setStatus(SysDataStatus.VALID.getCode());
	//
	// mapper.insertProduct(tos);
	// session.commit();
	// return tos;
	// } catch (Exception e) {
	// session.rollback(true);
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }
	// }

	// /**
	// * 删除服务
	// *
	// * @throws NullParameterException
	// * @throws DbException
	// * @throws NotPermissionException
	// * @throws UserOperateException
	// * @throws OrgOperateException
	// */
	// public void deleteProduct(Passport passport, long lProductId)
	// throws NullParameterException, DbException, NotPermissionException,
	// OrgOperateException {
	// if (passport == null)
	// throw new NullParameterException("passport");
	//
	// if (!UserService.instance.checkUserPermission(passport,
	// UserPermission.ORG_MGR))
	// throw new NotPermissionException();
	//
	// SqlSession session = null;
	//
	// try {
	// session = SessionFactory.getSession();
	//
	// TOrgProduct tos =
	// session.getMapper(OrgMapper.class).selectProductByIdAndLock(lProductId);
	//
	// if (tos == null)
	// throw new OrgOperateException("未知的产品");
	//
	// if (tos.getOrg_id() != passport.getOrgId())
	// throw new NotPermissionException();
	//
	// // session.getMapper(OrgMapper.class).deleteProductById(lProductId);
	// Map<String, Object> prms = new HashMap<String, Object>();
	// prms.put("id", lProductId);
	// prms.put("status", OrgProductStatus.INVALID.getCode());
	// session.getMapper(OrgMapper.class).modifyProductStatus(prms);
	// session.commit();
	// } catch (OrgOperateException | NotPermissionException e) {
	// session.rollback(true);
	// throw e;
	// } catch (Exception e) {
	// session.rollback(true);
	// throw new DbException();
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	// /**
	// * 启/禁用
	// *
	// * @param passport
	// * @param lProductId
	// * @param isEnable
	// * @throws NullParameterException
	// * @throws UserOperateException
	// * @throws DbException
	// * @throws NotPermissionException
	// * @throws OrgOperateException
	// */
	// public void enableProduct(Passport passport, long lProductId, boolean
	// isEnable)
	// throws NullParameterException, DbException, NotPermissionException,
	// OrgOperateException {
	// if (passport == null)
	// throw new NullParameterException("passport");
	//
	// if (!UserService.instance.checkUserPermission(passport,
	// UserPermission.ORG_MGR))
	// throw new NotPermissionException();
	//
	// SqlSession session = null;
	//
	// try {
	// session = SessionFactory.getSession();
	// TOrgProduct tos =
	// session.getMapper(OrgMapper.class).selectProductByIdAndLock(lProductId);
	//
	// if (tos == null)
	// throw new OrgOperateException("未知的产品");
	//
	// if (tos.getOrg_id() != passport.getOrgId())
	// throw new NotPermissionException();
	//
	// Map<String, Object> prms = new HashMap<String, Object>();
	// prms.put("id", lProductId);
	// prms.put("status", isEnable ? OrgProductStatus.VALID.getCode() :
	// OrgProductStatus.DISABLE.getCode());
	// session.getMapper(OrgMapper.class).modifyProductStatus(prms);
	// session.commit();
	// } catch (OrgOperateException | NotPermissionException e) {
	// session.rollback(true);
	// throw e;
	// } catch (Exception e) {
	// session.rollback(true);
	// throw new DbException();
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	// /**
	// * 检查机构产品
	// *
	// * @param passport
	// * @param lProductId
	// * @param lOrgId
	// * @return
	// */
	// public boolean checkProductByOrg(Passport passport, long lProductId, Long
	// lOrgId) {
	// if (passport == null)
	// return false;
	//
	// long oid = lOrgId == null ? passport.getOrgId() : lOrgId.longValue();
	//
	// SqlSession session = null;
	//
	// try {
	// session = SessionFactory.getSession();
	// Map<String, Object> prms = new HashMap<String, Object>();
	// prms.put("id", lProductId);
	// prms.put("org_id", oid);
	// prms.put("status", OrgProductStatus.VALID.getCode());
	// TOrgProduct top =
	// session.getMapper(OrgMapper.class).selectProductById(prms);
	// return top != null;
	// } catch (Exception e) {
	// return false;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	// /**
	// * 根据ID获取服务
	// *
	// * @throws NullParameterException
	// * @throws DbException
	// */
	// public TOrgProduct searchProductById(long lProductId) throws
	// NullParameterException, DbException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// Map<String, Object> prms = new HashMap<String, Object>();
	// prms.put("id", lProductId);
	// return session.getMapper(OrgMapper.class).selectProductById(prms);
	// } catch (Exception e) {
	// throw new DbException();
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	// /**
	// * 获取指定机构的机构服务
	// *
	// * @param lOrgId
	// * @param strProductName
	// * @param lDeviceTypeId
	// * @param lPartTypeId
	// * @param status
	// * @param spu
	// * @return
	// * @throws OrgOperateException
	// */
	// public List<TOrgProduct> queryProductList(long lOrgId, String
	// strProductName, Long lDeviceTypeId, Long lPartTypeId,
	// OrgProductStatus status, SplitPageUtil spu) throws OrgOperateException {
	// SqlSession session = null;
	//
	// try {
	// session = SessionFactory.getSession();
	//
	// Map<String, Object> prms = new HashMap<String, Object>();
	// if (spu != null) {
	// prms.put("begin_idx", spu.getCurrMinRowNum());
	// prms.put("end_idx", spu.getCurrMaxRowNum());
	// }
	// prms.put("org_id", lOrgId);
	// prms.put("product_name", strProductName == null ||
	// strProductName.isEmpty() ? null : strProductName);
	// prms.put("device_type_id", lDeviceTypeId);
	// prms.put("part_type_id", lPartTypeId);
	// prms.put("status", status == null ? null : status.getCode());
	// return session.getMapper(OrgMapper.class).selectProductsByOrg(prms);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new OrgOperateException(e);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	// /**
	// * @throws OrgOperateException
	// */
	// public int queryProductCount(long lOrgId, String strProductName, Long
	// lDeviceTypeId, Long lPartTypeId,
	// OrgProductStatus status) throws OrgOperateException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// Map<String, Object> prms = new HashMap<String, Object>();
	// prms.put("org_id", lOrgId);
	// prms.put("product_name", strProductName == null ||
	// strProductName.isEmpty() ? null : strProductName);
	// prms.put("device_type_id", lDeviceTypeId);
	// prms.put("part_type_id", lPartTypeId);
	// prms.put("status", status == null ? null : status.getCode());
	// return session.getMapper(OrgMapper.class).selectProductsCount(prms);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new OrgOperateException(e);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

//	/**
//	 * 判断机构权限
//	 * 
//	 * @param passport
//	 * @param orgPermission
//	 * @return
//	 */
//	public boolean checkOrgPermission(Passport passport, OrgPermission orgPermission) {
//		if (passport == null || orgPermission == null)
//			return false;
//
//		SqlSession session = null;
//
//		try {
//			session = SessionFactory.getSession();
//			TOrganization org = session.getMapper(OrgMapper.class).selectOrgById(passport.getOrgId());
//
//			if (org == null)
//				return false;
//
//			if (!OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())))
//				return false;
//
//			List<OrgPermission> ops = PermissionsSerializer.fromOrgPermissionString(org.getPermission());
//
//			return ops.contains(orgPermission);
//		} catch (Exception e) {
//			return false;
//		} finally {
//			SessionFactory.closeSession(session);
//		}
//	}

//	public void authorizeOrg(Passport passport, long lOrgId, List<OrgPermission> ops)
//			throws NullParameterException, NotPermissionException, OrgOperateException, DbException {
//		if (passport == null || ops == null || ops.isEmpty())
//			throw new NullParameterException("passport");
//
//		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
//			throw new NotPermissionException();
//
//		SqlSession session = null;
//
//		try {
//			session = SessionFactory.getSession();
//			TOrganization org = session.getMapper(OrgMapper.class).selectOrgByIdAndLock(lOrgId);
//
//			if (org == null)
//				throw new OrgOperateException("未知的机构");
//
//			if (!OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())))
//				throw new OrgOperateException("无效的机构");
//
//			Map<String, Object> prms = new HashMap<String, Object>();
//			prms.put("id", lOrgId);
//			prms.put("permission", PermissionsSerializer.toOrgPermissionString(ops));
//			session.getMapper(OrgMapper.class).authorizeOrg(prms);
//			session.commit();
//
//			TOrganization new_org = session.getMapper(OrgMapper.class).selectOrgById(lOrgId);
//			OnlineManager.instance.updateOnlineOrg(new_org);
//
//		} catch (OrgOperateException e) {
//			session.rollback(true);
//			throw e;
//		} catch (Exception e) {
//			session.rollback(true);
//			throw new DbException();
//		} finally {
//			SessionFactory.closeSession(session);
//		}
//	}

	// /**
	// * 查询某机构的配置，当lOrgId==0时查询默认配置
	// *
	// * @param passport
	// * @param lOrgId
	// * 机构ID，默认配置机构ID为0
	// * @return
	// * @throws BaseException
	// */
	// public List<TOrgAffix> queryOrgAffixByOrgId(long orgId) throws
	// OrgOperateException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// return mapper.selectAffixByOrgId(orgId);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new OrgOperateException(e);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }
	//
	// public List<TOrgAffix> queryOrgAffixByOrgIdWithDefaultAffix(long orgId)
	// throws OrgOperateException {
	// List<TOrgAffix> result = this.queryOrgAffixByOrgId(orgId);
	// if (!CommonTools.isEmpty(result))
	// return result;
	// return this.queryDefaultOrgAffix();
	// }
	//
	// public TOrgAffix selectOrgAffixById(long lOrgAffixId) throws
	// BaseException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// return mapper.selectOrgAffixById(lOrgAffixId);
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }
	//
	// private static final String addOrUpdateOrgAffixLock =
	// "addOrUpdateOrgAffixLock";
	//
	// /**
	// * 添加一个机构附件配置
	// *
	// * @throws BaseException
	// */
	// public TOrgAffix addOrUpdateDefaultOrgAffix(Passport passport, TOrgAffix
	// orgAffix) throws BaseException {
	// synchronized (addOrUpdateOrgAffixLock) {
	// if (!passport.getUserType().equals(UserType.SUPER_ADMIN))
	// if (!UserService.instance.checkUserPermission(passport,
	// UserPermission.ADMIN_ORG_MGR))
	// throw new NotPermissionException("你没有权限执行此操作！");
	// this.checkOrgAffixObject(orgAffix);
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// List<TOrgAffix> listOldOrgAffix = mapper.selectAffixByOrgId(0);
	// if (listOldOrgAffix == null || listOldOrgAffix.size() <= 0) {
	// orgAffix.setOrg_id(0);
	// mapper.insertOrgAffix(orgAffix);
	// } else {
	// TOrgAffix oldOrgAffix = listOldOrgAffix.get(0);
	// oldOrgAffix.setAe_code(orgAffix.getAe_code());
	// oldOrgAffix.setDicomweb_url(orgAffix.getDicomweb_url());
	// oldOrgAffix.setPassword(orgAffix.getPassword());
	// oldOrgAffix.setQuery_url(orgAffix.getQuery_url());
	// oldOrgAffix.setRemote_server_version(RemoteServerVersion.V_1.getCode());
	// oldOrgAffix.setUser_name(orgAffix.getUser_name());
	// mapper.updateOrgAffix(oldOrgAffix);
	// orgAffix = oldOrgAffix;
	// }
	// session.commit();
	// return orgAffix;
	// } catch (Exception e) {
	// session.rollback(true);
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }
	// }
	//
	// private void checkOrgAffixObject(TOrgAffix orgAffix) throws
	// OrgOperateException {
	// if (orgAffix == null)
	// throw new OrgOperateException("请指定机构配置对象！");
	// RemoteServerVersion rsv =
	// RemoteServerVersion.parseCode(orgAffix.getRemote_server_version());
	// if (rsv == null)
	// throw new OrgOperateException("远程检索服务器版本必须指定！");
	// if (rsv.equals(RemoteServerVersion.V_1)) {
	// if (orgAffix.getAe_code() == null ||
	// orgAffix.getAe_code().trim().isEmpty())
	// throw new OrgOperateException("机构配置的IE号必须指定！");
	// orgAffix.setAe_code(orgAffix.getAe_code().trim());
	// }
	// if (orgAffix.getDicomweb_url() == null ||
	// orgAffix.getDicomweb_url().trim().isEmpty())
	// throw new OrgOperateException("DICOM图像服务地址必须指定！");
	// orgAffix.setDicomweb_url(orgAffix.getDicomweb_url().trim());
	// if (orgAffix.getInternet_ip() != null &&
	// !orgAffix.getInternet_ip().trim().isEmpty()) {
	// orgAffix.setInternet_ip(orgAffix.getInternet_ip().trim());
	// if (CommonTools.isIpAddr(orgAffix.getInternet_ip()))
	// throw new OrgOperateException("机构所在公网IP地址不合法！");
	// }
	// if (orgAffix.getIntranet_url() != null &&
	// !orgAffix.getIntranet_url().trim().isEmpty())
	// orgAffix.setIntranet_url(orgAffix.getIntranet_url().trim());
	// if (CommonTools.isIpAddr(orgAffix.getIntranet_url()))
	// throw new OrgOperateException("机构服务器所在内网URL地址不合法！");
	// if (orgAffix.getPassword() == null ||
	// orgAffix.getPassword().trim().isEmpty())
	// throw new OrgOperateException("登陆密码必须指定！");
	// orgAffix.setPassword(orgAffix.getPassword().trim());
	// if (orgAffix.getQuery_url() == null ||
	// orgAffix.getQuery_url().trim().isEmpty())
	// throw new OrgOperateException("检索服务地址必须指定！");
	// orgAffix.setQuery_url(orgAffix.getQuery_url().trim());
	// if (orgAffix.getUser_name() == null ||
	// orgAffix.getUser_name().trim().isEmpty())
	// throw new OrgOperateException("登陆用户名必须指定！");
	// orgAffix.setUser_name(orgAffix.getUser_name().trim());
	// }
	//
	// public void deleteOrgAffixById(Passport passport, long lOrgAffixId)
	// throws BaseException {
	// if (!passport.getUserType().equals(UserType.SUPER_ADMIN))
	// if (!UserService.instance.checkUserPermission(passport,
	// UserPermission.ADMIN_ORG_MGR))
	// throw new NotPermissionException("你没有权限执行此操作！");
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// TOrgAffix affix = mapper.selectOrgAffixById(lOrgAffixId);
	// if (affix == null)
	// throw new OrgOperateException("要删除的配置不存在！");
	// if (affix.getOrg_id() == 0)
	// throw new OrgOperateException("不能删除默认配置！");
	// mapper.deleteOrgAffixById(lOrgAffixId);
	// session.commit();
	// } catch (Exception e) {
	// session.rollback(true);
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	/**
	 * 删除机构下属用户
	 * 
	 * @param passport
	 * @param lOrgId
	 * @param lUserId
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws OrgOperateException
	 * @throws DbException
	 */
	public void deleteUserById(Passport passport, long lUserId)
			throws NullParameterException, NotPermissionException, OrgOperateException, DbException {
		if (passport == null)
			throw new NullParameterException("passport");

		// boolean isAdmin=UserService.instance.checkUserPermission(passport,
		// UserPermission.ADMIN_USER_MGR);

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();

			TOrganization org = this.searchOrgById(passport.getOrgId());

			if (org == null)
				throw new OrgOperateException("未知的机构");

			if (!OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())))
				throw new OrgOperateException("无效的机构");

			if (org.getCreator_id() == lUserId)
				throw new OrgOperateException("无法删除机构创建者");

			TBaseUser tbu = UserService.instance.queryUserById(passport, lUserId);

			if (tbu == null)
				throw new OrgOperateException("未知的用户");

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", passport.getOrgId());
			prms.put("user_id", lUserId);
			TOrgUserMapping tum = session.getMapper(UserMapper.class).selectMapperByUserAndLock(prms);

			if (tum == null)
				throw new OrgOperateException("非本机构用户，无权操作");

			if (!OrgUserMapperStatus.VALID.equals(OrgUserMapperStatus.parseCode(tum.getStatus())))
				throw new OrgOperateException("非本机构正常用户，无法操作");
			UserService.instance.deleteMapper(passport, tum, session);

			session.commit();
		} catch (UserOperateException | OrgOperateException e) {
			session.rollback(true);
			throw new OrgOperateException(e.getMessage());
		} catch (Exception e) {
			session.rollback(true);
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	// public List<TOrgProduct> queryAllProductOfOrgIdAndDPType(Passport
	// passport, long lOrgId, long device_type_id,
	// Long part_type_id) throws BaseException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// if (passport == null)
	// throw new OrgOperateException("你未登录，请重新登陆！");
	// if (lOrgId <= 0)
	// throw new OrgOperateException("请指定有效的机构ID");
	// if (device_type_id<=0)
	// throw new OrgOperateException("缺少设备类型！");
	// // if (strPartTypeName==null||strDeviceTypeName.trim().isEmpty())
	// // throw new OrgOperateException("请指定有效的部位类型！");
	//// TDicValue deviceType =
	// SysService.instance.queryAndAddDicValueByTypeAndValue(passport,
	// strDeviceTypeName,
	//// null, DictionaryType.DEVICE_TYPE);
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// Map<String, Object> mapArg = new HashMap<String, Object>();
	// mapArg.put("org_id", lOrgId);
	// mapArg.put("device_type_id", device_type_id);
	// if (part_type_id != null && part_type_id>0) {
	//// TDicValue partType =
	// SysService.instance.queryAndAddDicValueByTypeAndValue(passport,
	// strPartTypeName,
	//// deviceType.getId(), DictionaryType.BODY_PART_TYPE);
	// mapArg.put("part_type_id", part_type_id);
	// }
	// mapArg.put("status", OrgProductStatus.VALID.getCode());
	// return mapper.queryAllProductOfOrgIdAndDPType(mapArg);
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	/**
	 * 查询机构变更列表
	 * 
	 * @param passport
	 * @param lOrgId
	 * @param strOrgName
	 * @param lOrgCode
	 * @param status
	 * @param spu
	 * @return
	 * @throws DbException
	 */
	public List<TOrgChange> selectOrgChangeList(Passport passport, Long lOrgId, String strOrgName, Long lOrgCode,
			OrgChangeStatus status, SplitPageUtil spu) throws DbException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			if (spu != null) {
				prms.put("begin_idx", spu.getCurrMinRowNum());
				prms.put("end_idx", spu.getCurrMaxRowNum());
			}
			prms.put("org_id", lOrgId);
			prms.put("status", status != null ? status.getCode() : null);
			prms.put("org_name", strOrgName);
			prms.put("org_code", lOrgCode);

			return session.getMapper(OrgMapper.class).selectChangeListByOrg(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public int selectOrgChangeCount(Passport passport, Long lOrgId, String strOrgName, Long lOrgCode,
			OrgChangeStatus status) throws DbException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", lOrgId);
			prms.put("status", status != null ? status.getCode() : null);
			prms.put("org_name", strOrgName);
			prms.put("org_code", lOrgCode);

			return session.getMapper(OrgMapper.class).selectOrgChangeCount(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据机构ID获取变更记录
	 * 
	 * @param passport
	 * @param lOrgId
	 * @return
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws DbException
	 */
	public TOrgChange selectOrgChangeByOrg(Passport passport, long lChangeId)
			throws NullParameterException, NotPermissionException, DbException {
		if (passport == null)
			throw new NullParameterException("passport");

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			return session.getMapper(OrgMapper.class).selectChangeByOrg(lChangeId);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 是否同意机构变更
	 * 
	 * @param passport
	 * @param lChangeId
	 * @param isPass
	 * @throws BaseException
	 */
	public void confirmChange(Passport passport, long lChangeId, boolean isPass, String strNote) throws BaseException {
		if (passport == null)
			throw new NullParameterException("passport");

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			TOrgChange toc = mapper.selectOrgChangeByIdAndLock(lChangeId);
			if (toc == null)
				throw new OrgOperateException("未知的变更记录");

			if (!OrgChangeStatus.APPROVING.equals(toc.getChangeStatus()))
				throw new OrgOperateException("已经审核的变更记录，无法审核");

			TOrganization org = mapper.selectOrgByIdAndLock(toc.getOrg_id());

			if (org == null)
				throw new OrgOperateException("未知的机构");

			if (!OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())))
				throw new OrgOperateException("无效的机构");

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", lChangeId);
			prms.put("verify_user_id", passport.getUserId());
			prms.put("varify_time", new Timestamp(new Date().getTime()));
			prms.put("note", strNote);
			prms.put("change_status", isPass ? OrgChangeStatus.VALID.getCode() : OrgChangeStatus.REJECTED.getCode());

			mapper.approvedOrgChange(prms);

			if (isPass) {
				org.setDescription(toc.getDescription());
				org.setLevels(toc.getLevels());
				org.setLogo_file_id(toc.getLogo_file_id());
				org.setNote(strNote);
				org.setOrg_name(toc.getOrg_name());
				org.setScan_file_ids(toc.getScan_file_ids());
				org.setType(toc.getType());
				TDicValue d_type = SysService.instance.queryDicValueById(toc.getType(), session);
				TDicValue d_level = SysService.instance.queryDicValueById(toc.getLevels(), session);
				org.setType_name(d_type != null ? d_type.getValue() : "");
				org.setLevel_name(d_level != null ? d_level.getValue() : "");
				org.setOrg_property(toc.getOrg_property());
				this.modifyOrg(org, mapper);
				org.setCertification_level(OrgCertificationLevel.CERTIFICATIONED.getCode());
			}else{
				org.setCertification_level(OrgCertificationLevel.FAILED.getCode());
				org.setNote(strNote);
			}
			org.setVarify_time(new Date());
			org.setVerify_user_id(passport.getUserId());

			mapper.updateCertification(org);

			session.commit();

			// 覆盖原有机构
			if (isPass)
				OnlineManager.instance.updateOnlineOrg(org);

		} catch (BaseException e) {
			session.rollback(true);
			throw e;
		} catch (Exception e) {
			session.rollback(true);
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private TOrganization modifyOrg(TOrganization org, OrgMapper mapper) throws OrgOperateException {
		this.checkOrgObject(org);
		List<TOrganization> listOrg = mapper.selectOrgByName(org.getOrg_name());
		if (listOrg != null && listOrg.size() > 0) {
			if (listOrg.size() > 1)
				throw new OrgOperateException("机构重名，请重新命名！");
			TOrganization ooo = listOrg.get(0);
			if (ooo.getId() != org.getId())
				throw new OrgOperateException("机构重名，请重新命名！");
		}
		mapper.modifyOrg(org);
		return org;
	}

	/**
	 * 获取我的机构好友
	 * 
	 * @param passport
	 * @param strOrgName
	 * @param status
	 * @return
	 * @throws BaseException
	 */
	public List<TOrganization> queryMyFriendOrgList(Passport passport, String strOrgName, OrgStatus status)
			throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("org_id", passport.getOrgId());
			mapArg.put("org_name", strOrgName);
			mapArg.put("status", status == null ? null : status.getCode());
			mapArg.put("org_status", status == null ? null : status.getCode());
			return mapper.queryMyFriendOrgList(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TOrganization> queryAllValidOrg(Passport takePassport) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("status", OrgStatus.VALID.getCode());
			return mapper.queryAllOrgs(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 启用和禁用机构
	 * 
	 * @throws BaseException
	 */
	public void enableOrg(Passport passport, long lOrgId, boolean enable) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new OrgOperateException("你没有机构管理权限");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			TOrganization org = mapper.selectOrgByIdAndLock(lOrgId);
			if (org == null)
				throw new OrgOperateException("指定 的机构不存在！");
			if (enable) {
				if (org.getStatus() == OrgStatus.VALID.getCode())
					return;
				org.setStatus(OrgStatus.VALID.getCode());
			} else {
				if (org.getStatus() == OrgStatus.DISABLED.getCode())
					return;
				org.setStatus(OrgStatus.DISABLED.getCode());
			}
			mapper.modifyOrg(org);
			session.commit();

			TOrganization org2 = OnlineManager.instance.getOnlineOrgById(lOrgId);
			if (org2 != null)
				org2.setStatus(org.getStatus());

		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 隐藏和显示机构
	 * 
	 * @throws BaseException
	 */
	public void showOrg(Passport passport) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new OrgOperateException("你没有机构管理权限");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			TOrganization org = mapper.selectOrgByIdAndLock(passport.getOrgId());
			if (org == null)
				throw new OrgOperateException("指定 的机构不存在！");
			if (org.getVisible() == OrgVisible.SHOW.getCode())
				org.setVisible(OrgVisible.HIDE.getCode());
			else
				org.setVisible(OrgVisible.SHOW.getCode());
			this.modifyOrg(org, mapper);
			TOrganization org2 = OnlineManager.instance.getOnlineOrgById(passport.getOrgId());
			if (org2 != null)
				org2.setVisible(org.getVisible());
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void modifyOrgPublic(Passport passport, long lOrgId, OrgPublicStatus status) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new OrgOperateException("你没有机构管理权限");

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			TOrganization org = mapper.selectOrgByIdAndLock(passport.getOrgId());
			if (org == null)
				throw new OrgOperateException("未知的机构");

			if (!OrgStatus.VALID.equals(org.getOrgStatus()))
				throw new OrgOperateException("无效的机构");

			org.setIs_public(status == null ? OrgPublicStatus.NO.getCode() : status.getCode());
			mapper.modifyOrg(org);
			session.commit();

			TOrganization coo = OnlineManager.instance.getOnlineOrgById(passport.getOrgId());
			if (coo != null)
				coo.setIs_public(org.getIs_public());

		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void batAddUser4Org(Passport passport, TOrganization orgInfo, TBaseUser orgAdmin,
			List<TBaseUser> baseUserList) throws Exception {
		if (orgInfo == null)
			throw new OrgOperateException("机构名称不能为空");

		if (orgAdmin == null)
			throw new OrgOperateException("请指定机构负责人");

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new OrgOperateException("你没有机构管理权限");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, String> pwdMap = new HashMap<String, String>();
			TOrganization org = null;
			if (orgInfo.getId() > 0) {
				org = this.searchOrgById(orgInfo.getId());
				if (org == null)
					throw new OrgOperateException("未知的机构");

				if (!OrgStatus.VALID.equals(org.getOrgStatus()))
					throw new OrgOperateException("无效的机构");

			} else {
				// 添加机构
				OrgMapper mapper = session.getMapper(OrgMapper.class);
				List<TOrganization> listOrg = mapper.selectOrgByName(orgInfo.getOrg_name());
				if (listOrg != null && listOrg.size() > 0)
					throw new OrgOperateException("机构重名，请重新命名！");

				org = new TOrganization();
				org.setCreate_time(new Date());
				if (orgAdmin.getId() == 0) {
					if (UserService.instance.isExistsLoginName(orgAdmin.getMobile()))
						throw new UserOperateException(orgAdmin.getMobile() + "账号已存在");

					String strAdminPwd = VcgService.instance.genRandomCode(8, false);
					orgAdmin.setPwd(strAdminPwd);
					this.batAddUser(session, orgAdmin);
					pwdMap.put(orgAdmin.getMobile(), strAdminPwd);
				}
				org.setCreator_id(orgAdmin.getId());
				org.setLevels(orgInfo.getLevels());
				org.setOrg_name(orgInfo.getOrg_name());
				org.setType(orgInfo.getType());
				org.setLogo_file_id(orgInfo.getLogo_file_id());
				org.setScan_file_ids(orgInfo.getScan_file_ids());
				org.setPermission(orgInfo.getPermission());
				org.setStatus(OrgStatus.VALID.getCode());
				org.setOrg_code(SysService.instance.takeNextOrgCode());
				org.setVisible(OrgVisible.SHOW.getCode());
				org.setVarify_time(new Date());
				org.setVerify_user_id(passport.getUserId());
				org.setOrg_property(orgInfo.getOrg_property());

				mapper.insertOrgByAdmin(org);
				// 创建账户
				TFinanceAccount acc = new TFinanceAccount();
				acc.setCreate_Time(new Date());
				acc.setOrg_id(org.getId());
				acc.setAmount(0.00);
				acc.setStatus(AccountStatus.NORMAL.getCode());
				session.getMapper(FinanceMapper.class).addAccount(acc);

				// 创建负责人映射
				TOrgUserMapping oump = new TOrgUserMapping();
				oump.setCreate_time(new Date());
				oump.setOrg_id(org.getId());
				oump.setStatus(OrgUserMapperStatus.VALID.getCode());
				oump.setUser_id(org.getCreator_id());
//				List<OrgPermission> ops = org.getPermissionList();
				Set<UserPermission> ups = new HashSet<UserPermission>();
				ups.add(UserPermission.ORG_MGR);
				ups.add(UserPermission.ORG_MEDICAL_MGR);
				if (orgAdmin.getType()==UserType.ORG_DOCTOR.getCode()) {
					ups.add(UserPermission.ORG_AUDIT);
					ups.add(UserPermission.ORG_REPORT);
				}
//				if (ops.size() > 1) {
//				} else if (ops.contains(OrgPermission.REQUESTOR)) {
//					ups.add(UserPermission.ORG_MGR);
//					ups.add(UserPermission.ORG_MEDICAL_MGR);
//				} else if (ops.contains(OrgPermission.DIAGNOSISER)) {
//					ups.add(UserPermission.ORG_AUDIT);
//					ups.add(UserPermission.ORG_REPORT);
//					ups.add(UserPermission.ORG_MGR);
//				}
				oump.setPermission(PermissionsSerializer.toUserPermissionString(ups));
				session.getMapper(UserMapper.class).insertOrg2User(oump);
			}

			// 添加用户
			for (TBaseUser tbu : baseUserList) {
				String strPwd = VcgService.instance.genRandomCode(6, true);
				tbu.setPwd(strPwd);
				this.batAddUser(session, tbu);
				this.batAddUserMapping(session, org.getId(), tbu.getId());
				pwdMap.put(tbu.getMobile(), strPwd);
			}

			session.commit();

			String strSendContent = "";

			if (orgInfo.getId() <= 0) {
				strSendContent = "尊敬的用户，您的影泰平台用户["
						+ new StringBuffer(orgAdmin.getMobile()).replace(3, 8, "*****").toString() + "]已经被设置为["
						+ org.getOrg_name() + "]的机构负责人。【影泰科技】";

				SmsServer.instance.sendSMS(orgAdmin.getMobile(), strSendContent);
			}

			for (String tel : pwdMap.keySet()) {
				// strSendContent = "欢迎注册影泰平台，您的账号密码是：" + pwdMap.get(tel) +
				// "。如非本人操作，请不要告知其他人。【影泰科技】";
				strSendContent = "【影泰科技】欢迎加盟影泰科技远程影像平台，您的账号[" + new StringBuffer(tel).replace(3, 8, "*****").toString()
						+ "]，密码（" + pwdMap.get(tel)
						+ "）。请及时登陆我们的官方网站http://www.yingtai360.com/修改密码，按要求完善相关资料。电话：028-83332921";

				SmsServer.instance.sendSMS(tel, strSendContent);
			}

		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private void batAddUser(SqlSession session, TBaseUser tbu) throws NoSuchAlgorithmException, UserOperateException, UnsupportedEncodingException {
		if (session == null)
			throw new UserOperateException("数据库连接失败");

		if (tbu == null)
			throw new UserOperateException("用户信息不能为空");

		if (!RulesValidator.instance.validateMobilePhone(tbu.getMobile()))
			throw new UserOperateException(tbu.getMobile() + "请使用正确的手机号码注册");

		UserMapper mapper = session.getMapper(UserMapper.class);
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String strPwd = StringUtil.toHexString((md5.digest(tbu.getPwd().getBytes("utf-8"))));
		tbu.setPwd(strPwd);
		tbu.setStatus(UserStatus.NORMAL.getCode());
		tbu.setCreate_time(new Date());
		mapper.insertUser(tbu);

		if (UserType.ORG_DOCTOR.equals(tbu.getUserType())) {
			TDoctorUser tdoc = new TDoctorUser();
			tdoc.setId(tbu.getId());
			mapper.registerDoctor(tdoc);
		} else if (UserType.ORG_GENERAL.equals(tbu.getUserType())) {
			TGeneralUser tgen = new TGeneralUser();
			tgen.setId(tbu.getId());
			mapper.registerGeneral(tgen);
		}
	}

	private void batAddUserMapping(SqlSession session, long lOrgId, long lUserId) throws BaseException {
		try {
			TOrgUserMapping oump = new TOrgUserMapping();
			oump.setCreate_time(new Date());
			oump.setOrg_id(lOrgId);
			oump.setStatus(OrgUserMapperStatus.VALID.getCode());
			oump.setUser_id(lUserId);
			session.getMapper(UserMapper.class).insertOrg2User(oump);
		} catch (Exception e) {
			throw e;
		}
	}

	public TOrganization getOrgLite(long lOrgId) throws BaseException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TOrganization org = session.getMapper(OrgMapper.class).selectOrgById(lOrgId);

			if (org == null)
				return null;

			if (!OrgStatus.VALID.equals(org.getOrgStatus()))
				return null;

			TOrganization o = new TOrganization();
			o.setId(org.getId());
			o.setOrg_name(org.getOrg_name());
			o.setOrg_code(org.getOrg_code());
			o.setType_name(org.getType_name());
			o.setLevel_name(org.getLevel_name());
			o.setDescription(org.getDescription());
			o.setIs_public(org.getIs_public());
			o.setCreator_id(org.getCreator_id());

			return o;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	//
	//
	// /**
	// * 检查当前用户是否可以访问指定机构的远程病例库，不能则抛出异常
	// *
	// * @param passport
	// * 当前用户
	// * @param org_id
	// * 要查询的病例库的所属机构
	// * @return 是否允许查询
	// */
	// public boolean checkCaseAccessPerm(long org_id, long another_org_id) {
	// try {
	//
	// throw new OrgOperateException("你不能范围此机构的远程病例库！");
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }

	// public List<TOrgAffix> queryDefaultOrgAffix() throws OrgOperateException
	// {
	// return this.queryOrgAffixByOrgId(0);
	// }
	//
	// public TOrgAffix addOrgAffix(Passport passport, TOrgAffix orgAffix)
	// throws OrgOperateException {
	// if (!UserService.instance.checkUserPermission(passport,
	// UserPermission.ADMIN_ORG_MGR))
	// throw new OrgOperateException("您缺少机构管理员权限！");
	// this.checkOrgAffixObject(orgAffix);
	// if (orgAffix.getOrg_id() <= 0)
	// throw new OrgOperateException("机构ID必须指定！");
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// orgAffix.setCreate_time(new Date());
	// orgAffix.setStatus(1);
	// mapper.insertOrgAffix(orgAffix);
	// session.commit();
	// return orgAffix;
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new OrgOperateException(e);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }
	//
	// public TOrgAffix modifyOrgAffix(Passport passport, TOrgAffix orgAffix)
	// throws OrgOperateException {
	// if (!UserService.instance.checkUserPermission(passport,
	// UserPermission.ADMIN_ORG_MGR))
	// throw new OrgOperateException("您缺少机构管理员权限！");
	// this.checkOrgAffixObject(orgAffix);
	// if (orgAffix.getOrg_id() <= 0)
	// throw new OrgOperateException("机构ID必须指定！");
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// TOrgAffix oldOrgAffix =
	// mapper.selectOrgAffixByIdForUpdate(orgAffix.getId());
	// if (oldOrgAffix == null)
	// throw new OrgOperateException("要修改的数据不存在！");
	// oldOrgAffix.setAe_code(orgAffix.getAe_code());
	// oldOrgAffix.setDicomweb_url(orgAffix.getDicomweb_url());
	// oldOrgAffix.setInternet_ip(orgAffix.getInternet_ip());
	// oldOrgAffix.setIntranet_url(orgAffix.getIntranet_url());
	// oldOrgAffix.setPassword(orgAffix.getPassword());
	// oldOrgAffix.setQuery_url(orgAffix.getQuery_url());
	// oldOrgAffix.setRemote_server_version(orgAffix.getRemote_server_version());
	// oldOrgAffix.setUser_name(orgAffix.getUser_name());
	// mapper.updateOrgAffix(oldOrgAffix);
	// session.commit();
	// return oldOrgAffix;
	// } catch (Exception e) {
	// session.rollback(true);
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	public TOrganization queryOrgById(long orgId, SqlSession session) {
		OrgMapper mapper = session.getMapper(OrgMapper.class);
		return mapper.selectOrgById(orgId);
	}

	public TOrganization queryOrgById(long orgId) throws OrgOperateException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return this.queryOrgById(orgId, session);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TOrganization> selectOrgList4WX(String strWXUnionID, String strOrgName, Long lOrgCode,
			ShareRemoteServerVersion version) throws OrgOperateException, SystemException {
		if (strWXUnionID == null || strWXUnionID.isEmpty())
			throw new OrgOperateException("参数错误");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<>();
			prms.put("status", OrgStatus.VALID.getCode());
			prms.put("org_name", strOrgName == null || strOrgName.isEmpty() ? null : strOrgName);
			prms.put("org_code", lOrgCode);
			prms.put("remote_version", version.getCode());

			return session.getMapper(OrgMapper.class).selectOrgList4WX(prms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public TOrganization getMyOrgInfoById(Passport passport,long lOrgId) throws OrgOperateException, SystemException {
		if (passport == null || lOrgId<=0)
			throw new OrgOperateException("参数错误");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TOrganization to=session.getMapper(OrgMapper.class).selectOrgById(lOrgId);
			if(to==null)
				throw new OrgOperateException("未知的机构");
			if(to.getCreator_id()!=passport.getUserId())
				throw new OrgOperateException("未知的机构");

			return to;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void reSubmit(Passport passport, long lOrgId, String strName, Long lTypeId, List<Long> lScanFileIds,
																Long lLogoFileId, Long lLevelId, String strDesc, OrgProperty property) throws BaseException {
		if (passport == null)
			throw new OrgOperateException("内部错误！");

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);

			TBaseUser user = UserService.instance.queryUserById(passport, null);
			if (user == null)
				throw new OrgOperateException("未知的用户");
			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(user.getStatus())))
				throw new OrgOperateException("无效的用户");

			TOrganization org = mapper.selectOrgByIdAndLock(lOrgId);

			if(org==null)
				throw new OrgOperateException("未知的机构");
			if(user.getId()!=org.getCreator_id())
				throw new OrgOperateException("非法的机构");
			if(!OrgStatus.REJECTED.equals(org.getOrgStatus()))
				throw new OrgOperateException("无效的机构");

			if(!org.getOrg_name().equals(strName)){
				List<TOrganization> listOrg = mapper.selectOrgByName(strName);
				if (listOrg != null && listOrg.size() > 0)
					throw new OrgOperateException("机构重名，请重新命名！");
			}

			org.setDescription(strDesc);
			org.setLevels(lLevelId);
			org.setNote(null);
			org.setOrg_name(strName);
			org.setStatus(OrgStatus.APPROVING.getCode());
			org.setType(lTypeId);
			org.setLogo_file_id(lLogoFileId);
			String strIds = "";
			strIds = "";
			for (long l : lScanFileIds) {
				strIds += "," + l;
			}
			org.setScan_file_ids(strIds.isEmpty() ? "" : strIds.substring(1));
			org.setOrg_property(property.getCode());

			this.checkOrgObject(org);

			mapper.reSubmit(org);
			session.commit();

		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TOrganization> queryOrgByLogoFileId(long logo_file_id) throws OrgOperateException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			OrgMapper mapper = session.getMapper(OrgMapper.class);
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("logo_file_id", logo_file_id);
			prms.put("begin_idx", 1);
			prms.put("end_idx", 5);
			return mapper.selectAllOrgs(prms);
		} catch (Exception e) {
			throw new OrgOperateException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
	}
}
