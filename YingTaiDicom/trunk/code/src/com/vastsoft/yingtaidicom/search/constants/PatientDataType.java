package com.vastsoft.yingtaidicom.search.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.yingtaidicom.search.assist.AbstractRemoteEntity;
import com.vastsoft.yingtaidicom.search.entity.TCaseHistory;
import com.vastsoft.yingtaidicom.search.entity.TDicomImg;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.entity.TPatientOrgMapper;
import com.vastsoft.yingtaidicom.search.entity.TReport;
import com.vastsoft.yingtaidicom.search.entity.TSeries;

/** 病人资料类型 */
public final class PatientDataType extends SingleClassConstant {
	/** 上一级的资料类型 */
	private PatientDataType parent_type;
	/***/
	private Class<? extends AbstractRemoteEntity> entity_class;
	/** 病人档案 */
	public static final PatientDataType PATIENT_DA = new PatientDataType(20, "病人档案", null, TPatient.class);
	/** 病人机构映射 */
	public static final PatientDataType PATIENT_ORG_MAPPER_DA = new PatientDataType(21, "病人机构映射档案", PATIENT_DA,
			TPatientOrgMapper.class);
	/** 病例 */
	public static final PatientDataType CASE_HIS = new PatientDataType(10, "病例", PATIENT_DA, TCaseHistory.class);
	/** 诊断影像 */
	public static final PatientDataType DICOM_IMG = new PatientDataType(30, "诊断影像", CASE_HIS, TDicomImg.class);
	/** 诊断影像序列 */
	public static final PatientDataType DICOM_IMG_SERIES = new PatientDataType(31, "诊断影像序列", DICOM_IMG, TSeries.class);
	/** 诊断报告 */
	/** 报告从属于病例是因为以后有可能报告不是来自于影像检查 */
	public static final PatientDataType REPORT = new PatientDataType(40, "诊断报告", CASE_HIS, TReport.class);

	private static Map<Integer, PatientDataType> mapPatientInfoType = new HashMap<Integer, PatientDataType>();

	static {
		mapPatientInfoType.put(CASE_HIS.getCode(), CASE_HIS);
		mapPatientInfoType.put(PATIENT_DA.getCode(), PATIENT_DA);
		mapPatientInfoType.put(PATIENT_ORG_MAPPER_DA.getCode(), PATIENT_ORG_MAPPER_DA);
		mapPatientInfoType.put(DICOM_IMG.getCode(), DICOM_IMG);
		mapPatientInfoType.put(DICOM_IMG_SERIES.getCode(), DICOM_IMG_SERIES);
		mapPatientInfoType.put(REPORT.getCode(), REPORT);
	}

	protected PatientDataType(int iCode, String strName, PatientDataType parent_type,
			Class<? extends AbstractRemoteEntity> entity_class) {
		super(iCode, strName);
		this.parent_type = parent_type;
		this.entity_class = entity_class;
	}

	public static List<PatientDataType> getAll() {
		return new ArrayList<PatientDataType>(mapPatientInfoType.values());
	}

	public static PatientDataType parseCode(int code) {
		return mapPatientInfoType.get(code);
	}

	public PatientDataType getParent_type() {
		return parent_type;
	}

	public List<PatientDataType> getSubPiType() {
		List<PatientDataType> listPiType = getAll();
		List<PatientDataType> result = new ArrayList<PatientDataType>();
		for (PatientDataType patientInfoType : listPiType) {
			if (this.equals(patientInfoType.getParent_type()))
				result.add(patientInfoType);
		}
		return result;
	}

	public boolean isRootPatientDataType() {
		return this.parent_type == null;
	}

	public static PatientDataType getRootPdType() {
		List<PatientDataType> listPiType = getAll();
		for (PatientDataType patientInfoType : listPiType) {
			if (patientInfoType.getParent_type() == null)
				return patientInfoType;
		}
		return null;
	}

	public static int count() {
		return mapPatientInfoType.size();
	}

	public AbstractRemoteEntity getEntity() throws InstantiationException, IllegalAccessException {
		return (AbstractRemoteEntity) entity_class.newInstance();
	}
}
