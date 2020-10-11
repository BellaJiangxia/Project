package com.vastsoft.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOTools {
	/** 本类默认的最大缓存长度，可修改，修改后将一直有效直到下一次修改 */
	public static int DEFAULT_BUFF_MAX_LEN = 20 * 1024 * 1024;

	/**
	 * 将字节数组转换成输入流
	 * 
	 * @param data
	 *            数据
	 * @return
	 * @throws IOException
	 */
	public static InputStream bytesToStream(byte[] data) throws IOException {
		return new ByteArrayInputStream(data);
	}

	/**
	 * 将输入流转换成字节数组,写入完成之后会关闭输入流，会使用默认的最大缓存长度：DEFAULT_BUFF_MAX_LEN
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] streamToBytes(InputStream in) throws IOException {
		return streamToBytes(in, DEFAULT_BUFF_MAX_LEN, true);
	}

	/**
	 * 将输入流转换成字节数组，会使用默认的最大缓存长度：DEFAULT_BUFF_MAX_LEN
	 * 
	 * @param in
	 * @param needClose
	 *            写入完成之后是否需要关闭输入流
	 * @return
	 * @throws IOException
	 */
	public static byte[] streamToBytes(InputStream in, boolean needClose) throws IOException {
		return streamToBytes(in, DEFAULT_BUFF_MAX_LEN, needClose);
	}

	/**
	 * 将输入流转换成字节数组,写入完成之后会关闭输入流
	 * 
	 * @param in
	 * @param maxLen
	 *            转换的字节数组的最大长度
	 * @return
	 * @throws IOException
	 */
	public static byte[] streamToBytes(InputStream in, int maxLen) throws IOException {
		return streamToBytes(in, maxLen, true);
	}

	/**
	 * 将输入流转换成字节数组
	 * 
	 * @param in
	 * @param maxLen
	 *            转换的字节数组的最大长度
	 * @param needClose
	 *            写入完成之后是否需要关闭输入流
	 * @return
	 * @throws IOException
	 */
	public static byte[] streamToBytes(InputStream in, int maxLen, boolean needClose) throws IOException {
		ByteArrayOutputStream outTmp = new ByteArrayOutputStream(maxLen);
		writeTo(in, outTmp, needClose);
		return outTmp.toByteArray();
	}

	/**
	 * 从输入流从读取数据写入到输出流中，写入完成后会关闭流
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void writeTo(InputStream in, OutputStream out) throws IOException {
		writeTo(in, out, true);
	}

	/**
	 * 从输入流从读取数据写入到输出流中
	 * 
	 * @param in
	 * @param out
	 * @param needClose
	 *            写入完成之后是否需要关闭流
	 * @throws IOException
	 */
	public static void writeTo(InputStream in, OutputStream out, boolean needClose) throws IOException {
		byte[] buf = new byte[10 * 1024];
		int len = -1;
		while ((len = in.read(buf)) >= 0)
			out.write(buf, 0, len);
		out.flush();
		if (!needClose)
			return;
		in.close();
		out.close();
	}
}
