package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.MapBuilder;
import com.vastsoft.util.common.SplitStringBuilder;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.config.YingTaiPacsDataBaseMapper;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TImage;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TPatient;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TSeries;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TStudy;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.YingTaiPacsObject;

public class YingTaiPacsVer1Executor extends ExternalSystemSearchExecutor {
	private String[] source_application_entity_titles;

	public YingTaiPacsVer1Executor(SystemIdentity systemIdentity, SessionFactory factory,
			String source_application_entity_title) {
		super(systemIdentity, factory);
		this.source_application_entity_titles = StringTools.splitString(source_application_entity_title, ',', '，');
	}

	@Override
	public boolean canSearch(RemoteParamsType paramType) {
		if (paramType == null)
			return false;
		if (paramType.equals(RemoteParamsType.PASC_NUM))
			return true;
		return false;
	}

	@Override
	public void searchPatientData(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException {
		Iterator<RemoteParamEntry> iterator = remoteParamsManager.getKeyIterator();
		if (iterator == null)
			return;
		try {
			while (iterator.hasNext()) {
				RemoteParamEntry remoteParamEntry = (RemoteParamEntry) iterator.next();
				if (remoteParamEntry == null)
					continue;
				if (!canSearch(remoteParamEntry.getRemoteParamsType()))
					continue;
				if (RemoteParamsType.PASC_NUM.equals(remoteParamEntry.getRemoteParamsType())) {
					YingTaiPacsObject pacsObject = (YingTaiPacsObject) searchResult
							.findEntityByRemoteParamEntry(this.getSystemIdentity().getSystemType(), remoteParamEntry);
					if (pacsObject != null)
						continue;
					pacsObject = this.searchPacsObjectByPacsNum(remoteParamEntry.getRemoteParamValue());
					if (pacsObject != null)
						searchResult.saveObject(getSystemIdentity(), pacsObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		}
	}

	private YingTaiPacsObject searchPacsObjectByPacsNum(String patient_id) {
		if (StringTools.isEmpty(patient_id))
			return null;
		SqlSession session = super.getSession();
		try {
			YingTaiPacsDataBaseMapper mapper = session.getMapper(YingTaiPacsDataBaseMapper.class);
			List<TPatient> listPatient = mapper
					.selectPatient(new MapBuilder<String, Object>("patient_id", patient_id)
							.put("source_application_entity_titles",
									"'" + SplitStringBuilder.splitToString(this.source_application_entity_titles, "','") + "'")
							.toMap());
			if (CollectionTools.isEmpty(listPatient))
				return null;
			YingTaiPacsObject result = new YingTaiPacsObject(
					new RemoteParamEntry(RemoteParamsType.PASC_NUM, patient_id), super.getSystemIdentity());
			result.setPatient(listPatient.get(0));
			{
				List<TStudy> listStudy = mapper.selectStudy(
						new MapBuilder<String, Object>("patient_uuid", result.getPatient().getUuid()).toMap());
				if (!CollectionTools.isEmpty(listStudy)) {
					for (TStudy tStudy : listStudy) {
						result.getPatient().addStudy(tStudy);
						List<TSeries> listSeries = mapper
								.selectSeries(new MapBuilder<String, Object>("study_uuid", tStudy.getUuid()).toMap());
						if (!CollectionTools.isEmpty(listSeries)) {
							for (TSeries tSeries : listSeries) {
								tStudy.addSeries(tSeries);
								List<TImage> listImage = mapper.selectImage(
										new MapBuilder<String, Object>("series_uuid", tSeries.getUuid()).toMap());
								if (!CollectionTools.isEmpty(listImage)) {
									for (TImage tImage : listImage) {
										tSeries.addImage(tImage);
									}
								}
							}
						}
					}
				}
			}
			return result;
		} finally {
			super.closeSession(session);
		}
	}

	@Override
	public void searchLastCaseNums(LastCaseNums result, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException {
		SqlSession session = super.getSession();
		try {
			YingTaiPacsDataBaseMapper mapper = session.getMapper(YingTaiPacsDataBaseMapper.class);
			Map<String, Object> mapArg = new MapBuilder<String, Object>().put("minRow", 0).put("rowCount", 30)
					.put("source_application_entity_titles",
							"'" + SplitStringBuilder.splitToString(this.source_application_entity_titles, "','") + "'")
					.toMap();
			List<TPatient> listPatient = mapper.selectPatient(mapArg);
			if (CollectionTools.isEmpty(listPatient))
				return;
			for (TPatient tPatient : listPatient)
				result.add(RemoteParamsType.PASC_NUM, tPatient.getPatient_id(), tPatient.getPatient_name());
		} finally {
			super.closeSession(session);
		}
	}

	@Override
	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid)
			throws SearchExecuteException {
		if (StringTools.isEmpty(thumbnail_uid))
			return null;
		SqlSession session = super.getSession();
		try {
			YingTaiPacsDataBaseMapper mapper = session.getMapper(YingTaiPacsDataBaseMapper.class);
			List<TImage> listImage = mapper
					.selectImage(new MapBuilder<String, Object>().put("sop_instance_uid", thumbnail_uid).toMap());
			if (CollectionTools.isEmpty(listImage))
				return null;
			return listImage.get(0).getThumbnail();
		} finally {
			super.closeSession(session);
		}
	}

	@Override
	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(
			RemoteParamsManager remoteParamsManager, String patient_name, String identify_id) throws SearchExecuteException {
		// TODO Auto-generated method stub
		return null;
	}

}
