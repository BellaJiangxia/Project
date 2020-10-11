package com.vastsoft.yingtai.module.cross.service;

import java.util.List;

import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.db.VsSqlSession;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class PatientInfoDiagnosisCrossService {
	public static final PatientInfoDiagnosisCrossService instance = new PatientInfoDiagnosisCrossService();

	private PatientInfoDiagnosisCrossService() {
	}

	@SuppressWarnings("unchecked")
	public void savePatientInfo(Passport passport, TPatient patient, TCaseHistory caseHistory,
			List<? extends TDicomImg> listDicomImg) throws BaseException {
		String key = CommonTools.getUUID();
		VsSqlSession session = null;
		try {
			session = SessionFactory.getSession(key);
			patient = PatientService.instance.modifyPatient(passport, patient, session);
			caseHistory = CaseHistoryService.instance.modifyCaseHistory(passport, caseHistory, session);
			listDicomImg = DicomImgService.instance.modifyDicomImgList(passport, (List<TDicomImg>) listDicomImg, session);
			session.commit(key, true);
		} catch (Exception e) {
			session.rollback(true,key);
			throw e;
		} finally {
			SessionFactory.closeSession(session, key);
		}
	}
}
