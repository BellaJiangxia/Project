package com.vastsoft.util.exception;

public class ReflectException extends BaseException {
	private static final long serialVersionUID = 4708347831396790166L;

	public ReflectException(Throwable e) {
		super(e);
	}

	public ReflectException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 15896;
	}

}
