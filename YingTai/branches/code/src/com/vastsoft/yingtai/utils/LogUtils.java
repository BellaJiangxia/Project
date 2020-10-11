package com.vastsoft.yingtai.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;

public class LogUtils {
	private static LogUtils lu = null;
	RandomAccessFile randomFile;
	File logfile;
	public LogUtils() throws IOException{
		logfile=new File(StringTools.endWith(this.getClass().getClassLoader().getResource("/").getPath(), '/') +DateTools.dateToString(new Date(), DateTools.TimeExactType.DAY)+".log");
//		logfile=new File("D:\\Workspaces\\LOG"+DateTools.dateToString(new Date(), TimeExactType.DAY)+".log");
		if (!logfile.exists()) {
			logfile.createNewFile();
		}
		randomFile = new RandomAccessFile(logfile, "rw");
		randomFile.seek(logfile.length());
	}
	
	public void log(String logs) throws IOException{
		logs="["+ DateTools.dateToString(new Date())+"]"+logs+"\r\n";
		lu.randomFile.write(logs.getBytes());
	}
	
	public static void writeLog(String sre){
		try {
			if (lu==null)lu=new LogUtils();
			lu.log(sre);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}
