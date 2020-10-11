package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception;

import com.vastsoft.util.exception.BaseException;

public class RemotePatientInfoException extends BaseException {
	private static final long serialVersionUID = -9174359372105322361L;

	public RemotePatientInfoException(String strMessage) {
		super("远程查询病人资料错误，信息："+strMessage);
	}

	public RemotePatientInfoException(Exception e) {
		super(e);
	}

	@Override
	public int getCode() {
		return 1300;
	}

	public static RemotePatientInfoException exceptionOf(Exception e) {
		if (e instanceof RemotePatientInfoException)
			return (RemotePatientInfoException) e;
		e.printStackTrace();
		return new RemotePatientInfoException("远程病人数据检索异常，信息："+String.valueOf(e));
	}

}
