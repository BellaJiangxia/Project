package com.vastsoft.yingtai.action.dicom.exception;

import com.vastsoft.util.exception.BaseException;

public class DicomQueryException extends BaseException {
	private static final long serialVersionUID = -2648731599508216440L;

	public DicomQueryException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 2343;
	}

}
