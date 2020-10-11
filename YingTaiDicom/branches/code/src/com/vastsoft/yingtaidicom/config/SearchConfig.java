package com.vastsoft.yingtaidicom.config;

import com.vastsoft.util.common.FileTools;

public class SearchConfig {
	public static final SearchConfig instance = new SearchConfig();
	private ConfigDocument document;
	private SearchConfig(){
		this.document = new ConfigDocument(FileTools.getResourceFilePath("sys_config.xml"), 60*1000);
	}
}
