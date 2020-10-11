package com.vastsoft.yingtaidicom.search.interfase;

import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;

public interface PriorityController {

	/**
	 * 比较两个系统的优先级，根据指定病人数据类型<br>
	 * 前面系统优先级减去后面系统优先级，返回结果<br>
	 * 如果大于0表示前边低于后面,反之后面低于前面，如果等于0标识系统相同
	 * 
	 * @param esType
	 *            一个外部系统类型
	 * @param otherType
	 *            另外一个外部系统类型
	 * @param pdType
	 *            病人数据类型
	 * @return 返回优先级差
	 */
	public int comparePriority(ExternalSystemType esType, ExternalSystemType otherType, PatientDataType pdType);
}
