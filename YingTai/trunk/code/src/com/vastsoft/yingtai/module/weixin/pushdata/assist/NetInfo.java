package com.vastsoft.yingtai.module.weixin.pushdata.assist;

public class NetInfo {
	private String url;
	private String params;
	
	public NetInfo(String url, String params) {
		super();
		this.url = url;
		this.params = params;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	
}
