package com.vastsoft.yingtaidicom.search.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryType;
import com.vastsoft.yingtaidicom.search.constants.PatientDataSourceType;
import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.entity.TCaseHistory;
import com.vastsoft.yingtaidicom.search.entity.TDicomImg;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.entity.TPatientOrgMapper;
import com.vastsoft.yingtaidicom.search.entity.TSeries;
import com.vastsoft.yingtaidicom.search.exception.DataPriorityException;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.interfase.PriorityController;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;

/** 结果数据管理器 */
public class RemoteResult {
	private Map<String, RemoteEntityAgent> mapRea;
	/** 数据优先级控制器 */
	private PriorityController priorityController;

	public RemoteResult(PriorityController priorityController) {
		this();
		this.priorityController = priorityController;

	}

	private RemoteResult() {
		super();
		mapRea = new HashMap<String, RemoteEntityAgent>();
	}

	public static RemoteResult jsonDeserialize(JSONObject jsonObject) throws JSONException {
		RemoteResult result = new RemoteResult();
		JSONArray jsoarr = jsonObject.getJSONArray("mapRea");
		for (int i = 0; i < jsoarr.length(); i++) {
			JSONObject jo = jsoarr.getJSONObject(i);
			RemoteEntityAgent rea;
			try {
				rea = RemoteEntityAgent.jsonDeserialize(jo);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new JSONException(e);
			}
			result.mapRea.put(rea.getUid(), rea);
		}
		return result;
	}

	/**
	 * 系列化
	 * 
	 * @throws JSONException
	 */
	public JSONObject jsonSerialize() throws JSONException {
		JSONObject result = new JSONObject();
		JSONArray jsoarr = new JSONArray();
		for (Iterator<RemoteEntityAgent> iterator = mapRea.values().iterator(); iterator.hasNext();) {
			RemoteEntityAgent rea = (RemoteEntityAgent) iterator.next();
			jsoarr.put(rea.serialize());
		}
		result.put("mapRea", jsoarr);
		return result;
	}

	/**
	 * 向数据管理器里面添加数据<br>
	 * 注意：如果想要更新此数据管理器中的数据，请使用updateData方法，调用此方法将始终在数据管理器中新增一条数据
	 * 
	 * @param esType
	 *            添加数据的系统类型
	 * @param parentUid
	 *            父节点的UID
	 * @param entity
	 *            添加的数据实体对象<br>
	 *            保存的是该对象的一个副本，当后续修改此对象时不会对数据管理器中的本对象进行修改
	 * @param extraAttributes
	 *            附加属性
	 * @return 返回是否添加成功
	 * @throws PatientDataException
	 */
	private RemoteEntityAgent addData(String parentUid, AbstractRemoteEntity entity) throws PatientDataException {
		if (entity == null)
			throw new PatientDataException("要添加的实体数据必须指定！");
		if (entity.isCopy())
			throw new PatientDataException("副本不能再次保存,此实体是一个已经保存过的实体的拷贝！");
		if (entity.getPdType().isRootPatientDataType()) {
			List<RemoteEntityAgent> listRootRea = this.findReaListByPdType(entity.getPdType());
			if (!CollectionTools.isEmpty(listRootRea))
				return this.updateData(listRootRea.get(0).getUid(), entity);
		} else {
			if (StringTools.isEmpty(parentUid))
				throw new PatientDataException("非根数据类型的数据必须指定父数据UID！");
			RemoteEntityAgent parentRea = this.mapRea.get(parentUid);
			if (parentRea == null)
				throw new PatientDataException("没有找到父UID对应的对象！");
			if (!entity.getPdType().getParent_type().equals(parentRea.getPdType()))
				throw new PatientDataException("指定的父UID对应的数据不是当前数据的父类型数据");
		}
		RemoteEntityAgent rea = new RemoteEntityAgent(parentUid, entity);
		this.mapRea.put(rea.getUid(), rea);
		return rea;
	}

	/**
	 * 保存病人数据，默认只可能存在一个病人，因此将与以前的病人数据合并，如果存在
	 * 
	 * @param entity
	 * @return
	 * @throws PatientDataException
	 */
	public RemoteEntityAgent savePatientData(TPatient entity) throws PatientDataException {
		if (entity == null)
			throw new PatientDataException("要添加的实体数据必须指定！");
		return this.addData(null, entity);
	}

	/**
	 * 保存病人机构映射数据，默认只可能存在一个病人，因此将与以前的病人数据合并，如果存在
	 * 
	 * @param entity
	 * @return
	 * @throws PatientDataException
	 */
	public RemoteEntityAgent savePatientOrgMapperData(TPatientOrgMapper entity) throws PatientDataException {
		if (entity == null)
			throw new PatientDataException("要添加的实体数据必须指定！");
		RemoteEntityAgent patientRea = this.findPatientRea();
		if (patientRea == null)
			throw new PatientDataException("请先保存病人数据才能添加此类型数据！");
		List<RemoteEntityAgent> listOldRea = this.findReaListByPdType(PatientDataType.PATIENT_ORG_MAPPER_DA);
		if (CollectionTools.isEmpty(listOldRea))
			return this.addData(patientRea.getUid(), entity);
		else
			return this.updateData(listOldRea.get(0).getUid(), entity);
	}

	public RemoteEntityAgent saveSeriesData(String parentUid, TSeries series) throws PatientDataException {
		if (series == null)
			throw new PatientDataException("要添加的实体数据必须指定！");
		RemoteEntityAgent oldSeriesRea;
		try {
			oldSeriesRea = this.findSeriesReaByMarkChar(series.getMark_char());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new PatientDataException(e);
		}
		if (oldSeriesRea == null)
			return this.addData(parentUid, series);
		else
			return this.updateData(oldSeriesRea.getUid(), series);
	}

	private RemoteEntityAgent findSeriesReaByMarkChar(String mark_char) throws CloneNotSupportedException {
		if (StringTools.isEmpty(mark_char))
			return null;
		List<RemoteEntityAgent> listRea = this.findReaListByPdType(PatientDataType.DICOM_IMG_SERIES);
		if (CollectionTools.isEmpty(listRea))
			return null;
		for (RemoteEntityAgent remoteEntityAgent : listRea) {
			if (mark_char.equals(((TSeries) remoteEntityAgent.getEntity()).getMark_char()))
				return remoteEntityAgent;
		}
		return null;
	}

	public RemoteEntityAgent saveDicomImgData(String parentUid, TDicomImg dicomImg) throws PatientDataException {
		if (dicomImg == null)
			throw new PatientDataException("要添加的实体数据必须指定！");
		RemoteEntityAgent oldDicomImgRea;
		try {
			oldDicomImgRea = this.findDicomImgReaByMarkChar(dicomImg.getMark_char());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new PatientDataException(e);
		}
		if (oldDicomImgRea == null)
			return this.addData(parentUid, dicomImg);
		else
			return this.updateData(oldDicomImgRea.getUid(), dicomImg);
	}

	private RemoteEntityAgent findDicomImgReaByMarkChar(String mark_char) throws CloneNotSupportedException {
		if (StringTools.isEmpty(mark_char))
			return null;
		List<RemoteEntityAgent> listRea = this.findReaListByPdType(PatientDataType.DICOM_IMG);
		if (CollectionTools.isEmpty(listRea))
			return null;
		for (RemoteEntityAgent remoteEntityAgent : listRea) {
			if (mark_char.equals(((TDicomImg) remoteEntityAgent.getEntity()).getMark_char()))
				return remoteEntityAgent;
		}
		return null;
	}

	/**
	 * 保存病人病例数据
	 * 
	 * @param entity
	 * @return
	 * @throws PatientDataException
	 * @throws CloneNotSupportedException
	 */
	public RemoteEntityAgent saveCaseHistoryData(TCaseHistory entity) throws PatientDataException {
		if (entity == null)
			throw new PatientDataException("要添加的实体数据必须指定！");
		RemoteEntityAgent patientRea = this.findPatientRea();
		if (patientRea == null)
			throw new PatientDataException("请先保存病人数据才能添加此类型数据！");
		RemoteEntityAgent oldRea;
		try {
			oldRea = this.findCaseHistoryReaByCaseHistory(entity);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new PatientDataException(e);
		}
		if (oldRea==null)
			return this.addData(patientRea.getUid(), entity);
		else
			return this.updateData(oldRea.getUid(), entity);
	}

	public RemoteEntityAgent findPatientRea() {
		List<RemoteEntityAgent> listPatientRea = this.findReaListByPdType(PatientDataType.PATIENT_DA);
		return CollectionTools.isEmpty(listPatientRea) ? null : listPatientRea.get(0);
	}

	/**
	 * 更新数据管理器中的数据
	 * 
	 * @param esType
	 *            更新数据的系统类型
	 * @param entity
	 *            要更新的数据的实体对象
	 * @param uid
	 *            要更新的数据的UID
	 * @return 返回是否更新成功，当优先级不低于已有数据才会更新成功
	 * @throws PatientDataException
	 * @throws DataPriorityException
	 */
	private RemoteEntityAgent updateData(String uid, AbstractRemoteEntity entity) throws PatientDataException {
		if (entity == null)
			throw new PatientDataException("要添加的实体数据必须指定！");
		if (entity.isCopy())
			throw new PatientDataException("副本不能再次保存！");
		RemoteEntityAgent rea = this.mapRea.get(uid);
		if (rea == null)
			throw new PatientDataException("要更新的数据不存在，请检查UID是否正确！");
		if (this.priorityController.comparePriority(entity.getSystemType(), rea.getSystemType(),
				entity.getPdType()) > 0) {
			rea.merge(entity);
			return rea;
		}
		rea.replace(entity);
		return rea;
	}

	/**
	 * 获取备份的一个数据列表
	 */
	public List<RemoteEntityAgent> findReaListByPdType(PatientDataType pdType) {
		List<RemoteEntityAgent> listRea = new ArrayList<RemoteEntityAgent>();
		for (Iterator<RemoteEntityAgent> iterator = this.mapRea.values().iterator(); iterator.hasNext();) {
			RemoteEntityAgent rea = (RemoteEntityAgent) iterator.next();
			if (pdType.equals(rea.getPdType()))
				listRea.add(rea);
		}
		return listRea;
	}

	public List<RemoteEntityAgent> findSubReaListByPdType(String parentUid, PatientDataType pdType) {
		List<RemoteEntityAgent> result = new ArrayList<RemoteEntityAgent>();
		for (Iterator<RemoteEntityAgent> iterator = this.mapRea.values().iterator(); iterator.hasNext();) {
			RemoteEntityAgent rea = (RemoteEntityAgent) iterator.next();
			if (rea.getParentUid().equals(parentUid) && rea.getPdType().equals(pdType))
				result.add(rea);
		}
		return result;
	}

	public RemoteEntityAgent findReaByUid(String uid) {
		return this.mapRea.get(uid);
	}

	/** 通过病历号和病例类型查询病例实体代理 */
	public RemoteEntityAgent findCaseHistoryReaByCaseHisNum(String case_his_num, CaseHistoryType caseHistoryType)
			throws CloneNotSupportedException {
		if (StringTools.isEmpty(case_his_num) || caseHistoryType == null)
			return null;
		if (caseHistoryType.equals(CaseHistoryType.OTHER))
			return null;
		return this.findCaseHistoryReaByCaseHisNum(case_his_num, caseHistoryType, null);
	}

	/** 通过病历号和病例类型或者病例来源类型查询病例实体代理 */
	public RemoteEntityAgent findCaseHistoryReaByCaseHisNum(String case_his_num, CaseHistoryType caseHistoryType,
			PatientDataSourceType sourceType) throws CloneNotSupportedException {
		if (StringTools.isEmpty(case_his_num) || caseHistoryType == null)
			return null;
		for (Iterator<RemoteEntityAgent> iterator = this.mapRea.values().iterator(); iterator.hasNext();) {
			RemoteEntityAgent rea = (RemoteEntityAgent) iterator.next();
			if (rea.getPdType() != PatientDataType.CASE_HIS)
				continue;
			TCaseHistory caseHistory = (TCaseHistory) rea.getEntity();
			if (!case_his_num.equalsIgnoreCase(caseHistory.getCase_his_num()))
				continue;
			if (caseHistoryType.getCode() != caseHistory.getType())
				continue;
			if (!caseHistoryType.equals(CaseHistoryType.OTHER))
				return rea;
			if (sourceType == null)
				return null;
			if (sourceType.equals(caseHistory.getSystemType().getSource_type()))
				return rea;
		}
		return null;
	}

	public RemoteEntityAgent findPatientOrgMapperByCardNum(String card_no) throws CloneNotSupportedException {
		if (StringTools.isEmpty(card_no))
			return null;
		for (Iterator<RemoteEntityAgent> iterator = this.mapRea.values().iterator(); iterator.hasNext();) {
			RemoteEntityAgent rea = (RemoteEntityAgent) iterator.next();
			if (rea.getPdType() != PatientDataType.PATIENT_ORG_MAPPER_DA)
				continue;
			TPatientOrgMapper patientOrgMapper = (TPatientOrgMapper) rea.getEntity();
			if (card_no.equalsIgnoreCase(patientOrgMapper.getCard_num()))
				return rea;
		}
		return null;
	}

	public RemoteEntityAgent findPatientByIdentityId(String identity_id) throws CloneNotSupportedException {
		if (StringTools.isEmpty(identity_id))
			return null;
		for (Iterator<RemoteEntityAgent> iterator = this.mapRea.values().iterator(); iterator.hasNext();) {
			RemoteEntityAgent rea = (RemoteEntityAgent) iterator.next();
			if (rea.getPdType() != PatientDataType.PATIENT_DA)
				continue;
			TPatient patient = (TPatient) rea.getEntity();
			if (identity_id.equalsIgnoreCase(patient.getIdentity_id()))
				return rea;
		}
		return null;
	}

	/**
	 * 将已经关联好的数据组装成结果集
	 * 
	 * @param priorityController
	 * @param searchResult
	 * @return
	 * @throws PatientDataException
	 */
	public static RemoteResult organizeResult(PriorityController priorityController, SearchResult searchResult)
			throws PatientDataException {
		if (searchResult.isEmpty())
			throw new PatientDataException("整理数据时，没有数据需要处理！");
		RemoteResult remoteResult = new RemoteResult(priorityController);
		List<ExternalSystemType> listExternalSystemType = ExternalSystemType.getAll();
		for (ExternalSystemType externalSystemType : listExternalSystemType) {
			Iterator<AbstractSystemEntity> listAbstractSystemEntityIterator = searchResult
					.getIterator(externalSystemType);
			if (listAbstractSystemEntityIterator == null)
				continue;
			w: while (listAbstractSystemEntityIterator.hasNext()) {
				AbstractSystemEntity abstractSystemEntity = (AbstractSystemEntity) listAbstractSystemEntityIterator
						.next();
				if (abstractSystemEntity == null)
					continue w;
				abstractSystemEntity.organizeEntity(remoteResult);
			}
		}
		return remoteResult;
	}

	public RemoteEntityAgent findCaseHistoryReaByCaseHistory(TCaseHistory caseHistory)
			throws CloneNotSupportedException {
		return this.findCaseHistoryReaByCaseHisNum(caseHistory.getCase_his_num(),
				CaseHistoryType.parseCode(caseHistory.getType()), caseHistory.getSystemType().getSource_type());
	}
}
