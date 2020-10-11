package com.vastsoft.yingtai.module.basemodule.patientinfo.report.exception;

import com.vastsoft.util.exception.BaseException;

public class ReportException extends BaseException {
	private static final long serialVersionUID = -5666263208841695675L;

	public ReportException(String strMessage) {
		super(strMessage);
	}

	public ReportException(Exception e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 14334;
	}

}
