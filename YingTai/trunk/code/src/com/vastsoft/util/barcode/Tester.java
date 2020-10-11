package com.vastsoft.util.barcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class Tester {
	public static void toImage() {
		try {
			String content = "www.vastour.net:11013,joeradar,by vastsoft";
			String path = "D:/";

			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

			Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
			File file1 = new File(path, "aaa.jpg");
			MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void fromImage() {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			String filePath = "d:/aaa.jpg";
			File file = new File(filePath);
			BufferedImage image = ImageIO.read(file);

			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = formatReader.decode(binaryBitmap, hints);

			System.out.println("result = " + result.toString());
			System.out.println("resultFormat = " + result.getBarcodeFormat());
			System.out.println("resultText = " + result.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		toImage();
		fromImage();
	}
}
