package com.vastsoft.yingtai.module.weixin.service;

import java.util.Date;

import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class LastCaseImportantInfo
{
	private long lOrgId;
	private String strOrgName;
	private long lOrgLogoFileId;
	private String strCardNum;
	private Gender gender;
	private Date dtBirth;
	private CaseHistoryType type;
	private Date dtCase;
	private String strSymptom;
	private String strDiagnoses;

	public LastCaseImportantInfo(long lOrgId, String strOrgName, long lOrgLogoFileId, String strCardNum, Gender gender, Date dtBirth, CaseHistoryType type, Date dtCase, String strSymptom, String strDiagnoses)
	{
		this.lOrgId = lOrgId;
		this.strOrgName = strOrgName;
		this.lOrgLogoFileId = lOrgLogoFileId;
		this.strCardNum = strCardNum;
		this.gender = gender;
		this.dtBirth = dtBirth;
		this.type = type;
		this.dtCase = dtCase;
		this.strSymptom = strSymptom;
		this.strDiagnoses = strDiagnoses;
	}

	public long getOrgId()
	{
		return lOrgId;
	}

	public String getOrgName()
	{
		return strOrgName;
	}

	public long getOrgLogoFileId()
	{
		return lOrgLogoFileId;
	}

	public String getCardNum()
	{
		return strCardNum;
	}

	public Gender getGender()
	{
		return gender;
	}

	public Date getBirthDate()
	{
		return dtBirth;
	}

	public CaseHistoryType getType()
	{
		return type;
	}

	public Date getCaseDate()
	{
		return dtCase;
	}

	public String getSymptom()
	{
		return strSymptom;
	}

	public String getDiagnoses()
	{
		return strDiagnoses;
	}

}
