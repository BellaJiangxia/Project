package com.vastsoft.yingtai.module.diagnosis2.contants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/** 诊断状态 */
public class DiagnosisStatus2 extends SingleClassConstant {
	protected DiagnosisStatus2(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, DiagnosisStatus2> mapRequestStatus = new HashMap<Integer, DiagnosisStatus2>();
	/** 等待诊断 */
	public static final DiagnosisStatus2 NOTDIAGNOSIS = new DiagnosisStatus2(1, "等待诊断");
	/** 接受，并转交申请 */
	public static final DiagnosisStatus2 ACCEPT_TRANFER = new DiagnosisStatus2(10, "接受，并转交申请");
	/** 已被撤回-最终 */
	public static final DiagnosisStatus2 CANCELED = new DiagnosisStatus2(2, "已被撤回");
	/** 已被拒绝-最终 */
	public static final DiagnosisStatus2 BACKED = new DiagnosisStatus2(3, "已被拒绝");
	/** 正在诊断 */
	public static final DiagnosisStatus2 DIAGNOSISING = new DiagnosisStatus2(4, "正在诊断");
	/** 诊断结束 */
	public static final DiagnosisStatus2 DIAGNOSISED = new DiagnosisStatus2(5, "诊断结束");

	static {
		mapRequestStatus.put(NOTDIAGNOSIS.getCode(), NOTDIAGNOSIS);
		mapRequestStatus.put(ACCEPT_TRANFER.getCode(), ACCEPT_TRANFER);
		mapRequestStatus.put(CANCELED.getCode(), CANCELED);
		mapRequestStatus.put(BACKED.getCode(), BACKED);
		mapRequestStatus.put(DIAGNOSISING.getCode(), DIAGNOSISING);
		mapRequestStatus.put(DIAGNOSISED.getCode(), DIAGNOSISED);
	}

	public static DiagnosisStatus2 parseFrom(int status) {
		return DiagnosisStatus2.mapRequestStatus.get(status);
	}

	public static List<DiagnosisStatus2> getAll() {
		return new ArrayList<DiagnosisStatus2>(DiagnosisStatus2.mapRequestStatus.values());
	}

}
