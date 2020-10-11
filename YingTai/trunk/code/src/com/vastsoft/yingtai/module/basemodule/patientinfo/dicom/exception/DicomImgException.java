package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception;

import com.vastsoft.util.exception.BaseException;

public class DicomImgException extends BaseException {
	private static final long serialVersionUID = 320624338506227315L;

	public DicomImgException(Throwable e) {
		super(e);
	}

	public DicomImgException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 13305;
	}

}
