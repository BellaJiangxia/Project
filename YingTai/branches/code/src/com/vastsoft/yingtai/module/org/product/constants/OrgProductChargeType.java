package com.vastsoft.yingtai.module.org.product.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class OrgProductChargeType extends SingleClassConstant {
	private String desc;// 描述
	/** 按序列收费 */
	public static final OrgProductChargeType CHARGE_PART_TYPE = new OrgProductChargeType(10, "按部位收费",
			"按序列收费：最终的费用将使用影像的序列数X单价得到金额");
	/** 按曝光次数收费 */
	public static final OrgProductChargeType CHARGE_EXPOSE_TIMES_TYPE = new OrgProductChargeType(20, "按曝光次数收费",
			"将使用本次提交申请的影像的所有曝光次数X单价得到金额");
	/** 一次性收费 */
	public static final OrgProductChargeType CHARGE_REQUEST_TIMES_TYPE = new OrgProductChargeType(30, "按人次收费",
			"每一次诊断申请生成的费用就是单价所表示的金额");

	private static Map<Integer, OrgProductChargeType> mapOrgProductChargeType = new LinkedHashMap<Integer, OrgProductChargeType>();
	static {
		mapOrgProductChargeType.put(CHARGE_PART_TYPE.getCode(), CHARGE_PART_TYPE);
		mapOrgProductChargeType.put(CHARGE_EXPOSE_TIMES_TYPE.getCode(), CHARGE_EXPOSE_TIMES_TYPE);
		mapOrgProductChargeType.put(CHARGE_REQUEST_TIMES_TYPE.getCode(), CHARGE_REQUEST_TIMES_TYPE);
	}

	protected OrgProductChargeType(int iCode, String strName, String desc) {
		super(iCode, strName);
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static OrgProductChargeType parseCode(int code) {
		return mapOrgProductChargeType.get(code);
	}

	public static OrgProductChargeType parseCode(int code, OrgProductChargeType default_type) {
		OrgProductChargeType result = parseCode(code);
		return result == null ? default_type : result;
	}

	public static List<OrgProductChargeType> getAll() {
		return new ArrayList<OrgProductChargeType>(mapOrgProductChargeType.values());
	}
}
