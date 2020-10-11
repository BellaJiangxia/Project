package com.vastsoft.yingtai.utils.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MailTemplate {

	private String strName;
	private String strDescription;
	private StringBuilder sbText = new StringBuilder();

	public MailTemplate(String strName, String strDescription, String strFileName) {
		this.strName = strName;
		this.strDescription = strDescription;

		String strFilePath = MailTemplate.class.getResource("/").getPath();
		File file = new File(strFilePath + "/" + strFileName);

		char[] caData = new char[4096];

		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");

			int iReaded = 0;
			while ((iReaded = reader.read(caData)) != -1) {
				this.sbText.append(caData, 0, iReaded);
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String createText(Map<String, String> map) {
		String str = this.sbText.toString();

		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();

			String strKey = entry.getKey();
			String strValue = entry.getValue();

			str = str.replace("[vt]" + strKey + "[/vt]", strValue);
		}

		return str;
	}

	public String getName() {
		return this.strName;
	}

	public void setName(String strName) {
		this.strName = strName;
	}

	public String getDescription() {
		return this.strDescription;
	}

	public void setDescription(String strDescription) {
		this.strDescription = strDescription;
	}
}
