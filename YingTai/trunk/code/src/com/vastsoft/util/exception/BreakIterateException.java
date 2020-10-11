package com.vastsoft.util.exception;

/**
 * 打断循环异常，如果抛出此异常表示客户代码想要打断循环，类似于循环种的break
 * 
 * @author jben
 *
 */
public class BreakIterateException extends BaseException {
	private static final long serialVersionUID = -5737012039278799344L;

	public BreakIterateException() {
		super("循环被打断！");
	}

	@Override
	public int getCode() {
		return 11;
	}
}
