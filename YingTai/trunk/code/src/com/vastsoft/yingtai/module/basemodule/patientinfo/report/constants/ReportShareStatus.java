package com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class ReportShareStatus extends SingleClassConstant {
	private static final Map<Integer, ReportShareStatus> mapMedicalShareStatus = new HashMap<Integer, ReportShareStatus>();
	/**已分享*/
	public static final ReportShareStatus IS_SHARE = new ReportShareStatus(1, "正在分享");
	/**已关闭分享*/
	public static final ReportShareStatus NO_SHARE = new ReportShareStatus(2, "已关闭分享");

	static {
		ReportShareStatus.mapMedicalShareStatus.put(IS_SHARE.getCode(), IS_SHARE);
		ReportShareStatus.mapMedicalShareStatus.put(NO_SHARE.getCode(), NO_SHARE);
	}
	
	protected ReportShareStatus(int iCode, String strName) {
		super(iCode, strName);
	}
	
	public static ReportShareStatus parseCode(int iCode) {
		return ReportShareStatus.mapMedicalShareStatus.get(iCode);
	}

	public static List<ReportShareStatus> getAllShareStatus() {
		return new ArrayList<ReportShareStatus>(mapMedicalShareStatus.values());
	}
}
