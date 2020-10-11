package com.vastsoft.util.exception;

public abstract class BaseException extends Exception {
	private static final long serialVersionUID = 8797753407520606430L;

	public BaseException(String strMessage) {
		super(strMessage);
	}

	public BaseException(Throwable e) {
		super((e instanceof BaseException) ? e.getMessage() : "系统错误:" + e.getMessage());
		if (!(e instanceof BaseException))
			e.printStackTrace();
	}

	public abstract int getCode();

	public static BaseException exceptionOf(Throwable e) {
		if (e == null)
			return new SystemException();
		if (e instanceof BaseException)
			return (BaseException) e;
		e.printStackTrace();
		return new SystemException(e);
	}
}
