package com.vastsoft.util.service.mail;

public class Mail {
	private String strRecipient;
	private String strPersonal;
	private String strSubject;
	private String strContent;

	public Mail(String strRecipient, String strPersonal, String strSubject, String strContent) {
		this.strRecipient = strRecipient;
		this.strPersonal = strPersonal;
		this.strSubject = strSubject;
		this.strContent = strContent;
	}

	public String getRecipient() {
		return strRecipient;
	}

	public void setRecipient(String strRecipient) {
		this.strRecipient = strRecipient;
	}

	public String getPersonal() {
		return strPersonal;
	}

	public void setPersonal(String strPersonal) {
		this.strPersonal = strPersonal;
	}

	public String getSubject() {
		return strSubject;
	}

	public void setSubject(String strSubject) {
		this.strSubject = strSubject;
	}

	public String getContent() {
		return strContent;
	}

	public void setContent(String strContent) {
		this.strContent = strContent;
	}
}
