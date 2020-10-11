package com.vastsoft.util.service.sms;

public class Msg {
	private String strMobile;
	private String strContent;

	public Msg(String strMobile, String strContent) {
		this.strMobile = strMobile;
		this.strContent = strContent;
	}

	public String getMobile() {
		return this.strMobile;
	}

	public String getContent() {
		return this.strContent;
	}
}
