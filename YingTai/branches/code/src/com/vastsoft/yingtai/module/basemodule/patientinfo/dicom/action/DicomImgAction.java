package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImgNumObj;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteNumEntry;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;

public class DicomImgAction extends BaseYingTaiAction {
	@ActionDesc(value = "查询最近三天的病历号", grade = 4)
	public String queryLastDicomImgNum() {
		try {
			Map<ShareRemoteParamsType, List<ShareRemoteNumEntry>> listDicomImgNum = DicomImgService.instance
					.queryLastDicomImgNum(takePassport());
			addElementToData("listDicomImgNumObj", null);
			if (listDicomImgNum != null && !listDicomImgNum.isEmpty()) {
				List<FDicomImgNumObj> mapResult = new ArrayList<FDicomImgNumObj>();
				for (Iterator<ShareRemoteParamsType> iterator = listDicomImgNum.keySet().iterator(); iterator.hasNext();) {
					ShareRemoteParamsType type = (ShareRemoteParamsType) iterator.next();
					if (type == null)
						continue;
					mapResult.add(new FDicomImgNumObj(type, listDicomImgNum.get(type)));
				}
				addElementToData("listDicomImgNumObj", mapResult);
			}
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
}
