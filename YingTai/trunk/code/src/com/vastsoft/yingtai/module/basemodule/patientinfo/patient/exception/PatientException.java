package com.vastsoft.yingtai.module.basemodule.patientinfo.patient.exception;

import com.vastsoft.util.exception.BaseException;

public class PatientException extends BaseException {
	private static final long serialVersionUID = -1712150151478470226L;

	public PatientException(String strMessage) {
		super("病人档案模块错误！信息："+strMessage);
	}

    public PatientException(Exception e) {
        super(e);
    }

    @Override
	public int getCode() {
		return 4100;
	}

}
