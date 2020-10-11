package com.vastsoft.yingtai.module.org.configs.constants;

import com.vastsoft.util.common.SingleClassConstant;

public final class OrgConfigType extends SingleClassConstant {
	
	private String code_name;// 字段名
	/** 机构中设备名称与报告抬头设备名称映射配置 */
	public static final OrgConfigType DEVICE_NAME_2_REPORT_DEVICE_NAME_MAPPER_CONFIG = new OrgConfigType(10,
			"机构中设备名称与报告抬头设备名称映射配置", "device_map");
	/** 机构中设备名称与系统中设备名称映射配置 */
	public static final OrgConfigType ORG_DEVICE_NAME_2_SYS_DEVICE_NAME_MAPPER_CONFIG = new OrgConfigType(20,
			"机构中设备名称与系统中设备名称映射配置", "org_sys_device_name_mapping");

	protected OrgConfigType(int iCode, String strName, String code_name) {
		super(iCode, strName);
		this.code_name = code_name;
	}

	public String getCode_name() {
		return code_name;
	}
}
