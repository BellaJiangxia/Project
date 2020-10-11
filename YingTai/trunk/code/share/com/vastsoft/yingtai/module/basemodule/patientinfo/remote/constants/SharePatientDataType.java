package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatientOrgMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.AbstractShareEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReport;

/** 病人资料类型 */
public final class SharePatientDataType extends SingleClassConstant {
	/** 上一级的资料类型 */
	private SharePatientDataType parent_type;
	/***/
	private Class<? extends AbstractShareEntity> entity_class;
	/** 病人档案 */
	public static final SharePatientDataType PATIENT_DA = new SharePatientDataType(20, "病人档案", null, TPatient.class);
	/** 病人机构映射 */
	public static final SharePatientDataType PATIENT_ORG_MAPPER_DA = new SharePatientDataType(21, "病人机构映射档案", PATIENT_DA,
			TPatientOrgMapper.class);
	/** 病例 */
	public static final SharePatientDataType CASE_HIS = new SharePatientDataType(10, "病例", PATIENT_DA, TCaseHistory.class);
	/** 诊断影像 */
	public static final SharePatientDataType DICOM_IMG = new SharePatientDataType(30, "诊断影像", CASE_HIS, TDicomImg.class);
	/** 诊断影像序列 */
	public static final SharePatientDataType DICOM_IMG_SERIES = new SharePatientDataType(31, "诊断影像序列", DICOM_IMG, TSeries.class);
	/** 诊断报告 */
	/** 报告从属于病例是因为以后有可能报告不是来自于影像检查 */
	public static final SharePatientDataType REPORT = new SharePatientDataType(40, "诊断报告", CASE_HIS, TReport.class);

	private static Map<Integer, SharePatientDataType> mapPatientInfoType = new HashMap<Integer, SharePatientDataType>();

	static {
		mapPatientInfoType.put(CASE_HIS.getCode(), CASE_HIS);
		mapPatientInfoType.put(PATIENT_DA.getCode(), PATIENT_DA);
		mapPatientInfoType.put(PATIENT_ORG_MAPPER_DA.getCode(), PATIENT_ORG_MAPPER_DA);
		mapPatientInfoType.put(DICOM_IMG.getCode(), DICOM_IMG);
		mapPatientInfoType.put(DICOM_IMG_SERIES.getCode(), DICOM_IMG_SERIES);
		mapPatientInfoType.put(REPORT.getCode(), REPORT);
	}

	protected SharePatientDataType(int iCode, String strName, SharePatientDataType parent_type,
			Class<? extends AbstractShareEntity> entity_class) {
		super(iCode, strName);
		this.parent_type = parent_type;
		this.entity_class = entity_class;
	}

	public static List<SharePatientDataType> getAll() {
		return new ArrayList<SharePatientDataType>(mapPatientInfoType.values());
	}

	public static SharePatientDataType parseCode(int code) {
		return mapPatientInfoType.get(code);
	}

	public SharePatientDataType getParent_type() {
		return parent_type;
	}

	public List<SharePatientDataType> getSubPiType() {
		List<SharePatientDataType> listPiType = getAll();
		List<SharePatientDataType> result = new ArrayList<SharePatientDataType>();
		for (SharePatientDataType patientInfoType : listPiType) {
			if (this.equals(patientInfoType.getParent_type()))
				result.add(patientInfoType);
		}
		return result;
	}

	public boolean isRootPatientDataType() {
		return this.parent_type == null;
	}

	public static SharePatientDataType getRootPdType() {
		List<SharePatientDataType> listPiType = getAll();
		for (SharePatientDataType patientInfoType : listPiType) {
			if (patientInfoType.getParent_type() == null)
				return patientInfoType;
		}
		return null;
	}

	public static int count() {
		return mapPatientInfoType.size();
	}

	public AbstractShareEntity getEntity() throws InstantiationException, IllegalAccessException {
		return (AbstractShareEntity) entity_class.newInstance();
	}
}
