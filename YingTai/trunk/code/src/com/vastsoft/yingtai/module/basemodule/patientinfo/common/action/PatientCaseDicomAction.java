package com.vastsoft.yingtai.module.basemodule.patientinfo.common.action;

import java.util.List;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.exception.CaseHistoryException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.exception.PatientException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.cross.service.PatientInfoDiagnosisCrossService;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class PatientCaseDicomAction extends BaseYingTaiAction {
	private Long caseId;
	private TPatient patient;
	private FCaseHistory caseHistory;
	private List<FDicomImg> listDicomImg;

	public String queryCaseHistoryDetailByCaseId(){
		try {
			FCaseHistory caseHistory=CaseHistoryService.instance.queryCaseHistoryById(caseId,true);
			if (caseHistory==null)
				throw new CaseHistoryException("指定的病例未找到！");
			TPatient patient=PatientService.instance.queryPatientById(caseHistory.getPatient_id());
			if (patient==null)
				throw new PatientException("指定的病人信息未找到！");
			List<FDicomImg> listDicomImg=DicomImgService.instance.queryDicomImgByCaseHisId(caseId);
			addElementToData("patient", patient);
			addElementToData("caseHistory", caseHistory);
			addElementToData("listDicomImg", listDicomImg);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	public String savePatientInfo(){
		try{
			Passport passport = takePassport();
			if (!CollectionTools.isEmpty(listDicomImg)) {
				for (FDicomImg fDicomImg : listDicomImg) {
					long[] body_part_ids_arr = new long[0];
					for (TDicValue dicValue : fDicomImg.getListBodyPartType()) {
						body_part_ids_arr = (long[]) ArrayTools.add(body_part_ids_arr, dicValue.getId());
					}
					fDicomImg.setBody_part_ids_arr(body_part_ids_arr);
				}
			}
			PatientInfoDiagnosisCrossService.instance.savePatientInfo(passport, patient, caseHistory, listDicomImg);
			addElementToData("caseHistory", caseHistory);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public void setPatient(TPatient patient) {
		this.patient = patient;
	}

	public void setCaseHistory(FCaseHistory caseHistory) {
		this.caseHistory = caseHistory;
	}

	public void setListDicomImg(List<FDicomImg> listDicomImg) {
		this.listDicomImg = listDicomImg;
	}

}
