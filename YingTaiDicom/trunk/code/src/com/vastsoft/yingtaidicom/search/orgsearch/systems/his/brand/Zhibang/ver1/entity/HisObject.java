package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.list.SetUniqueList;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.common.constants.Gender;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.search.assist.AbstractSystemEntity;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.assist.RemoteResult;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryType;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.entity.TCaseHistory;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.entity.TPatientOrgMapper;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public class HisObject extends AbstractSystemEntity {
	@SuppressWarnings("unchecked")
	private List<B_BASY> listB_BASY = SetUniqueList.decorate(new ArrayList<B_BASY>());
	@SuppressWarnings("unchecked")
	private List<Z_BAH> listZ_BAH = SetUniqueList.decorate(new ArrayList<Z_BAH>());
	@SuppressWarnings("unchecked")
	private List<M_BRJBXX> listM_BRJBXX = SetUniqueList.decorate(new ArrayList<M_BRJBXX>());

	public HisObject(RemoteParamEntry remoteParamEntry, SystemIdentity systemIdentity) {
		super(remoteParamEntry, systemIdentity);
	}

	public List<B_BASY> getListB_BASY() {
		return listB_BASY;
	}

	public void addB_BASY(List<B_BASY> listB_BASY) {
		if (CollectionTools.isEmpty(listB_BASY))
			return;
		this.listB_BASY.addAll(listB_BASY);
	}

	public List<Z_BAH> getListZ_BAH() {
		return listZ_BAH;
	}

	public void addZ_BAH(List<Z_BAH> listZ_BAH) {
		if (CollectionTools.isEmpty(listZ_BAH))
			return;
		this.listZ_BAH.addAll(listZ_BAH);
	}

	public List<M_BRJBXX> getListM_BRJBXX() {
		return listM_BRJBXX;
	}

	public void addM_BRJBXX(List<M_BRJBXX> listM_BRJBXX) {
		if (CollectionTools.isEmpty(listM_BRJBXX))
			return;
		this.listM_BRJBXX.addAll(listM_BRJBXX);
	}

	@Override
	protected void organize(RemoteResult remoteResult) throws PatientDataException {
		LoggerUtils.logger.info("开始整理HIS数据，包含：M_BRJBXX数据" + (listM_BRJBXX == null ? 0 : listM_BRJBXX.size())
				+ "条；Z_BAH数据" + (listZ_BAH == null ? 0 : listZ_BAH.size()) + "条；B_BASY数据"
				+ (listB_BASY == null ? 0 : listB_BASY.size()) + "条。");
		if (!CollectionTools.isEmpty(listM_BRJBXX)) {
			for (M_BRJBXX m_BRJBXX : listM_BRJBXX) {
				TPatient patient = new TPatient(getSystemIdentity());
				patient.setBirthday(m_BRJBXX.getM_csny());
				patient.setBorn_address(m_BRJBXX.getM_zz());
				patient.setCreate_time(m_BRJBXX.getCreate_date());
				patient.setGender(Gender.parseString(m_BRJBXX.getM_xbbm(), Gender.MALE).getCode());
				patient.setHome_address(m_BRJBXX.getM_zz());
				patient.setIdentity_id(m_BRJBXX.getM_sfzh());
				patient.setContact_addr(m_BRJBXX.getM_zz());
				patient.setContact_name(m_BRJBXX.getM_brxm());
				patient.setContact_phone_num(m_BRJBXX.getM_lxdh());
				patient.setContact_rela("未知");
				patient.setName(m_BRJBXX.getM_brxm());
				patient.setPhone_num(m_BRJBXX.getM_lxdh());
				patient.setWork(m_BRJBXX.getM_gzdw());
				patient.setMingzu(m_BRJBXX.getM_mz_name());
				patient.setMarital_status("未知");
				remoteResult.savePatientData(patient);

				TPatientOrgMapper patientOrgMapper = new TPatientOrgMapper(getSystemIdentity());
				patientOrgMapper.setCard_num(m_BRJBXX.getCard_no());
				patientOrgMapper.setGot_card_time(m_BRJBXX.getCreate_date());
				remoteResult.savePatientOrgMapperData(patientOrgMapper);

				TCaseHistory caseHistory = new TCaseHistory(getSystemIdentity());
				caseHistory.setCase_his_num(m_BRJBXX.getJzxh());
				caseHistory.setCreate_time(m_BRJBXX.getRbrq());
				caseHistory.setDiagnosis(m_BRJBXX.getDiagnosis());
				caseHistory.setDoctor_advice(m_BRJBXX.getDoctor_say());
				caseHistory.setEnter_time(m_BRJBXX.getRbrq());
				caseHistory.setGuahao_type(m_BRJBXX.getM_ghmc());
				caseHistory.setHospitalized_num("");
				caseHistory.setLeave_time(m_BRJBXX.getRbrq());
				caseHistory.setPatient_say(
						StringTools.concat(m_BRJBXX.getZyxs(), m_BRJBXX.getZyxs2(), m_BRJBXX.getZyxs3()));
				caseHistory.setReception_doctor(m_BRJBXX.getYsxm());
				caseHistory.setReception_section(m_BRJBXX.getKsmc());
				caseHistory.setSymptom(m_BRJBXX.getCt());
				caseHistory.setType(CaseHistoryType.MENZHEN.getCode());
				caseHistory.setCheck_body(m_BRJBXX.getCt());
				remoteResult.saveCaseHistoryData(caseHistory);
			}
		}

		if (!CollectionTools.isEmpty(this.listZ_BAH)) {
			for (Z_BAH z_BAH : listZ_BAH) {
				TPatient patient = new TPatient(getSystemIdentity());
				patient.setBirthday(DateTools.strToDate(z_BAH.getZ_csny()));
				patient.setBorn_address(z_BAH.getZ_jtzz());
				patient.setCreate_time(z_BAH.getZ_ryrq());
				patient.setGender(Gender.parseString(z_BAH.getZ_xb(),Gender.MALE).getCode());
				patient.setHome_address(z_BAH.getZ_jtzz());
				patient.setIdentity_id(z_BAH.getZ_sfzh());
				patient.setContact_addr(z_BAH.getLxrdz());
				patient.setContact_name(z_BAH.getLxr());
				patient.setContact_phone_num(z_BAH.getLxrdh());
				patient.setContact_rela(z_BAH.getLxrgx());
				patient.setName(z_BAH.getZ_xm());
				patient.setPhone_num(z_BAH.getGzdwdh());
				patient.setWork("");
				patient.setMingzu(z_BAH.getMzbm_name());
				patient.setMarital_status("未知");
				remoteResult.savePatientData(patient);
				TPatientOrgMapper patientOrgMapper = new TPatientOrgMapper(getSystemIdentity());
				patientOrgMapper.setCard_num(z_BAH.getCard_no());
				patientOrgMapper.setGot_card_time(z_BAH.getZ_ryrq());
				remoteResult.savePatientOrgMapperData(patientOrgMapper);
				TCaseHistory caseHistory = new TCaseHistory(getSystemIdentity());
				caseHistory.setCase_his_num(z_BAH.getZ_bah());
				caseHistory.setCreate_time(z_BAH.getZ_ryrq());
				caseHistory.setDiagnosis(z_BAH.getZ_mzzd());
				caseHistory.setDoctor_advice("");
				caseHistory.setEnter_time(z_BAH.getZ_ryrq());
				caseHistory.setGuahao_type("");
				caseHistory.setHospitalized_num(z_BAH.getZ_bah());
				caseHistory.setLeave_time(z_BAH.getZ_cyrq());
				caseHistory.setPatient_say("");
				caseHistory.setReception_doctor(z_BAH.getYsbm_name());
				caseHistory.setReception_section(z_BAH.getZ_jsr());
				caseHistory.setSymptom(caseHistory.getDiagnosis());
				caseHistory.setType(CaseHistoryType.HOSPITALIZED.getCode());
				caseHistory.setCheck_body(z_BAH.getZ_mzzd());
				remoteResult.saveCaseHistoryData(caseHistory);
			}
		}
		if (!CollectionTools.isEmpty(this.listB_BASY)) {
			for (B_BASY b_BASY : listB_BASY) {
				TPatient patient = new TPatient(getSystemIdentity());
				patient.setBirthday(b_BASY.getCsny());
				patient.setBorn_address(b_BASY.getCsd());
				patient.setCreate_time(b_BASY.getRysj());
				patient.setGender(Gender.parseString(b_BASY.getXb(),Gender.MALE).getCode());
				patient.setHome_address(b_BASY.getHkdz());
				patient.setIdentity_id(b_BASY.getSfzh());
				patient.setContact_addr(b_BASY.getLxrdz());
				patient.setContact_name(b_BASY.getLxrxm());
				patient.setContact_phone_num(b_BASY.getLxrdh());
				patient.setContact_rela(b_BASY.getLxrgx());
				patient.setName(b_BASY.getXm());
				patient.setPhone_num(b_BASY.getHkdh());
				patient.setWork(b_BASY.getGzdw());
				patient.setMingzu(b_BASY.getMzbm_name());
				patient.setMarital_status(b_BASY.getHyzk() == null ? "未知"
						: (b_BASY.getHyzk().equals("1") ? "未婚" : (b_BASY.getHyzk().equals("2") ? "已婚" : "未知")));
				remoteResult.savePatientData(patient);
				TPatientOrgMapper patientOrgMapper = new TPatientOrgMapper(getSystemIdentity());
				patientOrgMapper.setCard_num("");
				patientOrgMapper.setGot_card_time(b_BASY.getRysj());
				remoteResult.savePatientOrgMapperData(patientOrgMapper);
				TCaseHistory caseHistory = new TCaseHistory(getSystemIdentity());
				caseHistory.setCase_his_num(b_BASY.getBah());
				caseHistory.setCreate_time(b_BASY.getRysj());
				caseHistory.setDiagnosis(b_BASY.getDiagnosis());
				caseHistory.setDoctor_advice("");
				caseHistory.setEnter_time(b_BASY.getRysj());
				caseHistory.setGuahao_type("");
				caseHistory.setHospitalized_num(b_BASY.getBah());
				caseHistory.setLeave_time(b_BASY.getCysj());
				caseHistory.setPatient_say("");
				caseHistory.setReception_doctor(b_BASY.getMzys_name());
				caseHistory.setReception_section(b_BASY.getRykb_name());
				caseHistory.setSymptom(b_BASY.getSymptom());
				caseHistory.setType(CaseHistoryType.HOSPITALIZED.getCode());
				caseHistory.setCheck_body(b_BASY.getSymptom());
				remoteResult.saveCaseHistoryData(caseHistory);
			}
		}
	}
	
	public static void tranfarZ_BAH(List<PatientInfoResult> listPatientInfoResult,List<Z_BAH> listZ_BAH,SystemIdentity systemIdentity){
		if (CollectionTools.isEmpty(listZ_BAH))
			return;
		for (Z_BAH z_BAH : listZ_BAH) {
			TPatient patient = new TPatient(systemIdentity);
			patient.setBirthday(DateTools.strToDate(z_BAH.getZ_csny()));
			patient.setBorn_address(z_BAH.getZ_jtzz());
			patient.setCreate_time(z_BAH.getZ_ryrq());
			patient.setGender(Gender.parseString(z_BAH.getZ_xb(),Gender.MALE).getCode());
			patient.setHome_address(z_BAH.getZ_jtzz());
			patient.setIdentity_id(z_BAH.getZ_sfzh());
			patient.setContact_addr(z_BAH.getLxrdz());
			patient.setContact_name(z_BAH.getLxr());
			patient.setContact_phone_num(z_BAH.getLxrdh());
			patient.setContact_rela(z_BAH.getLxrgx());
			patient.setName(z_BAH.getZ_xm());
			patient.setPhone_num(z_BAH.getGzdwdh());
			patient.setWork("");
			patient.setMingzu(z_BAH.getMzbm_name());
			patient.setMarital_status("未知");
			if (StringTools.wasIdentityId(patient.getIdentity_id()))
				listPatientInfoResult.add(new PatientInfoResult(patient, RemoteParamsType.IDENTITY_ID, patient.getIdentity_id()));
			if (!StringTools.isEmpty(z_BAH.getZ_bah()))
				listPatientInfoResult.add(new PatientInfoResult(patient, RemoteParamsType.BINGAN_NUM, z_BAH.getZ_bah()));
		}
	}

	public static void tranfarB_BASY(List<PatientInfoResult> listPatientInfoResult, List<B_BASY> listB_BASY,
			SystemIdentity systemIdentity) {
		if (CollectionTools.isEmpty(listB_BASY))
			return;
		for (B_BASY b_BASY : listB_BASY) {
			TPatient patient = new TPatient(systemIdentity);
			patient.setBirthday(b_BASY.getCsny());
			patient.setBorn_address(b_BASY.getCsd());
			patient.setCreate_time(b_BASY.getRysj());
			patient.setGender(Gender.parseString(b_BASY.getXb(),Gender.MALE).getCode());
			patient.setHome_address(b_BASY.getHkdz());
			patient.setIdentity_id(b_BASY.getSfzh());
			patient.setContact_addr(b_BASY.getLxrdz());
			patient.setContact_name(b_BASY.getLxrxm());
			patient.setContact_phone_num(b_BASY.getLxrdh());
			patient.setContact_rela(b_BASY.getLxrgx());
			patient.setName(b_BASY.getXm());
			patient.setPhone_num(b_BASY.getHkdh());
			patient.setWork(b_BASY.getGzdw());
			patient.setMingzu(b_BASY.getMzbm_name());
			patient.setMarital_status(b_BASY.getHyzk() == null ? "未知"
					: (b_BASY.getHyzk().equals("1") ? "未婚" : (b_BASY.getHyzk().equals("2") ? "已婚" : "未知")));
			if (StringTools.wasIdentityId(patient.getIdentity_id()))
				listPatientInfoResult.add(new PatientInfoResult(patient, RemoteParamsType.IDENTITY_ID, patient.getIdentity_id()));
			if (!StringTools.isEmpty(b_BASY.getBah()))
				listPatientInfoResult.add(new PatientInfoResult(patient, RemoteParamsType.BINGAN_NUM, b_BASY.getBah()));
		}
	}

	public static void tranfarM_BRJBXX(List<PatientInfoResult> listPatientInfoResult, List<M_BRJBXX> listM_BRJBXX,
			SystemIdentity systemIdentity) {
		if (CollectionTools.isEmpty(listM_BRJBXX))
			return;
		for (M_BRJBXX m_BRJBXX : listM_BRJBXX) {
			TPatient patient = new TPatient(systemIdentity);
			patient.setBirthday(m_BRJBXX.getM_csny());
			patient.setBorn_address(m_BRJBXX.getM_zz());
			patient.setCreate_time(m_BRJBXX.getCreate_date());
			patient.setGender(Gender.parseString(m_BRJBXX.getM_xbbm(), Gender.MALE).getCode());
			patient.setHome_address(m_BRJBXX.getM_zz());
			patient.setIdentity_id(m_BRJBXX.getM_sfzh());
			patient.setContact_addr(m_BRJBXX.getM_zz());
			patient.setContact_name(m_BRJBXX.getM_brxm());
			patient.setContact_phone_num(m_BRJBXX.getM_lxdh());
			patient.setContact_rela("未知");
			patient.setName(m_BRJBXX.getM_brxm());
			patient.setPhone_num(m_BRJBXX.getM_lxdh());
			patient.setWork(m_BRJBXX.getM_gzdw());
			patient.setMingzu(m_BRJBXX.getM_mz_name());
			patient.setMarital_status("未知");
			if (StringTools.wasIdentityId(patient.getIdentity_id()))
				listPatientInfoResult.add(new PatientInfoResult(patient, RemoteParamsType.IDENTITY_ID, patient.getIdentity_id()));
			if (!StringTools.isEmpty(m_BRJBXX.getCard_no()))
				listPatientInfoResult.add(new PatientInfoResult(patient, RemoteParamsType.DIAGNOSIS_CARD_NUM, m_BRJBXX.getCard_no()));
		}
	}
}
