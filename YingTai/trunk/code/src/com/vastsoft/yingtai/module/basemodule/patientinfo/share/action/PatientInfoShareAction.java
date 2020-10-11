package com.vastsoft.yingtai.module.basemodule.patientinfo.share.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.assist.SearchCaseHistoryParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImgNumObj;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteNumEntry;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.share.assist.PatientInfoShareGapEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.share.exception.PatientInfoShareException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.share.service.PatientInfoShareService;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationPatientInfoShareType;
import com.vastsoft.yingtai.module.org.realtion.exception.OrgRelationException;
import com.vastsoft.yingtai.module.org.realtion.service.OrgRelationService;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class PatientInfoShareAction extends BaseYingTaiAction {
	private Long relation_org_id;
	private String case_his_num;
	private SplitPageUtil spu;
	private Long patient_id;
	private String patient_name;
	private String patient_identity_id;
	private Gender patient_gender;
	private String hospitalized_num;
	private ShareRemoteParamsType remoteParamsType;
	private String remoteParamsValue;
	private int type;
	private String value,uuid;

	public static final String PATIENTINFOSHAREGAPENTITY_KEY = "PATIENTINFOSHAREGAPENTITY_KEY";
	
	/**通过补全之后搜索
	 * @return
	 */
	public String searchCaseHistorybyGapGreat(){
		try {
			@SuppressWarnings("unchecked")
			Map<String,PatientInfoShareGapEntity> mapPatientInfoShareGapEntity = (Map<String,PatientInfoShareGapEntity>) super.takeBySession(PATIENTINFOSHAREGAPENTITY_KEY);
			if (CommonTools.isEmpty(mapPatientInfoShareGapEntity))
				throw new PatientInfoShareException("没有信息！");
			PatientInfoShareGapEntity gapEntity = mapPatientInfoShareGapEntity.get(this.uuid);
			if (gapEntity == null)
				throw new PatientInfoShareException("没有信息！");
			OrgRelationPatientInfoShareType infoShareType = this.checkIdentity();
			if (infoShareType == null || infoShareType.equals(OrgRelationPatientInfoShareType.NONE)) 
				throw new OrgRelationException("对方机构未向您当前所在机构开放病例共享！");
			List<FCaseHistory> listCaseHistory = CaseHistoryService.instance.queryCaseHistory(relation_org_id,
					gapEntity.getPatientInfoResult().getRemoteParamsType(), gapEntity.getPatientInfoResult().getParamValue());
			addElementToData("listCaseHistory", listCaseHistory);
		} catch (Exception e) {
			super.catchException(e);
		}
		return SUCCESS;
	}
	
	/**验证补全信息
	 * @return
	 */
	public String verificationCompletion(){
		try {
			@SuppressWarnings("unchecked")
			Map<String,PatientInfoShareGapEntity> mapPatientInfoShareGapEntity = (Map<String,PatientInfoShareGapEntity>) super.takeBySession(PATIENTINFOSHAREGAPENTITY_KEY);
			if (CommonTools.isEmpty(mapPatientInfoShareGapEntity))
				throw new PatientInfoShareException("没有信息需要补全！");
			PatientInfoShareGapEntity gapEntity = mapPatientInfoShareGapEntity.get(this.uuid);
			if (gapEntity == null)
				throw new PatientInfoShareException("没有信息需要补全！");
			if(!gapEntity.verification(value))
				throw new PatientInfoShareException("补全失败，您的补全信息不正确！");
		} catch (Exception e) {
			super.catchException(e);
		}
		return SUCCESS;
	}
	
	/**
	 * 通过病人姓名查询病人号
	 * 
	 * @throws RemotePatientInfoException
	 */
	public String searchParamValuesByPatientName() {
		try {
			switch (type) {
			case 1:
				this.patient_name = this.value;
				break;
			case 2:
				this.patient_identity_id = this.value;
				break;
			default:
				throw new PatientInfoShareException("必须指定病人姓名或身份证号码！");
			}
			OrgRelationPatientInfoShareType infoShareType = this.checkIdentity();
			if (infoShareType == null || infoShareType.equals(OrgRelationPatientInfoShareType.NONE)) 
				throw new OrgRelationException("对方机构未向您当前所在机构开放病例共享！");
			Map<String,PatientInfoShareGapEntity> listPatientInfoShareGapEntity = PatientInfoShareService.instance
					.searchParamValuesByPatientNameOrPatientIdentifyId(takePassport(),infoShareType, relation_org_id, patient_name,
							patient_identity_id);
			super.addToSession(PATIENTINFOSHAREGAPENTITY_KEY, listPatientInfoShareGapEntity);
			super.addElementToData("listPatientInfoResult", new ArrayList<PatientInfoShareGapEntity>(listPatientInfoShareGapEntity.values()));
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 综合搜索病例 */
	public String searchCaseHistoryGreat() {
		try {
			OrgRelationPatientInfoShareType infoShareType = this.checkIdentity();
			if (infoShareType == null || infoShareType.equals(OrgRelationPatientInfoShareType.NONE)) 
				throw new OrgRelationException("对方机构未向您当前所在机构开放病例共享！");
			List<FCaseHistory> listCaseHistory = CaseHistoryService.instance.queryCaseHistory(relation_org_id,
					remoteParamsType, remoteParamsValue);
			addElementToData("listCaseHistory", listCaseHistory);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryLastDicomImgNum() {
		try {
			OrgRelationPatientInfoShareType infoShareType = this.checkIdentity();
			if (infoShareType == null || infoShareType.equals(OrgRelationPatientInfoShareType.NONE)) 
				throw new OrgRelationException("对方机构未向您当前所在机构开放病例共享！");
			Map<ShareRemoteParamsType, List<ShareRemoteNumEntry>> listDicomImgNum = PatientInfoShareService.instance
					.queryLastDicomImgNum(relation_org_id);
			addElementToData("listDicomImgNumObj", null);
			if (listDicomImgNum != null && !listDicomImgNum.isEmpty()) {
				List<FDicomImgNumObj> mapResult = new ArrayList<FDicomImgNumObj>();
				for (Iterator<ShareRemoteParamsType> iterator = listDicomImgNum.keySet().iterator(); iterator
						.hasNext();) {
					ShareRemoteParamsType type = (ShareRemoteParamsType) iterator.next();
					if (type == null)
						continue;
					mapResult.add(new FDicomImgNumObj(type, listDicomImgNum.get(type)));
				}
				addElementToData("listDicomImgNumObj", mapResult);
			}
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String searchCaseHistory() {
		try {
			OrgRelationPatientInfoShareType infoShareType = this.checkIdentity();
			if (infoShareType == null || infoShareType.equals(OrgRelationPatientInfoShareType.NONE)) 
				throw new OrgRelationException("对方机构未向您当前所在机构开放病例共享！");
			SearchCaseHistoryParam chsp = new SearchCaseHistoryParam();
			chsp.setCase_his_num(case_his_num);
			chsp.setOrg_id(relation_org_id);
			chsp.setSpu(spu);
			chsp.setPatient_id(patient_id);
			chsp.setPatient_name(patient_name);
			chsp.setPatient_identity_id(patient_identity_id);
			chsp.setPatient_gender(patient_gender);
			chsp.setHospitalized_num(hospitalized_num);
			List<FCaseHistory> listCaseHistory = CaseHistoryService.instance.searchCaseHistory(chsp);
			addElementToData("listCaseHistory", listCaseHistory);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**检查机构身份，是否可跨机构访问病例库
	 * @throws OrgRelationException
	 */
	private OrgRelationPatientInfoShareType checkIdentity() throws OrgRelationException {
		return OrgRelationService.instance.queryOrgRelationPatientInfoShareType(takePassport(),relation_org_id);
	}

	public void setRelation_org_id(Long relation_org_id) {
		this.relation_org_id = filterParam(relation_org_id);
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = filterParam(case_his_num);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setPatient_id(Long patient_id) {
		this.patient_id = filterParam(patient_id);
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}

	public void setPatient_identity_id(String patient_identity_id) {
		this.patient_identity_id = filterParam(patient_identity_id);
	}

	public void setPatient_gender(int patient_gender) {
		this.patient_gender = Gender.parseCode(patient_gender);
	}

	public void setHospitalized_num(String hospitalized_num) {
		this.hospitalized_num = filterParam(hospitalized_num);
	}

	public void setRemoteParamsType(int remoteParamsType) {
		this.remoteParamsType = ShareRemoteParamsType.parseCode(remoteParamsType);
	}

	public void setRemoteParamsValue(String remoteParamsValue) {
		this.remoteParamsValue = filterParam(remoteParamsValue);
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setUuid(String uuid) {
		this.uuid = filterParam(uuid);
	}
}
