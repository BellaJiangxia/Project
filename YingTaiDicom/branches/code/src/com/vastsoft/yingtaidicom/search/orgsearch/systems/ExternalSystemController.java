package com.vastsoft.yingtaidicom.search.orgsearch.systems;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.database.exception.DataBaseException;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.depend.interfase.DependInterface;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;

/** 树形结构的外部系统控制器 */
public class ExternalSystemController {
	/** 父系统控制器 */
	private ExternalSystemController parentCtrl;
	/** 当前系统执行器 */
	private ExternalSystemSearchExecutor esExecutor;
	/** 当前系统的依赖接口 */
	private DependInterface dependInf;
	/** 下级系统执行器列表 */
	private List<ExternalSystemController> listSubCtrl = new ArrayList<ExternalSystemController>();

	public static ExternalSystemController parse(Element element) throws SearchConfigFileParseException {
		ExternalSystemController result = null;
		String esName = element.getName();
		if (StringTools.isEmpty(esName))
			throw new SearchConfigFileParseException(element, "外部系统名称未找到！");
		ExternalSystemType esType = ExternalSystemType.parseName(esName = esName.trim());
		if (esType == null)
			throw new SearchConfigFileParseException(element, "系统暂不支持此外部系统类型！");
		ExternalSystemSearchExecutor esExecutor = null;
		DependInterface dependInf = null;
		try {
			esExecutor = esType.getAdapter().parse(element);
			String className = element.attributeValue("interface");
			if (!StringTools.isEmpty(className))
				dependInf = (DependInterface) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new SearchConfigFileParseException(element, e.getMessage());
		}
		if (esExecutor == null)
			throw new SearchConfigFileParseException(element, "通过此配置获取到的执行器为null！");
		LoggerUtils.logger.info("获取到执行器：" + esExecutor.getSystemIdentity().toString());
		result = new ExternalSystemController(dependInf, esExecutor);
		@SuppressWarnings("unchecked")
		List<Element> listSubEle = element.elements();
		if (CollectionTools.isEmpty(listSubEle))
			return result;
		for (Element eleTmp : listSubEle) {
			ExternalSystemController escTmp = ExternalSystemController.parse(eleTmp);
			if (escTmp == null)
				continue;
			escTmp.parentCtrl = result;
			result.listSubCtrl.add(escTmp);
		}
		return result;

	}

	private ExternalSystemController(DependInterface dependInf, ExternalSystemSearchExecutor esExecutor) {
		super();
		this.dependInf = dependInf;
		this.esExecutor = esExecutor;
		if (this.esExecutor == null)
			throw new RuntimeException("给定的执行器不能为空");
		LoggerUtils.logger.info("创建控制器：" + esExecutor.getSystemIdentity().toString());
	}

	private List<ExternalSystemController> getControllerByParamType(RemoteParamsManager remoteParamsManager) {
		if (remoteParamsManager.isEmpty())
			return null;
		List<RemoteParamsType> listParamType = remoteParamsManager.getParamTypes();
		if (CollectionTools.isEmpty(listParamType))
			return null;
		final List<ExternalSystemController> result = new ArrayList<ExternalSystemController>();
		for (RemoteParamsType remoteParamsType : listParamType) {
			boolean canSearch = esExecutor.canSearch(remoteParamsType);
			LoggerUtils.logger.info("控制器：" + esExecutor.getSystemIdentity().toString() + " 在通过参数确认是否能查询数据时，返回："
					+ canSearch + " 参数：" + String.valueOf(remoteParamsType));
			if (canSearch)
				result.add(this);
		}
		// remoteParamsManager.forEach(new RemoteParamEntryForeach() {
		// @Override
		// public void doForeach(RemoteParamsType paramType, List<String>
		// paramValues) {
		//
		// }
		// });
		// for (Iterator<RemoteParamsType> iterator =
		// params.keySet().iterator(); iterator.hasNext();) {
		// RemoteParamsType rpType = (RemoteParamsType) iterator.next();
		// boolean canSearch = this.esExecutor.canSearch(rpType);
		// LoggerUtils.logger.info("控制器：" +
		// esExecutor.getSystemIdentity().toString() + " 在通过参数确认是否能查询数据时，返回："
		// + canSearch + " 参数：" + String.valueOf(rpType));
		// if (canSearch)
		// result.add(this);
		// }
		for (ExternalSystemController esCtrl : listSubCtrl) {
			List<ExternalSystemController> tmp = esCtrl.getControllerByParamType(remoteParamsManager);
			if (tmp == null || tmp.size() <= 0)
				continue;
			result.addAll(tmp);
		}
		LoggerUtils.logger.info("通过参数确定用于确认身份查询的控制器！");
		return result;
	}

	public void checkValid() throws SearchExecuteException {
		if (this.esExecutor == null)
			throw new SearchExecuteException("外部系统的执行器必须指定！");
		for (ExternalSystemController esController : listSubCtrl) {
			esController.checkValid();
		}
	}

	public void searchPatientData(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws DataBaseException, PatientDataException, SearchExecuteException, SQLException {
		this.esExecutor.searchPatientData(searchResult, remoteParamsManager);
		LoggerUtils.logger.info("执行器被执行-搜索病人数据：" + esExecutor.getSystemIdentity().toString()
				+ "[方法：searchPatientData][执行器类：" + this.esExecutor.getClass().getName() + "]");
		if (CollectionTools.isEmpty(this.listSubCtrl))
			return;
		for (ExternalSystemController externalSystemController : listSubCtrl) {
			if (this.dependInf == null)
				return;
			externalSystemController.dependInf.sequenceMatching(searchResult, remoteParamsManager);
			externalSystemController.searchPatientData(searchResult, remoteParamsManager);
			externalSystemController.dependInf.organizeSearchResult(searchResult, remoteParamsManager);
		}
	}

	private void splitConfirmIdentity(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws PatientDataException, SearchExecuteException, DataBaseException {
		this.esExecutor.searchPatientData(searchResult, remoteParamsManager);
		LoggerUtils.logger.info("执行器被执行-确认身份：" + esExecutor.getSystemIdentity().toString()
				+ "[方法：splitConfirmIdentity][执行器类：" + this.esExecutor.getClass().getName() + "]");
		if (this.dependInf == null || this.parentCtrl == null)
			return;
		this.dependInf.reverseMatching(searchResult, remoteParamsManager);
		this.parentCtrl.splitConfirmIdentity(searchResult, remoteParamsManager);
		this.dependInf.organizeSearchResult(searchResult, remoteParamsManager);
	}

	public void confirmIdentity(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException, PatientDataException, DataBaseException {
		List<ExternalSystemController> listEsControllerTmp = this.getControllerByParamType(remoteParamsManager);
		if (CollectionTools.isEmpty(listEsControllerTmp))
			return;
		for (ExternalSystemController esController : listEsControllerTmp)
			esController.splitConfirmIdentity(searchResult, remoteParamsManager);
	}

	public void searchLastCaseNums(LastCaseNums result,
			RemoteParamsManager remoteParamsManager) throws SearchExecuteException, DataBaseException {
		this.esExecutor.searchLastCaseNums(result, remoteParamsManager);
		LoggerUtils.logger.info("执行器被执行：" + esExecutor.getSystemIdentity().toString() + "[方法：searchLastCaseNums][执行器类："
				+ this.esExecutor.getClass().getName() + "]");
		if (CollectionTools.isEmpty(this.listSubCtrl))
			return;
		for (ExternalSystemController externalSystemController : this.listSubCtrl) {
			externalSystemController.searchLastCaseNums(result, remoteParamsManager);
		}
	}

	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid)
			throws SearchExecuteException, DataBaseException {
		byte[] result = this.esExecutor.readThumbnail(remoteParamsManager, thumbnail_uid);
		LoggerUtils.logger.info("执行器被执行：" + esExecutor.getSystemIdentity().toString() + "[方法：readThumbnail][执行器类："
				+ this.esExecutor.getClass().getName() + "]");
		LoggerUtils.logger.info("返回值长度：" + (result == null ? 0 : result.length));
		if (!ArrayTools.isEmpty(result))
			return result;
		if (this.listSubCtrl != null && !this.listSubCtrl.isEmpty()) {
			for (ExternalSystemController externalSystemController : this.listSubCtrl) {
				result = externalSystemController.readThumbnail(remoteParamsManager, thumbnail_uid);
				if (!ArrayTools.isEmpty(result))
					return result;
			}
		}
		return null;
	}


	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(RemoteParamsManager remoteParamsManager, String patient_name, String identify_id) throws SearchExecuteException {
		LoggerUtils.logger.info("开始执行执行器：" + esExecutor.getSystemIdentity().toString() + "[方法：searchPatientInfoByPatientName][执行器类："
				+ this.esExecutor.getClass().getName() + "] 病人姓名："+patient_name +" 病人身份证号："+identify_id);
		List<PatientInfoResult> result = this.esExecutor.searchPatientInfoByPatientNameOrIdentifyId(remoteParamsManager, patient_name,identify_id);
		LoggerUtils.logger.info("执行器被执行：" + esExecutor.getSystemIdentity().toString() + "[方法：searchPatientInfoByPatientName][执行器类："
				+ this.esExecutor.getClass().getName() + "] 病人姓名："+patient_name +" 病人身份证号："+identify_id);
		LoggerUtils.logger.info("返回条数：" + (result == null ? 0 : result.size()));
		if (!CollectionTools.isEmpty(result))
			return result;
		if (this.listSubCtrl != null && !this.listSubCtrl.isEmpty()) {
			for (ExternalSystemController externalSystemController : this.listSubCtrl) {
				result = externalSystemController.searchPatientInfoByPatientNameOrIdentifyId(remoteParamsManager, patient_name,identify_id);
				if (!ArrayTools.isEmpty(result))
					return result;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "ExternalSystemController 其中-执行器：" + esExecutor.getSystemIdentity().toString() + "  子控制器列表："
				+ String.valueOf(this.listSubCtrl);
	}
}
