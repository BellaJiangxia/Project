package com.vastsoft.util.service.mail.std_smtp;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuth extends Authenticator {
	private String strAccount;
	private String strPassword;

	SmtpAuth(String strAccount, String strPassword) {
		this.strAccount = strAccount;
		this.strPassword = strPassword;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.strAccount, this.strPassword);
	}
}
