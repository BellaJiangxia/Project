package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity;

import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareExternalSystemType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareSystemIdentity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity.ShareSeries;

public class TSeries extends ShareSeries {

	public TSeries() {
		super();
	}

	public TSeries(long dicom_img_id, String mark_char) {
		super(dicom_img_id, mark_char);
	}

	public TSeries(ShareExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	public TSeries(ShareSystemIdentity systemIdentity) {
		super(systemIdentity);
	}
	
}
