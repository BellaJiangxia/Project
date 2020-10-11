package com.vastsoft.yingtai.module.org.realtion.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class PublishReportType extends SingleClassConstant {
	protected PublishReportType(int iCode, String strName) {
		super(iCode, strName);
	}

	// 机构好友发布报告类型
	private static final Map<Integer, PublishReportType> mapApplyReportType = new HashMap<Integer, PublishReportType>();
	// 申请者
	public static final PublishReportType Requestor = new PublishReportType(1, "以申请方名义发布报告");
	// 诊断者
	public static final PublishReportType Diagnosiser = new PublishReportType(2, "以诊断方名义发布报告");
	// 可选择
	public static final PublishReportType CanChoose = new PublishReportType(3, "自定义诊断报告发布者");

	static {
		mapApplyReportType.put(Requestor.getCode(), Requestor);
		mapApplyReportType.put(Diagnosiser.getCode(), Diagnosiser);
		mapApplyReportType.put(CanChoose.getCode(), CanChoose);
	}

	public static PublishReportType parseFrom(int iCode) {
		return mapApplyReportType.get(iCode);
	}

	public static List<PublishReportType> getAllPublishReportType() {
		return new ArrayList<PublishReportType>(mapApplyReportType.values());
	}
}
