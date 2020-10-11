package com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class ReportMsgType extends SingleClassConstant {

	protected ReportMsgType(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, ReportMsgType> mapDiagnosisMsgType = new HashMap<Integer, ReportMsgType>();
	/** 发送给申请者 */
	public static final ReportMsgType TO_REQUESTER = new ReportMsgType(1, "发送给申请者");
	/** 发送给诊断者 */
	public static final ReportMsgType TO_DIAGNOSISER = new ReportMsgType(2, "发送给诊断者");
	static {
		mapDiagnosisMsgType.put(TO_REQUESTER.getCode(), TO_REQUESTER);
		mapDiagnosisMsgType.put(TO_DIAGNOSISER.getCode(), TO_DIAGNOSISER);
	}

	public static ReportMsgType parseFrom(int iCode) {
		return ReportMsgType.mapDiagnosisMsgType.get(iCode);
	}

	public static List<ReportMsgType> getAll() {
		return new ArrayList<ReportMsgType>(ReportMsgType.mapDiagnosisMsgType.values());
	}
}
