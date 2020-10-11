package com.vastsoft.yingtai.module.basemodule.patientinfo.share.assist;

import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.DateTools.TimeExactType;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.PatientInfoResult;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationPatientInfoShareType;

/**
 * 病人信息缺失需要补全的描述实体
 * 
 * @author jiangyubin
 *
 */
public class PatientInfoShareGapEntity {
	private QueryType queryType;
	private OrgRelationPatientInfoShareType infoShareType;
	private PatientInfoResult patientInfoResult;
	private String uuid;

	public PatientInfoShareGapEntity(QueryType queryType, OrgRelationPatientInfoShareType infoShareType,PatientInfoResult patientInfoResult) {
		super();
		this.queryType = queryType;
		this.infoShareType = infoShareType;
		if (this.infoShareType == null)
			throw new NullPointerException("infoShareType 不能未空！");
		this.patientInfoResult = patientInfoResult;
		this.uuid = StringTools.getUUID();
	}

	/**
	 * 验证补全的信息，如果不需要补全返回true
	 * 
	 * @param obj
	 * @return
	 */
	public boolean verification(String obj) {
		if (this.infoShareType.equals(OrgRelationPatientInfoShareType.SHARED)) 
			return true;
		CompletionType completionType = this.getNeedCompletionType();
		if (completionType == null)
			return true;
		return completionType.verification(patientInfoResult, obj);
	}

	public boolean isNeedCompletion() {
		if (this.infoShareType.equals(OrgRelationPatientInfoShareType.SHARED)) 
			return false;
		return this.getNeedCompletionType() != null;
	}
	
	public CompletionType getNeedCompletionType() {
		if (this.infoShareType.equals(OrgRelationPatientInfoShareType.SHARED)) 
			return null;
		if (this.patientInfoResult == null)
			return null;
		if (this.patientInfoResult.getPatient() == null)
			return null;
		switch (queryType) {
		case PATIENT_NAME:
			if (StringTools.isMobileNum(this.patientInfoResult.getPatient().getMobile()))
				return CompletionType.PATIENT_MOBILE;
			if (StringTools.wasIdentityId(this.patientInfoResult.getPatient().getIdentity_id()))
				return CompletionType.PATIENT_IDENTITY;
			if (DateTools.isBirthday(this.patientInfoResult.getPatient().getBirthday()))
				return CompletionType.PATIENT_BIRTHDAY;
			break;
		case PATIENT_IDENTITY:
			if (StringTools.isMobileNum(this.patientInfoResult.getPatient().getMobile()))
				return CompletionType.PATIENT_MOBILE;
			if (DateTools.isBirthday(this.patientInfoResult.getPatient().getBirthday()))
				return CompletionType.PATIENT_BIRTHDAY;
			break;
		default:
			break;
		}
		return null;
	}

	public String getPatient_name() {
		return this.patientInfoResult.getPatient().getName();
	}

	public String getPatient_mobile() {
		return this.getNeedCompletionType() == CompletionType.PATIENT_MOBILE
				? StringTools.formatBetweenStr(this.patientInfoResult.getPatient().getMobile(), 3, 4, '*') : this.patientInfoResult.getPatient().getMobile();
	}

	public String getPatient_identity() {
		return this.getNeedCompletionType() == CompletionType.PATIENT_IDENTITY
				? StringTools.formatEndStringWithTrim(this.patientInfoResult.getPatient().getIdentity_id(), 4, '*') : this.patientInfoResult.getPatient().getIdentity_id();
	}

	public String getPatient_brithday() {
		return this.getNeedCompletionType() == CompletionType.PATIENT_BIRTHDAY ? DateTools.dateToString(this.patientInfoResult.getPatient().getBirthday(),"****-MM-dd") : DateTools.dateToString(this.patientInfoResult.getPatient().getBirthday(),TimeExactType.DAY);
	}

	public String getUuid() {
		return uuid;
	}
	
	@JSON(serialize = false)
	public PatientInfoResult getPatientInfoResult() {
		return patientInfoResult;
	}

	public static enum QueryType {
		PATIENT_NAME, PATIENT_IDENTITY;
	}

	public static enum CompletionType {
		PATIENT_MOBILE {
			@Override
			public boolean verification(PatientInfoResult patientInfoResult, String obj) {
				if (obj == null)
					return false;
				return obj.equals(patientInfoResult.getPatient().getMobile());
			}
		},
		PATIENT_IDENTITY {
			@Override
			public boolean verification(PatientInfoResult patientInfoResult, String obj) {
				if (obj == null)
					return false;
				return obj.equals(patientInfoResult.getPatient().getIdentity_id());
			}
		},
		PATIENT_BIRTHDAY {
			@Override
			public boolean verification(PatientInfoResult patientInfoResult, String obj) {
				if (obj == null)
					return false;
				return obj.trim().equals(DateTools.getYear(patientInfoResult.getPatient().getBirthday())+"");
			}
		};

		public abstract boolean verification(PatientInfoResult patientInfoResult, String obj);
	}
}
