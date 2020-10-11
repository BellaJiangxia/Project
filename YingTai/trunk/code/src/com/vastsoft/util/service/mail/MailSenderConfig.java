package com.vastsoft.util.service.mail;

import java.util.HashMap;
import java.util.Map;

public class MailSenderConfig {
	private String strClassPath;
	private Map<String, String> mapCfg = new HashMap<String, String>();

	public MailSenderConfig(String strClassPath) {
		this.strClassPath = strClassPath;
	}

	public String getClassPath() {
		return this.strClassPath;
	}

	public void putConfig(String strKey, String strValue) {
		this.mapCfg.put(strKey, strValue);
	}

	public String getConfig(String strKey) {
		return this.mapCfg.get(strKey);
	}
}
