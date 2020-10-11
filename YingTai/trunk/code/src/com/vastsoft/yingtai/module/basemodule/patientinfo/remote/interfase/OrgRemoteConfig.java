package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase;

import java.util.Map;

import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;

public interface OrgRemoteConfig {
	public String getQuery_url();

	public ShareRemoteServerVersion getRemoteServerVersion();

	public boolean valid();

	public long getOrg_id();
	
	public long getId();
	/** 装载检索参数 */
	public Map<ShareRemoteParamsType, String> getAdditionalRemoteParams();
}
