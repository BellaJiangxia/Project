package com.vastsoft.util.exception;

/**
 * 循环中返回异常，抛出此异常表示客户代码想要返回一个值，并且打断循环<br>
 * 返回值可以使用getResult()方法获取
 * 
 * @author jben
 *
 */
public class ReturnIterateException extends Exception {
	private static final long serialVersionUID = 5709359945630017246L;
	private Object result;

	public ReturnIterateException(Object result) {
		super("迭代有了返回值！");
		this.result = result;
	}

	public Object getResult() {
		return this.result;
	}

}
