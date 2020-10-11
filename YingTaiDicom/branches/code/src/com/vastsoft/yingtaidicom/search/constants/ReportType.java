package com.vastsoft.yingtaidicom.search.constants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class ReportType extends SingleClassConstant {
	/**影像报告*/
	public static final ReportType DICOM_IMG_REPORT = new ReportType(10,"影像报告");
	/**检验报告*/
	public static final ReportType INSPECT_REPORT = new ReportType(20, "检验报告");
	/**其他报告*/
	public static final ReportType OTHER_REPORT = new ReportType(99, "其他报告");
	
	private static Map<Integer, ReportType> mapReportType = new LinkedHashMap<Integer, ReportType>();
	
	static{
		mapReportType.put(DICOM_IMG_REPORT.getCode(), DICOM_IMG_REPORT);
		mapReportType.put(INSPECT_REPORT.getCode(), INSPECT_REPORT);
		mapReportType.put(OTHER_REPORT.getCode(), OTHER_REPORT);
	}
	
	protected ReportType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static ReportType parseCode(int code) {
		return mapReportType.get(code);
	}

}
