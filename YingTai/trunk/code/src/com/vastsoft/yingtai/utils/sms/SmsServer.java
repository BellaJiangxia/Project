package com.vastsoft.yingtai.utils.sms;

import com.vastsoft.util.service.sms.SmsService;

public class SmsServer {
	private SmsService ss = new SmsService();

	public static SmsServer instance = new SmsServer();

	private SmsServer() {
		this.ss.start();
	}

	public boolean sendSMS(String strPhoneNum, String strMsg) {
		return this.ss.sendSMS(strPhoneNum, strMsg);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.ss.stop();
	}
}
