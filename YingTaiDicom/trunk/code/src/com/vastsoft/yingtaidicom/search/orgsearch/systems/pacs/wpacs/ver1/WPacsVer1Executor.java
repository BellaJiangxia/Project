package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.ArrayTools;
import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.collection.UniqueList;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.MapBuilder;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.http.HttpConnection;
import com.vastsoft.util.io.IOTools;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.WPacsVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist.WPacsImageSearchParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist.WPacsSeriesSearchParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist.WPacsStudySearchParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.config.WPacsDataBaseMapper;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.Image;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.Series;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.Study;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.WPacsObject;

public class WPacsVer1Executor extends ExternalSystemSearchExecutor {
	private String[] source_application_entity_titles;
	private String wpacs_host;
	private int wpacs_port;

	public WPacsVer1Executor(SystemIdentity systemIdentity, SessionFactory factory,
			String source_application_entity_title, String wpacs_host, int wpacs_port) {
		super(systemIdentity, factory);
		this.source_application_entity_titles = StringTools.splitString(source_application_entity_title, ',', '，');
		this.wpacs_host = wpacs_host;
		this.wpacs_port = wpacs_port;
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
				if (!this.canSearch(remoteParamEntry.getRemoteParamsType()))
					continue;
				if (RemoteParamsType.PASC_NUM.equals(remoteParamEntry.getRemoteParamsType())) {
					WPacsObject pacsObject = (WPacsObject) searchResult
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

	/**
	 * 通过病人ID查询数据
	 * 
	 * @param patient_id
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SearchExecuteException
	 */
	private WPacsObject searchPacsObjectByPacsNum(String patient_id)
			throws IllegalArgumentException, IllegalAccessException, SearchExecuteException {
		if (StringTools.isEmpty(patient_id))
			return null;
		SqlSession session = super.getSession();
		try {
			WPacsDataBaseMapper mapper = session.getMapper(WPacsDataBaseMapper.class);
			WPacsStudySearchParam wpacsSSP = new WPacsStudySearchParam(this.source_application_entity_titles);
			wpacsSSP.setPid(patient_id);
			List<Study> listStudy = mapper.selectStudy(wpacsSSP.buildMap());
			if (CollectionTools.isEmpty(listStudy))
				return null;
			WPacsObject result = new WPacsObject(new RemoteParamEntry(RemoteParamsType.PASC_NUM, patient_id),
					super.getSystemIdentity());
			Study study = Study.listMerge(listStudy);
			result.setStudy(study);
			{
				WPacsSeriesSearchParam wpacsSsp = new WPacsSeriesSearchParam(this.source_application_entity_titles);
				wpacsSsp.setStuuid(study.getStuuid());
				List<Series> listSeries = mapper.selectSeries(wpacsSsp.buildMap());
				if (!CollectionTools.isEmpty(listSeries)) {
					for (Series tSeries : listSeries) {
						study.addSeries(tSeries);
						WPacsImageSearchParam wpacsIsp = new WPacsImageSearchParam(
								this.source_application_entity_titles);
						wpacsIsp.setSrsuid(tSeries.getSrsuid());
						wpacsIsp.setStuuid(tSeries.getStuuid());
						List<Image> listImage = mapper.selectImage(wpacsIsp.buildMap());
						if (!CollectionTools.isEmpty(listImage)) {
							fita:for (Image tImage : listImage) {
								byte[] thumbnail = this.readThumbnail(null, tImage.getImguid());
								tImage.setThumbnail(thumbnail);
								tSeries.addImage(tImage);
								if (!ArrayTools.isEmpty(thumbnail))
									break fita;
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
			WPacsDataBaseMapper mapper = session.getMapper(WPacsDataBaseMapper.class);
			WPacsStudySearchParam wpacsSSP = new WPacsStudySearchParam(this.source_application_entity_titles);
			wpacsSSP.setSpu(new SplitPageUtil(1, 30));
			List<Study> listStudy;
			try {
				listStudy = mapper.selectStudy(wpacsSSP.buildMap());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				throw new SearchExecuteException(e);
			}
			if (CollectionTools.isEmpty(listStudy))
				return;
			for (Study study : listStudy) {
				if (StringTools.isEmpty(study.getPid()))
					continue;
				result.add(RemoteParamsType.PASC_NUM, study.getPid(), StringTools.strTrimGreat(study.getPname()));
			}
		} finally {
			super.closeSession(session);
		}
	}

	@Override
	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid)
			throws SearchExecuteException {
		if (StringTools.isEmpty(thumbnail_uid))
			return null;
		try {
			// wpacs 使用wado获取
			Map<String, String> params = new MapBuilder<String, String>("requesttype", "WADO")
					.put("objectuid", thumbnail_uid).put("contenttype", "image/jpeg").put("columns", "200")
					.put("rows", "200").toMap();
			InputStream in = HttpConnection
					.request("http://" + this.wpacs_host + ":" + this.wpacs_port + "/webpacs/wpswado.aspx", params);
			return IOTools.streamToBytes(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vastsoft.yingtaidicom.search.orgsearch.interfase.
	 * ExternalSystemSearchExecutor#searchPatientInfoByPatientNameOrIdentifyId(
	 * com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(RemoteParamsManager remoteParamsManager,
			String patient_name, String identify_id) throws SearchExecuteException {
		if (StringTools.isEmpty(patient_name))
			return null;
		SqlSession session = super.getSession();
		try {
			WPacsDataBaseMapper mapper = session.getMapper(WPacsDataBaseMapper.class);
			WPacsStudySearchParam wpacsSSP = new WPacsStudySearchParam(this.source_application_entity_titles);
			wpacsSSP.setPname(patient_name);
			List<Study> listStudy;
			try {
				listStudy = mapper.selectStudy(wpacsSSP.buildMap());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				throw new SearchExecuteException(e);
			}
			if (CollectionTools.isEmpty(listStudy))
				return null;
			List<PatientInfoResult> result = new UniqueList<PatientInfoResult>();
			for (Study study : listStudy) {
				TPatient patient = study.takePatient(this.getSystemIdentity());
				result.add(new PatientInfoResult(patient, RemoteParamsType.PASC_NUM, study.getPid()));
			}
			return result;
		} finally {
			super.closeSession(session);
		}
	}

}
