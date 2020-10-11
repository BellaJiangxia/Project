package com.vastsoft.yingtai.module.org.configs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.module.org.configs.constants.OrgConfigType;
import com.vastsoft.yingtai.module.org.configs.entity.TOrgConfigs;
import com.vastsoft.yingtai.module.org.configs.exception.OrgConfigsException;
import com.vastsoft.yingtai.module.org.configs.mapper.OrgConfigsMapper;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.configUtils.ConfigUtil;

public class OrgConfigService {
	public static final OrgConfigService instance = new OrgConfigService();

	public static void main(String[] args) throws BaseException {
		TDicValue dv = instance.takeDeviceTypeByOrgIdAndDeviceTypeName(100045, "CT");
		System.out.println(dv.getValue());
	}

	/** 获取机构与系统的设备类型映射 */
	public TDicValue takeDeviceTypeByOrgIdAndDeviceTypeName(long org_id, String device_type_name) throws BaseException {
		if (StringTools.isEmpty(device_type_name))
			throw new OrgConfigsException("设备类型名称必须指定！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgConfigsMapper mapper = session.getMapper(OrgConfigsMapper.class);
			TOrgConfigs orgConfigs = mapper.selectOrgConfigByOrgIdForUpdate(org_id);
			if (orgConfigs == null)
				return SysService.instance.queryDicValueByTypeAndValue(device_type_name, null,
						DictionaryType.DEVICE_TYPE);
			ConfigUtil cu = new ConfigUtil(orgConfigs);
			List<TDicValue> listDeviceType = SysService.instance.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
			if (listDeviceType == null || listDeviceType.size() <= 0)
				return SysService.instance.queryDicValueByTypeAndValue(device_type_name, null,
						DictionaryType.DEVICE_TYPE);
			for (TDicValue tDicValue : listDeviceType) {
				String kkeys = cu.getValue(OrgConfigType.ORG_DEVICE_NAME_2_SYS_DEVICE_NAME_MAPPER_CONFIG.getCode_name()
						+ "." + tDicValue.getValue(), "");
				if (StringTools.isEmpty(kkeys))
					continue;
				String[] strs = StringTools.splitString(kkeys, ',', '，');
				if (ArrayTools.exist(strs, device_type_name))
					return tDicValue;
			}
			return SysService.instance.queryDicValueByTypeAndValue(device_type_name, null, DictionaryType.DEVICE_TYPE);
		} finally {
			SessionFactory.closeSession(session);
		}
	}
	
	/**获取报告设备名称的映射名，如果未找到返回原始名称
	 * @throws OrgConfigsException */
	public String takeDeviceNameReportMapperByOrgIdAndDeviceTypeName(long org_id, String device_type_name) throws OrgConfigsException{
		if (StringTools.isEmpty(device_type_name))
			throw new OrgConfigsException("设备类型名称必须指定！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgConfigsMapper mapper = session.getMapper(OrgConfigsMapper.class);
			TOrgConfigs orgConfigs = mapper.selectOrgConfigByOrgIdForUpdate(org_id);
			if (orgConfigs == null)
				return device_type_name;
			ConfigUtil cu = new ConfigUtil(orgConfigs);
			//List<TDicValue> listDeviceType = SysService.instance.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
//			if (listDeviceType == null || listDeviceType.size() <= 0)
//				return device_type_name;
//			for (TDicValue tDicValue : listDeviceType) {
			String kkeys = cu.getValue(OrgConfigType.DEVICE_NAME_2_REPORT_DEVICE_NAME_MAPPER_CONFIG.getCode_name()
					+ "." + device_type_name, "");
			if (!StringTools.isEmpty(kkeys))
				return kkeys;
//			}
			return device_type_name;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 拉取配置，通过配置类型
	 * 
	 * @throws BaseException
	 */
	public List<Map<String, String>> takeOrgConfigMapper(long org_id,OrgConfigType orgConfigType)
			throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgConfigsMapper mapper = session.getMapper(OrgConfigsMapper.class);
			TOrgConfigs orgConfig = mapper.selectOrgConfigByOrgIdForUpdate(org_id);
			if (orgConfig == null)
				orgConfig = new TOrgConfigs();
			ConfigUtil cu = new ConfigUtil(orgConfig);
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			if (ArrayTools.contains(new OrgConfigType[] { OrgConfigType.DEVICE_NAME_2_REPORT_DEVICE_NAME_MAPPER_CONFIG,
					OrgConfigType.ORG_DEVICE_NAME_2_SYS_DEVICE_NAME_MAPPER_CONFIG }, orgConfigType)) {
				List<TDicValue> listDeviceType = SysService.instance
						.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
				if (listDeviceType == null || listDeviceType.size() <= 0)
					return result;
				for (TDicValue tDicValue : listDeviceType) {
					Map<String, String> mapa = new HashMap<>();
					mapa.put("key", tDicValue.getValue());
					mapa.put("value", cu.getValue(orgConfigType.getCode_name() + "." + tDicValue.getValue(), ""));
					result.add(mapa);
				}
			}
			return result;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// /**
	// * 拉取设备映射关系
	// *
	// * @throws BaseException
	// */
	// public List<Map<String, String>> takeDeviceMapper(Passport passport)
	// throws BaseException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// OrgMapper mapper = session.getMapper(OrgMapper.class);
	// TOrgConfigs orgConfig =
	// mapper.selectOrgConfigByOrgIdForUpdate(passport.getOrgId());
	// if (orgConfig == null)
	// orgConfig = new TOrgConfigs();
	// ConfigUtil cu = new ConfigUtil(orgConfig);
	// List<Map<String, String>> result = new ArrayList<Map<String, String>>();
	// List<TDicValue> listDeviceType =
	// SysService.instance.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
	// if (listDeviceType == null || listDeviceType.size() <= 0)
	// return result;
	// for (TDicValue tDicValue : listDeviceType) {
	// Map<String, String> mapa = new HashMap<>();
	// mapa.put("key", tDicValue.getValue());
	// mapa.put("value", cu.getValue("device_map." + tDicValue.getValue(), ""));
	// result.add(mapa);
	// }
	// session.commit();
	// return result;
	// } catch (Exception e) {
	// session.rollback(true);
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	/**
	 * @throws BaseException
	 */
	public void saveDeviceMapper(Passport passport, String jsonDeviceMapper) throws BaseException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new OrgConfigsException("你缺少机构管理权限！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgConfigsMapper mapper = session.getMapper(OrgConfigsMapper.class);
			LoggerUtils.logger.info("jsonDeviceMapper:" + jsonDeviceMapper);
			TOrgConfigs orgConfig = mapper.selectOrgConfigByOrgIdForUpdate(passport.getOrgId());
			if (orgConfig == null) {
				orgConfig = new TOrgConfigs();
				orgConfig.setDevice_map(jsonDeviceMapper);
				orgConfig.setOrg_id(passport.getOrgId());
				mapper.insertOrgConfigs(orgConfig);
			} else {
				orgConfig.setDevice_map(jsonDeviceMapper);
				mapper.updateOrgConfigs(orgConfig);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private TOrgConfigs queryOrgConfigsByOrgId(long request_org_id) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgConfigsMapper mapper = session.getMapper(OrgConfigsMapper.class);
			TOrgConfigs orgConfig = mapper.selectOrgConfigByOrgIdForUpdate(request_org_id);
			session.commit();
			return orgConfig;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public void saveOrgDeviceName2SysDeviceNameMapperConfig(Passport passport, String org_sys_device_name_mapping)
			throws OrgConfigsException {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new OrgConfigsException("你缺少机构管理权限！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgConfigsMapper mapper = session.getMapper(OrgConfigsMapper.class);
			LoggerUtils.logger.info("jsonDeviceMapper:" + org_sys_device_name_mapping);
			TOrgConfigs orgConfig = mapper.selectOrgConfigByOrgIdForUpdate(passport.getOrgId());
			if (orgConfig == null) {
				orgConfig = new TOrgConfigs();
				orgConfig.setOrg_sys_device_name_mapping(org_sys_device_name_mapping);
				orgConfig.setOrg_id(passport.getOrgId());
				mapper.insertOrgConfigs(orgConfig);
			} else {
				orgConfig.setOrg_sys_device_name_mapping(org_sys_device_name_mapping);
				mapper.updateOrgConfigs(orgConfig);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}
}
