package com.vastsoft.yingtai.module.org.orgAffix.exception;

import com.vastsoft.util.exception.BaseException;

public class OrgAffixException extends BaseException {
	private static final long serialVersionUID = -2606442964974851705L;

	public OrgAffixException(Throwable e) {
		super(e);
	}

	public OrgAffixException(String strMessage) {
		super(strMessage);
	}

	@Override
	public int getCode() {
		return 3025;
	}

}
