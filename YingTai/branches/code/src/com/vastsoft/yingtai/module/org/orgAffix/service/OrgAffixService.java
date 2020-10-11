package com.vastsoft.yingtai.module.org.orgAffix.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.org.orgAffix.entity.TOrgAffix;
import com.vastsoft.yingtai.module.org.orgAffix.exception.OrgAffixException;
import com.vastsoft.yingtai.module.org.orgAffix.mapper.OrgAffixMapper;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class OrgAffixService {
	public static final OrgAffixService instance = new OrgAffixService();

	private OrgAffixService() {
	}

	/**
	 * 查询某机构的配置，当lOrgId==0时查询默认配置
	 * 
	 * @param passport
	 * @param lOrgId
	 *            机构ID，默认配置机构ID为0
	 * @return
	 * @throws BaseException
	 */
	public TOrgAffix queryOrgAffixByOrgId(long orgId) throws OrgAffixException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgAffixMapper mapper = session.getMapper(OrgAffixMapper.class);
			List<TOrgAffix> listAffix = mapper.selectAffixByOrgId(orgId);
			return CollectionTools.isEmpty(listAffix) ? null : listAffix.get(0);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public TOrgAffix queryOrgAffixByOrgIdWithDefaultAffix(long orgId) throws OrgAffixException {
		TOrgAffix result = this.queryOrgAffixByOrgId(orgId);
		if (result != null)
			return result;
		result = this.queryDefaultOrgAffix();
		if (result == null)
			return null;
		result.setOrg_id(orgId);
		return result;
	}

	public TOrgAffix selectOrgAffixById(long lOrgAffixId) throws OrgAffixException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgAffixMapper mapper = session.getMapper(OrgAffixMapper.class);
			return mapper.selectOrgAffixById(lOrgAffixId);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

//	private static final String addOrUpdateOrgAffixLock = "addOrUpdateOrgAffixLock";
//
//	/**
//	 * 添加一个机构附件配置
//	 * 
//	 * @throws NotPermissionException
//	 * 
//	 * @throws BaseException
//	 */
//	public TOrgAffix addOrUpdateDefaultOrgAffix(Passport passport, TOrgAffix orgAffix)
//			throws OrgAffixException, NotPermissionException {
//		synchronized (addOrUpdateOrgAffixLock) {
//			if (!passport.getUserType().equals(UserType.SUPER_ADMIN))
//				if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
//					throw new NotPermissionException("你没有权限执行此操作！");
//			this.checkOrgAffixObject(orgAffix);
//			SqlSession session = null;
//			try {
//				session = SessionFactory.getSession();
//				OrgMapper mapper = session.getMapper(OrgMapper.class);
//				List<TOrgAffix> listOldOrgAffix = mapper.selectAffixByOrgId(0);
//				if (listOldOrgAffix == null || listOldOrgAffix.size() <= 0) {
//					orgAffix.setOrg_id(0);
//					mapper.insertOrgAffix(orgAffix);
//				} else {
//					TOrgAffix oldOrgAffix = listOldOrgAffix.get(0);
//					oldOrgAffix.setAe_code(orgAffix.getAe_code());
//					oldOrgAffix.setDicomweb_url(orgAffix.getDicomweb_url());
//					oldOrgAffix.setPassword(orgAffix.getPassword());
//					oldOrgAffix.setQuery_url(orgAffix.getQuery_url());
//					oldOrgAffix.setRemote_server_version(RemoteServerVersion.V_1.getCode());
//					oldOrgAffix.setUser_name(orgAffix.getUser_name());
//					mapper.updateOrgAffix(oldOrgAffix);
//					orgAffix = oldOrgAffix;
//				}
//				session.commit();
//				return orgAffix;
//			} catch (Exception e) {
//				session.rollback(true);
//				throw e;
//			} finally {
//				SessionFactory.closeSession(session);
//			}
//		}
//	}

	private void checkOrgAffixObject(TOrgAffix orgAffix) throws OrgAffixException {
		if (orgAffix == null)
			throw new OrgAffixException("请指定机构配置对象！");
		ShareRemoteServerVersion rsv = ShareRemoteServerVersion.parseCode(orgAffix.getRemote_server_version());
		if (rsv == null)
			throw new OrgAffixException("远程检索服务器版本必须指定！");
		if (rsv.equals(ShareRemoteServerVersion.V_1)) {
			if (orgAffix.getAe_code() == null || orgAffix.getAe_code().trim().isEmpty())
				throw new OrgAffixException("机构配置的IE号必须指定！");
			orgAffix.setAe_code(orgAffix.getAe_code().trim());
		}
		if (orgAffix.getDicomweb_url() == null || orgAffix.getDicomweb_url().trim().isEmpty())
			throw new OrgAffixException("DICOM图像服务地址必须指定！");
		orgAffix.setDicomweb_url(orgAffix.getDicomweb_url().trim());
		if (orgAffix.getInternet_ip() != null && !orgAffix.getInternet_ip().trim().isEmpty()) {
			orgAffix.setInternet_ip(orgAffix.getInternet_ip().trim());
			if (StringTools.isIpAddr(orgAffix.getInternet_ip()))
				throw new OrgAffixException("机构所在公网IP地址不合法！");
		}
		if (orgAffix.getIntranet_url() != null && !orgAffix.getIntranet_url().trim().isEmpty())
			orgAffix.setIntranet_url(orgAffix.getIntranet_url().trim());
		if (StringTools.isIpAddr(orgAffix.getIntranet_url()))
			throw new OrgAffixException("机构服务器所在内网URL地址不合法！");
		if (orgAffix.getPassword() == null || orgAffix.getPassword().trim().isEmpty())
			throw new OrgAffixException("登陆密码必须指定！");
		orgAffix.setPassword(orgAffix.getPassword().trim());
		if (orgAffix.getQuery_url() == null || orgAffix.getQuery_url().trim().isEmpty())
			throw new OrgAffixException("检索服务地址必须指定！");
		orgAffix.setQuery_url(orgAffix.getQuery_url().trim());
		if (orgAffix.getUser_name() == null || orgAffix.getUser_name().trim().isEmpty())
			throw new OrgAffixException("登陆用户名必须指定！");
		orgAffix.setUser_name(orgAffix.getUser_name().trim());
	}

	public void deleteOrgAffixById(Passport passport, long lOrgAffixId)
			throws OrgAffixException, NotPermissionException {
		if (!passport.getUserType().equals(UserType.SUPER_ADMIN))
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
				throw new NotPermissionException("你没有权限执行此操作！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgAffixMapper mapper = session.getMapper(OrgAffixMapper.class);
			TOrgAffix affix = mapper.selectOrgAffixById(lOrgAffixId);
			if (affix == null)
				throw new OrgAffixException("要删除的配置不存在！");
			if (affix.getOrg_id() == 0)
				throw new OrgAffixException("不能删除默认配置！");
			mapper.deleteOrgAffixById(lOrgAffixId);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public TOrgAffix queryDefaultOrgAffix() throws OrgAffixException {
		return this.queryOrgAffixByOrgId(0);
	}

	public TOrgAffix saveOrgAffix(Passport passport, TOrgAffix orgAffix) throws OrgAffixException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR))
			throw new OrgAffixException("您缺少机构管理员权限！");
		this.checkOrgAffixObject(orgAffix);
		if (orgAffix.getOrg_id() < 0)
			throw new OrgAffixException("机构ID必须指定！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgAffixMapper mapper = session.getMapper(OrgAffixMapper.class);
			List<TOrgAffix> listOrgAffix = mapper.selectAffixByOrgId(orgAffix.getOrg_id());
			if (CollectionTools.isEmpty(listOrgAffix)) {
				orgAffix.setCreate_time(new Date());
				orgAffix.setStatus(1);
				mapper.insertOrgAffix(orgAffix);
			} else {
				TOrgAffix oldOrgAffix = listOrgAffix.get(0);
				oldOrgAffix.setAe_code(orgAffix.getAe_code());
				oldOrgAffix.setDicomweb_url(orgAffix.getDicomweb_url());
				oldOrgAffix.setInternet_ip(orgAffix.getInternet_ip());
				oldOrgAffix.setIntranet_url(orgAffix.getIntranet_url());
				oldOrgAffix.setPassword(orgAffix.getPassword());
				oldOrgAffix.setQuery_url(orgAffix.getQuery_url());
				oldOrgAffix.setRemote_server_version(orgAffix.getRemote_server_version());
				oldOrgAffix.setUser_name(orgAffix.getUser_name());
				oldOrgAffix.setView_type(orgAffix.getView_type());
				mapper.updateOrgAffix(oldOrgAffix);
				orgAffix = oldOrgAffix;
			}
			session.commit();
			return orgAffix;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}
}
