package com.vastsoft.util.service.sms;

import com.vastsoft.util.exception.BaseException;

public class MsgSenderException extends BaseException {
	public MsgSenderException() {
		super("短信发送异常！");
	}
	public MsgSenderException(String strMessage) {
		super(strMessage);
	}

	private static final long serialVersionUID = -8887662002163105164L;

	@Override
	public int getCode() {
		return 12132;
	}

}
