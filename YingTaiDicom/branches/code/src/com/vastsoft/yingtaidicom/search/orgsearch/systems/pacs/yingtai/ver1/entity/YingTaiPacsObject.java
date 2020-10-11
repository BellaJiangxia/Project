package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.common.constants.Gender;
import com.vastsoft.yingtaidicom.search.assist.AbstractSystemEntity;
import com.vastsoft.yingtaidicom.search.assist.RemoteEntityAgent;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryState;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryStatus;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryType;
import com.vastsoft.yingtaidicom.search.constants.DicomImgStatus;
import com.vastsoft.yingtaidicom.search.entity.TCaseHistory;
import com.vastsoft.yingtaidicom.search.entity.TDicomImg;
import com.vastsoft.yingtaidicom.search.entity.TPatientOrgMapper;
import com.vastsoft.yingtaidicom.search.entity.TSeries;
import com.vastsoft.yingtaidicom.search.assist.RemoteResult;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public class YingTaiPacsObject extends AbstractSystemEntity {
	private TPatient patient;

	public YingTaiPacsObject(RemoteParamEntry remoteParamEntry, SystemIdentity systemIdentity) {
		super(remoteParamEntry, systemIdentity);
	}

	@Override
	protected void organize(RemoteResult remoteResult) throws PatientDataException {
		if (this.patient == null)
			return;
		com.vastsoft.yingtaidicom.search.entity.TPatient pPatient = new com.vastsoft.yingtaidicom.search.entity.TPatient(
				super.getSystemIdentity());
		pPatient.setBirthday(
				DateTools.strToDate(this.patient.getPatient_birthdate() + (this.patient.getPatient_birth_time() == null
						? "" : this.patient.getPatient_birth_time().toString())));
		pPatient.setCreate_time(this.patient.getCreate_time());
		pPatient.setGender(Gender.parseString(this.patient.getPatient_sex(), Gender.MALE).getCode());
		pPatient.setMarital_status("未知");
		pPatient.setMingzu("未知");
		pPatient.setName(this.patient.getPatient_name());
		pPatient.setWork("未知");
		remoteResult.savePatientData(pPatient);
		TPatientOrgMapper patientOrgMapper = new TPatientOrgMapper(super.getSystemIdentity());
		patientOrgMapper.setGot_card_time(new Date());
		remoteResult.savePatientOrgMapperData(patientOrgMapper);
		
		List<TStudy> listStudy = this.patient.getListStudy();
		if (CollectionTools.isEmpty(listStudy))
			return;
		for (TStudy tStudy : listStudy) {
			TCaseHistory caseHistory = new TCaseHistory(super.getSystemIdentity());
			caseHistory.setCase_his_num(this.patient.getPatient_id());
			caseHistory.setType(CaseHistoryType.OTHER.getCode());
			Date create_time = DateTools.strToDate(tStudy.getStudy_date(), new Date());
			caseHistory.setCreate_time(create_time);
			caseHistory.setEnter_time(create_time);
			caseHistory.setLeave_time(create_time);
			caseHistory.setState(CaseHistoryState.NORMAL.getCode());
			caseHistory.setStatus(CaseHistoryStatus.NORMAL.getCode());
			RemoteEntityAgent caseHistoryRea = remoteResult.saveCaseHistoryData(caseHistory);
			TDicomImg dicomImg = new TDicomImg(getSystemIdentity());
			dicomImg.setAe_title(tStudy.getSpecific_character_set());
			dicomImg.setCheck_time(create_time);
			dicomImg.setPatient_id(patient.getPatient_id());
			dicomImg.setMark_char(tStudy.getStudy_instance_uid());
			dicomImg.setStatus(DicomImgStatus.NORMAL.getCode());
			if (CollectionTools.isEmpty(tStudy.getSerieses()))
				throw new PatientDataException("没有找到此影像的序列");
			dicomImg.setDevice_name(tStudy.getSerieses().get(0).getModality());
			dicomImg.setChecklist_num(tStudy.getAccession_number());
			RemoteEntityAgent dicomImgRea = remoteResult.saveDicomImgData(caseHistoryRea.getUid(),dicomImg);
			
			List<com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TSeries> listSeries = tStudy.getSerieses();
			if (CollectionTools.isEmpty(listSeries))
				continue;
			for (com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TSeries series : listSeries) {
				TSeries tSeries = new TSeries(super.getSystemIdentity());
				tSeries.setPart_name(StringTools.isEmpty(series.getBody_part_examined())
						? (StringTools.isEmpty(series.getProtocol_name()) ? "未指定" : series.getProtocol_name().trim())
						: series.getBody_part_examined().trim());
				tSeries.setMark_char(series.getSeries_instance_uid());
				tSeries.setThumbnail_uid(series.getThrumbnail_uid());
				tSeries.setThumbnail_data(series.getThumbnail_data());
				tSeries.setExpose_times(series.getExpose_times());
				remoteResult.saveSeriesData(dicomImgRea.getUid(), tSeries);
			}
		}
	}

	public TPatient getPatient() {
		return patient;
	}

	public void setPatient(TPatient patient) {
		this.patient = patient;
	}

}
