package com.vastsoft.util.service.mail;

import com.vastsoft.util.service.Worker;

public interface MailSender extends Worker {
	public void init(MailSenderConfig cfg, MailCache cacheMail) throws MailSenderException;
}
