package com.vastsoft.util.exception;

public class SystemException extends BaseException {
	private static final long serialVersionUID = -373153295924402002L;

	public SystemException(String strMessage) {
		super("系统错误：" + strMessage);
	}

	public SystemException() {
		super("系统错误!");
	}

	public SystemException(Throwable e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 9999;
	}

}
