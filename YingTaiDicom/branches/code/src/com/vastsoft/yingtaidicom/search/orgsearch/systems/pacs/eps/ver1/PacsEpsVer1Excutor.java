package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.collection.UniqueList;
import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.FileTools;
import com.vastsoft.util.common.SplitStringBuilder;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.assist.AbstractSystemEntity;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.assist.SearchImageParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.config.PacsEpsVer1Mapper;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Image;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.PacsObject;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Patient;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Series;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Study;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.assist.WPacsStudySearchParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.config.WPacsDataBaseMapper;

public class PacsEpsVer1Excutor extends ExternalSystemSearchExecutor {
	private String org_ae_num;
	private List<String> thumbnail_directory;
	private List<String> thumbnail_suffix;

	public PacsEpsVer1Excutor(SystemIdentity systemIdentity, SessionFactory factory, String org_ae_num,
			String thumbnail_directory, String thumbnail_suffix) {
		super(systemIdentity, factory);
		SplitStringBuilder<String> ssb = new SplitStringBuilder<String>("','",
				StringTools.splitStrAsList(org_ae_num, ',','，'));
		if (ssb.isEmpty())
			throw new RuntimeException("必须指定AE号！");
		this.org_ae_num = "'" + ssb.toString() + "'";
		this.thumbnail_directory = StringTools.splitStrAsList(thumbnail_directory, ',');
		this.thumbnail_suffix = StringTools.splitStrAsList(thumbnail_suffix, ',');
	}

	@Override
	public boolean canSearch(RemoteParamsType paramType) {
		LoggerUtils.logger.info("通过参数类型在PACS-EPS-V1中确认是否可以查询！参数类型：" + String.valueOf(paramType));
		if (RemoteParamsType.PASC_NUM.equals(paramType))
			return true;
		if (RemoteParamsType.EPS_NUM.equals(paramType))
			return true;
		// if (RemoteParamsType.PASC_NUM.equals(paramType))
		// return true;
		return false;
	}

	@Override
	public void searchLastCaseNums(LastCaseNums result,RemoteParamsManager remoteParamsManager) throws SearchExecuteException {
		SqlSession session = null;
		try {
			session = super.getSession();
			PacsEpsVer1Mapper mapper = session.getMapper(PacsEpsVer1Mapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("source_ae", this.org_ae_num);
			List<Patient> listPatient = mapper.selectLastPatient(mapArg);
			for (Patient patient : listPatient) {
				String ptnId = patient.getPtn_id();
				if (StringTools.isEmpty(ptnId))
					continue;
				ptnId = ptnId.trim();
				result.add(RemoteParamsType.PASC_NUM, ptnId, (patient.getPtn_name() == null ? "" : patient.getPtn_name().trim()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		} finally {
			super.closeSession(session);
		}
	}

	@Override
	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid)
			throws SearchExecuteException {
		if (StringTools.isEmpty(thumbnail_uid))
			return null;
		SqlSession session = null;
		try {
			session = super.getSession();
			PacsEpsVer1Mapper mapper = session.getMapper(PacsEpsVer1Mapper.class);
			List<Image> listImage = this.searchImage(new SearchImageParam(thumbnail_uid.trim()), mapper);
			if (CollectionTools.isEmpty(listImage))
				return null;
			for (Image image : listImage) {
				Date date = DateTools.strToDate(String.valueOf(image.getRcvd_date()));
				String year = "img" + DateTools.get(Calendar.YEAR, date) + "";
				String monthDay = DateTools.MonthDayToStr(date);
				String fileName = FileTools.takeFileName(image.getImage_file());
				thumbnail_uid = image.getInstance_uid();
				for (String strDir : this.thumbnail_directory) {
					try {
						ts: for (String strSuffix : this.thumbnail_suffix) {
							try {
								String filePath = strDir + "/" + year + '/' + monthDay + "/"
										+ FileTools.takeFIleNameNoSuffixUnCheck(fileName) + "." + strSuffix;
								if (!FileTools.isFileExsit(filePath))
									continue ts;
								File file = new File(filePath);
								long fileSize = file.length();
								if (fileSize <= 0)
									continue ts;
								if (fileSize > (10 * 1024 * 1024))
									continue ts;
								LoggerUtils.logger.info("发现缩略图文件：" + filePath);
								byte[] dataBt = new byte[(int) fileSize];
								FileInputStream in = new FileInputStream(file);
								try {
									in.read(dataBt);
									return dataBt;
								} finally {
									in.close();
								}
							} catch (Exception e) {
							}
						}
					} catch (Exception e) {
					}
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		} finally {
			super.closeSession(session);
		}
	}

	private PacsObject searchPacsObjectByEpsNum(String eps_num)
			throws IOException, IllegalArgumentException, IllegalAccessException {
		if (StringTools.isEmpty(eps_num))
			return null;
		SqlSession session = null;
		try {
			session = this.getSession();
			PacsEpsVer1Mapper mapper = session.getMapper(PacsEpsVer1Mapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("ptn_id", eps_num);
			mapArg.put("source_ae", this.org_ae_num);
			List<Patient> listPatient = mapper.selectPatientByPtnId(mapArg);
			if (CollectionTools.isEmpty(listPatient))
				return null;
			PacsObject result = new PacsObject(new RemoteParamEntry(RemoteParamsType.PASC_NUM, eps_num),
					this.getSystemIdentity());
			result.setPatient(listPatient.get(0));
			List<Study> listStudy = mapper.selectStudyByPtn_id_id(result.getPatient().getPtn_id_id());
			if (CollectionTools.isEmpty(listStudy))
				return null;
			result.setStudy(listStudy.get(0));
			List<Series> listSeries = mapper.selectSeriesByStudy_uid_id(result.getStudy().getStudy_uid_id());
			if (!CollectionTools.isEmpty(listSeries)) {
				for (Series series : listSeries) {
					SearchImageParam sip = new SearchImageParam();
					sip.setSeries_uid_id(series.getSeries_uid_id());
					sip.setSource_ae(this.org_ae_num);
					sip.setSpu(new SplitPageUtil(1, 10));
					rep1:do {
						List<Image> listImage = this.searchImage(sip, mapper);
						if (CollectionTools.isEmpty(listImage)) {
							series.setExpose_times(0);
							series.setSource_ae("");
						} else {
							series.setExpose_times((int) sip.getSpu().getTotalRow());
							for (Image image : listImage) {
								if (!StringTools.isEmpty(image.getSource_ae()))
									series.setSource_ae(image.getSource_ae().trim());
								Date date = DateTools.strToDate(String.valueOf(image.getRcvd_date()));
								String year = "img" + DateTools.get(Calendar.YEAR, date) + "";
								String monthDay = DateTools.MonthDayToStr(date);
								String fileName = FileTools.takeFileName(image.getImage_file());
								String thumbnail_uid = image.getInstance_uid();
								if (StringTools.isEmpty(thumbnail_uid)
										|| ArrayTools.isEmpty(series.getThumbnail_data())) {
									series.setThrumbnail_uid(thumbnail_uid);
									for (String strDir : this.thumbnail_directory) {
										ts: for (String strSuffix : this.thumbnail_suffix) {
											String filePath = strDir + "/" + year + '/' + monthDay + "/"
													+ FileTools.takeFIleNameNoSuffixUnCheck(fileName) + "." + strSuffix;
											if (!FileTools.isFileExsit(filePath))
												continue ts;
											File file = new File(filePath);
											long fileSize = file.length();
											if (fileSize <= 0)
												continue ts;
											if (fileSize > (10 * 1024 * 1024))
												continue ts;
											LoggerUtils.logger.info("发现缩略图文件：" + filePath);
											byte[] dataBt = new byte[(int) fileSize];
											FileInputStream in = new FileInputStream(file);
											try {
												in.read(dataBt);
												series.setThumbnail_data(dataBt);
												break rep1;
											} finally {
												in.close();
											}
										}
									}
								}
							}
						}
					} while (sip.getSpu().nextPage());
				}
			}
			result.getStudy().addSeries(listSeries);
			return result;
		} finally {
			this.closeSession(session);
		}
	}

	private List<Image> searchImage(SearchImageParam sip, PacsEpsVer1Mapper mapper)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapArg = sip.buildMap();
		if (sip.getSpu() != null) {
			int count = mapper.selectImageCount(mapArg);
			sip.getSpu().setTotalRow(count);
			if (count <= 0)
				return null;
			mapArg.put("minRow", sip.getSpu().getCurrMinRowNum());
			mapArg.put("maxRow", sip.getSpu().getCurrMaxRowNum());
		}
		return mapper.selectImage(mapArg);
	}

	private PacsObject findPacsObjectByEpsNum(SearchResult searchResult, final String eps_num)
			throws CloneNotSupportedException {
		if (StringTools.isEmpty(eps_num))
			return null;
		Iterator<AbstractSystemEntity> iterator = searchResult.getIterator(ExternalSystemType.PACS);
		if (iterator == null)
			return null;
		while (iterator.hasNext()) {
			AbstractSystemEntity abstractSystemEntity = (AbstractSystemEntity) iterator.next();
			if (abstractSystemEntity == null)
				continue;
			PacsObject objTmp = (PacsObject) abstractSystemEntity;
			if (objTmp.getPatient() == null)
				continue;
			if (eps_num.equalsIgnoreCase(objTmp.getPatient().getPtn_id()))
				return objTmp;
		}
		return null;
	}

	@Override
	public void searchPatientData(final SearchResult searchResult, RemoteParamsManager remoteParamsManager)
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
				if (RemoteParamsType.PASC_NUM.equals(remoteParamEntry.getRemoteParamsType())
						|| RemoteParamsType.EPS_NUM.equals(remoteParamEntry.getRemoteParamsType())) {
					PacsObject pacsObject = findPacsObjectByEpsNum(searchResult,
							remoteParamEntry.getRemoteParamValue());
					if (pacsObject != null)
						continue;
					pacsObject = this.searchPacsObjectByEpsNum(remoteParamEntry.getRemoteParamValue());
					if (pacsObject != null)
						searchResult.saveObject(getSystemIdentity(), pacsObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		}
	}

	@Override
	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(RemoteParamsManager remoteParamsManager, String patient_name, String identify_id)
			throws SearchExecuteException {
		return null;
//		if (StringTools.isEmpty(patient_name))
//			return null;
//		SqlSession session = super.getSession();
//		try {
//			PacsEpsVer1Mapper mapper = session.getMapper(PacsEpsVer1Mapper.class);
//			WPacsStudySearchParam wpacsSSP = new WPacsStudySearchParam(this.source_application_entity_titles);
//			wpacsSSP.setPname(patient_name);
//			List<Study> listStudy;
//			try {
//				listStudy = mapper.selectStudy(wpacsSSP.buildMap());
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				e.printStackTrace();
//				throw new SearchExecuteException(e);
//			}
//			if (CollectionTools.isEmpty(listStudy))
//				return null;
//			List<PatientInfoResult> result = new UniqueList<PatientInfoResult>();
//			for (Study study : listStudy) {
//				TPatient patient = study.takePatient(this.getSystemIdentity());
//				result.add(new PatientInfoResult(patient, RemoteParamsType.PASC_NUM, study.getPid()));
//			}
//			return result;
//		} finally {
//			super.closeSession(session);
//		}
	}
}
