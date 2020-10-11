package com.vastsoft.yingtai.module.user.action;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vastsoft.util.StringUtil;
import com.vastsoft.util.TripleDES;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.core.Constants;
import com.vastsoft.yingtai.core.PermissionsSerializer;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.core.VCType;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.online.OnlineManager;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.constants.OrgUserMapperStatus;
import com.vastsoft.yingtai.module.user.constants.UserChangeStatus;
import com.vastsoft.yingtai.module.user.constants.UserStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.entity.FOrgUserMapping;
import com.vastsoft.yingtai.module.user.entity.TAdminUser;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;
import com.vastsoft.yingtai.module.user.entity.TDoctorUser;
import com.vastsoft.yingtai.module.user.entity.TGeneralUser;
import com.vastsoft.yingtai.module.user.entity.TUserChange;
import com.vastsoft.yingtai.module.user.entity.TUserConfig;
import com.vastsoft.yingtai.module.user.exception.UserOperateException;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.module.user.service.UserService.ValidateCode;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;

/**
 * @author jyb
 */
public class UserAction extends BaseYingTaiAction {
	private String strLoginName;
	private String strPwd;
	private String strValidateCode;
	private String strNewPwd;
	private long lUserId;
	private String strNote;
	private TUserConfig userConfig;
	private String userInfo;
	private long lOrgId;
	private Set<UserPermission> ups;
	private boolean isPass;
	private String strUserName;
	private String strMobile;
	private int iPageIdx;
	private int iPageSize;
	private UserStatus userStatus;
	private VCType vcType;
	private boolean enable;
	private String bbs_token;
	private OrgUserMapperStatus oumStatus;
	
	/**保存个人配置*/
	public String savePersonalConfig(){
		try {
			userConfig=UserService.instance.savePersonalConfig(takePassport(),userConfig);
			addElementToData("userConfig", userConfig);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	/**用户拉取个人配置*/
	public String takePersonalConfig(){
		try {
			TUserConfig userConfig=UserService.instance.takePersonalConfig(takePassport().getUserId());
			addElementToData("userConfig", userConfig);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
//	@ActionDesc("获取当前机构的一个用户和权限，以及他可以拥有的所有权限")
	public String queryUserInfoByUserId() {
		try {
			Passport passport = takePassport();
			FOrgUserMapping userOrgInfo = UserService.instance.queryUserOrgInfoByUserId(passport, lUserId);
			addElementToData("userOrgInfo", userOrgInfo);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("机构管理员踢出用户")
	public String kickOutUser() {
		try {
			Passport passport = takePassport();
			UserService.instance.quitOrg(passport, lUserId, passport.getOrgId());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("用户主动退出机构")
	public String quitOrg() {
		try {
			Passport passport = takePassport();
			UserService.instance.quitOrg(passport, passport.getUserId(), lOrgId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("删除一个用户")
	public String deleteUser() {
		try {
			UserService.instance.deleteUser(takePassport(), lUserId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("禁用或激活用户")
	public String enableUser() {
		try {
			UserService.instance.enableUser(takePassport(), lUserId, enable);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 用户注册
	 * 
	 * @param 必需
	 *            loginName,pwd
	 */
	@ActionDesc(value = "用户注册")
	public String register() {
		try {
			ValidateCode vc = (ValidateCode) this.takeBySession("vc");

			if (vc == null || !this.strLoginName.equals(vc.getLoginName())) {
				this.setCode(100);
				this.setName("验证错误，请重新获取");
				return SUCCESS;
			}

			String key = "" + vc.getCode() + vc.getCode();

			String pwd = TripleDES.decryptFromBase64(key.toLowerCase(), this.strPwd);
			TBaseUser user = UserService.instance.regsiterUser(this.strLoginName, pwd);
			if (user != null) {
				Passport passport = UserService.instance.loginByPwd(user.getMobile(), pwd);
				this.addToSession(PASSPORT, passport);
			}

		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 用户登出 */
	@ActionDesc(value = "用户退出系统")
	public String logout() {
		try {
			Passport passport = this.takePassport();
			if (passport != null) {
				UserService.instance.loginOut(passport);
				this.removeFromSession(PASSPORT);
			}
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 用户登陆
	 * 
	 * @param 必需：loginName,
	 *            pwd
	 */
	@ActionDesc(value = "用户登陆")
	public String loginByPwd() {
		try {
			String vc = (String) this.takeBySession("login_vc");
			if (StringTools.isEmpty(vc)) {
				this.setCode(100);
				this.setName("请刷新验证码");
				return SUCCESS;
			}
			this.removeFromSession("login_vc");
			String key = vc + vc;
			if (StringTools.isEmpty(strPwd))
				throw new UserOperateException("登陆失败，密码错误！");
			String pwd = null;
			try {
				pwd = TripleDES.decryptFromBase64(key.toLowerCase(), this.strPwd);
			} catch (Exception e) {
				throw new UserOperateException("登陆失败，验证码错误！");
			}
			Passport passport = UserService.instance.loginByPwd(this.strLoginName, pwd);

			TBaseUser bu = UserService.instance.queryUserById(passport, null);

			if (UserType.ADMIN.equals(bu.getUserType()) || UserType.SUPER_ADMIN.equals(bu.getUserType())) {
				this.addElementToData("is_admin", true);
			} else {
				this.addElementToData("is_guest", UserType.GUEST.equals(UserType.parseCode(bu.getType())));
				this.addElementToData("is_valid", UserStatus.NORMAL.equals(UserStatus.parseCode(bu.getStatus())));

				List<TOrganization> orgList = OrgService.instance.searchOrgListByUser(passport, null);
				if (orgList != null && !orgList.isEmpty()) {
					if (orgList.size() > 1) {
						this.addElementToData("mulit_org", true);
					} else {
						if (UserService.instance.checkUserBelongOrg(passport, orgList.get(0).getId()))
							UserService.instance.entryOrg(passport, orgList.get(0).getId());
						else
							this.addElementToData("no_org", true);
					}
				} else {
					this.addElementToData("no_org", true);
				}
			}
			this.addToSession(PASSPORT, passport);
		}catch (Exception e) {
			catchException(e);
		} 
		return SUCCESS;
	}

	/**
	 * 获取验证码
	 * 
	 * @param 必需:loginName,vcType
	 * @return
	 */
	@ActionDesc(value = "获取验证码")
	public String reqValidateCode() {
		try {
			ValidateCode vc = UserService.instance.generateCode(this.strLoginName, this.vcType);
			this.addToSession("vc", vc);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 
	 * @param 必需:loginName
	 * @return
	 */
//	@ActionDesc(value = "验证是否存在")
	public String isExistsLogName() {
		try {
			boolean isExists = UserService.instance.isExistsLoginName(this.strLoginName);
			this.addElementToData("is_exists", isExists);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	@ActionDesc(value = "获取当前用户信息")
	public String queryUserByPassport() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;
		try {
			boolean isAdmin = UserType.ADMIN.equals(passport.getUserType())
					|| UserType.SUPER_ADMIN.equals(passport.getUserType());
			TBaseUser user = UserService.instance.queryUserById(passport, null);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", user.getId());
			userMap.put("user_name", user.getUser_name());
			userMap.put("mobile",isAdmin ? user.getMobile() : new StringBuffer(user.getMobile()).replace(3, 8, "*****").toString());

			String email = user.getEmail();
			userMap.put("email", isAdmin ? user.getEmail()
					: email == null ? "" : new StringBuffer(email).replace(3, email.indexOf("@"), "*****").toString());
			userMap.put("type", UserType.GUEST.equals(user.getUserType()) ? null : UserType.parseCode(user.getType()));
			// userMap.put("status", UserStatus.parseCode(user.getStatus()));
			// userMap.put("gender", UserGender.parseCode(user.getGender()));
			// userMap.put("type",user.getType());
			userMap.put("status", user.getStatus());
			userMap.put("gender", user.getGender());
			userMap.put("identity_id", user.getIdentity_id());
			userMap.put("birthday", user.getBirthday());
			userMap.put("photo_file_id", user.getPhoto_file_id());
			userMap.put("grade", user.getGrade());

			// TFileMgr tfm=FileService.instance.queryFileById(passport,
			// user.getPhoto_file_id());
			// userMap.put("photo_url", tfm==null?"":tfm.getPath());
			// userMap.put("", user.getPhoto_file_id());

			if (UserType.ORG_DOCTOR.equals(user.getUserType())) {
				TDoctorUser docU = (TDoctorUser) user;
				userMap.put("startwork_time", docU.getStartwork_time());
				userMap.put("work_years", docU.getWork_years());
				userMap.put("hospital", docU.getHospital());
				userMap.put("section", docU.getSection());
				userMap.put("note", docU.getNote());
				userMap.put("scan_file_ids", docU.getScan_file_ids());
				userMap.put("sign_file_id", docU.getSign_file_id());
				userMap.put("unit_phone", docU.getUnit_phone());
				userMap.put("job_note", docU.getJob_note());
				userMap.put("identify_file_id", docU.getIdentify_file_id());
				userMap.put("qualification_id", docU.getQualification_id());
				userMap.put("device_opreator_id", docU.getDevice_opreator_id());
			} else if (UserType.ORG_GENERAL.equals(user.getUserType())) {
				TGeneralUser genU = (TGeneralUser) user;
				userMap.put("note", genU.getNote());
				userMap.put("section", genU.getSection());
			} else if (UserType.ADMIN.equals(user.getUserType()) || UserType.SUPER_ADMIN.equals(user.getUserType())) {
				TAdminUser admU = (TAdminUser) user;
				userMap.put("permission", admU.getPermissionList());
			}
			this.addElementToData("is_approving", UserStatus.VERITIING.equals(UserStatus.parseCode(user.getStatus())));
			this.addElementToData("is_guest", UserType.GUEST.equals(UserType.parseCode(user.getType())));
			this.addElementToData("is_not_pass",
					UserStatus.VERITY_NOT_PASS.equals(UserStatus.parseCode(user.getStatus())));

			this.addElementToData("user", userMap);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 根据类型申请用户
	 * 
	 * @param 必需：userInfo(json)
	 * @return
	 */
	@ActionDesc(value = "保存用户信息")
	public String saveUser() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			JSONObject json = new JSONObject(this.userInfo);
			JSONObject type = json.getJSONObject("type");
			UserType ut = UserType.parseCode(type.optInt("code"));
			boolean isNew = true, isReSubmit = false;

			TBaseUser baseU = UserService.instance.queryUserById(passport, null);

			if (UserType.ORG_DOCTOR.equals(ut)) {
				TDoctorUser docUser = null;
				isReSubmit = !UserStatus.NORMAL.equals(UserStatus.parseCode(baseU.getStatus()));
				if (baseU instanceof TDoctorUser) {
					docUser = (TDoctorUser) baseU;
					isNew = false;
				} else {
					if (baseU != null && UserType.ORG_GENERAL.equals(baseU.getUserType()))
						isNew = false;
					else
						isNew = true;

					docUser = new TDoctorUser();
					docUser.setType(ut.getCode());
				}
				docUser.setBirthday(string2date(json.optString("birthday", "")));
				docUser.setGender(json.optInt("gender"));
				docUser.setGrade(json.optString("grade"));
				docUser.setHospital(json.optString("hospital"));
				docUser.setIdentity_id(json.optString("identity_id"));
				docUser.setPhoto_file_id(json.optInt("photo_file_id"));
				docUser.setScan_file_ids(json.optString("scan_file_ids"));
				docUser.setSection(json.optString("section"));
				docUser.setStartwork_time(string2date(json.optString("startwork_time")));
				docUser.setUser_name(json.optString("user_name"));
				docUser.setWork_years(json.optInt("work_years"));
				docUser.setSign_file_id(json.optLong("sign_file_id"));
				docUser.setUnit_phone(json.optString("unit_phone"));
				docUser.setJob_note(json.optString("job_note"));
				docUser.setIdentify_file_id(json.optLong("identify_file_id"));
				docUser.setQualification_id(json.optLong("qualification_id"));
				docUser.setDevice_opreator_id(json.optLong("device_opreator_id"));

				if (isReSubmit) {
					UserService.instance.reSubmitDoctorUser(passport, docUser);
				} else if (isNew) {
					UserService.instance.addDoctorUser(passport, docUser);
				} else {
					UserService.instance.modifyDoctorUser(passport, docUser);
				}
			} else if (UserType.ORG_GENERAL.equals(ut)) {
				TGeneralUser genUser = null;
				isReSubmit = !UserStatus.NORMAL.equals(UserStatus.parseCode(baseU.getStatus()));
				if (baseU instanceof TGeneralUser) {
					genUser = (TGeneralUser) baseU;
					isNew = false;
				} else {
					if (baseU != null && UserType.ORG_DOCTOR.equals(baseU.getUserType()))
						isNew = false;
					else
						isNew = true;

					genUser = new TGeneralUser();
					genUser.setType(ut.getCode());
				}

				genUser.setBirthday(string2date(json.optString("birthday", "")));
				genUser.setGender(json.optInt("gender"));
				genUser.setIdentity_id(json.optString("identity_id"));
				genUser.setPhoto_file_id(json.optInt("photo_file_id"));

				genUser.setSection(json.optString("section"));
				genUser.setUser_name(json.optString("user_name"));
				genUser.setGrade(json.optString("grade"));

				if (isReSubmit) {
					UserService.instance.reSubmitGeneralUser(passport, genUser);
				} else if (isNew) {
					UserService.instance.addGeneralUser(passport, genUser);
				} else {
					UserService.instance.modifyGeneralUser(passport, genUser);
				}
			}else{
				this.setCode(500);
				this.setName("系统无法识别的用户类型");
			}

		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 修改手机、邮箱
	 * 
	 * @param 必需:loginName,code
	 * @return
	 */
	@ActionDesc(value = "修改手机、邮箱")
	public String modifyMobileOrEmail() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		ValidateCode vc = (ValidateCode) this.takeBySession("vc");

		if (vc == null) {
			this.setCode(300);
			this.setName("验证码错误");
			return SUCCESS;
		}

		this.removeFromSession("vc");

		if (!vc.getCode().equals(this.strValidateCode)) {
			this.setCode(300);
			this.setName("验证码错误");
			return SUCCESS;
		}

		try {
			UserService.instance.modifyMobileOrEmailByUser(passport, this.strLoginName, vc);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 修改登录密码
	 * 
	 * @param 必需
	 *            pwd,newPwd
	 * @return
	 */
	@ActionDesc(value = "修改登录密码")
	public String modifyLoginPwd() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			String key = "" + this.strPwd + this.strPwd;
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String old_pwd = StringUtil.toHexString((md5.digest(this.strPwd.getBytes("utf-8"))));
			String new_pwd = TripleDES.decryptFromBase64(key.toLowerCase(), this.strNewPwd);
			UserService.instance.modifyLoginPwd(passport, old_pwd, new_pwd);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 请求加入机构
	 * 
	 * @param 必需
	 *            :orgId
	 * @return
	 */
	@ActionDesc(value = "请求加入机构")
	public String requestJoinOrg() {
		try {
			UserService.instance.requestJoinOrg(takePassport(), this.lOrgId);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 登录机构
	 * 
	 * @param 必需：orgId
	 * @return
	 */
	@ActionDesc(value = "登录机构")
	public String entryOrg() {
		try {
			Passport passport =takePassport();
			UserService.instance.entryOrg(passport, this.lOrgId);
			this.addElementToData("user_name", passport.getUserName());
			this.addElementToData("org_name", passport.getOrgName());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	/**
	 * 根据ID查询用户
	 * 
	 * @param 必需：userId
	 * @return
	 */
	@ActionDesc(value = "登录机构")
	public String queryUserById() {
		try {
			Passport passport = this.isOnline();
			TBaseUser user = UserService.instance.queryUserById(passport, this.lUserId);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", user.getId());
			userMap.put("user_name", user.getUser_name());
			userMap.put("mobile", user.getMobile());
			userMap.put("email", user.getEmail() == null ? "" : user.getEmail());
			userMap.put("type", UserType.parseCode(user.getType()));
			userMap.put("status", UserStatus.parseCode(user.getStatus()));
			userMap.put("gender", Gender.parseCode(user.getGender()));
			userMap.put("birthday", user.getBirthday());
			userMap.put("identity_id", user.getIdentity_id());
			userMap.put("permission", UserService.instance.getAllPermissionByUser(this.lUserId, passport.getOrgId()));
			userMap.put("note", user.getNote());
			userMap.put("photo_file_id", user.getPhoto_file_id());
			userMap.put("grade", user.getGrade());

			if (UserType.ORG_DOCTOR.equals(user.getUserType())) {
				TDoctorUser docU = (TDoctorUser) user;
				userMap.put("startwork_time", docU.getStartwork_time());
				userMap.put("work_years", docU.getWork_years());
				userMap.put("hospital", docU.getHospital());
				userMap.put("section", docU.getSection());
				userMap.put("scan_file_ids", docU.getScan_file_ids());
				userMap.put("sign_file_id", docU.getSign_file_id());
				userMap.put("unit_phone", docU.getUnit_phone());
				userMap.put("job_note", docU.getJob_note());
				userMap.put("identify_file_id", docU.getIdentify_file_id());
				userMap.put("qualification_id", docU.getQualification_id());
				userMap.put("device_opreator_id", docU.getDevice_opreator_id());
			} else if (UserType.ORG_GENERAL.equals(user.getUserType())) {
				TGeneralUser genU = (TGeneralUser) user;
				userMap.put("section", genU.getSection());
			}

			this.addElementToData("user", userMap);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/** 用户审核用户 */
	@ActionDesc("用户审核用户")
	public String verityUser() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			UserService.instance.verifyUser(passport, this.lUserId, this.isPass, this.strNote,this.ups);
//			if (isPass) {
//				UserService.instance.conferPermission(passport, this.lUserId, this.ups);
//			}

		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 为用户赋权
	 * 
	 * @param 必需:userId,ups([code1,code2])
	 */
	@ActionDesc(value = "用户授权")
	public String conferPowerToUser() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			UserService.instance.conferPermission(passport, this.lUserId, this.ups);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

//	@ActionDesc("获取当前机构和当前用户资料")
	public String currentUser() {
		Passport passport = this.takePassport();
		this.addElementToData("curr_user", passport);
		TBaseUser bu = UserService.instance.queryOnlineUserById(passport);
		this.addElementToData("logo_id", bu.getPhoto_file_id());
		if (passport.getUserType().equals(UserType.ORG_DOCTOR) || passport.getUserType().equals(UserType.ORG_GENERAL)) {
			Set<UserPermission> listUp = OnlineManager.instance.getPermisByUser(passport.getOrgId(),
					passport.getUserId());
			if (listUp == null)
				listUp = new HashSet<UserPermission>();
			this.addElementToData("isAuditer", listUp.contains(UserPermission.ORG_AUDIT));
			this.addElementToData("isMedicalHisMgr", listUp.contains(UserPermission.ORG_MEDICAL_MGR));
			this.addElementToData("isOrgMgr", listUp.contains(UserPermission.ORG_MGR));
			this.addElementToData("isReportor", listUp.contains(UserPermission.ORG_REPORT));
		}
		TOrganization currOrg = OnlineManager.instance.getOnlineOrgById(passport.getOrgId());
		this.addElementToData("currOrg", currOrg);
		return SUCCESS;
	}

	@ActionDesc(value = "管理员信息保存")
	public String saveAdminUser() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			JSONObject obj = new JSONObject(this.userInfo);
			if (obj != null) {
				JSONObject type = obj.optJSONObject("type");
				UserType ut = type == null ? UserType.ADMIN : UserType.parseCode(type.getInt("code"));
				long id = obj.optLong("id", 0);

				if (UserType.SUPER_ADMIN.equals(ut) && id != 0) {
					this.setCode(100);
					this.setName("超级管理员无法修改信息");
					return SUCCESS;
				} else if (UserType.ADMIN.equals(ut) && id == 0) {
					if (!"".equals(obj.optString("mobile"))
							&& UserService.instance.isExistsLoginName(obj.optString("mobile"))) {
						this.setCode(100);
						this.setName("电话号码已被注册！");
						return SUCCESS;
					}

					if (!"".equals(obj.optString("email"))
							&& UserService.instance.isExistsLoginName(obj.optString("email"))) {
						this.setCode(100);
						this.setName("电子邮箱已被注册！");
						return SUCCESS;
					}

					TAdminUser adminUser = new TAdminUser();
					adminUser.setUser_name(obj.optString("user_name"));
					adminUser.setMobile(obj.optString("mobile"));
					adminUser.setEmail(obj.optString("email"));
					adminUser.setGender(obj.optInt("gender"));
					adminUser.setIdentity_id(obj.optString("identity_id"));
					adminUser.setBirthday(string2date(obj.optString("birthday", "")));
					adminUser.setPhoto_file_id(obj.optInt("photo_file_id"));
					adminUser.setPermission(PermissionsSerializer.toUserPermissionString(this.ups));

					TAdminUser ntau = UserService.instance.addAdminUser(passport, adminUser, Constants.DEFAULT_PWD);
					this.addElementToData("user_id", ntau.getId());
				} else if (UserType.ADMIN.equals(ut) && id != 0) {
					TBaseUser baseU = UserService.instance.queryUserById(passport, null);

					if (!baseU.getMobile().equals(obj.optString("mobile"))
							&& UserService.instance.isExistsLoginName(obj.optString("mobile"))) {
						this.setCode(100);
						this.setName("电话号码已被注册！");
						return SUCCESS;
					}

					if (!baseU.getEmail().equals(obj.optString("email"))
							&& UserService.instance.isExistsLoginName(obj.optString("email"))) {
						this.setCode(100);
						this.setName("电子邮箱已被注册！");
						return SUCCESS;
					}
					TAdminUser adminUser = (TAdminUser) baseU;
					adminUser.setUser_name(obj.optString("user_name"));
					adminUser.setMobile(obj.optString("mobile"));
					adminUser.setEmail(obj.optString("email"));
					adminUser.setGender(obj.optInt("gender"));
					adminUser.setIdentity_id(obj.optString("identity_id"));
					adminUser.setBirthday(string2date(obj.optString("birthday", "")));
					adminUser.setPhoto_file_id(obj.optInt("photo_file_id"));
					UserService.instance.modifyAdminUser(passport, adminUser);
				}
			}
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

//	@ActionDesc(value = "获取管理员列表")
	public String queryAdminUserList() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		SplitPageUtil spu = null;
		if (this.iPageSize == 0) {
			spu = new SplitPageUtil(this.iPageIdx, this.iPageSize);
		}
		try {
			boolean isSuper = UserType.SUPER_ADMIN.equals(passport.getUserType());
			int total = UserService.instance.selectAdminCount(passport, this.strUserName, this.strMobile);
			List<TAdminUser> adminList = UserService.instance.selectAdminList(passport, this.strUserName,
					this.strMobile, spu);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (TAdminUser a : adminList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user_id", a.getId());
				map.put("user_name", a.getUser_name());
				map.put("mobile", a.getMobile());
				map.put("create_time", a.getCreate_time());
				map.put("userType", a.getUserType());
				map.put("status", UserStatus.parseCode(a.getStatus()));
				String psn = "";
				if (!a.getPermissionList().isEmpty()) {
					for (UserPermission up : a.getPermissionList()) {
						psn += "," + up.getName();
					}
					psn = psn.substring(1);
				}
				map.put("permission", psn);

				map.put("can_auth", isSuper && UserStatus.NORMAL.equals(UserStatus.parseCode(a.getStatus())));
				map.put("can_disable", passport.getUserId() != a.getId() && isSuper
						&& UserStatus.NORMAL.equals(UserStatus.parseCode(a.getStatus())));
				map.put("can_enable", passport.getUserId() != a.getId() && isSuper
						&& UserStatus.DELETED.equals(UserStatus.parseCode(a.getStatus())));

				list.add(map);
			}
			this.addElementToData("total", total);
			this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	@ActionDesc(value = "管理员启用/禁用")
	public String enableAdmin() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			UserService.instance.enableAdmin(passport, this.lUserId, this.isPass);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * @param 必需：pageIdx,pageSize;可选:userName,mobile,userStatus
	 * @return
	 */
//	@ActionDesc(value = "管理员查询平台用户列表")
	public String queryUserListByAdmin() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			int total = UserService.instance.selectUserCountByAdmin(passport, this.strUserName, this.strMobile,
					this.userStatus);
			SplitPageUtil spu = new SplitPageUtil(this.iPageIdx, this.iPageSize, total);
			List<TBaseUser> users = UserService.instance.selectUserListByAdmin(passport, this.strUserName,
					this.strMobile, this.userStatus, spu);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(10);

			for (TBaseUser u : users) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user_id", u.getId());
				map.put("user_name", u.getUser_name());
				map.put("mobile", u.getMobile());
				map.put("email", u.getEmail());
				map.put("user_type", u.getUserType());
				map.put("gender", Gender.parseCode(u.getGender()));
				map.put("create_time", u.getCreate_time());
				UserStatus ust = UserStatus.parseCode(u.getStatus());
				map.put("status", ust);

				map.put("can_approve", UserStatus.VERITIING.equals(ust) && !UserType.GUEST.equals(u.getUserType()));

				list.add(map);
			}

			this.addElementToData("total", total);
			this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * @param 必需:userId,isPass;可选：strNote
	 * @return
	 */
	@ActionDesc(value = "管理员审核用户")
	public String approvedUserByAdmin() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			UserService.instance.approvedUserByAdmin(passport, this.lUserId, this.isPass, this.strNote);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

//	@ActionDesc(value = "根据机构类型获取用户权限")
	public String queryUserPersission() {
		try {
			Passport passport = this.isOnline();
			List<UserPermission> list = UserService.instance.getUserPermissionByOrg(passport);
			this.addElementToData("user_perms", list);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * @param loginName,newPwd,validateCode
	 * @return
	 */
	@ActionDesc(value = "找回密码")
	public String resetPwd() {
		try {
			if (!UserService.instance.isExistsLoginName(this.strLoginName)) {
				this.setCode(100);
				this.setName("帐号不存在");
				return SUCCESS;
			}

			ValidateCode vc = (ValidateCode) this.takeBySession("vc");

			if (vc == null || !this.strLoginName.equals(vc.getLoginName())) {
				this.setCode(100);
				this.setName("验证错误，请重新获取");
				return SUCCESS;
			}

			this.removeFromSession("vc");

			String key = "" + vc.getCode() + vc.getCode();
			String pwd = TripleDES.decryptFromBase64(key.toLowerCase(), this.strNewPwd);
			UserService.instance.resetPwd(this.strLoginName, pwd, vc);

		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("管理员重设密码")
	public String resetPwdByAdmin() {
		try {
			Passport passport = this.isOnline();
			UserService.instance.resetPwdByAdmin(passport, this.lUserId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	@ActionDesc("获取用户数量")
	public String approvingUserCount() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		int count = 0, change_user_count = 0;

		try {
			count = UserService.instance.selectUserCountByAdmin(passport, null, null, UserStatus.VERITIING);
			change_user_count = UserService.instance.selectChangeCount(passport, null, null, null,
					UserChangeStatus.APPROVING);
		} catch (NullParameterException | NotPermissionException | DbException e) {
			count = 0;
			change_user_count = 0;
		}

		this.addElementToData("approving_user_count", count);
		this.addElementToData("change_user_count", change_user_count);
		return SUCCESS;
	}

//	@ActionDesc("获取加入机构待审核列表")
	public String joinWatingUserCount() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		int count = 0;

		try {
			count = UserService.instance.queryUserListCountByOrg(passport, null, null, null,
					OrgUserMapperStatus.APPROVING);
		} catch (NullParameterException | NotPermissionException | DbException e) {
			count = 0;
		}

		this.addElementToData("joining_user_count", count);
		return SUCCESS;
	}

//	@ActionDesc("获取用户变更列表")
	public String queryUserChangeList() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			int total = UserService.instance.selectChangeCount(passport, null, this.strUserName, this.strMobile, null);
			SplitPageUtil spu = new SplitPageUtil(this.iPageIdx, this.iPageSize, total);
			List<TUserChange> list = UserService.instance.selectChangeList(passport, null, this.strUserName,
					this.strMobile, null, spu);

			this.addElementToData("total", total);
			this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

//	@ActionDesc(value = "管理员获取变更信息")
	public String queryUserChangeByUser() {
		try {
			Passport passport = this.isOnline();
			TUserChange tuc = UserService.instance.selectChangeByUser(passport, this.lUserId);
			this.addElementToData("user", tuc);

		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("确认用户变更")
	public String confirmChange() {
		try {
			Passport passport = this.isOnline();
			UserService.instance.confirmChange(passport, this.lUserId, this.isPass, this.strNote);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}
	
	
	public String queryUserLiteByOrg(){
		try {
			Passport passport = this.isOnline();
			List<TBaseUser> list=UserService.instance.getUserLiteByOrg(this.lOrgId,this.userStatus,this.oumStatus);
			addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}
		
		return SUCCESS;
	}
	
	//用户登录论坛
	public String generateBBSToken(){
		Passport passport=this.isOnline();
		if (passport == null)
			return SUCCESS;
		
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			List<TDicValue> vs=SysService.instance.queryDicValueListByType(DictionaryType.BBS_PATH);
			if(vs!=null&&!vs.isEmpty()){
//			response.sendRedirect("http://192.168.2.68:8080/YingTaiBBS/forums/list.page?bbs_token="+passport.getBBSToken());
				TDicValue tdv=vs.get(0);
				if(tdv.getValue()!=null&&!tdv.getValue().isEmpty())
					response.sendRedirect(tdv.getValue()+"?bbs_token="+passport.getBBSToken());
			}
		} catch (Exception e) {
			catchException(e);
		}
		
		return SUCCESS;
	}
	
	//论坛验证用户
	public String checkBbsToken(){
		try {
			TBaseUser user=UserService.instance.getUserInfo4BBS(this.bbs_token);
			
			if(user!=null){
				if(UserType.SUPER_ADMIN.equals(user.getUserType())){
					addElementToData("user_name", "admin");
					addElementToData("real_name", "admin");
				}else{
					addElementToData("user_name", user.getMobile());
					addElementToData("real_name", user.getUser_name());
				}
			}
			
		} catch (Exception e) {
			catchException(e);
		}
		
		return SUCCESS;
	}

	/**
	 * 修改用户资料
	 */
	@ActionDesc("修改用户")
	public String modifyUser() {

		return SUCCESS;
	}

	public void setPwd(String pwd) {
		this.strPwd = pwd;
	}

	public void setUserId(long userId) {
		this.lUserId = userId;
	}

	public void setIsPass(boolean isPass) {
		this.isPass = isPass;
	}

	public void setNote(String note) {
		this.strNote = note;
	}

	public void setLoginName(String strLoginName) {
		this.strLoginName = strLoginName;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public void setNewPwd(String strNewPwd) {
		this.strNewPwd = strNewPwd;
	}

	public void setOrgId(long lOrgId) {
		this.lOrgId = lOrgId;
	}

	private Date string2date(String str) {
		Date date = null;
		if (str != null && !str.isEmpty()) {
			try {
				TimeZone tz = TimeZone.getTimeZone("GMT+08:00");
				Calendar cld = Calendar.getInstance(tz);
				SimpleDateFormat sdf = null;
				if (str.endsWith("Z")) {
					sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				} else if (str.indexOf("T") == -1) {
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				} else {
					sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				}
				sdf.setCalendar(cld);
				cld.setTime(sdf.parse(str));
				date = cld.getTime();
			} catch (ParseException e) {
				date = null;
			}
		}

		return date;
	}

	public void setUps(String ups) {
		if (ups == null || ups.isEmpty()) {
			this.ups = null;
		} else {
			try {
				JSONArray arr = new JSONArray(ups);
				Set<UserPermission> list = new HashSet<>(4);
				for (int i = 0; i < arr.length(); i++) {
					UserPermission up = UserPermission.parseCode(arr.optInt(i));
					list.add(up);
				}
				this.ups = list;
			} catch (Exception e) {
				this.ups = null;
			}
		}
	}

	public void setUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public void setMobile(String strMobile) {
		this.strMobile = strMobile;
	}

	public void setPageIdx(int iPageIdx) {
		this.iPageIdx = iPageIdx;
	}

	public void setPageSize(int iPageSize) {
		this.iPageSize = iPageSize;
	}

	public void setValidateCode(String strValidateCode) {
		this.strValidateCode = strValidateCode;
	}

	public void setUserStatus(String userStatus) {
		try {
			int code = Integer.parseInt(userStatus);
			this.userStatus = UserStatus.parseCode(code);
		} catch (NumberFormatException e) {
			this.userStatus = null;
		}
	}

	public void setVcType(int vcType) {
		this.vcType = VCType.parseCode(vcType);
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setBbs_token(String bbs_token) {
		this.bbs_token = bbs_token;
	}

	public void setOumStatus(int oumStatus) {
		this.oumStatus = OrgUserMapperStatus.parseCode(oumStatus);
	}

	public void setUserConfig(TUserConfig userConfig) {
		this.userConfig = userConfig;
	}

}
