package com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class ReportModifyRequestStatus extends SingleClassConstant {
	/**初始状态*/
	public static final ReportModifyRequestStatus INIT_STATUS=new ReportModifyRequestStatus(10, "待处理");
	/**已接受*/
	public static final ReportModifyRequestStatus ACCEPTED=new ReportModifyRequestStatus(20, "已接受");
	/**已拒绝*/
	public static final ReportModifyRequestStatus REJECTED=new ReportModifyRequestStatus(30, "已拒绝");
	/**已完成*/
	public static final ReportModifyRequestStatus MODIFY_FINISH=new ReportModifyRequestStatus(40, "修改完成");
	private static Map<Integer, ReportModifyRequestStatus> mapModifyReportRequestStatus=new HashMap<Integer, ReportModifyRequestStatus>();
	static{
		mapModifyReportRequestStatus.put(INIT_STATUS.getCode(), INIT_STATUS);
		mapModifyReportRequestStatus.put(ACCEPTED.getCode(), ACCEPTED);
		mapModifyReportRequestStatus.put(REJECTED.getCode(), REJECTED);
		mapModifyReportRequestStatus.put(MODIFY_FINISH.getCode(), MODIFY_FINISH);
	}
	
	public ReportModifyRequestStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static ReportModifyRequestStatus parseCode(int status) {
		return mapModifyReportRequestStatus.get(status);
	}

	public static List<ReportModifyRequestStatus> getAll() {
		return new ArrayList<ReportModifyRequestStatus>(mapModifyReportRequestStatus.values());
	}

}
