package com.vastsoft.yingtai.module.diagnosis2.contants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class DiagnosisHandleStatus2 extends SingleClassConstant {
	/** 书写中 */
	public static final DiagnosisHandleStatus2 WRITING = new DiagnosisHandleStatus2(1, "正在书写");
	/** 已移交待接受 */
	public static final DiagnosisHandleStatus2 TRANFERED = new DiagnosisHandleStatus2(2, "已移交待接受");
	/** 已移交待审核 */
	public static final DiagnosisHandleStatus2 TRANFER_AUDIT = new DiagnosisHandleStatus2(25, "已移交待审核");
	/** 审核完成,最终 */
	public static final DiagnosisHandleStatus2 AUDITED = new DiagnosisHandleStatus2(3, "审核完成");
	/** 转交被接受*/
	public static final DiagnosisHandleStatus2 TRANFER_ACCEPTED = new DiagnosisHandleStatus2(4, "转交已被接受");
	/** 移交审核已被接受*/
	public static final DiagnosisHandleStatus2 TRANFER_AUDIT_ACCEPTED = new DiagnosisHandleStatus2(45, "转交审核已被接受");
	/** 报告已修改*/
	public static final DiagnosisHandleStatus2 REPORT_MODIFYED = new DiagnosisHandleStatus2(5, "已修改报告");

	private static Map<Integer, DiagnosisHandleStatus2> mapHandleStatus = new LinkedHashMap<Integer, DiagnosisHandleStatus2>();

	static {
		mapHandleStatus.put(WRITING.getCode(), WRITING);
		mapHandleStatus.put(TRANFERED.getCode(), TRANFERED);
		mapHandleStatus.put(TRANFER_AUDIT.getCode(), TRANFER_AUDIT);
		mapHandleStatus.put(AUDITED.getCode(), AUDITED);
		mapHandleStatus.put(TRANFER_ACCEPTED.getCode(), TRANFER_ACCEPTED);
		mapHandleStatus.put(TRANFER_AUDIT_ACCEPTED.getCode(), TRANFER_AUDIT_ACCEPTED);
		mapHandleStatus.put(REPORT_MODIFYED.getCode(), REPORT_MODIFYED);
	}

	protected DiagnosisHandleStatus2(int iCode, String strName) {
		super(iCode, strName);
	}

	public static DiagnosisHandleStatus2 parseFrom(int iCode) {
		return DiagnosisHandleStatus2.mapHandleStatus.get(iCode);
	}

	public static List<DiagnosisHandleStatus2> getAll() {
		return new ArrayList<DiagnosisHandleStatus2>(DiagnosisHandleStatus2.mapHandleStatus.values());
	}

}
