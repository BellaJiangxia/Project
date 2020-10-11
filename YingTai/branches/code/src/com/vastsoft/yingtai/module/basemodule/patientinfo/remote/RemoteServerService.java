package com.vastsoft.yingtai.module.basemodule.patientinfo.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.list.SetUniqueList;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.PatientInfoResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteNumEntry;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.RemoteInterface;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.OrgRemoteConfig;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.RemoteServerController;
import com.vastsoft.yingtai.module.org.configs.exception.NotFoundDeviceTypeMapperException;

public class RemoteServerService {
	public static final RemoteServerService instance = new RemoteServerService();

	private RemoteServerService() {
	}

	@SuppressWarnings("unchecked")
	public Map<ShareRemoteParamsType, List<ShareRemoteNumEntry>> onSearchRemotePascCaseNum(
			List<? extends OrgRemoteConfig> listOrConfig, Map<ShareRemoteParamsType, String> params) {
		try {
			if (listOrConfig == null || listOrConfig.size() <= 0)
				return null;
			Map<ShareRemoteParamsType, List<ShareRemoteNumEntry>> result = new HashMap<ShareRemoteParamsType, List<ShareRemoteNumEntry>>();
			fa: for (OrgRemoteConfig orConfig : listOrConfig) {
				try {
					if (orConfig == null || !orConfig.valid())
						throw new RemotePatientInfoException("此机构远程配置无效:" + String.valueOf(orConfig));
					ShareRemoteServerVersion rev = orConfig.getRemoteServerVersion();
					if (rev == null)
						throw new RemotePatientInfoException("没有找到机构支持的远程服务器版本！");
					RemoteServerController rsCtrl = rev.getRemoteServerController();
					if (rsCtrl == null)
						throw new RemotePatientInfoException("没有找到远程服务器版本：" + rev.getName() + "的控制器！");
					if (!rsCtrl.isSupportInterface(RemoteInterface.QUERY_LAST_PASC_NUMS))
						throw new RemotePatientInfoException("此版本的远程服务不支持调用此接口！");
					Map<ShareRemoteParamsType, String> paramsTmp = CommonTools.combine(params,
							orConfig.getAdditionalRemoteParams());
					if (paramsTmp == null || paramsTmp.isEmpty())
						throw new RemotePatientInfoException("检索参数必须存在！");
					LoggerUtils.logger
							.info("发起查询最近的病例数据号的远程请求@" + orConfig.getQuery_url() + " 参数：" + String.valueOf(paramsTmp));
					Map<ShareRemoteParamsType, List<String>> resultTmp = rsCtrl
							.searchSearchRemotePascCaseNum(orConfig.getQuery_url(), paramsTmp);
					if (resultTmp != null && !resultTmp.isEmpty()) {
						fb: for (Iterator<ShareRemoteParamsType> iterator = resultTmp.keySet().iterator(); iterator
								.hasNext();) {
							ShareRemoteParamsType rpt = (ShareRemoteParamsType) iterator.next();
							if (rpt == null)
								continue fb;
							List<String> listTmp = resultTmp.get(rpt);
							if (CollectionTools.isEmpty(listTmp))
								continue fb;
							List<ShareRemoteNumEntry> lltt = result.get(rpt);
							if (lltt == null) {
								lltt = SetUniqueList.decorate(new ArrayList<ShareRemoteNumEntry>());
								result.put(rpt, lltt);
							}
							fc: for (String str : listTmp) {
								if (StringTools.isEmpty(str))
									continue fc;
								if (!str.contains("[")) {
									lltt.add(new ShareRemoteNumEntry(str, ""));
									continue fc;
								}
								String[] ssst = StringTools.splitString(str, '[');
								if (ArrayTools.isEmpty(ssst) || ssst.length != 2)
									continue fc;
								String patient_name = ssst[1];
								if (StringTools.isEmpty(patient_name)) {
									lltt.add(new ShareRemoteNumEntry(ssst[0], ""));
									continue fc;
								}
								lltt.add(new ShareRemoteNumEntry(ssst[0],
										patient_name.substring(0, patient_name.length() - 1)));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue fa;
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public synchronized boolean onSearchRemotePatientInfo(List<? extends OrgRemoteConfig> listOrConfig,
			Map<ShareRemoteParamsType, String> params) throws NotFoundDeviceTypeMapperException {
		try {
			if (listOrConfig == null || listOrConfig.size() <= 0)
				throw new RemotePatientInfoException("没有指定检索配置！");
			for (OrgRemoteConfig orConfig : listOrConfig) {
				if (orConfig == null || !orConfig.valid())
					throw new RemotePatientInfoException("机构的远程检索配置无效！");
				ShareRemoteServerVersion rev = orConfig.getRemoteServerVersion();
				if (rev == null)
					throw new RemotePatientInfoException("没有找到机构支持的远程服务器版本！");
				RemoteServerController rsCtrl = rev.getRemoteServerController();
				if (rsCtrl == null)
					throw new RemotePatientInfoException("没有找到远程服务器版本：" + rev.getName() + "的控制器！");
				if (!rsCtrl.isSupportInterface(RemoteInterface.SEARCH_PATIENT_INFO))
					throw new RemotePatientInfoException("此版本的远程服务不支持调用此接口！");
				Map<ShareRemoteParamsType, String> addtrp = orConfig.getAdditionalRemoteParams();
				if (addtrp != null && addtrp.size() > 0)
					params.putAll(orConfig.getAdditionalRemoteParams());
				LoggerUtils.logger.info("发起搜索病人数据的远程请求@" + orConfig.getQuery_url() + " 参数：" + String.valueOf(params));
				ShareRemoteResult remoteResult = rsCtrl.searchPatientInfo(orConfig, params);
				rev.getRemoteDataHandler().handleRemotePatientData(remoteResult, params);
			}
			return true;
		} catch(NotFoundDeviceTypeMapperException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public byte[] onReadThumbnail(OrgRemoteConfig orConfig, Map<ShareRemoteParamsType, String> params,
			String thumbnail_uid) {
		try {
			if (orConfig == null || !orConfig.valid())
				throw new RemotePatientInfoException("机构的远程检索配置无效！");
			ShareRemoteServerVersion rev = orConfig.getRemoteServerVersion();
			if (rev == null)
				throw new RemotePatientInfoException("没有找到机构支持的远程服务器版本！");
			RemoteServerController rsCtrl = rev.getRemoteServerController();
			if (rsCtrl == null)
				throw new RemotePatientInfoException("没有找到远程服务器版本：" + rev.getName() + "的控制器！");
			if (!rsCtrl.isSupportInterface(RemoteInterface.QUERY_THUMBNAIL))
				throw new RemotePatientInfoException("此版本的远程服务不支持调用此接口！");
			LoggerUtils.logger.info("发起读取缩略图的远程请求@" + orConfig.getQuery_url() + " 参数：" + String.valueOf(params));
			return rsCtrl.readThumbnail(orConfig, params, thumbnail_uid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**通过病人姓名查询病人号
	 * @param patient_identity_id 
	 * @throws RemotePatientInfoException */
	public List<PatientInfoResult> onSearchParamValuesByPatientNameOrPatientIdentifyId(OrgRemoteConfig orgConfig,String orgCode,
			String patientName, String patient_identity_id) throws RemotePatientInfoException{
		if (orgConfig == null)
			throw new RemotePatientInfoException("必须指定用于检索的机构配置！");
		ShareRemoteServerVersion rev = orgConfig.getRemoteServerVersion();
		if (rev == null)
			throw new RemotePatientInfoException("没有找到机构支持的远程服务器版本！");
		RemoteServerController rsCtrl;
		try {
			rsCtrl = rev.getRemoteServerController();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RemotePatientInfoException(e);
		}
		if (rsCtrl == null)
			throw new RemotePatientInfoException("没有找到远程服务器版本：" + rev.getName() + "的控制器！");
		if (!rsCtrl.isSupportInterface(RemoteInterface.QUERY_PARAM_VALUES_BY_PATIENT_NAME))
			throw new RemotePatientInfoException("此版本的远程服务不支持调用此接口：【"+RemoteInterface.QUERY_PARAM_VALUES_BY_PATIENT_NAME.getName()+"】！");
		LoggerUtils.logger.info("发起读取缩略图的远程请求@" + orgConfig.getQuery_url() + " 参数：patientName=" + patientName+" patient_identity_id="+patient_identity_id);
		return rsCtrl.searchParamValuesByPatientNameOrPatientIdentifyId(orgConfig, orgCode, patientName,patient_identity_id);
	}
}
