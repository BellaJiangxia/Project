package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase;

import java.util.List;
import java.util.Map;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;

/** 搜索远程病人信息触发器 */
public interface SearchRemotePatientInfoTrigger {
	/**
	 * 触发查询远程病人资料
	 * 
	 * @param list
	 *            远程检索机构配置
	 * @param params
	 *            参数数据
	 * @return
	 * @throws BaseException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public boolean onSearchRemotePatientInfo(List<? extends OrgRemoteConfig> list,
			Map<ShareRemoteParamsType, String> params);

	/**
	 * 查询最近几天的影像
	 * 
	 * @param list
	 * @return
	 */
	public List<String> onSearchLastDicomImgNum(List<? extends OrgRemoteConfig> list);
}
