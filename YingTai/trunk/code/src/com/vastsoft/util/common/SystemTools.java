package com.vastsoft.util.common;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class SystemTools {
	/** 获取桌面路径，失败返回null */
	public static String getDesktopPath() {
		try {
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			return desktopDir.getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
	}

	/** 在桌面上创建指定文件名的文件，如果存在直接返回 */
	public static File createFileOnDesktop(String fileName) {
		File file = new File(getDesktopPath() + "\\" + (fileName));
		if (file.exists())
			return file;
		try {
			if (!file.createNewFile())
				return null;
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 获取桌面上的指定文件名的文件，文件不存在或者失败返回null */
	public static File getDesktopFilePath(String fileName) {
		if (StringTools.isEmpty(fileName))
			return null;
		try {
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			File[] listSubFile = desktopDir.listFiles();
			if (ArrayTools.isEmpty(listSubFile))
				return null;
			for (File file : listSubFile) {
				if (fileName.equals(file.getName()))
					return file;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/** 获取当前操作系统的文件路径分隔符 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator", "\\");
	}

	/** 获取当前操作系统用户名 */
	public static String getOperatingSystemUserName(String default_value) {
		return System.getProperty("user.name", default_value);
	}

	/** 获取当前文件编码 */
	public static String getFileEncoding(String default_value) {
		return System.getProperty("file.encoding", default_value);
	}

	/** 获取当前用户的用户目录 */
	public static String getUserDir(String default_value) {
		return System.getProperty("user.home", default_value);
	}

	/** 获取当前系统换行符 */
	public static String getSeparator(String default_value) {
		return System.getProperty("line.separator", default_value);
	}

	/** 获取当前系统换行符,默认为‘\r\n’ */
	public static String getSeparator() {
		return getSeparator("\r\n");
	}
}
