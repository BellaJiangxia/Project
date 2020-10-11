package com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.exception;

import com.vastsoft.util.exception.BaseException;

public class CaseHistoryException extends BaseException {
	private static final long serialVersionUID = -703711428279280271L;

	public CaseHistoryException(Throwable e) {
		super(e);
	}

	public CaseHistoryException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 33;
	}

}
