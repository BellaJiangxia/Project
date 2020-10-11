package com.vastsoft.yingtai.module.reservation.exception;

import com.vastsoft.util.exception.BaseException;

public class ReservationException extends BaseException {

	public ReservationException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 1001;
	}
}
