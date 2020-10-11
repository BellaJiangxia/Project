package com.vastsoft.yingtai.module.org.configs.entity;

import org.apache.struts2.json.annotations.JSON;
import org.json.JSONObject;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.utils.configUtils.ConfigInf;

public class TOrgConfigs implements ConfigInf {
	private long id;
	private long org_id;
	private String device_map;
	private String org_sys_device_name_mapping;// 机构设备名称与系统设备名称的映射

	public long getId() {
		return id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public String getDevice_map() {
		return device_map;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public void setDevice_map(String device_map) {
		this.device_map = device_map;
	}

	@JSON(serialize = false)
	@Override
	public JSONObject getJsonObject() {
		JSONObject root = new JSONObject();
		try {
			JSONObject device_mapObj = new JSONObject((device_map == null || device_map.isEmpty()) ? "{}" : device_map);
			root.put("device_map", device_mapObj);
			JSONObject json_org_sys_device_name_mapping = new JSONObject(
					StringTools.isEmpty(org_sys_device_name_mapping) ? "{}" : org_sys_device_name_mapping);
			root.put("org_sys_device_name_mapping", json_org_sys_device_name_mapping);
			return root;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getOrg_sys_device_name_mapping() {
		return org_sys_device_name_mapping;
	}

	public void setOrg_sys_device_name_mapping(String org_sys_device_name_mapping) {
		this.org_sys_device_name_mapping = org_sys_device_name_mapping;
	}
}
