package com.vastsoft.util.service.mail.std_smtp;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.vastsoft.util.service.RunStatus;
import com.vastsoft.util.service.mail.Mail;
import com.vastsoft.util.service.mail.MailCache;
import com.vastsoft.util.service.mail.MailSender;
import com.vastsoft.util.service.mail.MailSenderConfig;
import com.vastsoft.util.service.mail.MailSenderException;

public class SmtpSender implements MailSender, Runnable {
	private static int iInstIdx = 1;
	private String strWorkerName;

	private Date dtCreated = new Date();
	private Date dtLastStart = new Date();

	private String strSmtpHost;
	@SuppressWarnings("unused")
	private int iPort;
	@SuppressWarnings("unused")
	private boolean bSSL;
	private String strAccountName;
	private String strPassword;
	private String strMailFrom;
	private String strPersonal;

	private MailCache cacheMail;

	private boolean bStopFlag = false;
	private Thread threadSend = null;

	public SmtpSender() {
		this.strWorkerName = "SmtpMailSender" + SmtpSender.iInstIdx;
		SmtpSender.iInstIdx++;
	}

	@Override
	public String getName() {
		return this.strWorkerName;
	}

	@Override
	public void init(MailSenderConfig cfg, MailCache cacheMail) throws MailSenderException {
		if (cacheMail == null || cfg == null)
			throw new MailSenderException();

		String strClassPath = cfg.getClassPath();
		if (this.getClass().getName().equals(strClassPath) == false)
			throw new MailSenderException();

		this.cacheMail = cacheMail;

		this.strSmtpHost = cfg.getConfig("host");
		this.iPort = Integer.parseInt(cfg.getConfig("port"));
		this.bSSL = Boolean.parseBoolean(cfg.getConfig("ssl"));
		this.strAccountName = cfg.getConfig("account");
		this.strPassword = cfg.getConfig("pwd");

		this.strMailFrom = cfg.getConfig("mailfrom");
		this.strPersonal = cfg.getConfig("name");
	}

	@Override
	public boolean open() {
		this.threadSend = new Thread(this);
		this.threadSend.start();

		System.out.println("[SmptMail] started");

		return true;
	}

	@Override
	public boolean close() {
		if (this.threadSend == null)
			return false;

		this.bStopFlag = true;

		try {
			this.threadSend.join();
			this.threadSend = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("[SmptMail] stopped");

		return true;
	}

	@Override
	public RunStatus getStatus() {
		if (this.bStopFlag == false)
			return RunStatus.LIVE;
		else
			return RunStatus.STOP;
	}

	@Override
	public Date getCreatedDate() {
		return new Date(this.dtCreated.getTime());
	}

	@Override
	public Date getLastStartDate() {
		return new Date(this.dtLastStart.getTime());
	}

	@Override
	public void finalize() {
		this.close();
	}

	@Override
	public void run() {
		Properties props = new Properties();

		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.host", this.strSmtpHost);

		// -------------------------SSL支持---------------------------------------------
		// 由于需要ssl证书，比较麻烦。暂屏蔽SSL加密功能。
		//
		// props.setProperty("mail.smtp.socketFactory.class",
		// "javax.net.ssl.SSLSocketFactory");
		// props.setProperty("mail.smtp.socketFactory.class",
		// "com.sun.mail.util.MailSSLSocketFactory");
		// props.setProperty("mail.smtp.socketFactory.fallback", "false");
		// props.setProperty("mail.smtp.port", this.iPort.toString());
		// props.setProperty("mail.smtp.socketFactory.port",
		// this.iPort.toString());
		//
		// Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		// -------------------------------------------------------------------------------

		// 开始不间断的获取待发邮件并发送
		while (this.bStopFlag == false) {
			Mail mail = this.cacheMail.takeMail();

			if (mail == null) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}

				continue;
			}

			Session session = Session.getDefaultInstance(props, new SmtpAuth(this.strAccountName, this.strPassword));
			session.setDebug(true);

			Transport transport = null;

			try {
				transport = session.getTransport("smtp");
				transport.connect(this.strSmtpHost, this.strAccountName, this.strPassword);
			} catch (MessagingException e) {
				Logger.getLogger(this.getClass()).error(null, e);

				this.cacheMail.putMail(mail);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}

				continue;
			}

			try {
				MimeMessage message = new MimeMessage(session);

				// 发件地址
				Address addrFrom;
				try {
					addrFrom = new InternetAddress(this.strMailFrom, this.strPersonal);
				} catch (UnsupportedEncodingException e) {
					addrFrom = new InternetAddress(this.strMailFrom);
				}
				message.setFrom(addrFrom);

				// 收件地址
				Address addrTo;
				try {
					addrTo = new InternetAddress(mail.getRecipient(), mail.getPersonal());
				} catch (UnsupportedEncodingException e) {
					addrTo = new InternetAddress(mail.getRecipient());
				}
				message.setRecipient(MimeMessage.RecipientType.TO, addrTo);

				// 发送主题及内容
				message.setSubject(mail.getSubject());
				message.setContent(mail.getContent(), "text/html;charset=utf-8");

				message.saveChanges();

				transport.sendMessage(message, message.getAllRecipients());

				transport.close();
			} catch (MessagingException e) {
				Logger.getLogger(this.getClass()).error(null, e);
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
