package com.vastsoft.yingtai.module.qualityControl.exception;

import com.vastsoft.util.exception.BaseException;

public class QualityControlException extends BaseException {
	private static final long serialVersionUID = -6173494974932829005L;

	public QualityControlException(String strMessage) {
		super(strMessage);
	}
	
	public QualityControlException(Throwable e) {
		super(e);
	}

	@Override
	public int getCode() {
		return -9562;
	}

}
