package com.vastsoft.yingtai.module.org.configs.exception;

public class NotFoundDeviceTypeMapperException extends OrgConfigsException {
	private static final long serialVersionUID = 184953276209063733L;
	private long org_id;
	private String old_device_type_name;

	public NotFoundDeviceTypeMapperException(long org_id, String old_device_type_name) {
		super("没有找到机构的设备映射，请先进行设置！机构ID：" + org_id + " 设备名称：" + old_device_type_name);
		this.old_device_type_name = old_device_type_name;
		this.org_id = org_id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public String getOld_device_type_name() {
		return old_device_type_name;
	}
}
