package com.vastsoft.yingtai.module.org.configs.mapper;

import com.vastsoft.yingtai.module.org.configs.entity.TOrgConfigs;

public interface OrgConfigsMapper {

	public TOrgConfigs selectOrgConfigByOrgIdForUpdate(long org_id);

	public void updateOrgConfigs(TOrgConfigs orgConfig);

	public void insertOrgConfigs(TOrgConfigs orgConfig);
}
