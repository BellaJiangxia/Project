package com.vastsoft.yingtai.module.org.configs.action;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.org.configs.constants.OrgConfigType;
import com.vastsoft.yingtai.module.org.configs.service.OrgConfigService;

public class OrgConfigAction extends BaseYingTaiAction {
	private String jsonDeviceMapper;
	
	/**保存*/
	public String saveOrgDeviceName2SysDeviceNameMapperConfig(){
		try {
			OrgConfigService.instance.saveOrgDeviceName2SysDeviceNameMapperConfig(takePassport(), jsonDeviceMapper);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	/**拉取机构设备名称与系统中设备名称的配置*/
	public String takeOrgDeviceNameTransToSysDeviceNameMapper() {
		try {
			List<Map<String, String>> mapDeviceMapper = OrgConfigService.instance.takeOrgConfigMapper(takePassport().getOrgId(),
					OrgConfigType.ORG_DEVICE_NAME_2_SYS_DEVICE_NAME_MAPPER_CONFIG);
			addElementToData("org_device_name_2_sys_device_name_mapper_config", mapDeviceMapper);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setJsonDeviceMapper(String jsonDeviceMapper) {
		this.jsonDeviceMapper = jsonDeviceMapper;
	}
}
