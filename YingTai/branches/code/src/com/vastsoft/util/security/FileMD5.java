package com.vastsoft.util.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件的通用MD5算法，支持大文件
 * 
 * @author jben
 * @since 0.0004
 */
public final class FileMD5 {

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	private static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			nsaex.printStackTrace();
		}
	}
	// public static void main(String[] args) throws IOException {
	// long begin = System.currentTimeMillis();
	//
	// File big = new File("E:/游戏/冲绳那霸.rar");
	//
	// String md5 = getFileMD5String(big);
	//
	// long end = System.currentTimeMillis();
	// System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) +
	// "s");
	// }

	public static String getFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			messagedigest.update(byteBuffer);
			return bufferToHex(messagedigest.digest());
		} finally {
			in.close();
		}
	}

	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	/** 检查密码是否是指定的MD5码标识的密码 */
	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}
}
