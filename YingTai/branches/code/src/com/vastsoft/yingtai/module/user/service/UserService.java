package com.vastsoft.yingtai.module.user.service;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.vastsoft.util.GUID;
import com.vastsoft.util.RulesValidator;
import com.vastsoft.util.StringUtil;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.vcg.VcgService;
import com.vastsoft.yingtai.core.Constants;
import com.vastsoft.yingtai.core.PermissionsSerializer;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.core.VCType;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.online.OnlineManager;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.mapper.OrgMapper;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.sys.exception.SysOperateException;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.constants.OrgUserMapperStatus;
import com.vastsoft.yingtai.module.user.constants.UserChangeStatus;
import com.vastsoft.yingtai.module.user.constants.UserStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.entity.FOrgUserMapping;
import com.vastsoft.yingtai.module.user.entity.TAdminUser;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;
import com.vastsoft.yingtai.module.user.entity.TDoctorUser;
import com.vastsoft.yingtai.module.user.entity.TGeneralUser;
import com.vastsoft.yingtai.module.user.entity.TOrgUserMapping;
import com.vastsoft.yingtai.module.user.entity.TUserChange;
import com.vastsoft.yingtai.module.user.entity.TUserConfig;
import com.vastsoft.yingtai.module.user.exception.UserOperateException;
import com.vastsoft.yingtai.module.user.mapper.UserMapper;
import com.vastsoft.yingtai.utils.mail.MailServer;
import com.vastsoft.yingtai.utils.sms.SmsServer;

/**
 * @author jyb
 */
public class UserService {
	private static final TOrganization ADMIN_ORG = new TOrganization(Constants.ADMIN_ORG_ID);
	private static final Logger logger = Logger.getLogger(UserService.class);

//	fordebug
//	public Passport getDeBug(){
//		return new Passport(500494,"DEBUG","DEBUG",UserType.ORG_DOCTOR,100048L,null,true);
//	}

	/**
	 * 为接口服务提供passport支持
	 * @param lOrgId
	 * @return
	 */
	public Passport getPrivodePassport(long lOrgId) throws UserOperateException {
		if(lOrgId>0) {
			try {
				TOrganization to = OrgService.instance.getOrgLite(lOrgId);
				if (to == null) throw new UserOperateException("不支持的服务机构");
				Passport passport=new Passport(to.getCreator_id(), "", to.getOrg_name(), null, to.getId(), null, true);
				Set<UserPermission> listUserPermission = this.getAllPermissionByUser(passport.getUserId(), lOrgId);
				TBaseUser user=new TBaseUser();
				user.setId(to.getCreator_id());
				OnlineManager.instance.addOnlineUser(to, user, listUserPermission);
				return passport;
			} catch (BaseException e) {
				throw new UserOperateException(e.getMessage());
			}
		}
		return null;
	}

	public final class Passport {
		private long lUserId;
		private String strUserName;
		private Long lOrgId;
		private String strOrgName;
		private UserType ut;
		private boolean bValid;
		private String strBBSToken;

		private Passport(long lUserId, String strUserName, String strOrgName, UserType ut, Long lOrgId, String strBBSId,
				boolean isValid) {
			this.lUserId = lUserId;
			this.lOrgId = lOrgId;
			this.ut = ut;
			this.strOrgName = strOrgName;
			this.strUserName = strUserName;
			this.bValid = isValid;
			this.strBBSToken = strBBSId;
		}

		public long getUserId() {
			return lUserId;
		}

		public UserType getUserType() {
			return this.ut;
		}

		public String getBBSToken() {
			return this.strBBSToken;
		}

		private void setOrgId(Long lOrgId) {
			this.lOrgId = lOrgId;
		}

		private void setOrgName(String strOrgName) {
			this.strOrgName = strOrgName;
		}

		private void setUserType(UserType ut) {
			this.ut = ut;
		}

		public Long getOrgId() {
			return lOrgId;
		}

		public String getUserName() {
			return strUserName;
		}

		public String getOrgName() {
			return strOrgName;
		}

		public boolean getValid() {
			return bValid;
		}

		private void setValid(boolean bValid) {
			this.bValid = bValid;
		}
	}

	public final class ValidateCode {
		private String strLoginName;
		private String strCode;
		private Date dtCreatDate;

		public String getLoginName() {
			return strLoginName;
		}

		public String getCode() {
			return strCode;
		}

		public Date getCreatDate() {
			return dtCreatDate;
		}

		private ValidateCode(String strLoginName, String strCode, Date dtCreatDate) {
			this.strLoginName = strLoginName;
			this.strCode = strCode;
			this.dtCreatDate = dtCreatDate;
		}
	}

	public static final UserService instance = new UserService();

	private Map<Long, Passport> mapPassport = new ConcurrentHashMap<Long, Passport>();

	private UserService() {
	}

	/**
	 * 检查用户权限
	 * 
	 * @param passport
	 *            身份认证
	 * @param userPermission
	 *            用户权限
	 * @return 是否具有该权限
	 * @throws UserOperateException
	 *             用户操作出错异常
	 * @throws DbException
	 *             数据库操作异常
	 */
	public boolean checkUserPermission(Passport passport, UserPermission userPermission) {
		if (passport == null || userPermission == null)
			return false;

		Set<UserPermission> ups = null;

		// 如果缓存里面有当前用户，就不要再查询数据库了。
		if (passport.getOrgId() != null) {
			ups = OnlineManager.instance.getPermisByUser(passport.getOrgId(), passport.getUserId());
			return ups != null && ups.contains(userPermission);
		}

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(passport.getUserId());
			if (tbu == null)
				return false;

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				return false;

			if (UserType.ADMIN.equals(UserType.parseCode(tbu.getType()))
					|| UserType.SUPER_ADMIN.equals(UserType.parseCode(tbu.getType()))) {
				TAdminUser tau = session.getMapper(UserMapper.class).queryAdminUserById(passport.getUserId());
				if (tau == null)
					return false;

				ups = tau.getPermissionList();
			} else {
				Map<String, Object> prms = new HashMap<String, Object>();
				prms.put("org_id", passport.getOrgId());
				prms.put("user_id", passport.getUserId());
				prms.put("status", UserStatus.NORMAL.getCode());
				TOrgUserMapping mapper = session.getMapper(UserMapper.class).selectMapperByUser(prms);

				if (mapper == null)
					return false;

				ups = PermissionsSerializer.fromUserPermissionString(mapper.getPermission());
			}

			return ups != null && ups.contains(userPermission);
		} catch (Exception e) {
			return false;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取用户权限
	 * 
	 * @param lUserId
	 * @param lOrgId
	 * @return
	 * @throws NullParameterException
	 * @throws UserOperateException
	 * @throws DbException
	 */
	public Set<UserPermission> getAllPermissionByUser(long lUserId, Long lOrgId)
			throws NullParameterException, UserOperateException, DbException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(lUserId);

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (UserType.ADMIN.equals(tbu.getUserType()) || UserType.SUPER_ADMIN.equals(tbu.getUserType())) {
				TAdminUser tau = session.getMapper(UserMapper.class).queryAdminUserById(lUserId);
				return tau.getPermissionList();
			} else {
				Map<String, Object> prms = new HashMap<String, Object>();
				prms.put("org_id", lOrgId);
				prms.put("user_id", lUserId);
				prms.put("status", OrgUserMapperStatus.VALID.getCode());
				TOrgUserMapping tom = session.getMapper(UserMapper.class).selectMapperByUser(prms);
				if (tom == null)
					return null;

				return PermissionsSerializer.fromUserPermissionString(tom.getPermission());
			}
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	/**
	 * 验证指定用户是否属于指定机构
	 * 
	 * @throws DbException
	 * @throws UserOperateException
	 */
	public boolean checkUserBelongOrg(Passport passport, long lUserId, long lOrgId) {
		if (passport == null)
			return false;
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", lOrgId);
			prms.put("user_id", lUserId);
			TOrgUserMapping mapper = session.getMapper(UserMapper.class).selectMapperByUser(prms);
			return mapper != null;
		} catch (Exception e) {
			return false;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 验证用户是否属于该机构
	 * 
	 * @throws DbException
	 * @throws UserOperateException
	 */
	public boolean checkUserBelongOrg(Passport passport, long lOrgId) {
		if (passport == null)
			return false;

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", lOrgId);
			prms.put("user_id", passport.getUserId());
			prms.put("status", OrgUserMapperStatus.VALID.getCode());
			TOrgUserMapping mapper = session.getMapper(UserMapper.class).selectMapperByUser(prms);
			return mapper != null;
		} catch (Exception e) {
			return false;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/** 查询所有用户 */
	// @Deprecated
	// public List<User> queryAllUserList(Passport passport)
	// {
	// return null;
	// }

	/**
	 * 用户加入机构
	 * 
	 * @param passport
	 *            身份对象
	 * @param lOrgId
	 * @throws BaseException
	 */
	public void requestJoinOrg(Passport passport, long lOrgId)
			throws BaseException {
		if (passport == null)
			throw new NullParameterException();

		TBaseUser user = this.queryUserById(passport, null);

		if (user == null)
			throw new UserOperateException("未知的用户");

		if (!UserStatus.NORMAL.equals(UserStatus.parseCode(user.getStatus())))
			throw new UserOperateException("无效的用户");

		TOrganization org = OrgService.instance.searchOrgById(lOrgId);

		if (org == null)
			throw new UserOperateException("未知的机构");

		if (!OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())))
			throw new UserOperateException("无效的机构");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("user_id", passport.getUserId());
			prms.put("org_id", lOrgId);
			TOrgUserMapping mapper = session.getMapper(UserMapper.class).selectMapperByUserAndLock(prms);

			if (mapper != null && UserStatus.NORMAL.equals(mapper.getStatus()))
				throw new UserOperateException("已经加入过该机构");

			if (mapper == null) {
				TOrgUserMapping tum = new TOrgUserMapping();
				tum.setCreate_time(new Date());
				tum.setOrg_id(lOrgId);
				tum.setStatus(OrgUserMapperStatus.APPROVING.getCode());
				tum.setUser_id(passport.getUserId());

				session.getMapper(UserMapper.class).insertOrg2User(tum);
			} else {
				mapper.setCreate_time(new Date());
				mapper.setNote("");
				mapper.setStatus(OrgUserMapperStatus.APPROVING.getCode());

				session.getMapper(UserMapper.class).modifyMapper(mapper);
			}

			session.commit();
		} catch (UserOperateException e) {
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
	 * 通过手机号码或者邮箱获取用户信息，一般用于登陆或者验证是否存在
	 * 
	 * @param strLoginName
	 *            指定的手机号码
	 * @return 获取到的用户列表
	 * @throws NullParameterException
	 * @throws DbException
	 *             数据库操作异常
	 */
	public boolean isExistsLoginName(String strLoginName) throws NullParameterException, DbException {
		if (strLoginName == null || strLoginName.isEmpty())
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbus = session.getMapper(UserMapper.class).selectUserByLoginName(strLoginName);
			return tbus != null;
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 添加管理员
	 * 
	 * @param passport
	 *            身份标志
	 * @param adminUser
	 *            要添加的管理员对象
	 * @param strPwd
	 *            用户密码
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 *             用户操作错误
	 * @throws DbException
	 *             数据库操作异常
	 */
	public TAdminUser addAdminUser(Passport passport, TAdminUser adminUser, String strPwd)
			throws NullParameterException, NotPermissionException, DbException {
		if (passport == null || adminUser == null || strPwd == null || strPwd.isEmpty())
			throw new NullParameterException();

		if (!UserType.SUPER_ADMIN.equals(passport.getUserType()))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			TBaseUser user = new TBaseUser();
			user.setEmail(adminUser.getEmail());
			user.setMobile(adminUser.getMobile());
			user.setUser_name(adminUser.getUser_name());
			user.setGender(adminUser.getGender());
			user.setBirthday(adminUser.getBirthday());
			user.setIdentity_id(adminUser.getIdentity_id());
			user.setPhoto_file_id(adminUser.getPhoto_file_id());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			strPwd = StringUtil.toHexString((md5.digest(strPwd.getBytes("utf-8"))));
			user.setPwd(strPwd);
			user.setStatus(UserStatus.NORMAL.getCode());
			user.setType(UserType.ADMIN.getCode());
			user.setCreate_time(new Date());
			chechUserObject(user);
			session.getMapper(UserMapper.class).insertUser(user);

			TAdminUser tau = new TAdminUser();
			tau.setId(user.getId());
			tau.setPermission(adminUser.getPermission());

			session.getMapper(UserMapper.class).addAdmin(tau);
			session.commit();

			return tau;
		} catch (Exception e) {
			session.rollback();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param strLoginName
	 * @param strPwd
	 * @return
	 * @throws NullParameterException
	 * @throws DbException
	 * @throws UserOperateException
	 */
	public TBaseUser regsiterUser(String strLoginName, String strPwd)
			throws NullParameterException, DbException, UserOperateException {
		if (strLoginName == null || strLoginName.isEmpty() || strPwd == null || strPwd.isEmpty())
			throw new NullParameterException();

//		if (!RulesValidator.instance.validateMobilePhone(strLoginName))
		if (!StringTools.isMobileNum(strLoginName))
			throw new UserOperateException("请使用正确的手机号码注册");

		TBaseUser user = null;

		if (this.isExistsLoginName(strLoginName))
			throw new UserOperateException("账号已存在");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			user = new TBaseUser();
			// user.setEmail(RulesValidator.instance.validateEmail(strLoginName)?strLoginName:null);
			user.setMobile(strLoginName);
			user.setUser_name(strLoginName);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			strPwd = StringUtil.toHexString((md5.digest(strPwd.getBytes("utf-8"))));
			user.setPwd(strPwd);
			user.setStatus(UserStatus.NORMAL.getCode());
			user.setType(UserType.GUEST.getCode());
			user.setCreate_time(new Date());
			session.getMapper(UserMapper.class).insertUser(user);
			session.commit();
			return user;
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 注册普通用户
	 * 
	 * @throws BaseException
	 */
	public void addGeneralUser(Passport passport, TGeneralUser generalUser) throws BaseException {
		if (passport == null || generalUser == null)
			throw new NullParameterException();

		if (!UserType.ORG_GENERAL.equals(UserType.parseCode(generalUser.getType())))
			throw new UserOperateException("用户类型错误");

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();

			TBaseUser user = session.getMapper(UserMapper.class).queryUserByIdAndLock(passport.getUserId());

			if (user == null)
				throw new UserOperateException("未知的用户");

			if (!UserType.GUEST.equals(UserType.parseCode(user.getType())))
				throw new UserOperateException("用户已存在");

			generalUser.setId(user.getId());
			// generalUser.setCreate_time(new Date());
			generalUser.setStatus(UserStatus.VERITIING.getCode());
			generalUser.setType(generalUser.getType());
			chechUserObject(generalUser);
			session.getMapper(UserMapper.class).registerGeneral(generalUser);
			session.getMapper(UserMapper.class).modifyUserInfo(generalUser);
			session.commit();

			passport.setUserType(UserType.ORG_GENERAL);
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	//
	/**
	 * 注册医生用户
	 * 
	 * @param passport
	 * @param doctorUser
	 * @throws BaseException
	 */
	public void addDoctorUser(Passport passport, TDoctorUser doctorUser) throws BaseException {
		if (passport == null || doctorUser == null)
			throw new NullParameterException();

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();

			TBaseUser user = session.getMapper(UserMapper.class).queryUserByIdAndLock(passport.getUserId());

			if (user == null)
				throw new UserOperateException("未知的用户");

			if (!UserType.GUEST.equals(UserType.parseCode(user.getType())))
				throw new UserOperateException("用户已存在");

			doctorUser.setId(user.getId());
			// doctorUser.setCreate_time(new Date());
			doctorUser.setStatus(UserStatus.VERITIING.getCode());
			chechUserObject(doctorUser);
			session.getMapper(UserMapper.class).registerDoctor(doctorUser);
			session.getMapper(UserMapper.class).modifyUserInfo(doctorUser);
			session.commit();

			passport.setUserType(UserType.ORG_DOCTOR);
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 用户登陆
	 * 
	 * @param strLoginName
	 *            手机或者邮箱
	 * @param strPwd
	 *            密码
	 * @return 身份对象
	 * @throws UserOperateException
	 * @throws NullParameterException
	 * @throws DbException
	 */
	public Passport loginByPwd(String strLoginName, String strPwd)
			throws UserOperateException, NullParameterException, DbException {
		if (strLoginName == null || strLoginName.isEmpty() || strPwd == null || strPwd.isEmpty())
			throw new UserOperateException("登录名和密码必须填写！");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("login_name", strLoginName);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String md5Pwd = StringUtil.toHexString((md5.digest(strPwd.getBytes("utf-8"))));
			prms.put("pwd", md5Pwd);

			TBaseUser user = session.getMapper(UserMapper.class).loginUser(prms);
			if (user == null)
				throw new UserOperateException("用户名或密码错误");
			if (user.getType() != UserType.SUPER_ADMIN.getCode()) {
				if (UserStatus.DELETED.equals(UserStatus.parseCode(user.getStatus())))
					throw new UserOperateException("无效的账号");
				if (UserStatus.DISABLE.getCode() == user.getStatus())
					throw new UserOperateException("你的账户已经被管理员禁用！");
			}

			user.setLast_login_time(new Date());
			session.getMapper(UserMapper.class).modifyUserInfo(user);
			session.commit();

			downLineUser(user.getId(), null);

			UserType ut = UserType.parseCode(user.getType());
			Passport newPassport = null;

			String token = new GUID().toString();

			if (UserType.ADMIN.equals(ut) || UserType.SUPER_ADMIN.equals(ut)) {
				TAdminUser admin = session.getMapper(UserMapper.class).queryAdminUserById(user.getId());
				admin.getUserInfo(user);
				OnlineManager.instance.addOnlineUser(ADMIN_ORG, admin, admin.getPermissionList());
				newPassport = new Passport(admin.getId(), admin.getUser_name(), null, ut, ADMIN_ORG.getId(), token,
						true);
			} else {
				newPassport = new Passport(user.getId(), user.getUser_name(), null, ut, null, token, true);
			}

			this.mapPassport.put(user.getId(), newPassport);
			return newPassport;
			// else if (UserType.ORG_DOCTOR.equals(ut))
			// {
			// buser =
			// session.getMapper(UserMapper.class).queryDoctorUserById(user.getId());
			// }
			// else if (UserType.ORG_GENERAL.equals(ut))
			// {
			// buser =
			// session.getMapper(UserMapper.class).queryGeneralUserById(user.getId());
			// }
		} catch (UserOperateException e) {
			session.rollback();
			throw e;
		} catch (Exception e) {
			session.rollback();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// 销毁Passport
	public void loginOut(Passport passport) throws NullParameterException {
		if (passport == null)
			throw new NullParameterException();

		if (passport.getOrgId() != null) {
			OnlineManager.instance.removeOnlineUser(passport.getOrgId(), passport.getUserId());
		}

		passport.setValid(false);
		this.mapPassport.remove(passport.getUserId());
	}

	/**
	 * 强制下线某一用户
	 * 
	 * @throws NullParameterException
	 */
	private void downLineUser(long lUserId, Long lOrgId) throws NullParameterException {
		Passport passport = mapPassport.get(lUserId);
		if (passport != null) {
			if (lOrgId != null && lOrgId > 0) {
				if (passport.getOrgId() != null && passport.getOrgId() > 0) {
					if (!passport.getOrgId().equals(lOrgId)) {
						return;
					}
				} else {
					return;
				}
			}
			loginOut(passport);
		}
	}

	/**
	 * 进入机构
	 * 
	 * @param passport
	 * @param lOrgId
	 * @throws BaseException 
	 */
	public void entryOrg(Passport passport, long lOrgId)
			throws BaseException {
		if (passport == null)
			throw new NullParameterException();

		TOrganization org = OrgService.instance.searchOrgById(lOrgId);

		if (org == null)
			throw new UserOperateException("指定的要加入的机构未找到！");

		if (!OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())))
			throw new UserOperateException("您要进入的机构无效");

		if (!this.checkUserBelongOrg(passport, lOrgId))
			throw new NotPermissionException();

		TBaseUser user = this.queryUserById(passport, null);
		Set<UserPermission> listUserPermission = this.getAllPermissionByUser(passport.getUserId(), lOrgId);
		OnlineManager.instance.addOnlineUser(org, user, listUserPermission);

		passport.setOrgId(lOrgId);
		passport.setOrgName(org.getOrg_name());
	}

	/**
	 * 发送短信或邮件验证码
	 * 
	 * @param strLoginName
	 *            手机号或邮箱
	 * @throws NullParameterException
	 * @throws UserOperateException
	 * @throws DbException
	 */
	public ValidateCode generateCode(String strLoginName, VCType type)
			throws NullParameterException, UserOperateException, DbException {
		if (strLoginName == null || strLoginName.isEmpty() || type == null)
			throw new NullParameterException();

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();

			// String strName = strLoginName == null || strLoginName.isEmpty() ?
			// null : strLoginName;
			// TBaseUser tbu =
			// session.getMapper(UserMapper.class).selectUserByLoginName(strName);
			//
			// if (tbu == null)
			// throw new UserOperateException("未知的用户");
			//
			// if
			// (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
			// throw new UserOperateException("无效的用户");

			String strCode = VcgService.instance.genRandomCode(4, true);
			String strSendContent = "";
			if (VCType.USER_REG.equals(type)) {
				strSendContent = "欢迎注册影泰平台，您的验证码是：" + strCode + "。如非本人操作，请不要告知其他人。【影泰科技】";
			} else if (VCType.USER_MODIFY.equals(type)) {
				if (this.isExistsLoginName(strLoginName))
					throw new UserOperateException("该号码已被使用");

				strSendContent = "欢迎使用影泰平台，您的验证码是：" + strCode + "。如非本人操作，请不要告知其他人。【影泰科技】";
			} else if (VCType.USER_FIND.equals(type)) {
				if (!this.isExistsLoginName(strLoginName))
					throw new UserOperateException("帐号不存在");

				strSendContent = "欢迎使用影泰平台，您的验证码是：" + strCode + "。如非本人操作，请不要告知其他人。【影泰科技】";
			}
			boolean isEmail = RulesValidator.instance.validateEmail(strLoginName);
			boolean isMobile = RulesValidator.instance.validateMobilePhone(strLoginName);
			logger.info(strSendContent);
			if (isEmail) {
				HashMap<String, String> mapContent = new HashMap<String, String>();
				mapContent.put("valid_code", strCode);
				mapContent.put("account_name", strLoginName);
				mapContent.put("valid_time", SysService.instance.takeCheckCodeValidMinutes() + "");

				MailServer.instance.sendMail(strLoginName, "亲", "mt_operat_code", mapContent);
			} else if (isMobile) {
				SmsServer.instance.sendSMS(strLoginName, strSendContent);
			} else {
				throw new UserOperateException("请输入正确的手机号码或邮箱地址");
			}

			ValidateCode vc = new ValidateCode(strLoginName, strCode, new Date());

			return vc;
		} catch (UserOperateException e) {
			throw e;
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// private boolean checkValidateCode(String strLoginName,String
	// strCode,ValidateCode vc){
	//
	// }

	/**
	 * @throws BaseException
	 * @throws SysOperateException
	 * @throws DbException
	 *             手机、邮箱重置密码
	 * 
	 * @param strLoginName
	 *            手机或者邮箱 @param strNewPwd 新密码 @param strCheckCode 验证码 @throws
	 *            UserOperateException 用户操作异常 @throws
	 *            NullParameterException @throws
	 */
	public void resetPwd(String strLoginName, String strNewPwd, ValidateCode vc) throws BaseException {
		if (strLoginName == null || strLoginName.isEmpty() || strNewPwd == null || strNewPwd.isEmpty() || vc == null)
			throw new NullParameterException();

		if (!strLoginName.equals(vc.getLoginName()))
			throw new UserOperateException("验证码错误");

		int mi = SysService.instance.takeCheckCodeValidMinutes();

		Calendar cl = Calendar.getInstance();
		cl.setTime(vc.getCreatDate());
		cl.add(Calendar.MINUTE, mi);

		if (cl.after(new Date()))
			throw new UserOperateException("验证码已过期，请重新获取");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			TBaseUser tbu = session.getMapper(UserMapper.class).selectUserByLoginName(strLoginName);
			if (tbu == null)
				throw new UserOperateException("未知的用户");

			Map<String, Object> prms = new HashMap<String, Object>();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			strNewPwd = StringUtil.toHexString((md5.digest(strNewPwd.getBytes("utf-8"))));
			prms.put("id", tbu.getId());
			prms.put("new_pwd", strNewPwd);
			session.getMapper(UserMapper.class).resetUserPwd(prms);
			session.commit();
		} catch (UserOperateException e) {
			session.rollback();
			throw e;
		} catch (Exception e) {
			session.rollback();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void modifyLoginPwd(Passport passport, String strOldPwd, String strNewPwd)
			throws NullParameterException, DbException, UserOperateException {
		if (passport == null || strOldPwd == null || strOldPwd.isEmpty() || strNewPwd == null || strNewPwd.isEmpty())
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			Map<String, Object> prms = new HashMap<String, Object>();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// strOldPwd =
			// StringUtil.toHexString((md5.digest(strOldPwd.getBytes("utf-8"))));
			strNewPwd = StringUtil.toHexString((md5.digest(strNewPwd.getBytes("utf-8"))));
			prms.put("id", passport.getUserId());
			prms.put("old_pwd", strOldPwd);
			prms.put("new_pwd", strNewPwd);
			int c = session.update(UserMapper.class.getName() + ".modifyUserPwd", prms);
			if (c == 0)
				throw new UserOperateException("密码错误");

			// session.getMapper(UserMapper.class).modifyUserPwd(prms);
			session.commit();
		} catch (UserOperateException e) {
			session.rollback();
			throw e;
		} catch (Exception e) {
			session.rollback();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	/**
	 * 通过机构ID查找机构下的所有用户
	 * 
	 * @param passport
	 *            身份认证
	 * @param lOrgid
	 *            工作机构ID
	 * @param spu
	 *            分页模型
	 * @return 查询到的所有用户
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws DbException
	 */
	public List<TOrgUserMapping> queryUserListByOrgId(Passport passport, Long lOrgid, String strUserName,
			String strMobile, OrgUserMapperStatus status, SplitPageUtil spu)
			throws NullParameterException, NotPermissionException, DbException {
		if (passport == null)
			throw new NullParameterException();

		boolean isAdmin = this.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR);
		boolean isMger = this.checkUserPermission(passport, UserPermission.ORG_MGR);
		Long orgid = isAdmin ? lOrgid : isMger ? passport.getOrgId() : null;

		if (orgid == null)
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("begin_idx", spu.getCurrMinRowNum());
			prms.put("end_idx", spu.getCurrMaxRowNum());
			prms.put("org_id", orgid.longValue());
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);
			prms.put("status", status != null ? status.getCode() : null);
			return session.getMapper(UserMapper.class).selectMapperByOrg(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public int queryUserListCountByOrg(Passport passport, Long lOrgId, String strUserName, String strMobile,
			OrgUserMapperStatus status) throws NullParameterException, NotPermissionException, DbException {
		if (passport == null)
			throw new NullParameterException();

		boolean isAdmin = this.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR);
		boolean isMger = this.checkUserPermission(passport, UserPermission.ORG_MGR);
		Long orgid = isAdmin ? lOrgId : isMger ? passport.getOrgId() : null;

		if (orgid == null)
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", orgid.longValue());
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);
			prms.put("status", status != null ? status.getCode() : null);

			return session.getMapper(UserMapper.class).selectMapperCountByOrg(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 通过ID查询用户，此方法只允许MsgService类调用，其他类调用将抛出异常 ,如果是医生用户，则会返回医生对象
	 * 
	 * @throws BaseException
	 */
	public TBaseUser queryBaseUserById(Passport passport, long lUserId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			TBaseUser baseUser = mapper.queryBaseUserById(lUserId);
			if (baseUser == null)
				return null;
			if (baseUser.getType() == UserType.ORG_DOCTOR.getCode()) {
				TDoctorUser docUser = mapper.queryDoctorUserById(lUserId);
				baseUser.copyTo(docUser);
				return docUser;
			} else
				return baseUser;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询在线用户信息
	 * 
	 * @param passort
	 * @return
	 */
	public TBaseUser queryOnlineUserById(Passport passort) {
		return OnlineManager.instance.getOnlinUser(passort.getOrgId(), passort.getUserId());
	}

	/**
	 * 通过用户ID获取用户全部信息
	 * 
	 * @param lUserId
	 *            要查找的用户ID
	 * @return 查找到的用户，没有找到返回null
	 * @throws UserOperateException
	 * @throws DbException
	 * @throws NullParameterException
	 */
	public TBaseUser queryUserById(Passport passport, Long lUserId)
			throws UserOperateException, DbException, NullParameterException {
		if (passport == null)
			throw new NullParameterException();

		boolean isAdmin = this.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR);
		boolean isMger = this.checkUserPermission(passport, UserPermission.ORG_MGR);

		if (lUserId != null) {
			if (passport.getUserId() != lUserId)
				if (isAdmin == false && isMger == false)
					throw new UserOperateException("无权查看其它用户的个人信息");
		} else {
			lUserId = passport.getUserId();
		}

		// 如果查询的用户已经是登陆的用户，就不需要再查询数据库了，已经缓存了。

		// BufferUser bfUser=this.mapUser.get(lUserId);
		// TBaseUser user=bfUser==null?null:bfUser.getUser();
		//
		// if (user != null)
		// return user;

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(lUserId);

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			UserType ut = UserType.parseCode(tbu.getType());

			if (ut == null)
				throw new UserOperateException("未知的用户类型");

			if (UserType.ADMIN.equals(ut) || UserType.SUPER_ADMIN.equals(ut)) {
				TAdminUser tau = session.getMapper(UserMapper.class).queryAdminUserById(lUserId);
				if (tau != null)
					tau.getUserInfo(tbu);
				return tau;
			} else if (UserType.ORG_DOCTOR.equals(ut)) {
				TDoctorUser tdu = session.getMapper(UserMapper.class).queryDoctorUserById(lUserId);
				if (tdu != null)
					tdu.getUserInfo(tbu);
				return tdu;
			} else if (UserType.ORG_GENERAL.equals(ut)) {
				TGeneralUser tgu = session.getMapper(UserMapper.class).queryGeneralUserById(lUserId);
				if (tgu != null)
					tgu.getUserInfo(tbu);
				return tgu;
			}

			return tbu;
		} catch (UserOperateException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 审核用户
	 * 
	 * @param passport
	 * @param lUserId
	 * @param isPass
	 * @param strNote
	 * @param listUserPermission
	 * @throws DbException
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 */
	public void verifyUser(Passport passport, long lUserId, boolean isPass, String strNote,
			Set<UserPermission> listUserPermission)
			throws UserOperateException, DbException, NullParameterException, NotPermissionException {
		if (passport == null)
			throw new NullParameterException();
		if (!this.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", passport.getOrgId());
			prms.put("user_id", lUserId);
			TOrgUserMapping toum = session.getMapper(UserMapper.class).selectMapperByUserAndLock(prms);

			if (toum == null)
				throw new UserOperateException("未知的用户");

			if (!OrgUserMapperStatus.APPROVING.equals(OrgUserMapperStatus.parseCode(toum.getStatus())))
				throw new UserOperateException("无效的状态");

			prms.clear();
			prms.put("id", toum.getId());
			prms.put("status", isPass ? OrgUserMapperStatus.VALID.getCode() : OrgUserMapperStatus.REJECTED.getCode());
			prms.put("note", strNote);
			session.getMapper(UserMapper.class).approveUser(prms);
			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(lUserId);
			if (tbu == null)
				throw new UserOperateException("未知的用户");
			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");
			TOrganization org = OrgService.instance.searchOrgById(passport.getOrgId());
			if (org == null)
				throw new UserOperateException("未知的机构");
			if (isPass && listUserPermission != null && !listUserPermission.isEmpty()) {
				UserType ut = UserType.parseCode(tbu.getType());
				if (UserType.ORG_DOCTOR.equals(ut) || UserType.ORG_GENERAL.equals(ut)) {
//					Set<OrgPermission> ops = org.getPermissionList();
//					if (ops != null && !ops.isEmpty() && ops.size() == 1) {
//						if (!UserPermission.isOrgPermission(listUserPermission, ops.get(0)))
//							throw new UserOperateException("权限与机构类型不符");
//					}
//					if (!UserPermission.isUserPermission(listUserPermission, ut))
//						throw new UserOperateException("权限与用户类型不符");
					prms.clear();
					prms.put("id", toum.getId());
					prms.put("permission", PermissionsSerializer.toUserPermissionString(listUserPermission));
					session.getMapper(UserMapper.class).authorizeUser(prms);
				} else {
					throw new UserOperateException("权限与用户类型不符");
				}
			}
			OnlineManager.instance.updateOnlineUserPermis(passport.getOrgId(), lUserId, listUserPermission);
			SmsServer.instance.sendSMS(tbu.getMobile(), "尊敬的用户，您提交加入机构" + org.getOrg_name() + "的申请"
					+ (isPass ? "已经审核通过，请登录平台进行后续操作！" : "审核未被通过，您可以按要求修改资料后重新提交申请！") + "【影泰科技】");
			session.commit();
		} catch (UserOperateException e) {
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
	 * 为用户赋权
	 * 
	 * @param passport
	 * @param lUserid
	 *            要赋权的用户ID
	 * @param listUserPermission
	 * @throws UserOperateException
	 * @throws DbException
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 */
	public void conferPermission(Passport passport, long lUserid, Set<UserPermission> listUserPermission)
			throws UserOperateException, DbException, NullParameterException, NotPermissionException {
		if (passport == null)
			throw new NullParameterException();

		boolean isAdminMgrer = UserType.SUPER_ADMIN.equals(passport.getUserType()); // this.checkUserPermission(passport,
																					// UserPermission.SUPER_ADMIN);
		boolean isOrgMger = this.checkUserPermission(passport, UserPermission.ORG_MGR);
		boolean isAdminPerms = UserPermission.isAdminPermission(listUserPermission);
//		boolean isOrgPerms = UserPermission.isOrgPermission(listUserPermission, null);

		if ((isAdminMgrer && isAdminPerms) && isOrgMger)
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(lUserid);

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");

			UserType ut = UserType.parseCode(tbu.getType());

			if (ut == null)
				throw new UserOperateException("未知的用户类型");

			if (isAdminMgrer) {
				TAdminUser admin = session.getMapper(UserMapper.class).queryAdminUserByIdAndLock(lUserid);
				if (admin == null)
					throw new UserOperateException("未知的用户");

				prms.clear();
				prms.put("id", lUserid);
				prms.put("permission", PermissionsSerializer.toUserPermissionString(listUserPermission));
				session.getMapper(UserMapper.class).authorizeAdmin(prms);
			} else if (isOrgMger && (UserType.ORG_DOCTOR.equals(ut) || UserType.ORG_GENERAL.equals(ut))) {
				TOrganization org = OrgService.instance.searchOrgById(passport.getOrgId());

				if (org == null)
					throw new UserOperateException("未知的机构");

				if (org.getCreator_id() == lUserid && !listUserPermission.contains(UserPermission.ORG_MGR))
					listUserPermission.add(UserPermission.ORG_MGR);

//				List<OrgPermission> ops = org.getPermissionList();

//				if (ops != null && !ops.isEmpty() && ops.size() == 1) {
//					if (!UserPermission.isOrgPermission(listUserPermission, ops.get(0)))
//						throw new UserOperateException("权限与机构类型不符");
//				}
//
//				if (!UserPermission.isUserPermission(listUserPermission, ut))
//					throw new UserOperateException("权限与用户类型不符");

				prms.clear();
				prms.put("org_id", passport.getOrgId());
				prms.put("user_id", lUserid);
				TOrgUserMapping oum = session.getMapper(UserMapper.class).selectMapperByUserAndLock(prms);
				if (oum == null)
					throw new UserOperateException("未知的用户");

				if (!OrgUserMapperStatus.VALID.equals(OrgUserMapperStatus.parseCode(oum.getStatus())))
					throw new UserOperateException("无效的用户");

				prms.clear();
				prms.put("id", oum.getId());
				prms.put("permission", PermissionsSerializer.toUserPermissionString(listUserPermission));
				session.getMapper(UserMapper.class).authorizeUser(prms);
			} else {
				throw new UserOperateException("权限与用户类型不符");
			}

			session.commit();

			// 同步缓存
			OnlineManager.instance.updateOnlineUserPermis(passport.getOrgId(), lUserid, listUserPermission);
		} catch (UserOperateException e) {
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
	 * 修改管理员用户资料，只允许自身
	 * 
	 * @param passport
	 * @param adminUser
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws DbException
	 */
	public void modifyAdminUser(Passport passport, TAdminUser adminUser)
			throws NullParameterException, NotPermissionException, UserOperateException, DbException {
		if (passport == null || adminUser == null)
			throw new NullParameterException();

		if (passport.getUserId() != adminUser.getId())
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(passport.getUserId());

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");

			TAdminUser tau = session.getMapper(UserMapper.class).queryAdminUserById(passport.getUserId());

			if (tau == null)
				throw new UserOperateException("未知的用户");

			session.getMapper(UserMapper.class).modifyUserInfo(adminUser);
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", tau.getId());
			prms.put("mobile", adminUser.getMobile());
			prms.put("email", adminUser.getEmail());
			chechUserObject(adminUser);
			session.getMapper(UserMapper.class).modifyUserEmailOrMobile(prms);
			session.commit();

			// 如果修改的用户当前已经登陆，则把缓存替换掉
			OnlineManager.instance.updateOnlineUser(passport.getOrgId(), adminUser);
		} catch (UserOperateException e) {
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
	 * 修改普通机构用户资料，只允许自身修改
	 * 
	 * @param passport
	 * @param generalUser
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws DbException
	 */
	public void modifyGeneralUser(Passport passport, TGeneralUser generalUser)
			throws NullParameterException, NotPermissionException, UserOperateException, DbException {
		if (passport == null || generalUser == null)
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(passport.getUserId());

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");

			if (passport.getUserId() != tbu.getId())
				throw new NotPermissionException();

			if (UserType.ORG_DOCTOR.equals(tbu.getUserType()))
				throw new UserOperateException("已审核的医生不能变更为普通用户");

			int count = this.selectChangeCount(passport, passport.getUserId(), null, null, UserChangeStatus.APPROVING);

			if (count > 0)
				throw new UserOperateException("正在审核中，不能重复提交");
			chechUserObject(generalUser);
			TUserChange tuc = new TUserChange();
			tuc.setPhoto_file_id(generalUser.getPhoto_file_id());
			tuc.setUser_name(generalUser.getUser_name());
			tuc.setUser_type(generalUser.getType());
			tuc.setUser_gender(generalUser.getGender());
			tuc.setBirthday(generalUser.getBirthday());
			tuc.setIdentity_id(generalUser.getIdentity_id());
			tuc.setChange_status(UserChangeStatus.APPROVING.getCode());
			tuc.setChange_time(new Date());
			tuc.setUser_id(passport.getUserId());
			tuc.setGrade(generalUser.getGrade());

			session.getMapper(UserMapper.class).insertUserChange(tuc);
			session.commit();
		} catch (UserOperateException e) {
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
	 * 修改机构医生用户的资料，只允许自身修改
	 * 
	 * @param passport
	 * @param doctorUser
	 * @throws UserOperateException
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws DbException
	 */
	public void modifyDoctorUser(Passport passport, TDoctorUser doctorUser)
			throws UserOperateException, NullParameterException, NotPermissionException, DbException {
		if (passport == null || doctorUser == null)
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryUserByIdAndLock(passport.getUserId());

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");

			if (passport.getUserId() != tbu.getId())
				throw new NotPermissionException();

			int count = this.selectChangeCount(passport, passport.getUserId(), null, null, UserChangeStatus.APPROVING);

			if (count > 0)
				throw new UserOperateException("正在审核中，不能重复提交");
			chechUserObject(doctorUser);
			TUserChange tuc = new TUserChange();
			tuc.setPhoto_file_id(doctorUser.getPhoto_file_id());
			tuc.setUser_name(doctorUser.getUser_name());
			tuc.setUser_type(doctorUser.getType());
			tuc.setUser_gender(doctorUser.getGender());
			tuc.setBirthday(doctorUser.getBirthday());
			tuc.setIdentity_id(doctorUser.getIdentity_id());
			tuc.setChange_status(UserChangeStatus.APPROVING.getCode());
			tuc.setChange_time(new Date());
			tuc.setUser_id(passport.getUserId());
			tuc.setGrade(doctorUser.getGrade());
			tuc.setHospital(doctorUser.getHospital());
			tuc.setScan_file_ids(doctorUser.getScan_file_ids());
			tuc.setSection(doctorUser.getSection());
			tuc.setStartwork_time(doctorUser.getStartwork_time());
			tuc.setWork_years(doctorUser.getWork_years());
			tuc.setSign_file_id(doctorUser.getSign_file_id());
			tuc.setUnit_phone(doctorUser.getUnit_phone());
			tuc.setJob_note(doctorUser.getJob_note());
			tuc.setIdentify_file_id(doctorUser.getIdentify_file_id());
			tuc.setQualification_id(doctorUser.getQualification_id());
			tuc.setDevice_opreator_id(doctorUser.getDevice_opreator_id());

			session.getMapper(UserMapper.class).insertUserChange(tuc);
			session.commit();
		} catch (UserOperateException e) {
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
	 * 指定用户退出其所在的所有机构
	 * 
	 * @param passport
	 * @param lUserId
	 * @param session
	 */
	public void quitAllOrg(Passport passport, long lUserId, SqlSession session) {

	}

	/**
	 * 用户退出机构
	 * @throws Exception 
	 * 
	 * @throws NullParameterException
	 * @throws UserOperateException
	 * @throws NotPermissionException
	 * @throws DbException
	 */
	public void quitOrg(Passport passport, long lUserId, long lOrgId) throws Exception {
		if (passport == null)
			throw new NullParameterException();
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			TOrganization org = session.getMapper(OrgMapper.class).selectOrgById(lOrgId);
			if (org == null)
				throw new UserOperateException("未知的机构");
			if (org.getCreator_id() == lUserId)
				throw new UserOperateException("机构的创建者不可以退出机构");
			if (!this.checkUserBelongOrg(passport, lUserId, lOrgId))
				throw new UserOperateException("用户不属于此机构");
			if (!DiagnosisService2.instance.checkFinishHandledByUserIdAndOrgId(lUserId,lOrgId))
				throw new UserOperateException("还有未处理的移交，请完成工作后再退出！");
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.deleteMapper(new TOrgUserMapping(lUserId, lOrgId));
			downLineUser(lUserId, lOrgId);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TBaseUser> selectDoctorUserByOrg(Passport passport, long lOrgId) throws DbException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", lOrgId);
			prms.put("status", UserStatus.NORMAL.getCode());
			prms.put("type", UserType.ORG_DOCTOR.getCode());
			return session.getMapper(UserMapper.class).queryBaseUsersByOrg(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void modifyMobileOrEmailByUser(Passport passport, String strLoginName, ValidateCode vc)
			throws BaseException {
		if (passport == null || vc == null || strLoginName == null || strLoginName.isEmpty())
			throw new NullParameterException();

		if (this.isExistsLoginName(strLoginName))
			throw new UserOperateException("修改的手机号码或电子邮箱已存在");

		if (!strLoginName.equals(vc.getLoginName()))
			throw new UserOperateException("验证码错误");

		int mi = SysService.instance.takeCheckCodeValidMinutes();

		Calendar cl = Calendar.getInstance();
		cl.setTime(vc.getCreatDate());
		cl.add(Calendar.MINUTE, mi);

		if (cl.after(new Date()))
			throw new UserOperateException("验证码已过期，请重新获取");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			TBaseUser tbu = session.getMapper(UserMapper.class).queryUserByIdAndLock(passport.getUserId());

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", tbu.getId());
			prms.put("mobile", RulesValidator.instance.validateMobilePhone(strLoginName) ? strLoginName : null);
			prms.put("email", RulesValidator.instance.validateEmail(strLoginName) ? strLoginName : null);
			session.getMapper(UserMapper.class).modifyUserEmailOrMobile(prms);
			session.commit();
		} catch (UserOperateException e) {
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
	 * 根据我的身份查询所有我可以移交的医生，<br>
	 * 如果我是报告员我可以移交给所有有效的医生<br>
	 * 如果我是审核员，我只能移交给审核员
	 * 
	 * @throws BaseException
	 */
	public List<TDoctorUser> queryCanTranferDoctors(Passport passport) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			List<TDoctorUser> doctors;
			boolean isAudit = this.checkUserPermission(passport, UserPermission.ORG_AUDIT);
			boolean isReport = this.checkUserPermission(passport, UserPermission.ORG_REPORT);
			if (!isAudit && !isReport)
				throw new NotPermissionException();
			HashMap<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("org_id", passport.getOrgId());
			mapArg.put("self_id", passport.getUserId());
			mapArg.put("user_status", UserStatus.NORMAL.getCode());
			mapArg.put("org_user_status", OrgUserMapperStatus.VALID.getCode());
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapArg.put("permission", UserPermission.ORG_AUDIT.getCode() + "");
			doctors = mapper.searchDoctorsByOrgIdAndPermission(mapArg);
			if (isAudit)
				return doctors;
			mapArg.put("permission", UserPermission.ORG_REPORT.getCode() + "");
			List<TDoctorUser> doctorsTmp = mapper.searchDoctorsByOrgIdAndPermission(mapArg);
			if (doctors == null)
				return doctorsTmp;
			return (List<TDoctorUser>) CollectionTools.discardDuplicate(doctors, doctorsTmp);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询本机构下的所有医生
	 * 
	 * @throws BaseException
	 */
	public List<TDoctorUser> queryDoctorsInOrg(Passport passport, Long lOrgId, boolean excludeSelf, boolean bOnlyVerity)
			throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("org_id", lOrgId == null || lOrgId.equals(0) ? passport.getOrgId() : lOrgId);
			mapArg.put("self_id", excludeSelf ? passport.getUserId() : null);
			mapArg.put("user_status", UserStatus.NORMAL.getCode());
			mapArg.put("org_user_status", OrgUserMapperStatus.VALID.getCode());
			mapArg.put("permission", UserPermission.ORG_AUDIT.getCode() + "");
			List<TDoctorUser> listResult = mapper.searchDoctorsByOrgIdAndPermission(mapArg);
			if (bOnlyVerity)
				return listResult;
			mapArg.put("permission", UserPermission.ORG_REPORT.getCode() + "");
			List<TDoctorUser> listTmp = mapper.searchDoctorsByOrgIdAndPermission(mapArg);
			if (listResult == null || listResult.size() <= 0)
				return listTmp;
			if (listTmp != null && listTmp.size() > 0)
				return (List<TDoctorUser>) CollectionTools.discardDuplicate(listResult, listTmp);
			return listResult;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取管理员列表
	 * 
	 * @param passport
	 * @param strUserName
	 * @param strMobile
	 * @return
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws DbException
	 */
	public List<TAdminUser> selectAdminList(Passport passport, String strUserName, String strMobile, SplitPageUtil spu)
			throws NullParameterException, NotPermissionException, DbException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserType.SUPER_ADMIN.equals(passport.getUserType()))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			if (spu != null) {
				prms.put("begin_idx", spu.getCurrMinRowNum());
				prms.put("end_idx", spu.getCurrMaxRowNum());
			}
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);

			return session.getMapper(UserMapper.class).selectAdminList(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public int selectAdminCount(Passport passport, String strUserName, String strMobile)
			throws DbException, NullParameterException, NotPermissionException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserType.SUPER_ADMIN.equals(passport.getUserType()))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);

			return session.getMapper(UserMapper.class).selectAdminCount(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 删除用户
	 * 
	 * @throws BaseException
	 */
	public void deleteUser(Passport passport, long lUserId) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR))
			throw new NotPermissionException("你没有用户管理权限！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			TBaseUser baseUser = mapper.queryUserByIdAndLock(lUserId);
			if (baseUser == null)
				throw new UserOperateException("指定的用户不存在！");
			if (baseUser.getStatus() == UserStatus.DELETED.getCode()) {
				session.commit();
				return;
			}
			/** 删除用户与机构关系映射 */
			mapper.deleteUserOrgMappingByUserId(lUserId);
			baseUser.setStatus(UserStatus.DELETED.getCode());
			mapper.modifyBaseUserStatus(baseUser);
			downLineUser(lUserId, null);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 禁用或激活用户
	 * 
	 * @throws BaseException
	 */
	public void enableUser(Passport passport, long lUserId, boolean enable) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR))
			throw new NotPermissionException("你没有用户管理权限！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			TBaseUser user = mapper.queryBaseUserByIdForUpdate(lUserId);
			if (user == null)
				throw new UserOperateException("用户不存在！");
			if (enable) {
				if (user.getStatus() != UserStatus.DISABLE.getCode())
					throw new UserOperateException("用户不是禁用状态！");
				user.setStatus(UserStatus.NORMAL.getCode());
			} else {
				if (user.getStatus() != UserStatus.NORMAL.getCode())
					throw new UserOperateException("用户不是可用状态！");
				user.setStatus(UserStatus.DISABLE.getCode());
			}
			mapper.modifyBaseUserStatus(user);
			downLineUser(lUserId, null);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 管理员启用/禁用
	 * 
	 * @param passport
	 * @param lUserId
	 * @param isEnalbe
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws DbException
	 */
	public void enableAdmin(Passport passport, long lUserId, boolean isEnalbe)
			throws NullParameterException, NotPermissionException, UserOperateException, DbException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserType.SUPER_ADMIN.equals(passport.getUserType()))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = this.queryUserById(passport, lUserId);
			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserType.ADMIN.equals(tbu.getUserType()) && !UserType.SUPER_ADMIN.equals(tbu.getUserType()))
				throw new UserOperateException("无效的用户");

			tbu.setStatus(isEnalbe ? UserStatus.NORMAL.getCode() : UserStatus.DELETED.getCode());

			session.getMapper(UserMapper.class).modifyUserInfo(tbu);
			session.commit();
		} catch (UserOperateException e) {
			session.rollback();
			throw e;
		} catch (Exception e) {
			session.rollback();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取平台用户列表
	 * 
	 * @param passport
	 * @param strUserName
	 * @param strMobile
	 * @param spu
	 * @return
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws DbException
	 */
	public List<TBaseUser> selectUserListByAdmin(Passport passport, String strUserName, String strMobile,
			UserStatus status, SplitPageUtil spu) throws NullParameterException, NotPermissionException, DbException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserType.SUPER_ADMIN.equals(passport.getUserType())
				&& !this.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();

			prms.put("begin_idx", spu.getCurrMinRowNum());
			prms.put("end_idx", spu.getCurrMaxRowNum());
			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			prms.put("status", status != null ? status.getCode() : null);

			return session.getMapper(UserMapper.class).selectUserListByAdmin(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public int selectUserCountByAdmin(Passport passport, String strUserName, String strMobile, UserStatus status)
			throws NullParameterException, NotPermissionException, DbException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserType.SUPER_ADMIN.equals(passport.getUserType())
				&& !this.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();

			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			prms.put("status", status != null ? status.getCode() : null);

			return session.getMapper(UserMapper.class).selectUserCountByAdmin(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 管理员审核注册用户
	 * 
	 * @param passport
	 * @param lUserId
	 * @param isPass
	 * @param strNote
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws DbException
	 */
	public void approvedUserByAdmin(Passport passport, long lUserId, boolean isPass, String strNote)
			throws NullParameterException, NotPermissionException, UserOperateException, DbException {
		if (passport == null)
			throw new NullParameterException();
		if (!this.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR))
			throw new NotPermissionException();
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			TBaseUser user = this.queryUserById(passport, lUserId);
			if (user == null)
				throw new UserOperateException("未知的用户");
			if (!UserStatus.VERITIING.equals(UserStatus.parseCode(user.getStatus())))
				throw new UserOperateException("已审核的用户，无法重复审核");

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", user.getId());
			prms.put("status", isPass ? UserStatus.NORMAL.getCode() : UserStatus.VERITY_NOT_PASS.getCode());
			prms.put("note", strNote);
			session.getMapper(UserMapper.class).approveUserByAdmin(prms);

			prms.clear();
			prms.put("id", user.getId());
			prms.put("verify_user_id", passport.getUserId());
			prms.put("varify_time", new Timestamp(new Date().getTime()));
			if (user instanceof TDoctorUser) {
				session.getMapper(UserMapper.class).modifyDoctorVarify(prms);
			} else if (user instanceof TGeneralUser) {
				session.getMapper(UserMapper.class).modifyGeneralVarify(prms);
			}
			SmsServer.instance.sendSMS(user.getMobile(),
					"尊敬的用户，您的影泰平台账户审核" + (isPass ? "已经通过，请登录平台进行后续操作！" : "未被通过，您可以按要求修改资料后重新提交申请！") + "【影泰科技】");
			session.commit();
		} catch (UserOperateException e) {
			session.rollback();
			throw e;
		} catch (Exception e) {
			session.rollback();
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private void chechUserObject(TBaseUser user) throws UserOperateException {
		if (user == null)
			throw new UserOperateException("用户对象必须指定！");
		if (user.getUser_name() == null || user.getUser_name().trim().isEmpty())
			throw new UserOperateException("用户名必须填写！");
//		if (user.getGender() != Gender.FAMALE.getCode() && user.getGender() != Gender.MALE.getCode())
//			throw new UserOperateException("性别必选！");
		if (UserType.parseCode(user.getType()) == null)
			throw new UserOperateException("用户类型必选！");
		if (user instanceof TDoctorUser) {
			TDoctorUser userTmp = (TDoctorUser) user;
//			if (userTmp.getPhoto_file_id() <= 0)
//				throw new UserOperateException("医生用户必须上传头像！");
			if (userTmp.getGrade() == null || userTmp.getGrade().trim().isEmpty())
				throw new UserOperateException("医生职称必须填写！");
			userTmp.setGrade(userTmp.getGrade().trim());

			if (userTmp.getSection() == null || userTmp.getSection().trim().isEmpty())
				throw new UserOperateException("单位科室必须填写！");
			userTmp.setSection(userTmp.getSection().trim());
//			if (userTmp.getIdentity_id() == null || userTmp.getIdentity_id().trim().isEmpty())
//				throw new UserOperateException("医生用户身份证号必填！");
//			if (!StringTools.wasIdentityId(userTmp.getIdentity_id()))
//				throw new UserOperateException("身份证号码不符合规范，必须是15位数字或者18位数字，结尾可以是‘X’！");
			if (userTmp.getHospital() == null || userTmp.getHospital().trim().isEmpty())
				throw new UserOperateException("工作单位必填！");
			userTmp.setHospital(userTmp.getHospital().trim());
			if (userTmp.getUnit_phone() == null)
				userTmp.setUnit_phone(userTmp.getUnit_phone().trim());
			if (userTmp.getJob_note() == null)
				userTmp.setJob_note(userTmp.getJob_note().trim());
		} else if (user instanceof TAdminUser) {
			if (user.getIdentity_id() == null || user.getIdentity_id().trim().isEmpty())
				throw new UserOperateException("用户身份证号必填！");
		} else if (user instanceof TGeneralUser) {
		}
	}

	/**
	 * . 医生用户二次提交审核
	 * 
	 * @param passport
	 * @param doctorUser
	 * @throws BaseException
	 */
	public void reSubmitDoctorUser(Passport passport, TDoctorUser doctorUser) throws BaseException {
		if (passport == null || doctorUser == null)
			throw new NullParameterException();

		// if (passport.getUserId() != doctorUser.getId())
		// throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryUserByIdAndLock(passport.getUserId());

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (passport.getUserId() != tbu.getId())
				throw new NotPermissionException();

			TDoctorUser tau = session.getMapper(UserMapper.class).queryDoctorUserAndLock(passport.getUserId());

			if (tau == null) {
				session.getMapper(UserMapper.class).deleteGeneralById(tbu.getId());
				doctorUser.setId(tbu.getId());
				session.getMapper(UserMapper.class).registerDoctor(doctorUser);
			} else {
				tau.getUserInfo(tbu);
				session.getMapper(UserMapper.class).modifyDoctorInfo(doctorUser);
			}

			session.getMapper(UserMapper.class).modifyUserInfo(doctorUser);
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", tbu.getId());
			prms.put("status", UserStatus.VERITIING.getCode());
			prms.put("note", "");
			chechUserObject(doctorUser);
			session.getMapper(UserMapper.class).approveUserByAdmin(prms);
			session.commit();

			// 如果修改的用户当前已经登陆，则把缓存替换掉
			if (passport.getOrgId() != null)
				OnlineManager.instance.updateOnlineUser(passport.getOrgId(), doctorUser);
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 用户二次提交审核
	 * 
	 * @param passport
	 * @param generalUser
	 * @throws BaseException
	 */
	public void reSubmitGeneralUser(Passport passport, TGeneralUser generalUser) throws BaseException {
		if (passport == null || generalUser == null)
			throw new NullParameterException();

		// if (passport.getUserId() != generalUser.getId())
		// throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TBaseUser tbu = session.getMapper(UserMapper.class).queryBaseUserById(passport.getUserId());

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (passport.getUserId() != tbu.getId())
				throw new NotPermissionException();

			TGeneralUser tau = session.getMapper(UserMapper.class).queryGeneralUserAndLock(passport.getUserId());

			if (tau == null) {
				session.getMapper(UserMapper.class).deleteDoctorById(tbu.getId());
				generalUser.setId(tbu.getId());
				session.getMapper(UserMapper.class).registerGeneral(generalUser);
			} else {
				tau.getUserInfo(tbu);
				session.getMapper(UserMapper.class).modifyGeneralInfo(generalUser);
			}

			session.getMapper(UserMapper.class).modifyUserInfo(generalUser);
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", tbu.getId());
			prms.put("status", UserStatus.VERITIING.getCode());
			prms.put("note", "");
			chechUserObject(generalUser);
			session.getMapper(UserMapper.class).approveUserByAdmin(prms);
			session.commit();

			// 如果修改的用户当前已经登陆，则把缓存替换掉
			if (passport.getOrgId() != null)
				OnlineManager.instance.updateOnlineUser(passport.getOrgId(), generalUser);
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取当前机构的用户权限
	 * 
	 * @param passport
	 * @return
	 * @throws BaseException 
	 */
	public List<UserPermission> getUserPermissionByOrg(Passport passport)
			throws BaseException {
		if (passport == null)
			throw new NullParameterException();

		TOrganization org = OrgService.instance.searchOrgById(passport.getOrgId());

		if (org == null)
			throw new UserOperateException("未知的机构");

//		List<OrgPermission> ops = org.getPermissionList();
//		Set<UserPermission> ups = new HashSet<UserPermission>(4);
//		if (ops != null && !ops.isEmpty()) {
//			if (ops.size() == 1) {
//				if (OrgPermission.DIAGNOSISER.equals(ops.get(0))) {
//					ups.add(UserPermission.ORG_AUDIT);
//					ups.add(UserPermission.ORG_REPORT);
//					ups.add(UserPermission.ORG_MGR);
////				} else {
//					ups.add(UserPermission.ORG_MEDICAL_MGR);
//					ups.add(UserPermission.ORG_MGR);
//				}
//			} else {
//				ups = UserPermission.getAllUserPower();
//			}
//		}

		return UserPermission.getAllUserPower();
	}

	/**
	 * 管理员重置用户密码
	 * 
	 * @param passport
	 * @param lUserId
	 * @throws BaseException
	 */
	public void resetPwdByAdmin(Passport passport, long lUserId) throws BaseException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserType.SUPER_ADMIN.equals(passport.getUserType()))
			throw new NotPermissionException();

		SqlSession session = null;
		try {
			session = SessionFactory.getSession();

			TBaseUser tbu = session.getMapper(UserMapper.class).queryUserByIdAndLock(lUserId);

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");

			Map<String, Object> prms = new HashMap<String, Object>();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String strNewPwd = StringUtil
					.toHexString((md5.digest(SysService.instance.takeDefaultPassword().getBytes("utf-8"))));
			prms.put("id", lUserId);
			prms.put("new_pwd", strNewPwd);
			session.getMapper(UserMapper.class).resetUserPwd(prms);
			session.commit();

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

	/**
	 * 获取用户变更列表
	 * 
	 * @param passport
	 * @param lUserId
	 * @param strUserName
	 * @param strMobile
	 * @param status
	 * @param spu
	 * @return
	 * @throws DbException
	 */
	public List<TUserChange> selectChangeList(Passport passport, Long lUserId, String strUserName, String strMobile,
			UserChangeStatus status, SplitPageUtil spu) throws DbException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			if (spu != null) {
				prms.put("begin_idx", spu.getCurrMinRowNum());
				prms.put("end_idx", spu.getCurrMaxRowNum());
			}
			prms.put("user_id", lUserId);
			prms.put("status", status != null ? status.getCode() : null);
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);

			return session.getMapper(UserMapper.class).selectUserChangeList(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public int selectChangeCount(Passport passport, Long lUserId, String strUserName, String strMobile,
			UserChangeStatus status) throws DbException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("user_id", lUserId);
			prms.put("status", status != null ? status.getCode() : null);
			prms.put("mobile", strMobile == null || strMobile.isEmpty() ? null : strMobile);
			prms.put("user_name", strUserName == null || strUserName.isEmpty() ? null : strUserName);
			return session.getMapper(UserMapper.class).selectUserChangeCount(prms);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public TUserChange selectChangeByUser(Passport passport, long lUserId)
			throws NullParameterException, NotPermissionException, DbException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			return session.getMapper(UserMapper.class).selectUserChangeByUser(lUserId);
		} catch (Exception e) {
			throw new DbException();
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 审核用户变更
	 * 
	 * @param passport
	 * @param lChangeId
	 * @param isPass
	 * @param strNote
	 * @throws NullParameterException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws DbException
	 */
	public void confirmChange(Passport passport, long lChangeId, boolean isPass, String strNote)
			throws NullParameterException, NotPermissionException, UserOperateException, DbException {
		if (passport == null)
			throw new NullParameterException();

		if (!this.checkUserPermission(passport, UserPermission.ADMIN_USER_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TUserChange tuc = session.getMapper(UserMapper.class).selectUserChangeByIdAndLock(lChangeId);
			if (tuc == null)
				throw new UserOperateException("未知的变更记录");

			if (!UserChangeStatus.APPROVING.equals(tuc.getUserChangeStatus()))
				throw new UserOperateException("已经审核的变更记录，无法审核");

			TBaseUser tbu = session.getMapper(UserMapper.class).queryUserByIdAndLock(tuc.getUser_id());

			if (tbu == null)
				throw new UserOperateException("未知的用户");

			if (!UserStatus.NORMAL.equals(UserStatus.parseCode(tbu.getStatus())))
				throw new UserOperateException("无效的用户");

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", lChangeId);
			prms.put("verify_user_id", passport.getUserId());
			prms.put("verify_time", new Timestamp(new Date().getTime()));
			prms.put("note", strNote);
			prms.put("change_status", isPass ? UserChangeStatus.VALID.getCode() : UserChangeStatus.REJECTED.getCode());
			session.getMapper(UserMapper.class).approveUserChange(prms);

			if (isPass) {
				if (UserType.ORG_DOCTOR.equals(tbu.getUserType())) {
					TDoctorUser doctorUser = new TDoctorUser();
					doctorUser.setId(tbu.getId());
					doctorUser.setStatus(tbu.getStatus());
					doctorUser.setPhoto_file_id(tuc.getPhoto_file_id());
					doctorUser.setUser_name(tuc.getUser_name());
					doctorUser.setType(tuc.getUser_type());
					doctorUser.setGender(tuc.getUser_gender());
					doctorUser.setBirthday(tuc.getBirthday());
					doctorUser.setIdentity_id(tuc.getIdentity_id());
					doctorUser.setGrade(tuc.getGrade());
					doctorUser.setHospital(tuc.getHospital());
					doctorUser.setScan_file_ids(tuc.getScan_file_ids());
					doctorUser.setSection(tuc.getSection());
					doctorUser.setStartwork_time(tuc.getStartwork_time());
					doctorUser.setWork_years(tuc.getWork_years());
					doctorUser.setSign_file_id(tuc.getSign_file_id());
					doctorUser.setUnit_phone(tuc.getUnit_phone());
					doctorUser.setJob_note(tuc.getJob_note());
					doctorUser.setIdentify_file_id(tuc.getIdentify_file_id());
					doctorUser.setQualification_id(tuc.getQualification_id());

					if (tbu.getUserType().equals(tuc.getType())) {
						chechUserObject(doctorUser);
						session.getMapper(UserMapper.class).modifyDoctorInfo(doctorUser);
						session.getMapper(UserMapper.class).modifyUserInfo(doctorUser);
					} else {
						throw new UserOperateException("已审核的医生不能变更为普通用户");
					}
				} else if (UserType.ORG_GENERAL.equals(tbu.getUserType())) {
					TGeneralUser generalUser = new TGeneralUser();
					generalUser.setId(tbu.getId());
					generalUser.setStatus(tbu.getStatus());
					generalUser.setPhoto_file_id(tuc.getPhoto_file_id());
					generalUser.setUser_name(tuc.getUser_name());
					generalUser.setType(tuc.getUser_type());
					generalUser.setGender(tuc.getUser_gender());
					generalUser.setBirthday(tuc.getBirthday());
					generalUser.setIdentity_id(tuc.getIdentity_id());
					generalUser.setGrade(tuc.getGrade());

					if (tbu.getUserType().equals(tuc.getType())) {
						chechUserObject(generalUser);
						session.getMapper(UserMapper.class).modifyGeneralInfo(generalUser);
						session.getMapper(UserMapper.class).modifyUserInfo(generalUser);
					} else {
						TDoctorUser doctorUser = new TDoctorUser();
						doctorUser.setId(tbu.getId());
						doctorUser.setStatus(tbu.getStatus());
						doctorUser.setPhoto_file_id(tuc.getPhoto_file_id());
						doctorUser.setUser_name(tuc.getUser_name());
						doctorUser.setType(tuc.getUser_type());
						doctorUser.setGender(tuc.getUser_gender());
						doctorUser.setBirthday(tuc.getBirthday());
						doctorUser.setIdentity_id(tuc.getIdentity_id());
						doctorUser.setGrade(tuc.getGrade());
						doctorUser.setHospital(tuc.getHospital());
						doctorUser.setScan_file_ids(tuc.getScan_file_ids());
						doctorUser.setSection(tuc.getSection());
						doctorUser.setStartwork_time(tuc.getStartwork_time());
						doctorUser.setWork_years(tuc.getWork_years());
						doctorUser.setSign_file_id(tuc.getSign_file_id());
						doctorUser.setUnit_phone(tuc.getUnit_phone());
						doctorUser.setJob_note(tuc.getJob_note());
						doctorUser.setIdentify_file_id(tuc.getIdentify_file_id());
						doctorUser.setQualification_id(tuc.getQualification_id());
						chechUserObject(doctorUser);
						session.getMapper(UserMapper.class).deleteGeneralById(tbu.getId());
						session.getMapper(UserMapper.class).registerDoctor(doctorUser);
						session.getMapper(UserMapper.class).modifyUserInfo(doctorUser);
					}
				}
			}

			session.commit();

			OnlineManager.instance.removeOnlineUser(tbu.getId());

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

	/**
	 * 删除用户机构映射关系
	 * 
	 * @throws BaseException
	 */
	public void deleteMapper(Passport passport, TOrgUserMapping tum, SqlSession session) throws BaseException {
		if (!CommonTools.judgeCaller(OrgService.class))
			throw new UserOperateException("你无权调用此方法");
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.deleteMapper(tum);
			Passport passportt = mapPassport.get(tum.getUser_id());
			if (passportt != null && passportt.getOrgId() != null && passportt.getOrgId().equals(tum.getOrg_id()))
				downLineUser(tum.getUser_id(), tum.getOrg_id());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 通过用户ID查询本机构的用户机构映射
	 * 
	 * @throws BaseException
	 */
	public FOrgUserMapping queryUserOrgInfoByUserId(Passport passport, long lUserId) throws BaseException {
		if (!checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new UserOperateException("你不是机构管理员，不能执行本操作！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return session.getMapper(UserMapper.class)
					.selectFOrgUserMapperByUserIdAndOrgId(new TOrgUserMapping(lUserId, passport.getOrgId()));
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TBaseUser> getUserLiteByOrg(long lOrgId,UserStatus userStatus,OrgUserMapperStatus omStatus) throws BaseException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("org_id", lOrgId);
			prms.put("user_status", userStatus==null?null:userStatus.getCode());
			prms.put("mapping_status", omStatus==null?null:omStatus.getCode());
			return session.getMapper(UserMapper.class).selectUserLiteByOrg(prms);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	public TBaseUser getUserInfo4BBS(String strToken) throws BaseException {
		try {
			Passport pst = null;
			for (Passport passport : this.mapPassport.values()) {
				if (passport.getValid() && passport.getBBSToken().equals(strToken)) {
					pst = passport;
					break;
				}
			}

			if (pst != null) {
				return this.queryOnlineUserById(pst);
			}

		} catch (Exception e) {
			throw e;
		}

		return null;
	}
	/**用户获取个人配置
	 * @throws BaseException */
	public TUserConfig takePersonalConfig(long userId) throws BaseException {
		SqlSession session=null;
		try {
			session=SessionFactory.getSession();
			UserMapper mapper=session.getMapper(UserMapper.class);
			TUserConfig userConfig = mapper.selectUserConfigByUserId(userId);
			if (userConfig==null)
				userConfig=new TUserConfig();
			return userConfig;
		} catch (Exception e) {
			throw e;
		}finally {
			SessionFactory.closeSession(session);
		}
	}

	public TUserConfig savePersonalConfig(Passport passport, TUserConfig userConfig) throws BaseException {
		SqlSession session=null;
		try {
			session=SessionFactory.getSession();
			UserMapper mapper=session.getMapper(UserMapper.class);
			TUserConfig oldUserConfig = mapper.selectUserConfigByUserIdForUpdate(passport.getUserId());
			if (oldUserConfig==null){
				oldUserConfig=userConfig;
				oldUserConfig.setUser_id(passport.getUserId());
				mapper.insertUserConfig(oldUserConfig);
			}else {
				oldUserConfig.setSms_config(userConfig.getSms_config());
				mapper.updateUserConfig(oldUserConfig);
			}
			session.commit();
			return oldUserConfig;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		}finally {
			SessionFactory.closeSession(session);
		}
	}
}
