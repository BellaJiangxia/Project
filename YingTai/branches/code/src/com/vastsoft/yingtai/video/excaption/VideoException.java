package com.vastsoft.yingtai.video.excaption;

import com.vastsoft.util.exception.BaseException;

public class VideoException extends BaseException {
	private static final long serialVersionUID = -6977250430421015587L;

	public VideoException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		// TODO Auto-generated method stub
		return 10;
	}

}
