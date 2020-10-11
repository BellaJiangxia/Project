package com.vastsoft.yingtai.module.weixin.pushdata.exception;

import com.vastsoft.util.exception.BaseException;

public class PushDataParamException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3864177090152944846L;

	public PushDataParamException (String paramName, String msg){
		super("参数："+paramName+"出现错误！"+(msg==null?"":msg));
	}

	@Override
	public int getCode() {
		return 10100;
	}

}
