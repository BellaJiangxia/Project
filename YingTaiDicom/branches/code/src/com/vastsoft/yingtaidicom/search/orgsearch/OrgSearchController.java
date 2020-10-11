package com.vastsoft.yingtaidicom.search.orgsearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.database.exception.DataBaseException;
import com.vastsoft.yingtaidicom.search.assist.DefaultPriorityController;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.RemoteResult;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.interfase.PriorityController;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.ExternalSystemController;

public class OrgSearchController {
	private String org_code;// 机构编码
	private String org_name;// 机构名称
	private PriorityController priorityController;// 病人数据系统优先级控制器
	private List<ExternalSystemController> listEsController = new ArrayList<ExternalSystemController>();// 根外部系统控制器

	public OrgSearchController(Element element) throws SearchConfigFileParseException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		this.org_code = element.attributeValue("org_code");
		this.org_name = element.attributeValue("org_name");
		String classStr = element.attributeValue("priorty_controller");
		if (StringTools.isEmpty(classStr)) {
			this.priorityController = new DefaultPriorityController();
		} else {
			try {
				Class<?> cla = Class.forName(classStr.trim());
				if (cla == null)
					throw new SearchConfigFileParseException(element, classStr + "类未找到！");
				this.priorityController = (PriorityController) cla.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new SearchConfigFileParseException(element, e.getMessage());
			}
		}
		this.initExternalSystemController(element.element("external_systems"));
	}

	private void initExternalSystemController(Element element) throws SearchConfigFileParseException {
		@SuppressWarnings("unchecked")
		List<Element> eles = element.elements();
		if (eles == null || eles.isEmpty())
			throw new SearchConfigFileParseException(element, "配置文件中没有配置外部系统！");
		for (Element eleTmp : eles)
			this.listEsController.add(ExternalSystemController.parse(eleTmp));
		LoggerUtils.logger.info("OrgSearchController在初始化控制器树时，获取到控制器：" + String.valueOf(this.listEsController));
	}

	/**
	 * 执行搜索
	 * 
	 * @param params
	 *            参数表
	 * @return
	 * @throws SearchExecuteException
	 * @throws PatientDataException
	 * @throws DataBaseException
	 * @throws SQLException
	 */
	public RemoteResult executeSearch(RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException, PatientDataException, DataBaseException, SQLException {
		// 确定病人身份
		SearchResult searchResult = this.confirmPatientIdentity(remoteParamsManager);
		if (searchResult == null)
			throw new SearchExecuteException("在确定病人身份时失败！");
		this.searchPatientData(searchResult, remoteParamsManager);
		LoggerUtils.logger.info("搜索完成，开始整理返回结果！");
		return RemoteResult.organizeResult(this.priorityController,searchResult);
	}

//	/**
//	 * 整理搜索结果,组装关联关系
//	 * 
//	 * @param searchResult
//	 */
//	private void organizeSearchResult(SearchResult searchResult) {
//		SearchResult.organize(new Organizer(searchResult){
//			@Override
//			protected void organize() {
//				
//			}
//		});
//	}

	/**
	 * 检索病人数据
	 * 
	 * @throws SearchExecuteException
	 * @throws SQLException
	 * @throws PatientDataException
	 * @throws DataBaseException
	 */
	private void searchPatientData(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException, DataBaseException, PatientDataException, SQLException {
		for (ExternalSystemController externalSystemController : listEsController)
			externalSystemController.searchPatientData(searchResult, remoteParamsManager);
	}

	/**
	 * 确定病人身份
	 * 
	 * @return 数据管理器
	 * 
	 * @throws SearchExecuteException
	 * @throws PatientDataException
	 * @throws DataBaseException
	 */
	private SearchResult confirmPatientIdentity(RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException, PatientDataException, DataBaseException {
		SearchResult searchResult = new SearchResult();
		for (ExternalSystemController externalSystemController : listEsController) {
			externalSystemController.confirmIdentity(searchResult, remoteParamsManager);
		}
		return searchResult;
	}

	public String getOrg_code() {
		return org_code;
	}

	public String getOrg_name() {
		return org_name;
	}

	/**
	 * 搜索最近几天的病历号或者ID
	 * 
	 * @throws SearchExecuteException
	 * @throws DataBaseException
	 */
	public LastCaseNums searchLastCaseNums(RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException, DataBaseException {
		LastCaseNums result = new LastCaseNums();
		for (ExternalSystemController externalSystemController : listEsController) {
			try {
				externalSystemController.searchLastCaseNums(result, remoteParamsManager);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/** 读取指定图像的缩略图 */
	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid) {
		for (ExternalSystemController externalSystemController : listEsController) {
			try {
				byte[] result = externalSystemController.readThumbnail(remoteParamsManager, thumbnail_uid);
				if (!ArrayTools.isEmpty(result))
					return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(RemoteParamsManager remoteParamsManager, String patient_name, String identify_id) {
		for (ExternalSystemController externalSystemController : listEsController) {
			try {
				List<PatientInfoResult> result = externalSystemController.searchPatientInfoByPatientNameOrIdentifyId(remoteParamsManager, patient_name,identify_id);
				if (!CollectionTools.isEmpty(result))
					return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
