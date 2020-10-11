package com.vastsoft.yingtai.module.basemodule.patientinfo.share.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.exception.CaseHistoryException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.RemoteServerService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.PatientInfoResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteNumEntry;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.share.assist.PatientInfoShareGapEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.share.assist.PatientInfoShareGapEntity.QueryType;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.orgAffix.entity.TOrgAffix;
import com.vastsoft.yingtai.module.org.orgAffix.service.OrgAffixService;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationPatientInfoShareType;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class PatientInfoShareService {
	public static final PatientInfoShareService instance = new PatientInfoShareService();
	
	/**
	 * 通过病人姓名查询病人号
	 * 
	 * @param passport
	 * @param infoShareType 
	 * @param patientName
	 * @param patient_identity_id 
	 * @return
	 * @throws CaseHistoryException
	 */
	public Map<String,PatientInfoShareGapEntity> searchParamValuesByPatientNameOrPatientIdentifyId(Passport passport,OrgRelationPatientInfoShareType infoShareType, long relation_org_id, String patientName, String patient_identity_id)
			throws CaseHistoryException {
		try {
			if (StringTools.isEmpty(patientName) && !StringTools.wasIdentityId(patient_identity_id))
				throw new RemotePatientInfoException("必须指定用于检索的病人姓名，至少是中文的2个字符或者英文的6个字符，或者有效的病人身份证号码！");
			if (!StringTools.isEmpty(patientName)) {
				patientName = patientName.trim();
				if (patientName.length()<2)
					throw new RemotePatientInfoException("病人姓名至少是中文的2个字符或者英文的6个字符！");
				if (!StringTools.isChineseByREG(patientName) && patientName.length()<6)
					throw new RemotePatientInfoException("病人姓名至少是中文的2个字符或者英文的6个字符！");
				patient_identity_id = null;
			}else{
				patientName = null;
			}
			TOrganization org = OrgService.instance.searchOrgById(relation_org_id);
			TOrgAffix orgAffix = OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(relation_org_id);
			List<PatientInfoResult> listPIR = RemoteServerService.instance.onSearchParamValuesByPatientNameOrPatientIdentifyId(orgAffix,
					String.valueOf(org.getOrg_code()), patientName,patient_identity_id);
			if (CollectionTools.isEmpty(listPIR)) 
				return null;
			QueryType queryType = StringTools.isEmpty(patientName)?QueryType.PATIENT_NAME:QueryType.PATIENT_IDENTITY;
			Map<String,PatientInfoShareGapEntity> result = new HashMap<String,PatientInfoShareGapEntity>();
			for (Iterator<PatientInfoResult> iterator = listPIR.iterator(); iterator.hasNext();) {
				PatientInfoResult patientInfoResult = (PatientInfoResult) iterator.next();
				if (patientInfoResult.getPatient() == null){
					iterator.remove();
					continue;
				}
				PatientInfoShareGapEntity tmPatientInfoShareGapEntity = new PatientInfoShareGapEntity(queryType, infoShareType, patientInfoResult);
				result.put(tmPatientInfoShareGapEntity.getUuid(),tmPatientInfoShareGapEntity);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CaseHistoryException(e);
		}
	}
	
	/**
	 * 查询最近几天的影像号
	 * 
	 * @throws BaseException
	 * @param passport
	 * @param remoteParamType
	 * @return
	 * @throws BaseException
	 */
	public Map<ShareRemoteParamsType, List<ShareRemoteNumEntry>> queryLastDicomImgNum(long org_id) throws BaseException {
		TOrgAffix orgAffix = OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(org_id);
		if (orgAffix == null)
			throw new DicomImgException("没有找到本机构的远程配置！");
		TOrganization org = OrgService.instance.searchOrgById(org_id);
		Map<ShareRemoteParamsType, String> params = new HashMap<ShareRemoteParamsType, String>();
		params.put(ShareRemoteParamsType.ORG_CODE, String.valueOf(org.getOrg_code()));
		return RemoteServerService.instance.onSearchRemotePascCaseNum(CollectionTools.buildList(orgAffix), params);
	}
}
