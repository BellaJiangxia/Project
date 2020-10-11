package com.vastsoft.yingtaidicom.search.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.interfase.PriorityController;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;

/** 默认的系统数据优先级 */
public class DefaultPriorityController implements PriorityController {
	Map<PatientDataType, List<ExternalSystemType>> priority;

	public DefaultPriorityController() {
		priority = new HashMap<PatientDataType, List<ExternalSystemType>>();
		List<PatientDataType> listPdType = PatientDataType.getAll();
		for (PatientDataType patientDataType : listPdType) {
			ArrayList<ExternalSystemType> listEsType = new ArrayList<ExternalSystemType>();
			priority.put(patientDataType, listEsType);
			if (patientDataType.equals(PatientDataType.CASE_HIS)) {
				listEsType.add(ExternalSystemType.HIS);
				listEsType.add(ExternalSystemType.RIS);
				listEsType.add(ExternalSystemType.EMR);
				listEsType.add(ExternalSystemType.PACS);
			} else if (patientDataType.equals(PatientDataType.DICOM_IMG)) {
				listEsType.add(ExternalSystemType.PACS);
				listEsType.add(ExternalSystemType.RIS);
				listEsType.add(ExternalSystemType.EMR);
				listEsType.add(ExternalSystemType.HIS);
			} else if (patientDataType.equals(PatientDataType.PATIENT_DA)) {
				listEsType.add(ExternalSystemType.HIS);
				listEsType.add(ExternalSystemType.RIS);
				listEsType.add(ExternalSystemType.EMR);
				listEsType.add(ExternalSystemType.PACS);
			} else if (patientDataType.equals(PatientDataType.PATIENT_ORG_MAPPER_DA)) {
				listEsType.add(ExternalSystemType.HIS);
				listEsType.add(ExternalSystemType.RIS);
				listEsType.add(ExternalSystemType.EMR);
				listEsType.add(ExternalSystemType.PACS);
			} else if (patientDataType.equals(PatientDataType.REPORT)) {
				listEsType.add(ExternalSystemType.RIS);
				listEsType.add(ExternalSystemType.EMR);
				listEsType.add(ExternalSystemType.PACS);
				listEsType.add(ExternalSystemType.HIS);
			}
		}
	}

	@Override
	public int comparePriority(ExternalSystemType esType, ExternalSystemType otherType, PatientDataType pdType) {
		List<ExternalSystemType> listEsType = priority.get(pdType);
		int i1 = listEsType.indexOf(esType);
		int i2 = listEsType.indexOf(otherType);
		return i1 - i2;
	}

}
