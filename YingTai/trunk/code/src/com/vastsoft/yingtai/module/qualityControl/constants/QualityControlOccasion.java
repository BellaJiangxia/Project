package com.vastsoft.yingtai.module.qualityControl.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.ActionInvocation;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.ReflectTools;
import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.qualityControl.assist.QualityControlTargetIdTranfer;

/**
 * 质控的时机
 * 
 * @author jben
 *
 */
public class QualityControlOccasion extends SingleClassConstant {
	private List<QualityControlTarget> listQualityControlTarget;
	private QualityControlTargetIdTranfer tranfer;
	/** 接受诊断时 */
	public static final QualityControlOccasion _ACCEPT_DIAGNOSIS = new QualityControlOccasion(10, "接受诊断时",
			new QualityControlTargetIdTranfer() {
				@Override
				public Map<QualityControlTarget, Long> tranfer(ActionInvocation invocation) {
					try {
						Object action = invocation.getAction();
						if (action == null)
							return null;
						Long diagnosisId = (Long) ReflectTools.readProperty(action, "diagnosisId");
						if (diagnosisId == null)
							return null;
						FDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(diagnosisId);
						if (diagnosis == null)
							return null;
						Map<QualityControlTarget, Long> result = new HashMap<QualityControlTarget, Long>();
						result.put(QualityControlTarget.DICOM_IMG, diagnosis.getDicom_img_id());
						return result;
					} catch (Exception e) {
						return null;
					}
				}
			}, QualityControlTarget.DICOM_IMG);
	// /** 诊断完成时 */
	// public static final QualityControlOccasion _DIAGNOSIS_FINISH = new
	// QualityControlOccasion(20, "诊断完成时", null,
	// QualityControlTarget.DICOM_IMG);
	// /** 发布报告时 */
	// public static final QualityControlOccasion _PUBLISH_REPORT = new
	// QualityControlOccasion(30, "发布报告时", null,
	// QualityControlTarget.DICOM_IMG);
	/** 打印时 */
	public static final QualityControlOccasion _PRINT = new QualityControlOccasion(40, "打印时",
			new QualityControlTargetIdTranfer() {
				@Override
				public Map<QualityControlTarget, Long> tranfer(ActionInvocation invocation) {
					try {
						Object action = invocation.getAction();
						if (action == null)
							return null;
						Long reportId = (Long) ReflectTools.readProperty(action, "reportId");
						if (reportId == null || reportId <= 0)
							return null;
						Map<QualityControlTarget, Long> result = new HashMap<QualityControlTarget, Long>();
						result.put(QualityControlTarget.DIAGNOSIS_REPORT, reportId);
						return result;
					} catch (Exception e) {
						return null;
					}
				}
			}, QualityControlTarget.DIAGNOSIS_REPORT);

	private static Map<Integer, QualityControlOccasion> mapQualityControlOccasion = new LinkedHashMap<Integer, QualityControlOccasion>();
	static {
		mapQualityControlOccasion.put(_ACCEPT_DIAGNOSIS.getCode(), _ACCEPT_DIAGNOSIS);
		// mapQualityControlOccasion.put(_DIAGNOSIS_FINISH.getCode(),
		// _DIAGNOSIS_FINISH);
		// mapQualityControlOccasion.put(_PUBLISH_REPORT.getCode(),
		// _PUBLISH_REPORT);
		mapQualityControlOccasion.put(_PRINT.getCode(), _PRINT);
	}

	protected QualityControlOccasion(int iCode, String strName, QualityControlTargetIdTranfer tranfer,
			QualityControlTarget... controlTargets) {
		super(iCode, strName);
		this.listQualityControlTarget = CollectionTools.arrayToList(controlTargets);
		this.tranfer = tranfer;
	}

	public static List<QualityControlOccasion> takeListByTarget(QualityControlTarget controlTarget) {
		List<QualityControlOccasion> result = new ArrayList<QualityControlOccasion>();
		for (Iterator<QualityControlOccasion> iterator = mapQualityControlOccasion.values().iterator(); iterator
				.hasNext();) {
			QualityControlOccasion qualityControlOccasion = (QualityControlOccasion) iterator.next();
			if (CollectionTools.isEmpty(qualityControlOccasion.listQualityControlTarget))
				continue;
			if (qualityControlOccasion.listQualityControlTarget.contains(controlTarget))
				result.add(qualityControlOccasion);
		}
		return result;
	}

	@JSON(serialize = false)
	public List<QualityControlTarget> getListQualityControlTarget() {
		return listQualityControlTarget;
	}

	@JSON(serialize = false)
	public QualityControlTargetIdTranfer getTranfer() {
		return this.tranfer;
	}

	public static QualityControlOccasion parseCode(int code) {
		return mapQualityControlOccasion.get(code);
	}
}
