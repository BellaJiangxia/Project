package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.constants.Gender;
import com.vastsoft.yingtaidicom.search.assist.AbstractSystemEntity;
import com.vastsoft.yingtaidicom.search.assist.RemoteEntityAgent;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.assist.RemoteResult;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryState;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryStatus;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryType;
import com.vastsoft.yingtaidicom.search.constants.DicomImgStatus;
import com.vastsoft.yingtaidicom.search.entity.TCaseHistory;
import com.vastsoft.yingtaidicom.search.entity.TDicomImg;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.entity.TPatientOrgMapper;
import com.vastsoft.yingtaidicom.search.entity.TSeries;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public class PacsObject extends AbstractSystemEntity {
	private Patient patient;
	private Study study;

	public PacsObject(RemoteParamEntry remoteParamEntry, SystemIdentity systemIdentity) {
		super(remoteParamEntry, systemIdentity);
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	@Override
	protected void organize(RemoteResult remoteResult) throws PatientDataException {
		if (patient == null)
			return;
		if (study == null)
			return;
		TPatient resultPatient = new TPatient(this.getSystemIdentity());
		resultPatient.setBirthday(DateTools.strToDate(patient.getBirth_date() + ""));
		resultPatient.setName(patient.getPtn_name());
		resultPatient.setCreate_time(new Date());
		resultPatient.setGender(Gender.parseString(patient.getSex(),Gender.MALE).getCode());
		remoteResult.savePatientData(resultPatient);
		TPatientOrgMapper patientOrgMapper = new TPatientOrgMapper(getSystemIdentity());
		patientOrgMapper.setGot_card_time(new Date());
		remoteResult.savePatientOrgMapperData(patientOrgMapper);
		
		TCaseHistory caseHistory = new TCaseHistory(getSystemIdentity());
		caseHistory.setCase_his_num(this.patient.getPtn_id());
		caseHistory.setType(CaseHistoryType.OTHER.getCode());
		Date create_time = DateTools.strToDate(this.study.getStudy_date() + "", new Date());
		caseHistory.setCreate_time(create_time);
		caseHistory.setEnter_time(create_time);
		caseHistory.setLeave_time(create_time);
		caseHistory.setState(CaseHistoryState.NORMAL.getCode());
		caseHistory.setStatus(CaseHistoryStatus.NORMAL.getCode());
		RemoteEntityAgent caseHistoryRea = remoteResult.saveCaseHistoryData(caseHistory);
		TDicomImg dicomImg = new TDicomImg(getSystemIdentity());
		dicomImg.setAe_title(study.getAe_title());
		dicomImg.setCheck_time(create_time);
		dicomImg.setPatient_id(patient.getPtn_id());
		dicomImg.setMark_char(study.getStudy_uid());
		dicomImg.setStatus(DicomImgStatus.NORMAL.getCode());
		dicomImg.setDevice_name(study.getDevice_name());
		dicomImg.setChecklist_num(study.getAccession_number());
		RemoteEntityAgent dicomImgRea = remoteResult.saveDicomImgData(caseHistoryRea.getUid(),dicomImg);
		List<TSeries> listSeries = study.organize(this.getSystemIdentity());
		if (!CollectionTools.isEmpty(listSeries)) {
			for (TSeries tSeries : listSeries) {
				remoteResult.saveSeriesData(dicomImgRea.getUid(), tSeries);
			}
		}
	}

}
