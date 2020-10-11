package com.vastsoft.yingtai.module.org.realtion.exception;

import com.vastsoft.util.exception.BaseException;

public class OrgRelationException extends BaseException {
	private static final long serialVersionUID = -5289556793793185697L;

	public OrgRelationException(String strMessage) {
		super(strMessage);
	}

	public OrgRelationException(Exception e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 10100;
	}

}
