package com.vastsoft.yingtai.module.sys.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.exception.FileException;
import com.vastsoft.yingtai.module.sys.service.FileService;

public class DownloadFileAction extends BaseYingTaiAction {
	private String strFileName;
	private long lFileId;
	private String filePath;
	
	public String downloadFile(){
		try {
			TFile fileMgr= FileService.instance.queryFileById(takePassport(), lFileId);
			filePath=ServletActionContext.getServletContext().getRealPath(fileMgr.getPath());
			if (!new File(filePath).exists())
				throw new FileException("文件未找到！");
			strFileName=fileMgr.getOri_name();
			return SUCCESS;
		} catch (Exception e) {
			catchException(e);
			return ERROR;
		}
	}
	@JSON(serialize=false)
	public InputStream getDownloadFile(){
		try {
			return new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFileName() {
		return strFileName;
	}

	public void setFileId(long lFileId) {
		this.lFileId = lFileId;
	}
}
