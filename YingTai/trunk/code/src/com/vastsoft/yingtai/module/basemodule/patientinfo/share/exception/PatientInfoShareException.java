package com.vastsoft.yingtai.module.basemodule.patientinfo.share.exception;

import com.vastsoft.util.exception.BaseException;

public class PatientInfoShareException extends BaseException {
	private static final long serialVersionUID = 8402223578477677082L;

	public PatientInfoShareException(String strMessage) {
		super(strMessage);
	}

	public PatientInfoShareException(Exception e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 12563;
	}

}
