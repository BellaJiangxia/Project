package com.vastsoft.util.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MediaTools {
	/**
	 * @param bufferedImage
	 * @param formatName
	 *            图片文件格式
	 * @return
	 * @throws IOException
	 */
	public static byte[] bufferedImageToBytes(BufferedImage bufferedImage, String formatName) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, formatName, output);
		return output.toByteArray();
	}
}
