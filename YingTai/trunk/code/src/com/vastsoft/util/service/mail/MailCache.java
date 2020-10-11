package com.vastsoft.util.service.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存即将发送的邮件内容
 * 
 * @author JoeRadar
 * 
 */
public class MailCache {
	private List<Mail> listMails = new ArrayList<Mail>();

	public MailCache() {
	}

	public synchronized void putMail(Mail mail) {
		this.listMails.add(mail);
	}

	public synchronized Mail takeMail() {
		if (this.listMails.isEmpty())
			return null;
		else
			return this.listMails.remove(0);
	}

	public synchronized boolean isEmpty() {
		return this.listMails.isEmpty();
	}

	public synchronized void clear() {
		this.listMails.clear();
	}
}
