package com.vastsoft.yingtai.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.struts2.ServletActionContext;

import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.service.FileService;

public class LogoAction extends BaseYingTaiAction
{

	private Long lFileId = null;

	public void setFileId(Long lFileId) {
		this.lFileId = lFileId;
	}

	public String execute()
	{
		return "logo";
	}

	public InputStream getImageStream()
	{
		if (this.lFileId != null)
		{
			try {
				if (this.lFileId != null && this.lFileId > 0) {
					TFile tfm = FileService.instance.getFileByID(lFileId);
					if (tfm != null) {
						File file = null;
						if (tfm.getThumbnail_path() != null && !tfm.getThumbnail_path().trim().isEmpty()) {
							file = new File(tfm.getThumbnail_path());
							if (file.exists())
								return new FileInputStream(file);
							else
								return new FileInputStream(getDefaultFile(tfm.getFileType()));
						}
						file = new File(tfm.getPath());
						if (file.exists())
							return new FileInputStream(file);
						else
							return new FileInputStream(getDefaultFile(tfm.getFileType()));
					}
				}

				return new FileInputStream(getDefaultFile(null));

			} catch (Exception e) {
				return null;
			}

		}
		else
		{
			return null;
		}
	}

	private File getDefaultFile(DictionaryType fileType) {
		File defaultFile = null;
		if (DictionaryType.USER_LOGO_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/user_default.png");
		} else if (DictionaryType.ORG_LOGO_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/hospital-default.png");
		} else if (DictionaryType.USER_SCAN_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/diploma_default.png");
		} else if (DictionaryType.ORG_SCAN_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/diploma_default.png");
		} else {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/no_file_record.png");
		}
		return defaultFile;
	}

}
