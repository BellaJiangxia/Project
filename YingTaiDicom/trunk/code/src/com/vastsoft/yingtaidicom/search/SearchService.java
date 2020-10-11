package com.vastsoft.yingtaidicom.search;

import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.RemoteResult;
import com.vastsoft.yingtaidicom.search.assist.SearchConfigFileParser;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.orgsearch.OrgSearchController;

public class SearchService {
	private static SearchService instance;
	// 机构编号 - 控制器
	private Map<String, OrgSearchController> mapOrgSearchCtrl;

	public static SearchService getInstance() throws SearchConfigFileParseException {
		if (instance == null){
			LoggerUtils.logger.info("开始初始化查询器服务！");
			instance = new SearchService();
			LoggerUtils.logger.info("结束初始化查询器服务！");
		}
		return instance;
	}

	private SearchService() throws SearchConfigFileParseException {
		mapOrgSearchCtrl = new SearchConfigFileParser("search_config.xml").parse();
	}

	/**
	 * 执行搜索
	 * 
	 * @param org_code
	 *            机构编号
	 * @param param_type
	 *            参数类型
	 * @param param_value
	 *            参数值
	 * @return 数据管理器
	 * @throws SearchExecuteException
	 */
	public RemoteResult executeSearch(RemoteParamsManager remoteParamsManager) throws SearchExecuteException {
		try {
			List<String> listOrg_code = remoteParamsManager.getParamValuesByType(RemoteParamsType.ORG_CODE);
			if (CollectionTools.isEmpty(listOrg_code))
				throw new SearchExecuteException("机构编号必须指定！");
			String org_code = listOrg_code.get(0);
			OrgSearchController osCtrl = mapOrgSearchCtrl.get(org_code = org_code.trim());
			if (osCtrl == null)
				throw new SearchExecuteException("本服务器不支持此机构[org_code=" + org_code + "]，请检查机构配置！");
			return osCtrl.executeSearch(remoteParamsManager);
		} catch (Exception e) {
			throw SearchExecuteException.exceptionOf(e);
		}
	}

	/**
	 * 搜索最近几天的病历号
	 * 
	 * @param params 查询参数
	 * @return
	 */
	public LastCaseNums searchLastCaseNums(RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException {
		try {
			List<String> listOrg_code = remoteParamsManager.getParamValuesByType(RemoteParamsType.ORG_CODE);
			if (CollectionTools.isEmpty(listOrg_code))
				throw new SearchExecuteException("机构编号必须指定！");
			String org_code = listOrg_code.get(0);
			OrgSearchController osCtrl = mapOrgSearchCtrl.get(org_code = org_code.trim());
			if (osCtrl == null)
				throw new SearchExecuteException("本服务器不支持此机构[org_code=" + org_code + "]，请检查机构配置！");
			return osCtrl.searchLastCaseNums(remoteParamsManager);
		} catch (Exception e) {
			throw SearchExecuteException.exceptionOf(e);
		}
	}
	/**读取指定图像唯一标识的缩略图
	 * @throws SearchExecuteException */
	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid) throws SearchExecuteException {
		try {
			List<String> listOrg_code = remoteParamsManager.getParamValuesByType(RemoteParamsType.ORG_CODE);
			if (CollectionTools.isEmpty(listOrg_code))
				throw new SearchExecuteException("机构编号必须指定！");
			String org_code = listOrg_code.get(0);
			OrgSearchController osCtrl = mapOrgSearchCtrl.get(org_code = org_code.trim());
			if (osCtrl == null)
				throw new SearchExecuteException("本服务器不支持此机构[org_code=" + org_code + "]，请检查机构配置！");
			return osCtrl.readThumbnail(remoteParamsManager,thumbnail_uid);
		} catch (Exception e) {
			throw new SearchExecuteException(e);
		}
	}

	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(RemoteParamsManager remoteParamsManager, String patient_name,String identify_id) throws SearchExecuteException {
		try {
			List<String> listOrg_code = remoteParamsManager.getParamValuesByType(RemoteParamsType.ORG_CODE);
			if (CollectionTools.isEmpty(listOrg_code))
				throw new SearchExecuteException("机构编号必须指定！");
			String org_code = listOrg_code.get(0);
			OrgSearchController osCtrl = mapOrgSearchCtrl.get(org_code = org_code.trim());
			if (osCtrl == null)
				throw new SearchExecuteException("本服务器不支持此机构[org_code=" + org_code + "]，请检查机构配置！");
			return osCtrl.searchPatientInfoByPatientNameOrIdentifyId(remoteParamsManager,patient_name,identify_id);
		} catch (Exception e) {
			throw new SearchExecuteException(e);
		}
	}
}
