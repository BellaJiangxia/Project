package com.vastsoft.yingtai.module.sys.action;

import java.io.File;

import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.service.FileService;
import com.vastsoft.yingtai.utils.FileTools;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;

public class FileAction extends BaseYingTaiAction {
	private long lFileId;
	
//	@ActionDesc(value="上传文件")
//	public String uploadFile(){
//		TFileMgr fileMgr = null;
//		try {
//			if (file==null) {
//				throw new NullParameterException();
//			}
//			InputStream in= new FileInputStream(file);
//			fileMgr= FileService.instance.fileUpload(takePassport(),fileFileName);
//			File file=new File(ServletActionContext.getServletContext().getRealPath(fileMgr.getPath()));
//			if (!file.exists()){
//				file.getParentFile().mkdirs();
//				file.createNewFile();
//			}
//			OutputStream out=new FileOutputStream(file);
//			byte [] buffer=new byte [10240];
//			int length;
//			while ((length=in.read(buffer))>0) {
//				out.write(buffer, 0, length);
//			}
//			out.flush();
//			out.close();
//			in.close();
//			super.addElementToData("fileMgr", fileMgr);
//		} catch (Exception e) {
//			e.printStackTrace();
//			iCode=-1000;
//			if (e instanceof BaseException)
//				iCode=((BaseException)e).getCode();
//			strName="上传文件出错："+e.getMessage();
//			if (fileMgr!=null) {
//				try {
//					FileService.instance.deleteFile(takePassport(), fileMgr.getId());
//				} catch (BaseException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
//		return SUCCESS;
//	}
//	@ActionDesc("通过文件ID查询文件是否存在")
	public String queryCheckFileExsit(){
		try {
			TFile file=FileService.instance.queryFileById(takePassport(), lFileId);
			if (file!=null) {
				if(FileTools.isFileExsit(file.getPath()))
					addElementToData("file", file);
			}
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	@ActionDesc("通过文件ID删除文件")
	public String deleteFileById(){
		try {
			FileService.instance.deleteFile(takePassport(), lFileId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	
//	@ActionDesc(value="通过文件ID查询文件路径")
	public String takeFilePath(){
		try {
			TFile fileMgr=FileService.instance.queryFileById(takePassport(), lFileId);
			this.addElementToData("filePath", fileMgr.getPath());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	public void setMyFile(File myFile) {
//		this.myFile = myFile;
//	}
//
//	public void setMyFileFileName(String myFileFileName) {
//		this.myFileFileName = myFileFileName;
//	}
	public void setFileId(long lFileId) {
		this.lFileId = lFileId;
	}
	public void setFile(File file) {
	}
	public void setFileFileName(String fileFileName) {
	}
	
}
