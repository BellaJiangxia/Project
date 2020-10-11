package com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class ReportMsgStatus extends SingleClassConstant {

	protected ReportMsgStatus(int iCode, String strName) {
		super(iCode, strName);
	}
	private static final Map<Integer, ReportMsgStatus> mapDiagnosisMsgStatus = new HashMap<Integer, ReportMsgStatus>();
	/** 已读 */
	public static final ReportMsgStatus READED = new ReportMsgStatus(1, "已读");
	/** 未读 */
	public static final ReportMsgStatus UNREAD = new ReportMsgStatus(2, "未读");

	static {
		mapDiagnosisMsgStatus.put(READED.getCode(), READED);
		mapDiagnosisMsgStatus.put(UNREAD.getCode(), UNREAD);
	}

	public static ReportMsgStatus parseFrom(int iCode) {
		return ReportMsgStatus.mapDiagnosisMsgStatus.get(iCode);
	}

	public static List<ReportMsgStatus> getAll() {
		return new ArrayList<ReportMsgStatus>(ReportMsgStatus.mapDiagnosisMsgStatus.values());
	}

}
