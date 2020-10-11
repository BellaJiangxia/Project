package com.vastsoft.yingtaidicom.search.exception;

import com.vastsoft.util.exception.BaseException;

public class PatientDataException extends BaseException {
	private static final long serialVersionUID = -8459616673112943843L;

	public PatientDataException(String strMessage) {
		super("病人数据整合异常！信息：" + strMessage);
	}

	public PatientDataException(Exception e) {
		this("不确定异常[" + e.getClass().getName() + "]：" + e.getMessage());
	}

	@Override
	public int getCode() {
		return 1211;
	}

}
