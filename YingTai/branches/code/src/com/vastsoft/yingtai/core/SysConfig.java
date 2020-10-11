package com.vastsoft.yingtai.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SysConfig {
	public final static SysConfig instance = new SysConfig();

	private boolean tip_msg = false;
	private String tuisong_wenzhenresponse_url = "xxx";
	// private String strOrgLogoFolder = null;
	// private String strApplicationURL = null;

	private SysConfig() {
		this.loadConfig();
	}
	
	private void loadConfig(){
		try {
			SAXReader sr = new SAXReader();
//			File config = new File(this.getClass().getClassLoader().getResource("").getPath() + "");
			Document dom = sr.read(this.getClass().getClassLoader().getResourceAsStream("sys_config.xml"));
			Element root = dom.getRootElement();
			Element e = root.element("tip_msg");
			this.tip_msg = e.getText() == null ? false : e.getText().trim().equals("true");
			e = root.element("tuisong_wenzhenresponse_url");
			this.tuisong_wenzhenresponse_url = (e.getText() == null || e.getText().trim().isEmpty())
					? this.tuisong_wenzhenresponse_url : e.getText();
			// e = root.element("org_folder");
			// this.strOrgLogoFolder = e.getText();
		} catch (DocumentException e) {
		}
	}

	// public String getApplicationURL()
	// {
	// return this.strApplicationURL;
	// }
	//
	// public String getOrgLogoFolder()
	// {
	// return this.strOrgLogoFolder;
	// }
	//
	// public String getUserLogoFolder()
	// {
	// return this.strUserLogoFolder;
	// }
	public String getTuisong_wenzhenresponse_url() {
		return this.tuisong_wenzhenresponse_url;
	}

	public boolean isTip_msg() {
		this.loadConfig();
		return tip_msg;
	}
}
