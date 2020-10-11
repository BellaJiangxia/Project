package com.vastsoft.yingtai.module.sys.exception;

import com.vastsoft.util.exception.BaseException;

public class FileException extends BaseException {
	private static final long serialVersionUID = 4672176389552798733L;

	public FileException(String strMessage) {
		super(strMessage == null ? "文件异常：" : strMessage);
	}

	public FileException(Exception e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 210;
	}

}
