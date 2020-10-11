package com.vastsoft.yingtai.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileTools {
	/**获取系统路径*/
	public static String getSysPath(){
		return FileTools.class.getClassLoader().getResource("/").getPath();
	}
	/**使用类加载器获取文件绝对路径，已src为根路径：“com/test.jpg” 注意此方法获取的文件路径有空格问题*/
	public static String getResourcePath(String fileName){
		String result = FileTools.class.getClassLoader().getResource("/").getPath();
		result+=fileName;
		return result==null?"":(result.replaceAll("%20", " ").replaceAll("//", "/"));
	}
	/**使用类加载器获取文件，已src为根路径：“com/test.jpg”*/
	public static File getResourceFile(String fileName){
		return new File(getResourcePath(fileName));
	}
	
	public static InputStream getResourceAsStream(String fileName){
		return FileTools.class.getClassLoader().getResourceAsStream(fileName);
	}
	/**根据路径判断文件是否存在*/
	public static boolean isFileExsit(String filePath){
		File file=new File(filePath);
		return file.exists();
	}
	/**通过文件路径获取文件*/
	public static File takeFileByPath(String filePath){
		File file=new File(filePath);
		return file;
	}
	/**通过文件路径获取文件名*/
	public static String takeFileName(String filePath){
		return takeFileByPath(filePath).getName();
	}
	
	/**获取文件名,不含后缀名，不判断文件是否存在*/
	public static String takeFIleNameNoSuffixUnCheck(String filePath){
		String fileName=takeFIleNameUnCheck(filePath);
		String[] arrayStrTmp=fileName.split(".");
		if (arrayStrTmp==null||arrayStrTmp.length<2)return fileName;
		return arrayStrTmp[arrayStrTmp.length-2];
	}
	/**获取文件名或文件路径的后缀名*/
	public static String takeFileNameSuffix(String filePath){
		String fileName=takeFIleNameUnCheck(filePath);
		String[] arrayStrTmp=fileName.split("\\.");
		if (arrayStrTmp==null||arrayStrTmp.length<=0)return "";
		return arrayStrTmp[arrayStrTmp.length-1];
	}
	/**根据文件名或文件全路径获取文件名，包含后缀名*/
	public static String takeFIleNameUnCheck(String fileName){
		if (fileName==null||fileName.trim().isEmpty())return "";
		fileName=fileName.trim();
		if (!fileName.contains("/")&&!fileName.contains("\\"))return fileName;
		fileName=fileName.replace('\\', '/');
		String[] arrayStrTmp=fileName.split("/");
		if (arrayStrTmp==null||arrayStrTmp.length<2)return fileName;
		return arrayStrTmp[arrayStrTmp.length-1];
	}
	/**创建文件，如果存在则直接返回
	 * @throws IOException */
	public static boolean createFile(File cacheFile) throws IOException {
		if (cacheFile.exists())
			return true;
		if (! cacheFile.getParentFile().mkdirs())
			return false;
		return cacheFile.createNewFile();
	}
}
