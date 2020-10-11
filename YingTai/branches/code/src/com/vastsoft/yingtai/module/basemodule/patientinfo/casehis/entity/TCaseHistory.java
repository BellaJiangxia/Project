package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity;

import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity.ShareCaseHistory;

/** 病例 */
public class TCaseHistory extends ShareCaseHistory {

	public TCaseHistory() {
		super();
	}

	public TCaseHistory(long org_id, String case_his_num) {
		super(org_id, case_his_num);
	}
	
}
