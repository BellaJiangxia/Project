package com.vastsoft.util.common;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

public class FileTools {
	/**获取SRC目录下的某个文件。*/
	public static File getResourceFile(String fileName){
		try {
			String filePath = getResourceFilePath(fileName);
			return new File(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**获取SRC目录下的某个文件的全路径。*/
	public static String getResourceFilePath(String fileName){
		try {
			Class<?> caller = CommonTools.getCaller();
			if (caller == null)
				caller = FileTools.class;
			URL url = caller.getClassLoader().getResource("sys_config.xml");
			return URLDecoder.decode(url.getPath(),SystemTools.getFileEncoding("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 获取文件名的后缀名,不含. */
	public static String getExtNameNoPot(String strFileName) {
		String[] token = strFileName.split("\\.");
		if (token == null || token.length <= 0)
			return "";
		String pf = token[token.length - 1];
		return pf;
	}

	/** 获取文件名的后缀名,含有. */
	public static String getExtName(String strFileName) {
		return '.' + getExtNameNoPot(strFileName);
	}

	/** 获取指定文件路径的目录路径 */
	public static String takeDirFileByFilePath(String filePath) {
		File file = new File(filePath);
		return file.getParentFile().getAbsolutePath();
	}

	/** 获取指定文件路径的目录路径 */
	public static String takeDirPathByFilePath(String filePath) {
		File file = new File(filePath);
		return file.getParent();
	}

	public static String buildPath(String[] strs) {
		SplitStringBuilder<String> result = new SplitStringBuilder<String>(File.separator);
		result.addAll(strs);
		return result.toString();
	}

	public static String buildPath(List<String> strs) {
		SplitStringBuilder<String> result = new SplitStringBuilder<String>(File.separator);
		result.addAll(strs);
		return result.toString();
	}

	/** 创建多级目录,仅支持创建目录,如果已经存在返回true */
	public static boolean createDirectory(String dirName) {
		try {
			File ff = new File(dirName);
			if (!ff.exists()) {
				ff.mkdirs();
				return true;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** 根据路径判断文件是否存在 */
	public static boolean isFileExsit(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/** 通过文件路径获取文件 */
	public static File takeFileByPath(String filePath) {
		File file = new File(filePath);
		return file;
	}

	/** 通过文件路径获取文件名 */
	public static String takeFileName(String filePath) {
		return takeFileByPath(filePath).getName();
	}

	/** 获取文件名,不含后缀名，不判断文件是否存在 */
	public static String takeFIleNameNoSuffixUnCheck(String filePath) {
		String fileName = takeFIleNameUnCheck(filePath);
		String[] arrayStrTmp = StringTools.splitString(fileName, '.');
		if (arrayStrTmp == null || arrayStrTmp.length < 2)
			return fileName;
		return arrayStrTmp[arrayStrTmp.length - 2];
	}

	/** 获取文件名或文件路径的后缀名 */
	public static String takeFileNameSuffix(String filePath) {
		String fileName = takeFIleNameUnCheck(filePath);
		String[] arrayStrTmp = fileName.split("\\.");
		if (arrayStrTmp == null || arrayStrTmp.length <= 0)
			return "";
		return arrayStrTmp[arrayStrTmp.length - 1];
	}

	/** 根据文件名或文件全路径获取文件名，包含后缀名 */
	public static String takeFIleNameUnCheck(String fileName) {
		if (fileName == null || fileName.trim().isEmpty())
			return "";
		fileName = fileName.trim();
		if (!fileName.contains("/") && !fileName.contains("\\"))
			return fileName;
		fileName = fileName.replace('\\', '/');
		String[] arrayStrTmp = fileName.split("/");
		if (arrayStrTmp == null || arrayStrTmp.length < 2)
			return fileName;
		return arrayStrTmp[arrayStrTmp.length - 1];
	}
}
