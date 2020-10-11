package com.vastsoft.yingtai.exception;

import java.util.Arrays;

import com.vastsoft.util.exception.BaseException;

public class NullParameterException extends BaseException {

	private static final long serialVersionUID = -7164692510139481664L;

//	public NullParameterException() {
//		super("参数值为空");
//	}
//	
	public NullParameterException(String... paramName) {
		super("参数“"+Arrays.toString(paramName)+"”必须有值！");
	}

	@Override
	public int getCode() {
		return 100;
	}

}
