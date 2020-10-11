package com.vastsoft.yingtaidicom.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.vastsoft.util.security.FileMD5;

public class ConfigDocument {
	private String filePath;
	private String fileMD5;
	private Document document;
	private long previousCheckChangeTime;
	private long checkChangeTimeout = 0;
	
	public ConfigDocument(String filePath){
		this.filePath = filePath;
	}
	
	/**检查配置文件修改时间，0表示每次都检查*/
	public ConfigDocument(String filePath,long checkChangeTimeout){
		this.filePath = filePath;
		this.checkChangeTimeout = checkChangeTimeout;
	}
	
	/**检查是否需要重载文件，如果未超时返回false*/
	public boolean needReload() throws IOException{
		if (this.checkChangeTimeout > 0) {
			if (Math.abs(System.currentTimeMillis() - this.previousCheckChangeTime) <= this.checkChangeTimeout)
				return false;
		}
		File file = new File(this.filePath);
		String MD5 = FileMD5.getFileMD5String(file);
		return !MD5.equals(this.fileMD5);
	}
	
	/**返回新的 rootElement*/
	public Element reload() throws FileNotFoundException, DocumentException{
		this.document = new SAXReader().read(new FileInputStream(new File(this.filePath)));
		return document.getRootElement();
	}
	
	public void save() throws IOException{
		XMLWriter writer = new XMLWriter(new FileOutputStream(new File(this.filePath)));
		writer.write(this.document);
	}
	
	public void saveAs(String filePath) throws IOException{
		this.filePath = filePath;
		XMLWriter writer = new XMLWriter(new FileOutputStream(new File(this.filePath)));
		writer.write(this.document);
	}
}
